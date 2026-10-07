package com.rental.controller;

import com.rental.dto.MaintenanceRequest;
import com.rental.entity.Maintenance;
import com.rental.service.MaintenanceService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/maintenance")
@CrossOrigin(origins = "*")
public class MaintenanceController {

    @Autowired
    private MaintenanceService maintenanceService;

    @GetMapping
    public ResponseEntity<List<Maintenance>> getAllMaintenance() {
        return ResponseEntity.ok(maintenanceService.getAllMaintenanceRecords());
    }

    @GetMapping("/vehicle/{vehicleId}")
    public ResponseEntity<List<Maintenance>> getByVehicle(@PathVariable Integer vehicleId) {
        return ResponseEntity.ok(maintenanceService.getMaintenanceByVehicle(vehicleId));
    }

    @PostMapping
    public ResponseEntity<?> scheduleMaintenance(@Valid @RequestBody MaintenanceRequest request) {
        try {
            Maintenance maintenance = maintenanceService.scheduleMaintenance(request);
            return ResponseEntity.ok(maintenance);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @PostMapping("/{id}/complete")
    public ResponseEntity<?> completeMaintenance(@PathVariable Integer id, @RequestBody(required = false) Map<String, Object> body) {
        try {
            Double cost = null;
            String notes = null;
            if (body != null) {
                if (body.get("cost") != null) {
                    cost = Double.valueOf(body.get("cost").toString());
                }
                if (body.get("notes") != null) {
                    notes = body.get("notes").toString();
                }
            }
            Maintenance m = maintenanceService.completeMaintenance(id, cost, notes);
            return ResponseEntity.ok(m);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/alerts")
    public ResponseEntity<List<Map<String, Object>>> getComplianceAlerts() {
        return ResponseEntity.ok(maintenanceService.getComplianceAndServiceAlerts());
    }
}
