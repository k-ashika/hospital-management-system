package com.hms.HospitalMS.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import com.hms.HospitalMS.model.Doctor;
import com.hms.HospitalMS.repository.DoctorRepository;
import com.hms.HospitalMS.repository.AppointmentRepository;

@Service
public class DoctorService {

    @Autowired
    private DoctorRepository doctorRepository;

    @Autowired
    private AppointmentRepository appointmentRepository;

    public Doctor saveDoctor(Doctor doctor) {
        return doctorRepository.save(doctor);
    }

    public Page<Doctor> getAllDoctors(Pageable pageable) {
        return doctorRepository.findAll(pageable);
    }

    public Page<Doctor> searchDoctors(String keyword, Pageable pageable) {
        return doctorRepository
                .findByNameContainingIgnoreCaseOrSpecializationContainingIgnoreCase(
                        keyword, keyword, pageable);
    }

    public Optional<Doctor> getDoctorById(Long id) {
        return doctorRepository.findById(id);
    }

    public Doctor updateDoctor(Long id, Doctor updated) {
        return doctorRepository.findById(id).map(d -> {
            d.setName(updated.getName());
            d.setSpecialization(updated.getSpecialization());
            d.setEmail(updated.getEmail());
            d.setPhone(updated.getPhone());
            d.setAddress(updated.getAddress());
            return doctorRepository.save(d);
        }).orElseThrow(() -> new RuntimeException("Doctor not found"));
    }

    // ✅ FIXED DELETE
    public void deleteDoctor(Long id) {

        // 1️⃣ Delete appointments linked to doctor
        appointmentRepository.deleteAll(
                appointmentRepository.findByDoctorId(id)
        );

        // 2️⃣ Delete doctor
        doctorRepository.deleteById(id);
    }
}