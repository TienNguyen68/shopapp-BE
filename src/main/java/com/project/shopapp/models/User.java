package com.project.shopapp.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "users")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class User extends BaseEntity {
   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   private Long id;

   @Column(name = "full_name",   length = 100)
   private String fullName;

   @Column(name = "phone_number",length = 10, nullable = false)
   private String phoneNumber;


   @Column(name = "address",  length = 200)
   private String address;

   @Column(name = "password",  length = 200, nullable = false)
   private String password;

   private boolean active;

   @Column(name = "date_of_birth")
   private String dateOfBirth;

   @Column(name = "facebook_account_id")
   private String facebookAccountId;

   @Column(name = "google_account_id")
   private String googleAccountId;

   @ManyToOne
   @Column(name = "role_id")
   private Role roleId;

}
