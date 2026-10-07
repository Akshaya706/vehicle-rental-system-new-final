package com.rental.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "customer")
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "customer_id")
    private Integer customerId;

    @Column(name = "user_id")
    private Integer userId;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String address;

    @Column(nullable = false)
    private String phone;

    @Column(name = "driving_licence", nullable = false, unique = true)
    private String drivingLicence;

    public Customer() {}

    public Customer(Integer userId, String name, String address, String phone, String drivingLicence) {
        this.userId = userId;
        this.name = name;
        this.address = address;
        this.phone = phone;
        this.drivingLicence = drivingLicence;
    }

    public Integer getCustomerId() { return customerId; }
    public void setCustomerId(Integer customerId) { this.customerId = customerId; }

    public Integer getUserId() { return userId; }
    public void setUserId(Integer userId) { this.userId = userId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getDrivingLicence() { return drivingLicence; }
    public void setDrivingLicence(String drivingLicence) { this.drivingLicence = drivingLicence; }
}
