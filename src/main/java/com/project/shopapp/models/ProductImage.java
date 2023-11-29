package com.project.shopapp.models;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Entity
@Table(name = "product_image")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductImage {
   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   private Long id;

   @Column(name = "name", nullable = false, length = 350)
   private String name;

   @ManyToOne
   @JoinColumn(name = "product_id")
   private Product product;

   @Column(name = "image_url", length = 300)
   private String imageUrl;

}
