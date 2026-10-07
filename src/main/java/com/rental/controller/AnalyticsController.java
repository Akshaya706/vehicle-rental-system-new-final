package com.rental.controller;

import com.rental.dto.AnalyticsSummaryDTO;
import com.rental.entity.Booking;
import com.rental.entity.DamageReport;
import com.rental.entity.Maintenance;
import com.rental.entity.Vehicle;
import com.rental.repository.*;
import com.rental.service.AnalyticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/analytics")
@CrossOrigin(origins = "*")
public class AnalyticsController {

    @Autowired
    private AnalyticsService analyticsService;

    @Autowired
    private VehicleRepository vehicleRepository;

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private MaintenanceRepository maintenanceRepository;

    @Autowired
    private DamageReportRepository damageReportRepository;

    @GetMapping("/summary")
    public ResponseEntity<AnalyticsSummaryDTO> getSummary() {
        return ResponseEntity.ok(analyticsService.getAnalyticsSummary());
    }

    // FR13 Report 1: Available Vehicles
    @GetMapping("/reports/available-vehicles")
    public ResponseEntity<List<Vehicle>> getAvailableVehiclesReport() {
        return ResponseEntity.ok(vehicleRepository.findByAvailability("AVAILABLE"));
    }

    // FR13 Report 2: Booked Vehicles
    @GetMapping("/reports/booked-vehicles")
    public ResponseEntity<List<Booking>> getBookedVehiclesReport() {
        return ResponseEntity.ok(bookingRepository.findByBookingStatus("APPROVED"));
    }

    // FR13 Report 3: Active Rentals
    @GetMapping("/reports/active-rentals")
    public ResponseEntity<List<Booking>> getActiveRentalsReport() {
        return ResponseEntity.ok(bookingRepository.findByBookingStatus("ACTIVE"));
    }

    // FR13 Report 4: Returned Vehicles
    @GetMapping("/reports/returned-vehicles")
    public ResponseEntity<List<Booking>> getReturnedVehiclesReport() {
        return ResponseEntity.ok(bookingRepository.findByBookingStatus("COMPLETED"));
    }

    // FR13 Report 5: Revenue Collection
    @GetMapping("/reports/revenue")
    public ResponseEntity<List<Map<String, Object>>> getRevenueReport() {
        return ResponseEntity.ok(analyticsService.getMonthlyRevenueReport());
    }

    // FR13 Report 6: Vehicle Maintenance
    @GetMapping("/reports/maintenance")
    public ResponseEntity<List<Maintenance>> getMaintenanceReport() {
        return ResponseEntity.ok(maintenanceRepository.findAllByOrderByMaintenanceIdDesc());
    }

    // FR13 Report 7: Damage Reports
    @GetMapping("/reports/damage")
    public ResponseEntity<List<DamageReport>> getDamageReport() {
        return ResponseEntity.ok(damageReportRepository.findAllByOrderByDamageIdDesc());
    }

    // FR13 Report 8: Most Popular Vehicles
    @GetMapping("/reports/popular-vehicles")
    public ResponseEntity<List<Map<String, Object>>> getPopularVehiclesReport() {
        return ResponseEntity.ok(analyticsService.getMostPopularVehiclesReport());
    }

    // FR13 Report 9: Top Customers
    @GetMapping("/reports/top-customers")
    public ResponseEntity<List<Map<String, Object>>> getTopCustomersReport() {
        return ResponseEntity.ok(analyticsService.getTopCustomersReport());
    }
}
