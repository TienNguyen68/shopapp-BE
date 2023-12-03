package com.project.shopapp.controllers;

import com.project.shopapp.dtos.OrderDTO;
import com.project.shopapp.models.Order;
import com.project.shopapp.repositories.OrderRepository;
import com.project.shopapp.services.IOrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;


@RestController
@RequestMapping("${api.prefix}/orders") //api/v1 = ${api.prefix}
@RequiredArgsConstructor
public class OrderController {
   private final IOrderService orderService;
   private final OrderRepository orderRepository;

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
         Order order = orderService.createOrder(orderDTO);
         return ResponseEntity.ok(order);
      } catch (Exception e) {
         return ResponseEntity.badRequest().body(e.getMessage());
      }
   }

   @GetMapping("/user/{user_id}")  //thêm đường dẫn "user_id"
   public ResponseEntity<?> getOrders(@Valid @PathVariable("user_id") Long userId) {
      try {
         List<Order> orders = orderService.findByUserId(userId);
         return ResponseEntity.ok(orders);

      } catch (Exception e) {
         return ResponseEntity.badRequest().body(e.getMessage());
      }

   }

   @GetMapping("/{id}")  //thêm đường dẫn "user_id"
   public ResponseEntity<?> getOrder(@Valid @PathVariable("id") Long orderId) {
      try {
         Order existingOrder = orderService.getOrder(orderId);
         return ResponseEntity.ok(existingOrder);

      } catch (Exception e) {
         return ResponseEntity.badRequest().body(e.getMessage());
      }

   }

   @PutMapping("/{id}")  //công việc adm
   public ResponseEntity<?> updateOrder(@Valid @PathVariable long id,
                                        @RequestBody OrderDTO orderDTO) {
      try {
         Order order = orderService.updateOrder(id, orderDTO);
         return ResponseEntity.ok(order);
      } catch (Exception e){
         return ResponseEntity.badRequest().body(e.getMessage());
      }
   }



   @DeleteMapping("/{id}")
   public ResponseEntity<?> deleteOrder(@Valid @PathVariable long id) {
      //xóa mềm => cập nhật trường active = false

  orderService.deleteOrder(id);


      return ResponseEntity.ok("Xóa order thành công");

   }


}
