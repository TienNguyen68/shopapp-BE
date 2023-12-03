package com.project.shopapp.services;

import com.project.shopapp.components.JwtTokenUtil;
import com.project.shopapp.dtos.UserDTO;
import com.project.shopapp.exceptions.DataNotFoundException;
import com.project.shopapp.models.Role;
import com.project.shopapp.models.User;
import com.project.shopapp.repositories.RoleRepository;
import com.project.shopapp.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService implements IUserService {
   private UserRepository userRepository;
   private RoleRepository roleRepository;

   private final PasswordEncoder passwordEncoder;

   private final JwtTokenUtil jwtTokenUtil;
   private final AuthenticationManager authenticationManager;

   @Override
   public User createUser(UserDTO userDTO) throws DataNotFoundException {
      // register user
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
          String encodePassword = passwordEncoder.encode(password);
          newUser.setPassword(encodePassword);

      }
      return userRepository.save(newUser);
   }

   @Override
   public String login(String phoneNumber, String password) throws Exception {
      Optional <User> optionalUser = userRepository.findByPhoneNumber(phoneNumber);
      if(optionalUser.isEmpty()){
         throw new DataNotFoundException("Sai phoneNumber & password");
      }
//      return optionalUser.get(); //tra ve token (JWT)
      User existingUser = optionalUser.get();
      //check pass
      if (existingUser.getFacebookAccountId() == 0 && existingUser.getGoogleAccountId() == 0){
         if(!passwordEncoder.matches(password,existingUser.getPassword() )){  //kiem tra mk nhap voi mk data
throw  new BadCredentialsException("Sai phone & pass");
         }
      }

      UsernamePasswordAuthenticationToken authenticationToken =
              new UsernamePasswordAuthenticationToken(phoneNumber, password    );
      //authentication with java spring
      authenticationManager.authenticate(authenticationToken);
      return jwtTokenUtil.generateToken(existingUser);
   }
}
