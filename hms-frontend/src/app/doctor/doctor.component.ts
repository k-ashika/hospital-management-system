import { Component, OnInit, ChangeDetectorRef, NgZone } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { RouterModule } from '@angular/router';
import { DoctorService } from './doctor.service';
import { finalize } from 'rxjs/operators';

@Component({
  selector: 'app-doctor',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterModule],
  templateUrl: './doctor.component.html'
})
export class DoctorComponent implements OnInit {

  doctors: any[] = [];
  loading = true;
  errorMsg = '';
  successMsg = '';

  name = ''; specialization = ''; email = ''; phone = ''; address = '';

  // Edit
  editMode = false;
  editId: number | null = null;

  // Search
  searchQuery = '';

  // Pagination
  currentPage = 0;
  totalPages = 0;
  pageSize = 5;

  constructor(
    private doctorService: DoctorService,
    private cdr: ChangeDetectorRef,
    private zone: NgZone
  ) {}

  ngOnInit(): void {
    setTimeout(() => this.loadDoctors(), 100);
  }

  loadDoctors() {
    this.loading = true;
    this.errorMsg = '';
    this.cdr.detectChanges();

    this.doctorService.getAllDoctors(this.currentPage, this.pageSize, this.searchQuery)
      .pipe(finalize(() => {
        this.zone.run(() => { this.loading = false; this.cdr.detectChanges(); });
      }))
      .subscribe({
        next: (res: any) => {
          this.zone.run(() => {
            this.doctors = res?.content ?? [];
            this.totalPages = res?.totalPages ?? 0;
            this.cdr.detectChanges();
          });
        },
        error: (err) => {
          this.zone.run(() => {
            this.errorMsg = err.status === 401
              ? 'Session expired. Please log in again.'
              : `Failed to load doctors. (${err.status})`;
            this.cdr.detectChanges();
          });
        }
      });
  }

  onSearch() {
    this.currentPage = 0;
    this.loadDoctors();
  }

  goToPage(page: number) {
    if (page < 0 || page >= this.totalPages) return;
    this.currentPage = page;
    this.loadDoctors();
  }

  editDoctor(d: any) {
    this.editMode = true;
    this.editId = d.id;
    this.name = d.name;
    this.specialization = d.specialization;
    this.email = d.email;
    this.phone = d.phone;
    this.address = d.address || '';
    window.scrollTo({ top: 0, behavior: 'smooth' });
  }

  cancelEdit() {
    this.editMode = false;
    this.editId = null;
    this.name = ''; this.specialization = '';
    this.email = ''; this.phone = ''; this.address = '';
  }

  saveDoctor() {
    if (!this.name || !this.specialization || !this.email || !this.phone) {
      this.errorMsg = 'Please fill all required fields.';
      return;
    }
    this.errorMsg = '';
    const doctor = {
      name: this.name, specialization: this.specialization,
      email: this.email, phone: this.phone, address: this.address
    };

    if (this.editMode && this.editId) {
      this.doctorService.updateDoctor(this.editId, doctor).subscribe({
        next: () => {
          this.successMsg = 'Doctor updated successfully!';
          this.cancelEdit();
          this.loadDoctors();
          setTimeout(() => this.successMsg = '', 3000);
        },
        error: (err) => {
          this.errorMsg = err.error && typeof err.error === 'object'
            ? Object.values(err.error).join(', ')
            : 'Failed to update doctor.';
        }
      });
    } else {
      this.doctorService.createDoctor(doctor).subscribe({
        next: () => {
          this.successMsg = 'Doctor added successfully!';
          this.cancelEdit();
          this.loadDoctors();
          setTimeout(() => this.successMsg = '', 3000);
        },
        error: (err) => {
          this.errorMsg = err.error && typeof err.error === 'object'
            ? Object.values(err.error).join(', ')
            : 'Failed to add doctor.';
        }
      });
    }
  }

  deleteDoctor(id: number) {
    if (!confirm('Are you sure you want to delete this doctor?')) return;
    this.doctorService.deleteDoctor(id).subscribe({
      next: () => {
        this.doctors = this.doctors.filter(d => d.id !== id);
        this.successMsg = 'Doctor deleted successfully!';
        setTimeout(() => this.successMsg = '', 3000);
      },
      error: () => { this.errorMsg = 'Failed to delete doctor.'; }
    });
  }

  get pages(): number[] {
    return Array.from({ length: this.totalPages }, (_, i) => i);
  }
}