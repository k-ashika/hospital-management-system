import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({ providedIn: 'root' })
export class AppointmentService {
  private apiUrl = 'http://localhost:8080/api/appointments';
  private doctorsUrl = 'http://localhost:8080/api/doctors';
  private patientsUrl = 'http://localhost:8080/api/patients';

  constructor(private http: HttpClient) {}

  getAllAppointments(page: number = 0, size: number = 5): Observable<any> {
    return this.http.get(`${this.apiUrl}?page=${page}&size=${size}`);
  }

  createAppointment(appointment: any): Observable<any> {
    return this.http.post(this.apiUrl, appointment);
  }

  updateAppointment(id: number, appointment: any): Observable<any> {
    return this.http.put(`${this.apiUrl}/${id}`, appointment);
  }

  deleteAppointment(id: number): Observable<any> {
    return this.http.delete(`${this.apiUrl}/${id}`, { responseType: 'text' });
  }

  getAllDoctors(): Observable<any> {
    return this.http.get(`${this.doctorsUrl}?size=100`);
  }

  getAllPatients(): Observable<any> {
    return this.http.get(`${this.patientsUrl}?size=100`);
  }
}