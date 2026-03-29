import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({ providedIn: 'root' })
export class DoctorService {
  private apiUrl = 'https://hms-backend-hr36.onrender.com/api/doctors';
  constructor(private http: HttpClient) {}

  getAllDoctors(page: number = 0, size: number = 5, search: string = ''): Observable<any> {
    let url = `${this.apiUrl}?page=${page}&size=${size}`;
    if (search) url += `&search=${search}`;
    return this.http.get(url);
  }

  createDoctor(doctor: any): Observable<any> {
    return this.http.post(this.apiUrl, doctor);
  }

  updateDoctor(id: number, doctor: any): Observable<any> {
    return this.http.put(`${this.apiUrl}/${id}`, doctor);
  }

  deleteDoctor(id: number): Observable<any> {
    return this.http.delete(`${this.apiUrl}/${id}`, { responseType: 'text' });
  }
}