package com.rental.repository;

import com.rental.entity.Maintenance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MaintenanceRepository extends JpaRepository<Maintenance, Integer> {
    List<Maintenance> findByVehicleId(Integer vehicleId);
    List<Maintenance> findByMaintenanceStatus(String maintenanceStatus);
    List<Maintenance> findAllByOrderByMaintenanceIdDesc();
}
