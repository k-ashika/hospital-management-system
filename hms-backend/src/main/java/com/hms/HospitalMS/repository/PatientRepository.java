package com.hms.HospitalMS.repository;
import org.springframework.data.domain.*; import org.springframework.data.jpa.repository.JpaRepository;
import com.hms.HospitalMS.model.Patient;
public interface PatientRepository extends JpaRepository<Patient,Long> {
    Page<Patient> findByNameContainingIgnoreCaseOrEmailContainingIgnoreCase(String name, String email, Pageable p);
}
