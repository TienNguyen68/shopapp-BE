package com.project.shopapp.controllers;

import com.project.shopapp.dtos.ProductDTO;
import com.project.shopapp.dtos.ProductImageDTO;
import com.project.shopapp.models.Product;
import com.project.shopapp.models.ProductImage;
import com.project.shopapp.responses.ProductListResponse;
import com.project.shopapp.responses.ProductResponse;
import com.project.shopapp.services.IProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;


@RestController
@RequestMapping("${api.prefix}/products")
@RequiredArgsConstructor
public class ProductController {
   private final IProductService productService;


   @PostMapping("")
   public ResponseEntity<?> createProduct(@Valid @RequestBody ProductDTO productDto,
                                          BindingResult result) {
      try {
         if (result.hasErrors()) {
            List<String> errorMessages = result.getFieldErrors()
                    .stream()
                    .map(FieldError::getDefaultMessage)
                    .toList();
            return ResponseEntity.badRequest().body(errorMessages);
         }
         Product newProduct = productService.createProduct(productDto);
         return ResponseEntity.ok(newProduct);
      } catch (Exception e) {
         return ResponseEntity.badRequest().body(e.getMessage());
      }
   }

   @PostMapping(value = "uploads/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
   public ResponseEntity<?> uploadImages(
           @PathVariable("id") Long productId,
           @ModelAttribute("files") List<MultipartFile> files) {
      try {
         Product existingProduct = productService.getProductById(productId);
         files = files == null ? new ArrayList<MultipartFile>() : files;
         if (files.size() > ProductImage.MAXIMUM_IMAGES_PER_PRODUCT) {
            return ResponseEntity.badRequest().body("Chi upload 5 <= 5 anh");

         }
         List<ProductImage> productImages = new ArrayList<>();

         for (MultipartFile file : files) {
            if (file.getSize() == 0) {
               continue;
            }

            // Kiểm tra kích thước file
            if (file.getSize() > 10 * 1024 * 1024) {  // 10MB
               return ResponseEntity.status(HttpStatus.PAYLOAD_TOO_LARGE)
                       .body("File is too large! Maximum size is 10MB");
            }

            // Kiểm tra định dạng file
            String contentType = file.getContentType();
            if (contentType == null || !contentType.startsWith("image/")) {
               return ResponseEntity.status(HttpStatus.UNSUPPORTED_MEDIA_TYPE)
                       .body("File must be an image");
            }

            // Lưu file và cập nhật thumbnail trong DTO
            String filename = storeFile(file);  // Thay thế hàm này với code của bạn để lưu file
            // Lưu vào đối tượng product trong DB => làm sau
            ProductImage productImage = productService.createProductImage(
                    existingProduct.getId(),
                    ProductImageDTO.builder()
                            .imageUrl(filename)
                            .build()
            );
            productImages.add(productImage);
         }
         return ResponseEntity.ok().body(productImages);
      } catch (Exception e) {
         return ResponseEntity.badRequest().body(e.getMessage());
      }


   }

   private String storeFile(MultipartFile file) throws IOException {
      if (!isImagesFile(file) || file.getOriginalFilename() == null) {
//         throw new IOException("Anh khong dung dinh dang");
         String errorMessage = "Anh khong dung dinh dang: " +
                 "isImagesFile: " + isImagesFile(file) +
                 ", Original Filename: " + file.getOriginalFilename();
         throw new IOException(errorMessage);
      }

      String filename = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));
      // Thêm UUID vào trước tên file để đảm bảo tên là duy nhất
      String uniqueFilename = UUID.randomUUID().toString() + "_" + filename;
      //đường dẫn đến thư mục lưu file
      Path uploadDir = Paths.get("uploads");    //Path: dùng của java.nio.file.Path;
      // Kiểm tra và tạo thư mục nếu nó không tồn tại
      if (!Files.exists(uploadDir)) {
         Files.createDirectories(uploadDir);
      }
      // Đường dẫn đầy đủ file
      Path destination = Paths.get(uploadDir.toString(), uniqueFilename);
      //sao chép file vào thư mục đích
      Files.copy(file.getInputStream(), destination, StandardCopyOption.REPLACE_EXISTING);
      return uniqueFilename;
   }

   private boolean isImagesFile(MultipartFile file) {
      String contentType = file.getContentType();
      return contentType != null && contentType.startsWith("image/");
   }

   @GetMapping("")
   public ResponseEntity<ProductListResponse> getProducts(
           @RequestParam("page") int page,
           @RequestParam("limit") int limit
   ) {
      // Tạo Pageable từ thông tin trang và giới hạn
      PageRequest pageRequest = PageRequest.of(
              page, limit,
              Sort.by("createAt").descending());
      Page<ProductResponse> productPage = productService.getAllProducts(pageRequest);
      // Lấy tổng số trang
      int totalPages = productPage.getTotalPages();
      List<ProductResponse> products = productPage.getContent();
      return ResponseEntity.ok(ProductListResponse
              .builder()
              .products(products)
              .totalPages(totalPages)
              .build());
   }

   @GetMapping("/{id}")
   public ResponseEntity<?> getProductById(@PathVariable("id") Long productId) {
      try {
         Product existingProduct = productService.getProductById(productId);
         return ResponseEntity.ok(ProductResponse.fromProduct(existingProduct));
      } catch (Exception e) {
         return ResponseEntity.badRequest().body(e.getMessage());
      }
   }


   @DeleteMapping("/{id}")
   public ResponseEntity<String> deleteProduct(@PathVariable Long id) {
      try {
         productService.deleteProduct(id);
         return ResponseEntity.ok(String.format("Product with id = %d  deleted successfully", id));

      } catch (Exception e) {
         return ResponseEntity.badRequest().body(e.getMessage());
      }
   }

   @PutMapping("/{id}")
   public ResponseEntity<?> updateProduct(   //CHƯA LÀM XONG
           @PathVariable long id,
           @RequestBody ProductDTO productDTO) {
      try {
         Product updateProduct = productService.updateProduct(id,productDTO);
         return ResponseEntity.ok(updateProduct);
      } catch (Exception e) {
         return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
      }
   }



//   @PostMapping("/generateFakeProducts")
//   public ResponseEntity<String> generateFakeProducts(){
//      Faker faker = new Faker();
//      for(int i = 0; i<100; i++){
//         String productName = faker.commerce().productName();
//         if(productService.existsByName(productName)){
//            continue;
//         }
//         ProductDTO productDTO = ProductDTO
//                 .builder()
//                 .name(productName)
//                 .price((float)faker.number().numberBetween(10,9_000_000))
//                 .description(faker.lorem().sentence())
//                 .thumbnail(" ")
//                 .categoryId((long)faker.number().numberBetween(1,4))
//                 .build();
//         try {
//            productService.createProduct(productDTO);
//         } catch (Exception e) {
//            return ResponseEntity.badRequest().body(e.getMessage());
//         }
//      }
//return ResponseEntity.ok("Tao Product fake thanh cong");
//   }

}
