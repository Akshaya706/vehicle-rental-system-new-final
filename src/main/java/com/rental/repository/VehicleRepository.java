package com.rental.repository;

import com.rental.entity.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface VehicleRepository extends JpaRepository<Vehicle, Integer> {
    Optional<Vehicle> findByRegistrationNo(String registrationNo);
    boolean existsByRegistrationNo(String registrationNo);
    List<Vehicle> findByAvailability(String availability);
    List<Vehicle> findByVehicleTypeIgnoreCase(String vehicleType);

    @Query("SELECT v FROM Vehicle v WHERE " +
           "(:type IS NULL OR LOWER(v.vehicleType) = LOWER(:type) OR " +
           " (LOWER(:type) = 'car' AND LOWER(v.vehicleType) IN ('car', 'sedan', 'suv', 'luxury', 'electric', 'hatchback', 'coupe', 'van', 'pickup', 'convertible', 'hybrid', 'minivan'))) AND " +
           "(:brand IS NULL OR LOWER(v.brand) LIKE LOWER(CONCAT('%', :brand, '%'))) AND " +
           "(:model IS NULL OR LOWER(v.model) LIKE LOWER(CONCAT('%', :model, '%'))) AND " +
           "(:maxRate IS NULL OR v.dailyRate <= :maxRate) AND " +
           "(:availability IS NULL OR v.availability = :availability)")
    List<Vehicle> searchVehicles(@Param("type") String type,
                                @Param("brand") String brand,
                                @Param("model") String model,
                                @Param("maxRate") Double maxRate,
                                @Param("availability") String availability);
}
