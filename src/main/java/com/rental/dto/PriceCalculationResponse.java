package com.rental.dto;

public class PriceCalculationResponse {
    private int rentalDays;
    private double dailyRate;
    private double dynamicRate;
    private double dynamicMultiplier;
    private String pricingTierName; // Standard, Weekend (+15%), Peak/Festival (+25%)
    private double baseTotal;
    private double extraCharges;
    private double totalRentalCharge;

    public PriceCalculationResponse() {}

    public int getRentalDays() { return rentalDays; }
    public void setRentalDays(int rentalDays) { this.rentalDays = rentalDays; }

    public double getDailyRate() { return dailyRate; }
    public void setDailyRate(double dailyRate) { this.dailyRate = dailyRate; }

    public double getDynamicRate() { return dynamicRate; }
    public void setDynamicRate(double dynamicRate) { this.dynamicRate = dynamicRate; }

    public double getDynamicMultiplier() { return dynamicMultiplier; }
    public void setDynamicMultiplier(double dynamicMultiplier) { this.dynamicMultiplier = dynamicMultiplier; }

    public String getPricingTierName() { return pricingTierName; }
    public void setPricingTierName(String pricingTierName) { this.pricingTierName = pricingTierName; }

    public double getBaseTotal() { return baseTotal; }
    public void setBaseTotal(double baseTotal) { this.baseTotal = baseTotal; }

    public double getExtraCharges() { return extraCharges; }
    public void setExtraCharges(double extraCharges) { this.extraCharges = extraCharges; }

    public double getTotalRentalCharge() { return totalRentalCharge; }
    public void setTotalRentalCharge(double totalRentalCharge) { this.totalRentalCharge = totalRentalCharge; }
}
