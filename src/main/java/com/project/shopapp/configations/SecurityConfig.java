//BƯỚC 1: SECURITY
//BƯỚC 1.1 - user implements UserDetails
//Buoc 2: WebsecurityConfig: xet quyen di qua
//Buoc 3: vao userservice lam login
//Buoc4: tao component chua JWT
package com.project.shopapp.configations;

import com.project.shopapp.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {
   public final UserRepository userRepository;

   // Tạo UserDetail
   @Bean
   public UserDetailsService userDetailsService() {
      return phoneNumber -> userRepository
              .findByPhoneNumber(phoneNumber)
              .orElseThrow(() -> new UsernameNotFoundException(
                      "Khong the tim thay user voi phoneNumber= " + phoneNumber));
   }

   // Tạo mã hóa
   @Bean
   public PasswordEncoder passwordEncoder() {
      return new BCryptPasswordEncoder();
   }

   //Tao Authen
   @Bean
   public AuthenticationProvider authenticationProvider() {
      DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
      authProvider.setUserDetailsService(userDetailsService());   //userDetailsService: vua tao ben tren
      authProvider.setPasswordEncoder(passwordEncoder());   //passwordEncoder
      return authProvider;
   }

   @Bean
   public AuthenticationManager authenticationManager(
           AuthenticationConfiguration config
   ) throws Exception {
      return config.getAuthenticationManager();
   }

}
