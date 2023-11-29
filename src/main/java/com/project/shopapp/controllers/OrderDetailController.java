package com.project.shopapp.controllers;

import com.project.shopapp.dtos.OrderDetailDTO;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("${api.prefix}/order_details")
public class OrderDetailController {
   @PostMapping
   public ResponseEntity<?> createOrderDetail(
           @Valid @RequestBody OrderDetailDTO orderDetaiDTO) {
      return ResponseEntity.ok("createOrderDetail here");
   }

   @GetMapping("/{id}") //lấy chi tiết đơn hàng
   public ResponseEntity<?> getOrderDetail(
           @Valid @PathVariable ("id") Long id) {
      return ResponseEntity.ok("getOrderDetail with id = " + id);
   }

   // lấy ra danh sách các order_details của 1 order nào đó
   @GetMapping("/order/{orderId}")
   public ResponseEntity<?> getOrderDetails(
           @Valid @PathVariable ("orderId") Long orderId) {
      return ResponseEntity.ok("getOrderDetails with orderId = " + orderId);
   }

   @PutMapping("/{id}")
   public ResponseEntity<?> updateOrderDetail(
           @Valid @PathVariable ("id") Long id,
           @RequestBody OrderDetailDTO newOrderDetaiDTO) {
      return ResponseEntity.ok("updateOrderDetail with id= "+id+ ", new OrderDetail"+ newOrderDetaiDTO);
   }

   @DeleteMapping("/{id}")
   public ResponseEntity<Void> deleteOrderDetail(
           @Valid @PathVariable ("id") Long id){
      return ResponseEntity.noContent().build();
   }

}
