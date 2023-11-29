package com.project.shopapp.models;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "products")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Product extends BaseEntity { //chứa tgian tạo-update
   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   private Long id;

   @Column(name = "name", nullable = false, length = 350)
   private String name;

   private Float price;

   @Column(name = "thumbnail", length = 300)
   private String thumbnail;

   @Column(name = "description", length = 1000)
   private String description;

   @ManyToOne
   @Column(name = "category_id")
   private Category categoryId;
}