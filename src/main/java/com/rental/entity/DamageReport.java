package com.rental.entity;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "damage_report")
public class DamageReport {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "damage_id")
    private Integer damageId;

    @Column(name = "booking_id", nullable = false)
    private Integer bookingId;

    @Column(name = "vehicle_id")
    private Integer vehicleId;

    @Column(name = "damage_description", nullable = false, length = 1000)
    private String damageDescription;

    @Column(name = "repair_cost", nullable = false)
    private Double repairCost;

    @Column(name = "inspection_date", nullable = false)
    private LocalDate inspectionDate = LocalDate.now();

    @Column(name = "inspector_name")
    private String inspectorName;

    @Column(name = "billed_to_customer")
    private Boolean billedToCustomer = true;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "vehicle_id", insertable = false, updatable = false)
    private Vehicle vehicle;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "booking_id", insertable = false, updatable = false)
    private Booking booking;

    public DamageReport() {}

    public DamageReport(Integer bookingId, Integer vehicleId, String damageDescription, 
                        Double repairCost, String inspectorName) {
        this.bookingId = bookingId;
        this.vehicleId = vehicleId;
        this.damageDescription = damageDescription;
        this.repairCost = repairCost;
        this.inspectionDate = LocalDate.now();
        this.inspectorName = inspectorName;
        this.billedToCustomer = true;
    }

    public Integer getDamageId() { return damageId; }
    public void setDamageId(Integer damageId) { this.damageId = damageId; }

    public Integer getBookingId() { return bookingId; }
    public void setBookingId(Integer bookingId) { this.bookingId = bookingId; }

    public Integer getVehicleId() { return vehicleId; }
    public void setVehicleId(Integer vehicleId) { this.vehicleId = vehicleId; }

    public String getDamageDescription() { return damageDescription; }
    public void setDamageDescription(String damageDescription) { this.damageDescription = damageDescription; }

    public Double getRepairCost() { return repairCost; }
    public void setRepairCost(Double repairCost) { this.repairCost = repairCost; }

    public LocalDate getInspectionDate() { return inspectionDate; }
    public void setInspectionDate(LocalDate inspectionDate) { this.inspectionDate = inspectionDate; }

    public String getInspectorName() { return inspectorName; }
    public void setInspectorName(String inspectorName) { this.inspectorName = inspectorName; }

    public Boolean getBilledToCustomer() { return billedToCustomer; }
    public void setBilledToCustomer(Boolean billedToCustomer) { this.billedToCustomer = billedToCustomer; }

    public Vehicle getVehicle() { return vehicle; }
    public void setVehicle(Vehicle vehicle) { this.vehicle = vehicle; }

    public Booking getBooking() { return booking; }
    public void setBooking(Booking booking) { this.booking = booking; }
}
