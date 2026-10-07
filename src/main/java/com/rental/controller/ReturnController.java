package com.rental.controller;

import com.rental.dto.VehicleReturnRequest;
import com.rental.entity.Booking;
import com.rental.service.ReturnService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/returns")
@CrossOrigin(origins = "*")
public class ReturnController {

    @Autowired
    private ReturnService returnService;

    @PostMapping
    public ResponseEntity<?> processReturn(@Valid @RequestBody VehicleReturnRequest request) {
        try {
            Booking booking = returnService.processVehicleReturn(request);
            return ResponseEntity.ok(booking);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
}
