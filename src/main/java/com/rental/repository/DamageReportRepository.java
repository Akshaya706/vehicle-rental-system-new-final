package com.rental.repository;

import com.rental.entity.DamageReport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DamageReportRepository extends JpaRepository<DamageReport, Integer> {
    List<DamageReport> findByBookingId(Integer bookingId);
    List<DamageReport> findByVehicleId(Integer vehicleId);
    List<DamageReport> findAllByOrderByDamageIdDesc();
}
