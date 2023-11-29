package com.project.shopapp.service;

import com.project.shopapp.dtos.UserDTO;
import com.project.shopapp.models.User;

public interface IUserService {
   User createUser(UserDTO userDTO);

   String login (String phoneNumber, String password);
}
