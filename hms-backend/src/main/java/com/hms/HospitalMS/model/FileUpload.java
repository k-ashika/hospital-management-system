package com.hms.HospitalMS.model;
import jakarta.persistence.*;
@Entity
public class FileUpload {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;
    private String fileName; private String fileType; private String filePath;
    @ManyToOne @JoinColumn(name="patient_id") private Patient patient;
    public Long getId() { return id; } public void setId(Long id) { this.id = id; }
    public String getFileName() { return fileName; } public void setFileName(String f) { this.fileName = f; }
    public String getFileType() { return fileType; } public void setFileType(String f) { this.fileType = f; }
    public String getFilePath() { return filePath; } public void setFilePath(String f) { this.filePath = f; }
    public Patient getPatient() { return patient; } public void setPatient(Patient p) { this.patient = p; }
}
