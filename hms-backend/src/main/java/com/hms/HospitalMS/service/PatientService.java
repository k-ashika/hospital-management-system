package com.hms.HospitalMS.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hms.HospitalMS.model.Patient;
import com.hms.HospitalMS.repository.PatientRepository;
import com.hms.HospitalMS.repository.AppointmentRepository;
import com.hms.HospitalMS.repository.FileUploadRepository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@Service
public class PatientService {

    @Autowired
    private PatientRepository repo;

    @Autowired
    private AppointmentRepository appointmentRepository;

    @Autowired
    private FileUploadRepository fileUploadRepository;

    public Page<Patient> getAllPatients(Pageable pageable) {
        return repo.findAll(pageable);
    }

    public Page<Patient> searchPatients(String keyword, Pageable pageable) {
        return repo.findByNameContainingIgnoreCaseOrEmailContainingIgnoreCase(
                keyword, keyword, pageable);
    }

    public Patient savePatient(Patient patient) {
        return repo.save(patient);
    }

    public Patient updatePatient(Long id, Patient updated) {
        Patient existing = repo.findById(id).orElseThrow();
        existing.setName(updated.getName());
        existing.setAge(updated.getAge());
        existing.setGender(updated.getGender());
        existing.setEmail(updated.getEmail());
        existing.setPhone(updated.getPhone());
        existing.setAddress(updated.getAddress());
        return repo.save(existing);
    }

    // ✅ FIXED DELETE
    public void deletePatient(Long id) {

        // 1️⃣ Delete files linked to patient
        fileUploadRepository.deleteAll(
                fileUploadRepository.findByPatientId(id)
        );

        // 2️⃣ Delete appointments linked to patient
        appointmentRepository.deleteAll(
                appointmentRepository.findByPatientId(id)
        );

        // 3️⃣ Delete patient
        repo.deleteById(id);
    }

    public Patient getPatientById(Long id) {
        return repo.findById(id).orElse(null);
    }
}