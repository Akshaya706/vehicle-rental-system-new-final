package com.rental.service;

import com.rental.entity.Vehicle;
import com.rental.repository.VehicleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class VehicleService {

    @Autowired
    private VehicleRepository vehicleRepository;

    public List<Vehicle> getAllVehicles() {
        return vehicleRepository.findAll();
    }

    public Vehicle getVehicleById(Integer id) {
        return vehicleRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Vehicle not found with ID: " + id));
    }

    public List<Vehicle> searchVehicles(String type, String brand, String model, Double maxRate, String availability) {
        String cleanType = (type != null && !type.isBlank() && !type.equalsIgnoreCase("ALL")) ? type.trim() : null;
        String cleanBrand = (brand != null && !brand.isBlank()) ? brand.trim() : null;
        String cleanModel = (model != null && !model.isBlank()) ? model.trim() : null;
        String cleanAvail = (availability != null && !availability.isBlank() && !availability.equalsIgnoreCase("ALL")) ? availability.trim() : null;

        return vehicleRepository.searchVehicles(cleanType, cleanBrand, cleanModel, maxRate, cleanAvail);
    }

    public List<Vehicle> getAvailableVehicles() {
        return vehicleRepository.findByAvailability("AVAILABLE");
    }

    @Transactional
    public Vehicle addVehicle(Vehicle vehicle) {
        if (vehicleRepository.existsByRegistrationNo(vehicle.getRegistrationNo().trim())) {
            throw new IllegalArgumentException("Duplicate registration number: A vehicle with Registration No '" + 
                                              vehicle.getRegistrationNo() + "' already exists.");
        }
        if (vehicle.getDailyRate() == null || vehicle.getDailyRate() <= 0) {
            throw new IllegalArgumentException("Daily rental rate must be greater than zero.");
        }
        if (vehicle.getAvailability() == null || vehicle.getAvailability().isBlank()) {
            vehicle.setAvailability("AVAILABLE");
        }
        return vehicleRepository.save(vehicle);
    }

    @Transactional
    public Vehicle updateVehicle(Integer id, Vehicle updated) {
        Vehicle existing = getVehicleById(id);
        
        if (!existing.getRegistrationNo().equalsIgnoreCase(updated.getRegistrationNo().trim())) {
            if (vehicleRepository.existsByRegistrationNo(updated.getRegistrationNo().trim())) {
                throw new IllegalArgumentException("Registration No '" + updated.getRegistrationNo() + "' is already in use by another vehicle.");
            }
            existing.setRegistrationNo(updated.getRegistrationNo().trim());
        }

        existing.setVehicleType(updated.getVehicleType());
        existing.setBrand(updated.getBrand());
        existing.setModel(updated.getModel());
        existing.setDailyRate(updated.getDailyRate());
        if (updated.getAvailability() != null) {
            existing.setAvailability(updated.getAvailability());
        }
        existing.setLastServiceDate(updated.getLastServiceDate());
        existing.setInsuranceExpiry(updated.getInsuranceExpiry());
        existing.setPollutionExpiry(updated.getPollutionExpiry());
        if (updated.getSeatingCapacity() != null) {
            existing.setSeatingCapacity(updated.getSeatingCapacity());
        }
        if (updated.getFuelType() != null) {
            existing.setFuelType(updated.getFuelType());
        }
        if (updated.getTransmission() != null) {
            existing.setTransmission(updated.getTransmission());
        }
        if (updated.getImageUrl() != null && !updated.getImageUrl().isBlank()) {
            existing.setImageUrl(updated.getImageUrl());
        }

        return vehicleRepository.save(existing);
    }

    @Transactional
    public void deleteVehicle(Integer id) {
        Vehicle vehicle = getVehicleById(id);
        vehicleRepository.delete(vehicle);
    }

    @Transactional
    public Vehicle updateAvailability(Integer id, String status) {
        Vehicle vehicle = getVehicleById(id);
        vehicle.setAvailability(status);
        return vehicleRepository.save(vehicle);
    }
}
