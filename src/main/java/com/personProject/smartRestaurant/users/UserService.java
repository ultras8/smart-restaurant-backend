package com.personProject.smartRestaurant.users;

import com.personProject.smartRestaurant.entities.User;
import com.personProject.smartRestaurant.users.dto.UserResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public UserResponse createUser(User user) {
        if(userRepository.existsByUsername(user.getUsername())) {
            throw new RuntimeException("ชื่อผู้ใช้งานนี้มีในระบบแล้ว");
        }
        if(userRepository.existsByEmail(user.getEmail())) {
            throw new RuntimeException("อีเมลนี้มีในระบบแล้ว");
        }

        user.setPassword(passwordEncoder.encode(user.getPassword())); //Hash password
        User savedUser = userRepository.save(user);

        UserResponse response = new UserResponse();
        response.setId(savedUser.getId());
        response.setUsername(savedUser.getUsername());
        response.setEmail(savedUser.getEmail());
        return response;
    }

    public List<UserResponse> getAllUsers() {
        List<User> users = userRepository.findAll();

        return users.stream().map(user -> {
            UserResponse response = new UserResponse();
            response.setId(user.getId());
            response.setUsername(user.getUsername());
            response.setEmail(user.getEmail());
            return response;
        }).collect(Collectors.toList());
    }

    public UserResponse getUserById(UUID id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException(("User not found with id: "+id)));
        UserResponse response = new UserResponse();
        response.setId(user.getId());
        response.setUsername(user.getUsername());
        response.setEmail(user.getEmail());
        return response;
    }

    public User updateUser(UUID id, User userDetail) {
        User user = userRepository.findById(id)
                        .orElseThrow(() -> new RuntimeException("User not found with id: " + id));
        user.setUsername(userDetail.getUsername());
        user.setEmail(userDetail.getEmail());
        if(userDetail.getPassword() != null && !userDetail.getPassword().isEmpty()) {
            user.setPassword(passwordEncoder.encode(userDetail.getPassword()));
        }
        return userRepository.save(user);
    }

    public void deleteUser(UUID id) {
        if(!userRepository.existsById(id)) {
            throw  new RuntimeException("User not found with id: " + id);
        }
        userRepository.deleteById(id);
    }
}
