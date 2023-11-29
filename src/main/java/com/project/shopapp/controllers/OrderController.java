package com.project.shopapp.controllers;

import com.project.shopapp.dtos.CategoryDTO;
import com.project.shopapp.dtos.OrderDTO;
import com.project.shopapp.dtos.UserDTO;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("api/v1/orders")
public class OrderController {
   @PostMapping("")
   public ResponseEntity<?> createOrder(
           @Valid @RequestBody OrderDTO orderDTO,
           BindingResult result) {

         try {
            if (result.hasErrors()) {
               List<String> errorMessages = result.getFieldErrors()
                       .stream()
                       .map(FieldError::getDefaultMessage)
                       .toList();
               return ResponseEntity.badRequest().body(errorMessages);

            }
            return ResponseEntity.ok("Create Order successfully");
         } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
         }
      }

      @GetMapping("/{user_id}")  //thêm đường dẫn "user_id"
      public ResponseEntity<?> getOrders(@Valid @PathVariable("user_id") Long userId) {
         try {
            return ResponseEntity.ok("Lấy danh sách order từ user_id");

         } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
         }

      }

   @PutMapping("/{id}")  //công việc adm
   public ResponseEntity<?> updateOrder(@Valid @PathVariable long id,
                                        @RequestBody OrderDTO orderDTO) {
      return ResponseEntity.ok("Cập nhật thông tin 1 order");

   }
   @DeleteMapping("/{id}")
   public ResponseEntity<?> deleteOrder(@Valid @PathVariable long id) {
      //xóa mềm => cập nhật trường active = false
      return ResponseEntity.ok("Xóa order thành công");

   }



}
