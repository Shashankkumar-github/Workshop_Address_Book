package com.bridgelabz.AddressBook_Workshop.service;

import com.bridgelabz.AddressBook_Workshop.dto.UserDTO;

public interface IUserService {
    String registerUser(UserDTO userDTO);
    String loginUser(String email, String password);
}
