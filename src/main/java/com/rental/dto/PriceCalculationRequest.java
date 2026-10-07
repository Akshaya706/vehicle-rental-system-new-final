package com.rental.dto;

import java.time.LocalDate;

public class PriceCalculationRequest {
    private Integer vehicleId;
    private LocalDate startDate;
    private LocalDate endDate;
    private boolean insuranceWaiver;
    private boolean gpsNavigation;

    public PriceCalculationRequest() {}

    public Integer getVehicleId() { return vehicleId; }
    public void setVehicleId(Integer vehicleId) { this.vehicleId = vehicleId; }

    public LocalDate getStartDate() { return startDate; }
    public void setStartDate(LocalDate startDate) { this.startDate = startDate; }

    public LocalDate getEndDate() { return endDate; }
    public void setEndDate(LocalDate endDate) { this.endDate = endDate; }

    public boolean isInsuranceWaiver() { return insuranceWaiver; }
    public void setInsuranceWaiver(boolean insuranceWaiver) { this.insuranceWaiver = insuranceWaiver; }

    public boolean isGpsNavigation() { return gpsNavigation; }
    public void setGpsNavigation(boolean gpsNavigation) { this.gpsNavigation = gpsNavigation; }
}
