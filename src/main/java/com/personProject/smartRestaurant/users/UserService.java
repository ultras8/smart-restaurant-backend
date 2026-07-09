package com.personProject.smartRestaurant.users;

import com.personProject.smartRestaurant.employees.EmployeeRepository;
import com.personProject.smartRestaurant.entities.Employee;
import com.personProject.smartRestaurant.entities.User;
import com.personProject.smartRestaurant.enums.UserRole;
import com.personProject.smartRestaurant.enums.UserStatus;
import com.personProject.smartRestaurant.employees.dto.EmployeeRequest;
import com.personProject.smartRestaurant.users.dto.LoginRequest;
import com.personProject.smartRestaurant.users.dto.UserResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Transactional
    public UserResponse createUser(User user) {
        if(userRepository.existsByUsername(user.getUsername())) {
            throw new RuntimeException("ชื่อผู้ใช้งานนี้มีในระบบแล้ว");
        }
        if(userRepository.existsByEmail(user.getEmail())) {
            throw new RuntimeException("อีเมลนี้มีในระบบแล้ว");
        }

        user.setPassword(passwordEncoder.encode(user.getPassword())); //Hash password
        user.setRole(UserRole.OWNER);
        User savedUser = userRepository.save(user);

        UserResponse response = new UserResponse();
        response.setId(savedUser.getId());
        response.setUsername(savedUser.getUsername());
        response.setEmail(savedUser.getEmail());
        response.setRole(savedUser.getRole());
        return response;
    }

    @Transactional
    public UserResponse registerEmployee(EmployeeRequest req) {
        String code = req.getEmployeeCode();

        Employee profile = employeeRepository.findByEmployeeCode(code)
                .orElseThrow(() -> new RuntimeException("ไม่พบรหัสพนักงานนี้ในระบบ"));

        if (profile.getUser() != null){
            throw new RuntimeException("รหัสพนักงานนี้ถูกลงทะเบียนใช้งานไปแล้ว");
        }

        if (profile.getStatus() == UserStatus.BANNED) {
            throw new RuntimeException("รหัสพนักงานนี้ถูกระงับสิทธิ์การใช้งาน");
        }

        User user = new User();
        user.setUsername(code);
        user.setEmail(code+"@smartrest.com");
        user.setPassword(passwordEncoder.encode(req.getPassword()));
        user.setRole(UserRole.EMPLOYEE);

        User savedUser = userRepository.save(user);

        profile.setUser(savedUser);
        employeeRepository.save(profile);

        UserResponse response = new UserResponse();
        response.setId(savedUser.getId());
        response.setUsername(savedUser.getUsername());
        response.setEmail(savedUser.getEmail());
        response.setRole(savedUser.getRole());
        return response;
    }

    @Transactional
    public UserResponse login(LoginRequest request) {
        User user = userRepository.findByUsernameOrEmail(request.getUsername(), request.getUsername())
                .orElseThrow(() -> new RuntimeException("ไม่พบชื่อผู้ใช้งานหรืออีเมลนี้ในระบบค่ะ"));

        if (user.getRole() == UserRole.EMPLOYEE && user.getEmployee() != null) {
            if (user.getEmployee().getStatus() == UserStatus.BANNED) {
                throw new RuntimeException("บัญชีของคุณถูกระงับการใช้งานชั่วคราว");
            }
        }

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        user.getUsername(),
                        request.getPassword()
                )
        );

        String token = jwtService.generateToken(user);

        UserResponse response = new UserResponse();
        response.setId(user.getId());
        response.setUsername(user.getUsername());
        response.setEmail(user.getEmail());
        response.setToken(token);
        response.setRole(user.getRole());
        return response;
    }

    @Transactional(readOnly = true)
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

    @Transactional(readOnly = true)
    public UserResponse getUserById(UUID id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException(("ไม่พบข้อมูลผู้ใช้งาน id: " + id)));
        UserResponse response = new UserResponse();
        response.setId(user.getId());
        response.setUsername(user.getUsername());
        response.setEmail(user.getEmail());
        return response;
    }

    @Transactional
    public User updateUser(UUID id, User userDetail) {
        User user = userRepository.findById(id)
                        .orElseThrow(() -> new RuntimeException("ไม่พบข้อมูลผู้ใช้งาน id: " + id));
        user.setUsername(userDetail.getUsername());
        user.setEmail(userDetail.getEmail());
        if(userDetail.getPassword() != null && !userDetail.getPassword().isEmpty()) {
            user.setPassword(passwordEncoder.encode(userDetail.getPassword()));
        }
        return userRepository.save(user);
    }

    @Transactional
    public void deleteUser(UUID id) {
        if(!userRepository.existsById(id)) {
            throw  new RuntimeException("ไม่พบข้อมูลผู้ใช้งาน id: " + id);
        }
        userRepository.deleteById(id);
    }
}
