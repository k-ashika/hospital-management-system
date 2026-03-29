package com.hms.HospitalMS.controller;
import java.io.*; import java.nio.file.Files; import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.*; import org.springframework.http.*;
import org.springframework.web.bind.annotation.*; import org.springframework.web.multipart.MultipartFile;
import com.hms.HospitalMS.model.FileUpload; import com.hms.HospitalMS.service.FileService;
@RestController @RequestMapping("/api/files") @CrossOrigin(origins = "*")
public class FileController {
    @Autowired private FileService fileService;
    @PostMapping("/upload/{patientId}") public FileUpload uploadFile(@RequestParam("file") MultipartFile file, @PathVariable Long patientId) throws IOException { return fileService.uploadFile(file, patientId); }
    @GetMapping("/patient/{patientId}") public List<FileUpload> getFiles(@PathVariable Long patientId) { return fileService.getFilesByPatient(patientId); }
    @GetMapping("/download/{fileId}") public ResponseEntity<Resource> downloadFile(@PathVariable Long fileId) throws IOException {
        File file = fileService.getFile(fileId); Resource resource = new UrlResource(file.toURI());
        return ResponseEntity.ok().contentType(MediaType.parseMediaType(Files.probeContentType(file.toPath())))
            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getName() + "\"").body(resource);
    }
}
