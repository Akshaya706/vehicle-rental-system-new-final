package com.rental.service;

import com.rental.dto.AnalyticsSummaryDTO;
import com.rental.entity.Booking;
import com.rental.entity.Payment;
import com.rental.entity.Vehicle;
import com.rental.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class AnalyticsService {

    @Autowired
    private VehicleRepository vehicleRepository;

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private MaintenanceRepository maintenanceRepository;

    @Autowired
    private DamageReportRepository damageReportRepository;

    public AnalyticsSummaryDTO getAnalyticsSummary() {
        AnalyticsSummaryDTO dto = new AnalyticsSummaryDTO();

        long totalVehicles = vehicleRepository.count();
        long available = vehicleRepository.findByAvailability("AVAILABLE").size();
        long booked = bookingRepository.countByBookingStatus("APPROVED") + bookingRepository.countByBookingStatus("PENDING_APPROVAL");
        long active = bookingRepository.countByBookingStatus("ACTIVE");
        long completed = bookingRepository.countByBookingStatus("COMPLETED");
        long underMaint = vehicleRepository.findByAvailability("UNDER_MAINTENANCE").size();
        long totalCustomers = customerRepository.count();

        Double totalRev = paymentRepository.getTotalRevenue();
        double revenue = totalRev != null ? totalRev : 0.0;

        double utilizationRate = 0.0;
        if (totalVehicles > 0) {
            long rentedOrBooked = totalVehicles - available;
            utilizationRate = Math.round(((double) rentedOrBooked / totalVehicles * 100.0) * 10.0) / 10.0;
        }

        dto.setTotalVehicles(totalVehicles);
        dto.setAvailableVehicles(available);
        dto.setBookedVehicles(booked);
        dto.setActiveRentals(active);
        dto.setCompletedRentals(completed);
        dto.setMaintenanceCount(underMaint);
        dto.setTotalCustomers(totalCustomers);
        dto.setTotalRevenue(Math.round(revenue * 100.0) / 100.0);
        dto.setFleetUtilizationRate(utilizationRate);

        // Most Popular Vehicles
        dto.setPopularVehicles(getMostPopularVehiclesReport());

        // Top Customers
        dto.setTopCustomers(getTopCustomersReport());

        // Revenue by Vehicle Type
        dto.setRevenueByVehicleType(getRevenueByVehicleTypeReport());

        // Monthly Revenue
        dto.setMonthlyRevenue(getMonthlyRevenueReport());

        return dto;
    }

    public List<Map<String, Object>> getMostPopularVehiclesReport() {
        List<Booking> bookings = bookingRepository.findAll();
        Map<Integer, Long> countMap = bookings.stream()
                .filter(b -> !"CANCELLED".equalsIgnoreCase(b.getBookingStatus()) && !"REJECTED".equalsIgnoreCase(b.getBookingStatus()))
                .collect(Collectors.groupingBy(Booking::getVehicleId, Collectors.counting()));

        List<Map<String, Object>> result = new ArrayList<>();
        countMap.entrySet().stream()
                .sorted(Map.Entry.<Integer, Long>comparingByValue().reversed())
                .limit(5)
                .forEach(e -> {
                    Vehicle v = vehicleRepository.findById(e.getKey()).orElse(null);
                    if (v != null) {
                        Map<String, Object> map = new LinkedHashMap<>();
                        map.put("vehicleId", v.getVehicleId());
                        map.put("brand", v.getBrand());
                        map.put("model", v.getModel());
                        map.put("vehicleType", v.getVehicleType());
                        map.put("totalRentals", e.getValue());
                        map.put("dailyRate", v.getDailyRate());
                        result.add(map);
                    }
                });
        return result;
    }

    public List<Map<String, Object>> getTopCustomersReport() {
        List<Booking> bookings = bookingRepository.findAll();
        Map<Integer, Double> customerSpentMap = new HashMap<>();
        Map<Integer, Long> customerBookingsMap = new HashMap<>();

        for (Booking b : bookings) {
            if ("PAID".equalsIgnoreCase(b.getPaymentStatus()) || "ACTIVE".equalsIgnoreCase(b.getBookingStatus()) || "COMPLETED".equalsIgnoreCase(b.getBookingStatus())) {
                customerSpentMap.merge(b.getCustomerId(), b.getTotalCharge(), Double::sum);
                customerBookingsMap.merge(b.getCustomerId(), 1L, Long::sum);
            }
        }

        List<Map<String, Object>> result = new ArrayList<>();
        customerSpentMap.entrySet().stream()
                .sorted(Map.Entry.<Integer, Double>comparingByValue().reversed())
                .limit(5)
                .forEach(e -> {
                    customerRepository.findById(e.getKey()).ifPresent(c -> {
                        Map<String, Object> map = new LinkedHashMap<>();
                        map.put("customerId", c.getCustomerId());
                        map.put("name", c.getName());
                        map.put("phone", c.getPhone());
                        map.put("drivingLicence", c.getDrivingLicence());
                        map.put("totalBookings", customerBookingsMap.getOrDefault(c.getCustomerId(), 0L));
                        map.put("totalSpent", Math.round(e.getValue() * 100.0) / 100.0);
                        result.add(map);
                    });
                });

        return result;
    }

    public List<Map<String, Object>> getRevenueByVehicleTypeReport() {
        List<Booking> bookings = bookingRepository.findAll();
        Map<String, Double> revByType = new LinkedHashMap<>();

        for (Booking b : bookings) {
            if (b.getVehicle() != null && ("PAID".equalsIgnoreCase(b.getPaymentStatus()) || "COMPLETED".equalsIgnoreCase(b.getBookingStatus()))) {
                String type = b.getVehicle().getVehicleType();
                revByType.merge(type, b.getTotalCharge(), Double::sum);
            }
        }

        List<Map<String, Object>> list = new ArrayList<>();
        revByType.forEach((type, rev) -> {
            Map<String, Object> map = new LinkedHashMap<>();
            map.put("type", type);
            map.put("revenue", Math.round(rev * 100.0) / 100.0);
            list.add(map);
        });
        return list;
    }

    public List<Map<String, Object>> getMonthlyRevenueReport() {
        List<Payment> payments = paymentRepository.findAll();
        Map<String, Double> monthly = new TreeMap<>();

        for (Payment p : payments) {
            if ("PAID".equalsIgnoreCase(p.getPaymentStatus()) && p.getPaymentDate() != null) {
                String monthKey = p.getPaymentDate().getYear() + "-" + String.format("%02d", p.getPaymentDate().getMonthValue());
                monthly.merge(monthKey, p.getAmount(), Double::sum);
            }
        }

        List<Map<String, Object>> list = new ArrayList<>();
        monthly.forEach((m, rev) -> {
            Map<String, Object> map = new LinkedHashMap<>();
            map.put("month", m);
            map.put("revenue", Math.round(rev * 100.0) / 100.0);
            list.add(map);
        });
        return list;
    }
}
