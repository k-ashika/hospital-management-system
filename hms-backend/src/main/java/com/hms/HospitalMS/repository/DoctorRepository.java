package com.hms.HospitalMS.repository;
import org.springframework.data.domain.*; import org.springframework.data.jpa.repository.JpaRepository;
import com.hms.HospitalMS.model.Doctor;
public interface DoctorRepository extends JpaRepository<Doctor,Long> {
    Page<Doctor> findByNameContainingIgnoreCaseOrSpecializationContainingIgnoreCase(String name, String spec, Pageable p);
}
