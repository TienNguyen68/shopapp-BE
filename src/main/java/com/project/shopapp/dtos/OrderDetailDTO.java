package com.project.shopapp.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Min;
import lombok.*;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class OrderDetailDTO {

   @JsonProperty("order_id")
   @Min(value = 1, message = "Order ID phải > 0")
   private Long orderId;

   @JsonProperty("product_id")
   @Min(value = 1, message = "Order ID phải > 0")
   private Long productId;

   @Min(value = 1, message = "Price phải >= 0")
   private String price;


   @JsonProperty("number_of_product")
   @Min(value = 1, message = "Số lượng sản phẩm > 0")
   private String numberOfProduct;

   @JsonProperty("total_money")
   @Min(value = 1, message = "Tổng tiền >= 0")
      private String totalMoney;

   private String color;


}
