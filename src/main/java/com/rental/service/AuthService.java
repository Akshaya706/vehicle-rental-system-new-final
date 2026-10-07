package com.rental.service;

import com.rental.dto.LoginRequest;
import com.rental.dto.LoginResponse;
import com.rental.dto.RegisterCustomerRequest;
import com.rental.entity.Customer;
import com.rental.entity.User;
import com.rental.repository.CustomerRepository;
import com.rental.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CustomerRepository customerRepository;

    public LoginResponse authenticate(LoginRequest request) {
        Optional<User> userOpt = userRepository.findByUsername(request.getUsername().trim());
        if (userOpt.isEmpty()) {
            return LoginResponse.failure("Invalid username or user does not exist");
        }

        User user = userOpt.get();
        if (!user.getPassword().equals(request.getPassword())) {
            return LoginResponse.failure("Invalid password credentials");
        }

        if (request.getRequiredRole() != null && !request.getRequiredRole().isBlank()) {
            if (!user.getRole().equalsIgnoreCase(request.getRequiredRole())) {
                return LoginResponse.failure("Access denied: Account role (" + user.getRole() + 
                                            ") is not authorized for " + request.getRequiredRole() + " portal");
            }
        }

        Customer customer = null;
        if ("CUSTOMER".equalsIgnoreCase(user.getRole())) {
            customer = customerRepository.findByUserId(user.getUserId()).orElse(null);
        }

        return LoginResponse.success(user.getUserId(), user.getUsername(), user.getRole(), user.getFullName(), customer);
    }

    @Transactional
    public LoginResponse registerCustomer(RegisterCustomerRequest request) {
        if (userRepository.existsByUsername(request.getUsername().trim())) {
            return LoginResponse.failure("Username '" + request.getUsername() + "' is already taken");
        }

        if (customerRepository.existsByDrivingLicence(request.getDrivingLicence().trim())) {
            return LoginResponse.failure("Driving Licence '" + request.getDrivingLicence() + "' is already registered");
        }

        // Validate driving license basic format (alphanumeric, at least 6 chars)
        String dl = request.getDrivingLicence().trim();
        if (dl.length() < 5) {
            return LoginResponse.failure("Driving licence number must be at least 5 characters");
        }

        User user = new User(
            request.getUsername().trim(),
            request.getPassword(),
            "CUSTOMER",
            request.getName().trim(),
            request.getEmail() != null ? request.getEmail().trim() : ""
        );
        user = userRepository.save(user);

        Customer customer = new Customer(
            user.getUserId(),
            request.getName().trim(),
            request.getAddress().trim(),
            request.getPhone().trim(),
            dl
        );
        customer = customerRepository.save(customer);

        return LoginResponse.success(user.getUserId(), user.getUsername(), user.getRole(), user.getFullName(), customer);
    }
}
