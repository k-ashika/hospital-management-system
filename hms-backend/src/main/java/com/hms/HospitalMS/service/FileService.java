package com.hms.HospitalMS.service;
import java.io.File; import java.io.IOException; import java.util.List;
import org.springframework.beans.factory.annotation.Autowired; import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import com.hms.HospitalMS.model.FileUpload; import com.hms.HospitalMS.model.Patient;
import com.hms.HospitalMS.repository.FileUploadRepository; import com.hms.HospitalMS.repository.PatientRepository;
@Service
public class FileService {
    @Autowired private FileUploadRepository fileRepo;
    @Autowired private PatientRepository patientRepo;
    private final String uploadDir = "uploads";
    public FileService() { File dir = new File(uploadDir); if (!dir.exists()) dir.mkdirs(); }
    public FileUpload uploadFile(MultipartFile file, Long patientId) throws IOException {
        Patient patient = patientRepo.findById(patientId).orElseThrow(() -> new RuntimeException("Patient not found"));
        String fileName = file.getOriginalFilename();
        String filePath = uploadDir + "/" + fileName;
        file.transferTo(new File(filePath));
        FileUpload f = new FileUpload();
        f.setFileName(fileName); f.setFileType(file.getContentType()); f.setFilePath(filePath); f.setPatient(patient);
        return fileRepo.save(f);
    }
    public List<FileUpload> getFilesByPatient(Long patientId) { return fileRepo.findByPatientId(patientId); }
    public File getFile(Long fileId) {
        return fileRepo.findById(fileId).map(f -> new File(f.getFilePath())).orElseThrow(() -> new RuntimeException("File not found"));
    }
}
