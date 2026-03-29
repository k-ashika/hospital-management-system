package com.hms.HospitalMS.controller;

import com.hms.HospitalMS.model.Doctor;
import com.hms.HospitalMS.service.DoctorService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api/doctors")
@Validated
public class DoctorController {

    @Autowired private DoctorService doctorService;

    @PostMapping
    public ResponseEntity<Doctor> addDoctor(@Valid @RequestBody Doctor doctor) {
        return ResponseEntity.ok(doctorService.saveDoctor(doctor));
    }

    @GetMapping
    public Page<Doctor> getDoctors(
            @RequestParam(required = false) String search, Pageable pageable) {
        return (search != null && !search.isEmpty())
                ? doctorService.searchDoctors(search, pageable)
                : doctorService.getAllDoctors(pageable);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Doctor> getDoctorById(@PathVariable Long id) {
        return doctorService.getDoctorById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Doctor> updateDoctor(
            @PathVariable Long id, @Valid @RequestBody Doctor doctor) {
        return ResponseEntity.ok(doctorService.updateDoctor(id, doctor));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDoctor(@PathVariable Long id) {
        try {
            doctorService.deleteDoctor(id);
            return ResponseEntity.noContent().build(); // 204 - no body
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
}