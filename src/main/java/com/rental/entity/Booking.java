package com.rental.entity;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "booking")
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "booking_id")
    private Integer bookingId;

    @Column(name = "customer_id", nullable = false)
    private Integer customerId;

    @Column(name = "vehicle_id", nullable = false)
    private Integer vehicleId;

    @Column(name = "booking_date", nullable = false)
    private LocalDate bookingDate = LocalDate.now();

    @Column(name = "rental_start_date", nullable = false)
    private LocalDate rentalStartDate;

    @Column(name = "rental_end_date", nullable = false)
    private LocalDate rentalEndDate;

    @Column(name = "booking_status", nullable = false)
    private String bookingStatus = "PENDING_APPROVAL"; 
    // PENDING_APPROVAL, APPROVED, REJECTED, ACTIVE, COMPLETED, CANCELLED

    @Column(name = "total_charge", nullable = false)
    private Double totalCharge;

    @Column(name = "base_daily_rate")
    private Double baseDailyRate;

    @Column(name = "rental_days")
    private Integer rentalDays;

    @Column(name = "dynamic_multiplier")
    private Double dynamicMultiplier = 1.0;

    @Column(name = "extra_charges")
    private Double extraCharges = 0.0;

    @Column(name = "actual_return_date")
    private LocalDate actualReturnDate;

    @Column(name = "return_odometer")
    private Integer returnOdometer;

    @Column(name = "return_fuel_level")
    private String returnFuelLevel;

    @Column(name = "late_fee")
    private Double lateFee = 0.0;

    @Column(name = "damage_fee")
    private Double damageFee = 0.0;

    @Column(name = "extension_charge")
    private Double extensionCharge = 0.0;

    @Column(name = "payment_status")
    private String paymentStatus = "PENDING"; // PENDING, PAID, REFUNDED

    @Column(name = "manager_notes")
    private String managerNotes;

    // Helper references (read-only relations)
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "vehicle_id", insertable = false, updatable = false)
    private Vehicle vehicle;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "customer_id", insertable = false, updatable = false)
    private Customer customer;

    public Booking() {}

    public Booking(Integer customerId, Integer vehicleId, LocalDate rentalStartDate, LocalDate rentalEndDate, 
                   Double totalCharge, Double baseDailyRate, Integer rentalDays, Double dynamicMultiplier, Double extraCharges) {
        this.customerId = customerId;
        this.vehicleId = vehicleId;
        this.bookingDate = LocalDate.now();
        this.rentalStartDate = rentalStartDate;
        this.rentalEndDate = rentalEndDate;
        this.totalCharge = totalCharge;
        this.baseDailyRate = baseDailyRate;
        this.rentalDays = rentalDays;
        this.dynamicMultiplier = dynamicMultiplier;
        this.extraCharges = extraCharges;
        this.bookingStatus = "PENDING_APPROVAL";
        this.paymentStatus = "PENDING";
    }

    public Integer getBookingId() { return bookingId; }
    public void setBookingId(Integer bookingId) { this.bookingId = bookingId; }

    public Integer getCustomerId() { return customerId; }
    public void setCustomerId(Integer customerId) { this.customerId = customerId; }

    public Integer getVehicleId() { return vehicleId; }
    public void setVehicleId(Integer vehicleId) { this.vehicleId = vehicleId; }

    public LocalDate getBookingDate() { return bookingDate; }
    public void setBookingDate(LocalDate bookingDate) { this.bookingDate = bookingDate; }

    public LocalDate getRentalStartDate() { return rentalStartDate; }
    public void setRentalStartDate(LocalDate rentalStartDate) { this.rentalStartDate = rentalStartDate; }

    public LocalDate getRentalEndDate() { return rentalEndDate; }
    public void setRentalEndDate(LocalDate rentalEndDate) { this.rentalEndDate = rentalEndDate; }

    public String getBookingStatus() { return bookingStatus; }
    public void setBookingStatus(String bookingStatus) { this.bookingStatus = bookingStatus; }

    public Double getTotalCharge() { return totalCharge; }
    public void setTotalCharge(Double totalCharge) { this.totalCharge = totalCharge; }

    public Double getBaseDailyRate() { return baseDailyRate; }
    public void setBaseDailyRate(Double baseDailyRate) { this.baseDailyRate = baseDailyRate; }

    public Integer getRentalDays() { return rentalDays; }
    public void setRentalDays(Integer rentalDays) { this.rentalDays = rentalDays; }

    public Double getDynamicMultiplier() { return dynamicMultiplier; }
    public void setDynamicMultiplier(Double dynamicMultiplier) { this.dynamicMultiplier = dynamicMultiplier; }

    public Double getExtraCharges() { return extraCharges; }
    public void setExtraCharges(Double extraCharges) { this.extraCharges = extraCharges; }

    public LocalDate getActualReturnDate() { return actualReturnDate; }
    public void setActualReturnDate(LocalDate actualReturnDate) { this.actualReturnDate = actualReturnDate; }

    public Integer getReturnOdometer() { return returnOdometer; }
    public void setReturnOdometer(Integer returnOdometer) { this.returnOdometer = returnOdometer; }

    public String getReturnFuelLevel() { return returnFuelLevel; }
    public void setReturnFuelLevel(String returnFuelLevel) { this.returnFuelLevel = returnFuelLevel; }

    public Double getLateFee() { return lateFee; }
    public void setLateFee(Double lateFee) { this.lateFee = lateFee; }

    public Double getDamageFee() { return damageFee; }
    public void setDamageFee(Double damageFee) { this.damageFee = damageFee; }

    public Double getExtensionCharge() { return extensionCharge; }
    public void setExtensionCharge(Double extensionCharge) { this.extensionCharge = extensionCharge; }

    public String getPaymentStatus() { return paymentStatus; }
    public void setPaymentStatus(String paymentStatus) { this.paymentStatus = paymentStatus; }

    public String getManagerNotes() { return managerNotes; }
    public void setManagerNotes(String managerNotes) { this.managerNotes = managerNotes; }

    public Vehicle getVehicle() { return vehicle; }
    public void setVehicle(Vehicle vehicle) { this.vehicle = vehicle; }

    public Customer getCustomer() { return customer; }
    public void setCustomer(Customer customer) { this.customer = customer; }
}
