import { Component, OnInit, ChangeDetectorRef } from '@angular/core';
import { RouterModule, Router } from '@angular/router';
import { CommonModule } from '@angular/common';
import { HttpClient } from '@angular/common/http';

@Component({
  selector: 'app-dashboard',
  standalone: true,
  imports: [CommonModule, RouterModule],
  templateUrl: './dashboard.component.html'
})
export class Dashboard implements OnInit {

  totalPatients: number = 0;
  totalDoctors: number = 0;
  totalAppointments: number = 0;
  completedToday: number = 0;

  private base = 'http://localhost:8080/api';

  constructor(
    private router: Router,
    private http: HttpClient,
    private cdr: ChangeDetectorRef
  ) {}

  ngOnInit(): void {
    this.loadStats();
  }

  loadStats() {
    this.http.get<any>(`${this.base}/patients?size=1`).subscribe({
      next: (res) => {
        this.totalPatients = res.totalElements ?? 0;
        this.cdr.detectChanges();
      },
      error: (err) => console.error('Patients FAILED:', err)
    });

    this.http.get<any>(`${this.base}/doctors?size=1`).subscribe({
      next: (res) => {
        this.totalDoctors = res.totalElements ?? 0;
        this.cdr.detectChanges();
      },
      error: (err) => console.error('Doctors FAILED:', err)
    });

    this.http.get<any>(`${this.base}/appointments/stats`).subscribe({
      next: (res) => {
        this.totalAppointments = res.total ?? 0;
        this.completedToday = res.completedToday ?? 0;
        this.cdr.detectChanges();
      },
      error: (err) => console.error('Appointments stats FAILED:', err)
    });
  }

  logout() {
    localStorage.removeItem('token');
    this.router.navigate(['/login']);
  }
}