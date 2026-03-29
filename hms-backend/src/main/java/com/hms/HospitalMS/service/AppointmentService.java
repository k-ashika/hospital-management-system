package com.hms.HospitalMS.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.hms.HospitalMS.model.Appointment;
import com.hms.HospitalMS.repository.AppointmentRepository;
import org.springframework.data.domain.*;
import java.time.LocalDate;
@Service
public class AppointmentService {

    @Autowired
    private AppointmentRepository repo;

    @Autowired(required = false)  // ✅ Make it optional
    private MailService mailService;

    public List<Appointment> getAllAppointments() {
        return repo.findAll();
    }

    public Appointment getAppointmentById(Long id) {
        return repo.findById(id).orElse(null);
    }

    public Appointment saveAppointment(Appointment appointment) {
        Appointment saved = repo.save(appointment);

        // ✅ Only send emails if mailService is available
        if (mailService != null) {
            try {
                // Send email to patient
                if (saved.getPatient() != null && saved.getPatient().getEmail() != null) {
                    String subject = "Appointment Scheduled / Updated";
                    String body = "Hello " + saved.getPatient().getName() +
                                  ",\nYour appointment with Dr. " + saved.getDoctor().getName() +
                                  " on " + saved.getAppointmentDate() + " at " + saved.getAppointmentTime() +
                                  " has been " + saved.getStatus() + ".\nReason: " + saved.getReason();
                    mailService.sendEmail(saved.getPatient().getEmail(), subject, body);
                }

                // Send email to doctor
                if (saved.getDoctor() != null && saved.getDoctor().getEmail() != null) {
                    String subject = "New / Updated Appointment";
                    String body = "Hello Dr. " + saved.getDoctor().getName() +
                                  ",\nYou have an appointment with patient " + saved.getPatient().getName() +
                                  " on " + saved.getAppointmentDate() + " at " + saved.getAppointmentTime() +
                                  ".\nReason: " + saved.getReason() +
                                  "\nStatus: " + saved.getStatus();
                    mailService.sendEmail(saved.getDoctor().getEmail(), subject, body);
                }
            } catch (Exception e) {
                System.err.println("Email sending failed: " + e.getMessage());
                // Don't let email failure break the appointment creation
            }
        }

        return saved;
    }
    public long countAll() {
        return repo.count();
    }
    public void deleteAppointment(Long id) {
        repo.deleteById(id);
    }
    public Page<Appointment> getAllAppointments(Pageable pageable) {
        return repo.findAll(pageable);
    }
    public long countCompletedToday() {
        return repo.countByStatusAndAppointmentDate("Completed", LocalDate.now());
    }
}