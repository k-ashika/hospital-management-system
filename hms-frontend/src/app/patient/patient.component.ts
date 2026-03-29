import { Component, OnInit, ChangeDetectorRef, NgZone } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { RouterModule } from '@angular/router';
import { PatientService } from './patient.service';
import { finalize } from 'rxjs/operators';

@Component({
  selector: 'app-patient',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterModule],
  templateUrl: './patient.component.html'
})
export class PatientComponent implements OnInit {

  patients: any[] = [];
  loading = true;
  errorMsg = '';
  successMsg = '';

  // Form fields
  name = '';
  age: number | null = null;
  gender = '';
  email = '';
  phone = '';
  address = '';

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
    private patientService: PatientService,
    private cdr: ChangeDetectorRef,
    private zone: NgZone
  ) {}

  ngOnInit(): void {
    setTimeout(() => this.loadPatients(), 100);
  }

  loadPatients() {
    this.loading = true;
    this.errorMsg = '';
    this.cdr.detectChanges();

    this.patientService.getAllPatients(this.currentPage, this.pageSize, this.searchQuery)
      .pipe(finalize(() => {
        this.zone.run(() => {
          this.loading = false;
          this.cdr.detectChanges();
        });
      }))
      .subscribe({
        next: (res: any) => {
          this.zone.run(() => {
            this.patients = res?.content ?? [];
            this.totalPages = res?.totalPages ?? 0;
            this.cdr.detectChanges();
          });
        },
        error: (err) => {
          this.zone.run(() => {
            this.errorMsg = err.status === 401
              ? 'Session expired. Please log in again.'
              : `Failed to load patients. (${err.status})`;
            this.cdr.detectChanges();
          });
        }
      });
  }

  onSearch() {
    this.currentPage = 0;
    this.loadPatients();
  }

  goToPage(page: number) {
    if (page < 0 || page >= this.totalPages) return;
    this.currentPage = page;
    this.loadPatients();
  }

  editPatient(p: any) {
    this.editMode = true;
    this.editId = p.id;
    this.name = p.name;
    this.age = p.age;
    this.gender = p.gender;
    this.email = p.email;
    this.phone = p.phone;
    this.address = p.address;
    window.scrollTo({ top: 0, behavior: 'smooth' });
  }

  cancelEdit() {
    this.editMode = false;
    this.editId = null;
    this.name = '';
    this.age = null;
    this.gender = '';
    this.email = '';
    this.phone = '';
    this.address = '';
  }

  savePatient() {
    if (!this.name || !this.email || !this.phone || !this.address || !this.gender || !this.age) {
      this.errorMsg = 'Please fill in all required fields.';
      return;
    }
    this.errorMsg = '';

    const patient = {
      name: this.name,
      age: this.age,
      gender: this.gender,
      email: this.email,
      phone: this.phone,
      address: this.address
    };

    if (this.editMode && this.editId) {
      // UPDATE
      this.patientService.updatePatient(this.editId, patient).subscribe({
        next: () => {
          this.successMsg = 'Patient updated successfully!';
          this.cancelEdit();
          this.loadPatients();
          setTimeout(() => this.successMsg = '', 3000);
        },
        error: (err) => {
          this.errorMsg = err.error && typeof err.error === 'object'
            ? Object.values(err.error).join(', ')
            : 'Failed to update patient.';
        }
      });
    } else {
      // CREATE
      this.patientService.createPatient(patient).subscribe({
        next: () => {
          this.successMsg = 'Patient added successfully!';
          this.cancelEdit();
          this.loadPatients();
          setTimeout(() => this.successMsg = '', 3000);
        },
        error: (err) => {
          this.errorMsg = err.error && typeof err.error === 'object'
            ? Object.values(err.error).join(', ')
            : 'Failed to add patient.';
        }
      });
    }
  }

  deletePatient(id: number) {
    if (!confirm('Are you sure you want to delete this patient?')) return;
    this.patientService.deletePatient(id).subscribe({
      next: () => {
        this.patients = this.patients.filter(p => p.id !== id);
        this.successMsg = 'Patient deleted successfully!';
        setTimeout(() => this.successMsg = '', 3000);
      },
      error: () => {
        this.errorMsg = 'Failed to delete patient.';
      }
    });
  }

  get pages(): number[] {
    return Array.from({ length: this.totalPages }, (_, i) => i);
  }
}