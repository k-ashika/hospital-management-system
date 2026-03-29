package com.hms.HospitalMS.repository;
import java.util.List; import org.springframework.data.jpa.repository.JpaRepository;
import com.hms.HospitalMS.model.FileUpload;
public interface FileUploadRepository extends JpaRepository<FileUpload,Long> {
    List<FileUpload> findByPatientId(Long patientId);
}
