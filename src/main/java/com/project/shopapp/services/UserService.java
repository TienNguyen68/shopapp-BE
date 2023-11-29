package com.project.shopapp.services;

import com.project.shopapp.dtos.UserDTO;
import com.project.shopapp.exceptions.DataNotFoundException;
import com.project.shopapp.models.Role;
import com.project.shopapp.models.User;
import com.project.shopapp.repositories.RoleRepository;
import com.project.shopapp.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;

@RequiredArgsConstructor
public class UserService implements IUserService {
   private UserRepository userRepository;
   private RoleRepository roleRepository;

   @Override
   public User createUser(UserDTO userDTO) throws DataNotFoundException {
      String phoneNumber = userDTO.getPhoneNumber();

      //kiểm tra sdt có tồn tại
      if (userRepository.existsByPhoneNumber(phoneNumber)) {
         throw new DataIntegrityViolationException("Sdt tồn tại");

      }
      //converter DTO -> entity
      User newUser = User.builder()
              .fullName(userDTO.getFullName())
              .phoneNumber(userDTO.getPhoneNumber())
              .password(userDTO.getPassword())
              .address(userDTO.getAddress())
              .dateOfBirth(userDTO.getDateOfBirth())
              .facebookAccountId(userDTO.getFacebookAccountId())
              .googleAccountId(userDTO.getGoogleAccountId())
              .build();

      Role role = roleRepository.findById(userDTO.getRoleId())
              .orElseThrow(() -> new DataNotFoundException("Role không tìm thấy"));

      newUser.setRole(role);

      //kiểm tra nếu có accountId, không yêu cầu password (dùng goodle...)
      if (userDTO.getFacebookAccountId() == 0 && userDTO.getGoogleAccountId() == 0){
         String password = userDTO.getPassword();

         // nói trong phần bảo mật
         // String encodePassword = passwordEncode.encode(password);
         // newUser.setPassword(encodePassword);

      }
      return userRepository.save(newUser);
   }

   @Override
   public String login(String phoneNumber, String password) {
      // sẽ làm cùng security => khó
      return null;
   }
}
