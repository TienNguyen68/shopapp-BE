package com.project.shopapp.controllers;

import com.project.shopapp.dtos.CategoryDTO;
import com.project.shopapp.models.Category;
import com.project.shopapp.services.CategoryService;
import com.project.shopapp.services.ICategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("${api.prefix}/categories")

//Dependency Injection
@RequiredArgsConstructor
public class CategoryController {
   private final ICategoryService categoryService;

   @PostMapping("")
   //nếu tham số truyền vào là 1 object thì sao ? Data Tranfer Obiect = Request Object
   public ResponseEntity<?> createCategory(@Valid
                                           //<?>: khi kiểu dữ liệu khai báo là String nhưng nhận String+ List String
                                           @RequestBody CategoryDTO categoryDto,
                                           BindingResult result) {
      if (result.hasErrors()) {
         List<String> errorMessages=result.getFieldErrors()
                 .stream()
//                 .map(fieldError -> fieldError.getDefaultMessage())   // dùng lấy đối tượng ra để chỉnh sửa
                 .map(FieldError::getDefaultMessage)  //lấy đối tượng không cần chỉnh sữa
                 .toList();
         return ResponseEntity.badRequest().body(errorMessages);
         //getFieldErrors: danh sách bị lõi
         //stream(): chức năng duyệt qua mãng, danh sách getFieldErrors
      }
      categoryService.createCategory(categoryDto);
      return ResponseEntity.ok("Thêm mới category thành công");
   }

   @GetMapping("")   //categories?page=1&limit=10
   public ResponseEntity<List<Category>> getAllCategories(
           @RequestParam("page") int page,
           @RequestParam("limit") int limit) {

      List<Category> categories = categoryService.getAllCategories();
      return ResponseEntity.ok(categories);
   }




   @PutMapping("/{id}")
   public ResponseEntity<String> updateCategory(@PathVariable Long id,
                                                @Valid @RequestBody CategoryDTO categoryDTO) {

      categoryService.updateCategoryById(id, categoryDTO);
      return ResponseEntity.ok("Cập nhật category thành công");
   }

   @DeleteMapping("/{id}")
   public ResponseEntity<String> deleteCategory(@PathVariable Long id) {
      categoryService.deleteCategory(id);
      return ResponseEntity.ok("Delete category with id =" + id + " thành công");
   }
}
