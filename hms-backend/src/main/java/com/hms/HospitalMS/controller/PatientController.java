package com.hms.HospitalMS.controller;

import com.hms.HospitalMS.model.Patient;
import com.hms.HospitalMS.service.PatientService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/patients")
@CrossOrigin(origins = "*")
public class PatientController {

    @Autowired private PatientService patientService;

    @PostMapping
    public ResponseEntity<Patient> addPatient(@Valid @RequestBody Patient patient) {
        return ResponseEntity.ok(patientService.savePatient(patient));
    }

    @GetMapping
    public Page<Patient> getPatients(
            @RequestParam(required = false) String search, Pageable pageable) {
        return (search != null && !search.isEmpty())
                ? patientService.searchPatients(search, pageable)
                : patientService.getAllPatients(pageable);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Patient> getPatientById(@PathVariable Long id) {
        return ResponseEntity.ok(patientService.getPatientById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Patient> updatePatient(
            @PathVariable Long id, @Valid @RequestBody Patient patient) {
        return ResponseEntity.ok(patientService.updatePatient(id, patient));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePatient(@PathVariable Long id) {
        try {
            patientService.deletePatient(id);
            return ResponseEntity.noContent().build(); // 204 - no body
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
}