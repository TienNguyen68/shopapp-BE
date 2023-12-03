package com.project.shopapp.services;

import com.project.shopapp.dtos.OrderDetailDTO;
import com.project.shopapp.exceptions.DataNotFoundException;
import com.project.shopapp.models.Order;
import com.project.shopapp.models.OrderDetail;
import com.project.shopapp.models.Product;
import com.project.shopapp.repositories.OrderDetailRepository;
import com.project.shopapp.repositories.OrderRepository;
import com.project.shopapp.repositories.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderDetailService implements IOrderDetailService {
   private final OrderRepository orderRepository;
   private final ProductRepository productRepository;
   private final OrderDetailRepository orderDetailRepository;

   @Override
   public OrderDetail createOrderDetail(OrderDetailDTO orderDetailDTO) throws Exception {

      // Tim  orderId co ton tai
      Order order = orderRepository.findById(orderDetailDTO.getOrderId())
              .orElseThrow(() -> new DataNotFoundException("Khong tim thay order voi id " + orderDetailDTO.getOrderId()));

      //Tim product theo id
      Product product = productRepository.findById(orderDetailDTO.getProductId()).orElseThrow(() -> new DataNotFoundException("Khong tin thay Product voi Id " + orderDetailDTO.getProductId()));

      //Xet doi tuong order moi
      OrderDetail orderDetail = OrderDetail.builder()
              .order(order)
              .product(product)
              .numberOfProducts(orderDetailDTO.getNumberOfProducts())
              .price(orderDetailDTO.getPrice())
              .totalMoney(orderDetailDTO.getTotalMoney())
              .color(orderDetailDTO.getColor())
              .build();
      return orderDetailRepository.save(orderDetail);
   }

   @Override
   public OrderDetail getOrderDetail(Long id) throws DataNotFoundException {
      return orderDetailRepository.findById(id)
              .orElseThrow(()->new DataNotFoundException("Khong tim thay orderDetail voi Id= "+id));
   }

   @Override
   public OrderDetail updateOrderDetail(Long id, OrderDetailDTO orderDetailDTO) throws DataNotFoundException {

      // Tim xem orderDetail co ton tai
      OrderDetail existingOrderDetail = orderDetailRepository.findById(id)
              .orElseThrow(()-> new DataNotFoundException("Khong the tim orderDetail voi id=" +id));

      // OrderId co thuoc order nao khac khong
      Order existingOrder = orderRepository.findById(orderDetailDTO.getOrderId())
              .orElseThrow(()-> new DataNotFoundException("Khong the tim Order voi id=" +id));

      //Kiem tra Product cos ton tai
      Product existingProduct = productRepository.findById(orderDetailDTO.getProductId()).orElseThrow(() -> new DataNotFoundException("Khong tin thay Product voi Id " + orderDetailDTO.getProductId()));

      existingOrderDetail.setPrice(orderDetailDTO.getPrice());
      existingOrderDetail.setNumberOfProducts(orderDetailDTO.getNumberOfProducts());
      existingOrderDetail.setTotalMoney(orderDetailDTO.getTotalMoney());
      existingOrderDetail.setColor(orderDetailDTO.getColor());
      existingOrderDetail.setOrder(existingOrder);
      existingOrderDetail.setProduct(existingProduct);
      return orderDetailRepository.save(existingOrderDetail);
   }

   @Override
   public void deleteById(Long id) {
      orderDetailRepository.deleteById(id);
   }

   @Override
   public List<OrderDetail> findByOrderId(Long orderId) {
      return orderDetailRepository.findByOrderId(orderId);
   }
}
