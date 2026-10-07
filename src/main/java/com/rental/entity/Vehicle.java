package com.rental.entity;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "vehicle")
public class Vehicle {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "vehicle_id")
    private Integer vehicleId;

    @Column(name = "vehicle_type", nullable = false)
    private String vehicleType; // Car, Bike, SUV, Sedan, Luxury, Electric

    @Column(nullable = false)
    private String brand;

    @Column(nullable = false)
    private String model;

    @Column(name = "registration_no", nullable = false, unique = true)
    private String registrationNo;

    @Column(name = "daily_rate", nullable = false)
    private Double dailyRate;

    @Column(nullable = false)
    private String availability = "AVAILABLE"; // AVAILABLE, BOOKED, RENTED, UNDER_MAINTENANCE

    @Column(name = "last_service_date")
    private LocalDate lastServiceDate;

    @Column(name = "insurance_expiry")
    private LocalDate insuranceExpiry;

    @Column(name = "pollution_expiry")
    private LocalDate pollutionExpiry;

    @Column(name = "seating_capacity")
    private Integer seatingCapacity = 5;

    @Column(name = "fuel_type")
    private String fuelType = "Petrol"; // Petrol, Diesel, Electric, Hybrid

    private String transmission = "Automatic"; // Automatic, Manual

    @Column(name = "image_url")
    private String imageUrl;

    public Vehicle() {}

    public Vehicle(String vehicleType, String brand, String model, String registrationNo, 
                   Double dailyRate, String availability, LocalDate lastServiceDate, 
                   LocalDate insuranceExpiry, LocalDate pollutionExpiry, Integer seatingCapacity, 
                   String fuelType, String transmission, String imageUrl) {
        this.vehicleType = vehicleType;
        this.brand = brand;
        this.model = model;
        this.registrationNo = registrationNo;
        this.dailyRate = dailyRate;
        this.availability = availability != null ? availability : "AVAILABLE";
        this.lastServiceDate = lastServiceDate;
        this.insuranceExpiry = insuranceExpiry;
        this.pollutionExpiry = pollutionExpiry;
        this.seatingCapacity = seatingCapacity;
        this.fuelType = fuelType;
        this.transmission = transmission;
        this.imageUrl = imageUrl;
    }

    public Integer getVehicleId() { return vehicleId; }
    public void setVehicleId(Integer vehicleId) { this.vehicleId = vehicleId; }

    public String getVehicleType() { return vehicleType; }
    public void setVehicleType(String vehicleType) { this.vehicleType = vehicleType; }

    public String getBrand() { return brand; }
    public void setBrand(String brand) { this.brand = brand; }

    public String getModel() { return model; }
    public void setModel(String model) { this.model = model; }

    public String getRegistrationNo() { return registrationNo; }
    public void setRegistrationNo(String registrationNo) { this.registrationNo = registrationNo; }

    public Double getDailyRate() { return dailyRate; }
    public void setDailyRate(Double dailyRate) { this.dailyRate = dailyRate; }

    public String getAvailability() { return availability; }
    public void setAvailability(String availability) { this.availability = availability; }

    public LocalDate getLastServiceDate() { return lastServiceDate; }
    public void setLastServiceDate(LocalDate lastServiceDate) { this.lastServiceDate = lastServiceDate; }

    public LocalDate getInsuranceExpiry() { return insuranceExpiry; }
    public void setInsuranceExpiry(LocalDate insuranceExpiry) { this.insuranceExpiry = insuranceExpiry; }

    public LocalDate getPollutionExpiry() { return pollutionExpiry; }
    public void setPollutionExpiry(LocalDate pollutionExpiry) { this.pollutionExpiry = pollutionExpiry; }

    public Integer getSeatingCapacity() { return seatingCapacity; }
    public void setSeatingCapacity(Integer seatingCapacity) { this.seatingCapacity = seatingCapacity; }

    public String getFuelType() { return fuelType; }
    public void setFuelType(String fuelType) { this.fuelType = fuelType; }

    public String getTransmission() { return transmission; }
    public void setTransmission(String transmission) { this.transmission = transmission; }

    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }
}
