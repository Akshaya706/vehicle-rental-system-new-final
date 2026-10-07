package com.rental.dto;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

public class MaintenanceRequest {

    @NotNull(message = "Vehicle ID is required")
    private Integer vehicleId;

    private LocalDate lastServiceDate;

    @NotNull(message = "Next service date is required")
    private LocalDate nextServiceDate;

    private String maintenanceStatus = "SCHEDULED";
    private String description;
    private Double serviceCost = 0.0;

    public MaintenanceRequest() {}

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
}
