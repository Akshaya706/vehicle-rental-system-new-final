package com.rental.entity;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "maintenance")
public class Maintenance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "maintenance_id")
    private Integer maintenanceId;

    @Column(name = "vehicle_id", nullable = false)
    private Integer vehicleId;

    @Column(name = "last_service_date")
    private LocalDate lastServiceDate;

    @Column(name = "next_service_date", nullable = false)
    private LocalDate nextServiceDate;

    @Column(name = "maintenance_status", nullable = false)
    private String maintenanceStatus = "SCHEDULED"; // SCHEDULED, IN_PROGRESS, COMPLETED

    private String description;

    @Column(name = "service_cost")
    private Double serviceCost = 0.0;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "vehicle_id", insertable = false, updatable = false)
    private Vehicle vehicle;

    public Maintenance() {}

    public Maintenance(Integer vehicleId, LocalDate lastServiceDate, LocalDate nextServiceDate, 
                       String maintenanceStatus, String description, Double serviceCost) {
        this.vehicleId = vehicleId;
        this.lastServiceDate = lastServiceDate;
        this.nextServiceDate = nextServiceDate;
        this.maintenanceStatus = maintenanceStatus != null ? maintenanceStatus : "SCHEDULED";
        this.description = description;
        this.serviceCost = serviceCost != null ? serviceCost : 0.0;
    }

    public Integer getMaintenanceId() { return maintenanceId; }
    public void setMaintenanceId(Integer maintenanceId) { this.maintenanceId = maintenanceId; }

    public Integer getVehicleId() { return vehicleId; }
    public void setVehicleId(Integer vehicleId) { this.vehicleId = vehicleId; }

    public LocalDate getLastServiceDate() { return lastServiceDate; }
    public void setLastServiceDate(LocalDate lastServiceDate) { this.lastServiceDate = lastServiceDate; }

    public LocalDate getNextServiceDate() { return nextServiceDate; }
    public void setNextServiceDate(LocalDate nextServiceDate) { this.nextServiceDate = nextServiceDate; }

    public String getMaintenanceStatus() { return maintenanceStatus; }
    public void setMaintenanceStatus(String maintenanceStatus) { this.maintenanceStatus = maintenanceStatus; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public Double getServiceCost() { return serviceCost; }
    public void setServiceCost(Double serviceCost) { this.serviceCost = serviceCost; }

    public Vehicle getVehicle() { return vehicle; }
    public void setVehicle(Vehicle vehicle) { this.vehicle = vehicle; }
}
