package com.hms.HospitalMS.controller;

import com.hms.HospitalMS.model.Appointment;
import com.hms.HospitalMS.service.AppointmentService;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/appointments")
@CrossOrigin(origins = "*")
public class AppointmentController {

    @Autowired private AppointmentService service;

    // ✅ Now returns Page instead of List so frontend handles it consistently
    @GetMapping
    public Page<Appointment> getAllAppointments(Pageable pageable) {
        return service.getAllAppointments(pageable);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Appointment> getAppointment(@PathVariable Long id) {
        Appointment a = service.getAppointmentById(id);
        return a != null ? ResponseEntity.ok(a) : ResponseEntity.notFound().build();
    }

    @PostMapping
    public ResponseEntity<Appointment> addAppointment(@RequestBody Appointment appointment) {
        return ResponseEntity.ok(service.saveAppointment(appointment));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Appointment> updateAppointment(
            @PathVariable Long id, @RequestBody Appointment appointment) {
        Appointment existing = service.getAppointmentById(id);
        if (existing == null) return ResponseEntity.notFound().build();

        existing.setAppointmentDate(appointment.getAppointmentDate());
        existing.setAppointmentTime(appointment.getAppointmentTime());
        existing.setStatus(appointment.getStatus());
        existing.setReason(appointment.getReason());
        existing.setDoctor(appointment.getDoctor());
        existing.setPatient(appointment.getPatient());
        return ResponseEntity.ok(service.saveAppointment(existing));
    }
    @GetMapping("/stats")
    public ResponseEntity<Map<String, Long>> getStats() {
        Map<String, Long> stats = new HashMap<>();
        stats.put("completedToday", service.countCompletedToday());
        stats.put("total", service.countAll());
        return ResponseEntity.ok(stats);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAppointment(@PathVariable Long id) {
        try {
            service.deleteAppointment(id);
            return ResponseEntity.noContent().build(); // 204 - no body
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
}