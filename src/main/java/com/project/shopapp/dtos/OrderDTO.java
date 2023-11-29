package com.project.shopapp.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class OrderDTO {
   @JsonProperty("user_id")
   @Min(value = 1, message = "User's ID must be >o")
   private Long userId;

   @JsonProperty("fullname")
   private String fullName;

   private String email;

   @JsonProperty("phone_number")
   @NotBlank(message = "Phone number is required")
   @Size(min = 5, message = "Phone number must be at least 5 characters")
   private String phoneNumber;

   private String address;

   private String note;

   @JsonProperty("total_money")
   @Min(value = 1, message = "Total money must be >= o")
   private Float totalMoney;

   @JsonProperty("shiping_method")
   private String shipingMethod;

   @JsonProperty("shiping_address")
   private String shipingAddress;

   @JsonProperty("payment_method")
   private String paymentMethod;


}
