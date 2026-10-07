package com.rental.dto;

import jakarta.validation.constraints.NotBlank;

public class RegisterCustomerRequest {

    @NotBlank(message = "Full name is required")
    private String name;

    @NotBlank(message = "Username is required")
    private String username;

    @NotBlank(message = "Password is required")
    private String password;

    @NotBlank(message = "Phone number is required")
    private String phone;

    @NotBlank(message = "Address is required")
    private String address;

    @NotBlank(message = "Driving licence number is required")
    private String drivingLicence;

    private String email;

    public RegisterCustomerRequest() {}

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public String getDrivingLicence() { return drivingLicence; }
    public void setDrivingLicence(String drivingLicence) { this.drivingLicence = drivingLicence; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
}
