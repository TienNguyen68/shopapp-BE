package com.project.shopapp.configations;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MapperConfigation {
   @Bean
   public ModelMapper modelMapper() {
      return new ModelMapper();
   }
}
