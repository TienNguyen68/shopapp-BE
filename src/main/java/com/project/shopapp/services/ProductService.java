package com.project.shopapp.services;

import com.project.shopapp.dtos.ProductDTO;
import com.project.shopapp.dtos.ProductImageDTO;
import com.project.shopapp.exceptions.DataNotFoundException;
import com.project.shopapp.exceptions.InvalidParamException;
import com.project.shopapp.models.Category;
import com.project.shopapp.models.Product;
import com.project.shopapp.models.ProductImage;
import com.project.shopapp.repositories.CategoryRepository;
import com.project.shopapp.repositories.ProductImageRepository;
import com.project.shopapp.repositories.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductService implements IProductService {
   private final ProductRepository productRepository;
   private final CategoryRepository categoryRepository;
   private final ProductImageRepository productImageRepository;

   @Override
   public Product createProduct(ProductDTO productDTO) throws Exception {
      Category existingCategory = categoryRepository.findById(productDTO.getCategoryId())
              .orElseThrow(() -> new DataNotFoundException
                      ("Không tìm thấy category id = " + productDTO.getCategoryId()));
      Product newProduct = Product.builder()
              .name(productDTO.getName())
              .price(productDTO.getPrice())
              .thumbnail(productDTO.getThumbnail())
              .category(existingCategory)
              .build();
      return productRepository.save(newProduct);
   }

   @Override
   public Product getProductById(long productId) throws Exception {
      return productRepository.findById(productId)
              .orElseThrow(() -> new DataNotFoundException
                      ("Không tìm thấy product id = " + productId));
   }

   @Override
   public Page<Product> getAllProducts(PageRequest pageRequest) {
      return productRepository.findAll(pageRequest);
   }

   @Override
   public Product updateProduct(long id, ProductDTO productDTO) throws Exception {
      Product existingProduct = getProductById(id);

      if (existingProduct != null) {
         //copy các thuốcj tính DTO->Entity
         //có thể sử dụng MOdelMapper
         Category existingCategory = categoryRepository.findById(productDTO.getCategoryId())
                 .orElseThrow(() -> new DataNotFoundException
                         ("Không tìm thấy category id = " + productDTO.getCategoryId()));
         existingProduct.setName(productDTO.getName());
         existingProduct.setCategory(existingCategory);
         existingProduct.setPrice(productDTO.getPrice());
         existingProduct.setDescription(productDTO.getDescription());
         existingProduct.setThumbnail(productDTO.getThumbnail());
         productRepository.save(existingProduct);

      }
      return null;
   }

   @Override
   public void deleteProduct(long id) {
      Optional<Product> optionalProduct = productRepository.findById(id);
      optionalProduct.ifPresent(productRepository::delete);
      // dùng ifPresent truyền biểu thức lamda tham chiêu hàm delete
   }


   @Override
   public boolean existsByName(String name) {
      return productRepository.existsByName(name);
   }

   @Override
   public ProductImage createProductImage(
           Long productId,
           ProductImageDTO productImageDTO) throws Exception {
      Product existingProduct = productRepository
              .findById(productId)
              .orElseThrow(() ->
                      new DataNotFoundException
                      ("Không tìm thấy product id = " + productImageDTO.getProductId()));
      ProductImage newProductImage = ProductImage.builder()
              .product(existingProduct)
              .imageUrl(productImageDTO.getImageUrl())
              .build();
         //không cho thêm quá 5 ảnh cho 1 sản phẩm
      int size = productImageRepository.findByProductId(productId).size();
      if (size >= ProductImage.MAXIMUM_IMAGES_PER_PRODUCT) {
         throw new InvalidParamException("Số ảnh nhỏ hơn hoặc bằng " + ProductImage.MAXIMUM_IMAGES_PER_PRODUCT);
      }
      return productImageRepository.save(newProductImage);
   }
}
