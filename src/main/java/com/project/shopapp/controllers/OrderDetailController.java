package com.project.shopapp.controllers;

import com.project.shopapp.dtos.OrderDetailDTO;
import com.project.shopapp.exceptions.DataNotFoundException;
import com.project.shopapp.models.OrderDetail;
import com.project.shopapp.services.OrderDetailService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("${api.prefix}/order_details")
@RequiredArgsConstructor
public class OrderDetailController {
   private final OrderDetailService orderDetailService;
   @PostMapping
   public ResponseEntity<?> createOrderDetail(
           @Valid @RequestBody OrderDetailDTO orderDetailDTO) {

      try {
         OrderDetail newOrderDetail = orderDetailService.createOrderDetail(orderDetailDTO);
         return ResponseEntity.ok().body(newOrderDetail);
      } catch (Exception e) {
         return ResponseEntity.badRequest().body(e.getMessage());
      }
   }


   @GetMapping("/{id}") //lấy chi tiết đơn hàng
   public ResponseEntity<?> getOrderDetail(
           @Valid @PathVariable ("id") Long id) throws DataNotFoundException {
      OrderDetail orderDetail = orderDetailService.getOrderDetail(id);
      return ResponseEntity.ok(orderDetail);
   }

   // lấy ra danh sách các order_details của 1 order nào đó
   @GetMapping("/order/{orderId}")
   public ResponseEntity<?> getOrderDetails(
           @Valid @PathVariable ("orderId") Long orderId
   ) {
      List<OrderDetail> orderDetails =orderDetailService.findByOrderId(orderId);
      return ResponseEntity.ok(orderDetails);
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
