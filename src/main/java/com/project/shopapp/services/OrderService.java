package com.project.shopapp.services;

import com.project.shopapp.dtos.OrderDTO;
import com.project.shopapp.exceptions.DataNotFoundException;
import com.project.shopapp.models.Order;
import com.project.shopapp.models.OrderSatus;
import com.project.shopapp.models.User;
import com.project.shopapp.repositories.OrderReposirory;
import com.project.shopapp.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService implements IOrderService {
   private final UserRepository userRepository;
   private final OrderReposirory orderReposirory;
   private final ModelMapper modelMapper;


   @Override
   public Order createOrder(OrderDTO orderDTO) throws Exception {
      // tìm user_id có tồn tại?
      User user = userRepository
              .findById(orderDTO.getUserId())
              .orElseThrow(() -> new DataNotFoundException("Khong tim thay User voi Id: " + orderDTO.getUserId()));

      //converter orderDTO => order
      //dùng thư viện Model Mapper
      // Tạo luồng ánh xạ riêng để kiểm soát ánh xạ
      modelMapper.typeMap(OrderDTO.class, Order.class)
              .addMappings(mapper -> mapper.skip(Order::setId));

      // Cập nhật các trường của đơn hàng từ orderDTO
      Order order = new Order();
      modelMapper.map(orderDTO, order);
      order.setUserId(user);
      order.setOrderDate(new Date());  //thời điểm hiện tại
      order.setStatus(OrderSatus.PENDING);   //mặc định tạo là Pending

      //kiểm tra shipping data > ngày hôm nay
      LocalDate shipingDate = orderDTO.getShippingDate() == null
              ? LocalDate.now() : orderDTO.getShippingDate();
      if(shipingDate.isBefore(LocalDate.now())){
         throw new DataNotFoundException("Date must be at least today !");
      }
      order.setShippingDate(shipingDate);
      order.setActive(true);
      orderReposirory.save(order);

      return order; //ánh xạ order-> OrderResponse
   }

   @Override
   public Order getOrder(Long id) {
      return null;
   }

   @Override
   public Order updateOrder(Long id, OrderDTO orderDTO) {
      return null;
   }

   @Override
   public void deleteOrder(Long id) {

   }

   @Override
   public List<Order> getAllOrders(Long userId) {
      return null;
   }
}
