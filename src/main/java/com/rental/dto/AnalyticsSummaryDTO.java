package com.rental.dto;

import java.util.List;
import java.util.Map;

public class AnalyticsSummaryDTO {

    private long totalVehicles;
    private long availableVehicles;
    private long bookedVehicles;
    private long activeRentals;
    private long completedRentals;
    private long maintenanceCount;
    private long totalCustomers;
    private double totalRevenue;
    private double fleetUtilizationRate;

    private List<Map<String, Object>> popularVehicles;
    private List<Map<String, Object>> topCustomers;
    private List<Map<String, Object>> revenueByVehicleType;
    private List<Map<String, Object>> monthlyRevenue;

    public AnalyticsSummaryDTO() {}

    public long getTotalVehicles() { return totalVehicles; }
    public void setTotalVehicles(long totalVehicles) { this.totalVehicles = totalVehicles; }

    public long getAvailableVehicles() { return availableVehicles; }
    public void setAvailableVehicles(long availableVehicles) { this.availableVehicles = availableVehicles; }

    public long getBookedVehicles() { return bookedVehicles; }
    public void setBookedVehicles(long bookedVehicles) { this.bookedVehicles = bookedVehicles; }

    public long getActiveRentals() { return activeRentals; }
    public void setActiveRentals(long activeRentals) { this.activeRentals = activeRentals; }

    public long getCompletedRentals() { return completedRentals; }
    public void setCompletedRentals(long completedRentals) { this.completedRentals = completedRentals; }

    public long getMaintenanceCount() { return maintenanceCount; }
    public void setMaintenanceCount(long maintenanceCount) { this.maintenanceCount = maintenanceCount; }

    public long getTotalCustomers() { return totalCustomers; }
    public void setTotalCustomers(long totalCustomers) { this.totalCustomers = totalCustomers; }

    public double getTotalRevenue() { return totalRevenue; }
    public void setTotalRevenue(double totalRevenue) { this.totalRevenue = totalRevenue; }

    public double getFleetUtilizationRate() { return fleetUtilizationRate; }
    public void setFleetUtilizationRate(double fleetUtilizationRate) { this.fleetUtilizationRate = fleetUtilizationRate; }

    public List<Map<String, Object>> getPopularVehicles() { return popularVehicles; }
    public void setPopularVehicles(List<Map<String, Object>> popularVehicles) { this.popularVehicles = popularVehicles; }

    public List<Map<String, Object>> getTopCustomers() { return topCustomers; }
    public void setTopCustomers(List<Map<String, Object>> topCustomers) { this.topCustomers = topCustomers; }

    public List<Map<String, Object>> getRevenueByVehicleType() { return revenueByVehicleType; }
    public void setRevenueByVehicleType(List<Map<String, Object>> revenueByVehicleType) { this.revenueByVehicleType = revenueByVehicleType; }

    public List<Map<String, Object>> getMonthlyRevenue() { return monthlyRevenue; }
    public void setMonthlyRevenue(List<Map<String, Object>> monthlyRevenue) { this.monthlyRevenue = monthlyRevenue; }
}
