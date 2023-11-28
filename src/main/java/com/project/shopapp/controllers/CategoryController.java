package com.project.shopapp.controllers;

import com.project.shopapp.dtos.CategoryDTO;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("api/v1/categories")
public class CategoryController {

   @GetMapping("")   //categories?page=1&limit=10
   public ResponseEntity<String> getAllCategories(
           @RequestParam("page") int page,
           @RequestParam("limit") int limit) {
      return ResponseEntity.ok(String.format("GetAllCategories, page=%d, limit=%d", page, limit));
   }

   @PostMapping("")
   //nếu tham số truyền vào là 1 object thì sao ? Data Tranfer Obiect = Request Object
   public ResponseEntity<?> insertCategory(@Valid
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
      return ResponseEntity.ok("This is insert category" + categoryDto);
   }


   @PutMapping("/{id}")
   public ResponseEntity<String> updateCategory(@PathVariable Long id) {
      return ResponseEntity.ok("Update category with id =" + id);
   }

   @DeleteMapping("/{id}")
   public ResponseEntity<String> deleteCategory(@PathVariable Long id) {
      return ResponseEntity.ok("Delete category with id =" + id);
   }
}
