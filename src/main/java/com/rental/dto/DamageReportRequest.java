package com.rental.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class DamageReportRequest {

    @NotNull(message = "Booking ID is required")
    private Integer bookingId;

    private Integer vehicleId;

    @NotBlank(message = "Damage description is required")
    private String damageDescription;

    @NotNull(message = "Repair cost is required")
    private Double repairCost;

    private String inspectorName;

    public DamageReportRequest() {}

    public Integer getBookingId() { return bookingId; }
    public void setBookingId(Integer bookingId) { this.bookingId = bookingId; }

    public Integer getVehicleId() { return vehicleId; }
    public void setVehicleId(Integer vehicleId) { this.vehicleId = vehicleId; }

    public String getDamageDescription() { return damageDescription; }
    public void setDamageDescription(String damageDescription) { this.damageDescription = damageDescription; }

    public Double getRepairCost() { return repairCost; }
    public void setRepairCost(Double repairCost) { this.repairCost = repairCost; }

    public String getInspectorName() { return inspectorName; }
    public void setInspectorName(String inspectorName) { this.inspectorName = inspectorName; }
}
