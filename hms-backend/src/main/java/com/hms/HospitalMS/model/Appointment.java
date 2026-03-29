package com.hms.HospitalMS.model;
import java.time.LocalDate; import java.time.LocalTime;
import jakarta.persistence.*;
@Entity
public class Appointment {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;
    private LocalDate appointmentDate;
    private LocalTime appointmentTime;
    private String status;
    private String reason;
    @ManyToOne @JoinColumn(name="doctor_id") private Doctor doctor;
    @ManyToOne @JoinColumn(name="patient_id") private Patient patient;
    public Long getId() { return id; } public void setId(Long id) { this.id = id; }
    public LocalDate getAppointmentDate() { return appointmentDate; } public void setAppointmentDate(LocalDate d) { this.appointmentDate = d; }
    public LocalTime getAppointmentTime() { return appointmentTime; } public void setAppointmentTime(LocalTime t) { this.appointmentTime = t; }
    public String getStatus() { return status; } public void setStatus(String s) { this.status = s; }
    public String getReason() { return reason; } public void setReason(String r) { this.reason = r; }
    public Doctor getDoctor() { return doctor; } public void setDoctor(Doctor d) { this.doctor = d; }
    public Patient getPatient() { return patient; } public void setPatient(Patient p) { this.patient = p; }
}
