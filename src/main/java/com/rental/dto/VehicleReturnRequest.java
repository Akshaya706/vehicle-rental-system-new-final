package com.rental.dto;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

public class VehicleReturnRequest {

    @NotNull(message = "Booking ID is required")
    private Integer bookingId;

    @NotNull(message = "Return date is required")
    private LocalDate returnDate;

    @NotNull(message = "Odometer reading is required")
    private Integer odometerReading;

    @NotNull(message = "Fuel level is required")
    private String fuelLevel; // Full, 75%, 50%, 25%, Low

    private boolean damageFound;
    private String damageDescription;
    private Double estimatedRepairCost;
    private String inspectorNotes;

    public VehicleReturnRequest() {}

    public Integer getBookingId() { return bookingId; }
    public void setBookingId(Integer bookingId) { this.bookingId = bookingId; }

    public LocalDate getReturnDate() { return returnDate; }
    public void setReturnDate(LocalDate returnDate) { this.returnDate = returnDate; }

    public Integer getOdometerReading() { return odometerReading; }
    public void setOdometerReading(Integer odometerReading) { this.odometerReading = odometerReading; }

    public String getFuelLevel() { return fuelLevel; }
    public void setFuelLevel(String fuelLevel) { this.fuelLevel = fuelLevel; }

    public boolean isDamageFound() { return damageFound; }
    public void setDamageFound(boolean damageFound) { this.damageFound = damageFound; }

    public String getDamageDescription() { return damageDescription; }
    public void setDamageDescription(String damageDescription) { this.damageDescription = damageDescription; }

    public Double getEstimatedRepairCost() { return estimatedRepairCost; }
    public void setEstimatedRepairCost(Double estimatedRepairCost) { this.estimatedRepairCost = estimatedRepairCost; }

    public String getInspectorNotes() { return inspectorNotes; }
    public void setInspectorNotes(String inspectorNotes) { this.inspectorNotes = inspectorNotes; }
}
