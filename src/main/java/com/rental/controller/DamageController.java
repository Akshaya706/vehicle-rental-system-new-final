package com.rental.controller;

import com.rental.dto.DamageReportRequest;
import com.rental.entity.DamageReport;
import com.rental.service.DamageService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/damages")
@CrossOrigin(origins = "*")
public class DamageController {

    @Autowired
    private DamageService damageService;

    @GetMapping
    public ResponseEntity<List<DamageReport>> getAllDamageReports() {
        return ResponseEntity.ok(damageService.getAllDamageReports());
    }

    @GetMapping("/booking/{bookingId}")
    public ResponseEntity<List<DamageReport>> getByBooking(@PathVariable Integer bookingId) {
        return ResponseEntity.ok(damageService.getDamageReportsByBooking(bookingId));
    }

    @PostMapping
    public ResponseEntity<?> recordDamage(@Valid @RequestBody DamageReportRequest request) {
        try {
            DamageReport report = damageService.recordDamage(request);
            return ResponseEntity.ok(report);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
}
