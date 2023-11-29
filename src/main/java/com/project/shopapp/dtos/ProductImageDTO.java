package com.project.shopapp.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.*;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductImageDTO {

   @JsonProperty("product_id")
   @Min(value = 1, message = "Id phai lon hon 0")
   private Long productId;

   @Size(min = 5, max = 200, message = "Tên từ 3 đến 200 ký tự")
   @JsonProperty("image_url")
   private String imageUrl;
}
