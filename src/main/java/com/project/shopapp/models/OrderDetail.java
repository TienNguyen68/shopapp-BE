package com.project.shopapp.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "order_detail")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrderDetail {
   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   private Long id;

   @ManyToOne
   @JoinColumn(name = "order_id")
   private Order orderId;

   @ManyToOne
   @JoinColumn(name = "order_id")
   private Product product;

   @Column(name = "price", nullable = false)
   private Float price;


   @Column(name = "number_of_product", nullable = false)
   private int numberOfProduct;

   @Column(name = "total_money", nullable = false)
   private Float totalMoney;

   private String color;
}