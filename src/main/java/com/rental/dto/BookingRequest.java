package com.rental.dto;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

public class BookingRequest {

    private Integer customerId;

    @NotNull(message = "Vehicle ID is required")
    private Integer vehicleId;

    @NotNull(message = "Rental start date is required")
    private LocalDate rentalStartDate;

    @NotNull(message = "Rental end date is required")
    private LocalDate rentalEndDate;

    private boolean insuranceWaiver;
    private boolean gpsNavigation;
    private String notes;

    public BookingRequest() {}

    public Integer getCustomerId() { return customerId; }
    public void setCustomerId(Integer customerId) { this.customerId = customerId; }

    public Integer getVehicleId() { return vehicleId; }
    public void setVehicleId(Integer vehicleId) { this.vehicleId = vehicleId; }

    public LocalDate getRentalStartDate() { return rentalStartDate; }
    public void setRentalStartDate(LocalDate rentalStartDate) { this.rentalStartDate = rentalStartDate; }

    public LocalDate getRentalEndDate() { return rentalEndDate; }
    public void setRentalEndDate(LocalDate rentalEndDate) { this.rentalEndDate = rentalEndDate; }

    public boolean isInsuranceWaiver() { return insuranceWaiver; }
    public void setInsuranceWaiver(boolean insuranceWaiver) { this.insuranceWaiver = insuranceWaiver; }

    public boolean isGpsNavigation() { return gpsNavigation; }
    public void setGpsNavigation(boolean gpsNavigation) { this.gpsNavigation = gpsNavigation; }

    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }
}
