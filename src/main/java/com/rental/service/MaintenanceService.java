package com.rental.service;

import com.rental.dto.MaintenanceRequest;
import com.rental.entity.Maintenance;
import com.rental.entity.Vehicle;
import com.rental.repository.MaintenanceRepository;
import com.rental.repository.VehicleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;

@Service
public class MaintenanceService {

    @Autowired
    private MaintenanceRepository maintenanceRepository;

    @Autowired
    private VehicleRepository vehicleRepository;

    public List<Maintenance> getAllMaintenanceRecords() {
        return maintenanceRepository.findAllByOrderByMaintenanceIdDesc();
    }

    public List<Maintenance> getMaintenanceByVehicle(Integer vehicleId) {
        return maintenanceRepository.findByVehicleId(vehicleId);
    }

    @Transactional
    public Maintenance scheduleMaintenance(MaintenanceRequest request) {
        Vehicle vehicle = vehicleRepository.findById(request.getVehicleId())
                .orElseThrow(() -> new IllegalArgumentException("Vehicle not found: " + request.getVehicleId()));

        Maintenance maintenance = new Maintenance(
                vehicle.getVehicleId(),
                vehicle.getLastServiceDate(),
                request.getNextServiceDate(),
                request.getMaintenanceStatus() != null ? request.getMaintenanceStatus() : "SCHEDULED",
                request.getDescription(),
                request.getServiceCost()
        );
        maintenance = maintenanceRepository.save(maintenance);

        // Constraint 8: "Vehicles under maintenance cannot be booked."
        // Flag vehicle as UNDER_MAINTENANCE immediately
        vehicle.setAvailability("UNDER_MAINTENANCE");
        vehicleRepository.save(vehicle);

        return maintenance;
    }

    @Transactional
    public Maintenance completeMaintenance(Integer maintenanceId, Double actualCost, String notes) {
        Maintenance maintenance = maintenanceRepository.findById(maintenanceId)
                .orElseThrow(() -> new IllegalArgumentException("Maintenance record not found: " + maintenanceId));

        maintenance.setMaintenanceStatus("COMPLETED");
        if (actualCost != null && actualCost > 0) {
            maintenance.setServiceCost(actualCost);
        }
        if (notes != null) {
            maintenance.setDescription(maintenance.getDescription() + " [Completed: " + notes + "]");
        }
        maintenance = maintenanceRepository.save(maintenance);

        // Update vehicle's lastServiceDate and make available again
        Vehicle vehicle = maintenance.getVehicle();
        if (vehicle != null) {
            vehicle.setLastServiceDate(LocalDate.now());
            vehicle.setAvailability("AVAILABLE");
            vehicleRepository.save(vehicle);
        }

        return maintenance;
    }

    /**
     * FR10 Track insurance and pollution certificate expiry
     */
    public List<Map<String, Object>> getComplianceAndServiceAlerts() {
        List<Vehicle> vehicles = vehicleRepository.findAll();
        List<Map<String, Object>> alerts = new ArrayList<>();
        LocalDate today = LocalDate.now();

        for (Vehicle v : vehicles) {
            // Insurance check
            if (v.getInsuranceExpiry() != null) {
                long daysToIns = ChronoUnit.DAYS.between(today, v.getInsuranceExpiry());
                if (daysToIns <= 30) {
                    Map<String, Object> alert = new LinkedHashMap<>();
                    alert.put("vehicleId", v.getVehicleId());
                    alert.put("vehicleTitle", v.getBrand() + " " + v.getModel());
                    alert.put("registrationNo", v.getRegistrationNo());
                    alert.put("type", "INSURANCE");
                    alert.put("expiryDate", v.getInsuranceExpiry());
                    alert.put("daysRemaining", daysToIns);
                    alert.put("severity", daysToIns < 0 ? "EXPIRED" : (daysToIns <= 7 ? "CRITICAL" : "WARNING"));
                    alerts.add(alert);
                }
            }

            // Pollution certificate check
            if (v.getPollutionExpiry() != null) {
                long daysToPuc = ChronoUnit.DAYS.between(today, v.getPollutionExpiry());
                if (daysToPuc <= 30) {
                    Map<String, Object> alert = new LinkedHashMap<>();
                    alert.put("vehicleId", v.getVehicleId());
                    alert.put("vehicleTitle", v.getBrand() + " " + v.getModel());
                    alert.put("registrationNo", v.getRegistrationNo());
                    alert.put("type", "POLLUTION_CERTIFICATE");
                    alert.put("expiryDate", v.getPollutionExpiry());
                    alert.put("daysRemaining", daysToPuc);
                    alert.put("severity", daysToPuc < 0 ? "EXPIRED" : (daysToPuc <= 7 ? "CRITICAL" : "WARNING"));
                    alerts.add(alert);
                }
            }
        }

        return alerts;
    }
}
