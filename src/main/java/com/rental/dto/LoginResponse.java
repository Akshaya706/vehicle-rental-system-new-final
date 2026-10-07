package com.rental.dto;

import com.rental.entity.Customer;

public class LoginResponse {

    private boolean success;
    private String message;
    private Integer userId;
    private String username;
    private String role; // ADMIN, RENTAL_MANAGER, CUSTOMER
    private String fullName;
    private Integer customerId;
    private Customer customer;

    public LoginResponse() {}

    public LoginResponse(boolean success, String message) {
        this.success = success;
        this.message = message;
    }

    public static LoginResponse failure(String message) {
        return new LoginResponse(false, message);
    }

    public static LoginResponse success(Integer userId, String username, String role, String fullName, Customer customer) {
        LoginResponse resp = new LoginResponse(true, "Authentication successful");
        resp.setUserId(userId);
        resp.setUsername(username);
        resp.setRole(role);
        resp.setFullName(fullName);
        if (customer != null) {
            resp.setCustomerId(customer.getCustomerId());
            resp.setCustomer(customer);
        }
        return resp;
    }

    public boolean isSuccess() { return success; }
    public void setSuccess(boolean success) { this.success = success; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public Integer getUserId() { return userId; }
    public void setUserId(Integer userId) { this.userId = userId; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }

    public Integer getCustomerId() { return customerId; }
    public void setCustomerId(Integer customerId) { this.customerId = customerId; }

    public Customer getCustomer() { return customer; }
    public void setCustomer(Customer customer) { this.customer = customer; }
}
