package com.bridgelabz.AddressBook_Workshop.service;

import com.bridgelabz.AddressBook_Workshop.dto.UserDTO;
import com.bridgelabz.AddressBook_Workshop.model.User;
import com.bridgelabz.AddressBook_Workshop.repository.UserRepository;
import com.bridgelabz.AddressBook_Workshop.security.JwtUtil;
import com.bridgelabz.AddressBook_Workshop.security.PasswordEncoderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoderService passwordEncoderService;

    @Autowired
    private JwtUtil jwtUtil;

    public String registerUser(UserDTO userDTO) {
        if (userRepository.findByEmail(userDTO.getEmail()).isPresent()) {
            throw new RuntimeException("Email already exists");
        }

        User user = new User();
        user.setName(userDTO.getName());
        user.setEmail(userDTO.getEmail());
        user.setPassword(passwordEncoderService.encodePassword(userDTO.getPassword()));
        user.setRole("USER");

        userRepository.save(user);
        return "User registered successfully!";
    }

    public String loginUser(String email, String password) {
        Optional<User> userOpt = userRepository.findByEmail(email);
        if (userOpt.isEmpty() || !passwordEncoderService.matches(password, userOpt.get().getPassword())) {
            throw new RuntimeException("Invalid email or password");
        }

        return jwtUtil.generateToken(email);
    }
}
