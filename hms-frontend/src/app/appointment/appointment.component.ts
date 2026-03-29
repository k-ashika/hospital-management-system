import { Component, OnInit, ChangeDetectorRef, NgZone } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { RouterModule } from '@angular/router';
import { AppointmentService } from './appointment.service';
import { finalize } from 'rxjs/operators';

@Component({
  selector: 'app-appointment',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterModule],
  templateUrl: './appointment.component.html'
})
export class AppointmentComponent implements OnInit {

  appointments: any[] = [];
  doctors: any[] = [];
  patients: any[] = [];
  loading = true;
  errorMsg = '';
  successMsg = '';

  appointmentDate = '';
  appointmentTime = '';
  status = 'Scheduled';
  reason = '';
  selectedDoctorId: number | null = null;
  selectedPatientId: number | null = null;

  // Edit
  editMode = false;
  editId: number | null = null;

  // Pagination
  currentPage = 0;
  totalPages = 0;
  pageSize = 5;

  // ✅ Track status per row by appointment id
  rowStatus: { [id: number]: string } = {};

  constructor(
    private appointmentService: AppointmentService,
    private cdr: ChangeDetectorRef,
    private zone: NgZone
  ) {}

  ngOnInit(): void {
    setTimeout(() => this.loadAll(), 100);
  }

  loadAll() {
    this.loading = true;
    this.errorMsg = '';
    this.cdr.detectChanges();

    this.appointmentService.getAllAppointments(this.currentPage, this.pageSize)
      .pipe(finalize(() => {
        this.zone.run(() => {
          this.loading = false;
          this.cdr.detectChanges();
        });
      }))
      .subscribe({
        next: (res: any) => {
          this.zone.run(() => {
            this.appointments = res?.content ?? [];
            this.totalPages = res?.totalPages ?? 0;

            // ✅ Initialize rowStatus map from loaded appointments
            this.rowStatus = {};
            this.appointments.forEach(a => {
              this.rowStatus[a.id] = a.status;
            });

            this.cdr.detectChanges();
          });
        },
        error: (err) => {
          this.zone.run(() => {
            this.errorMsg = `Failed to load appointments. (${err.status})`;
            this.cdr.detectChanges();
          });
        }
      });

    this.appointmentService.getAllDoctors().subscribe({
      next: (res: any) => {
        this.zone.run(() => {
          this.doctors = res?.content ?? [];
          this.cdr.detectChanges();
        });
      }
    });

    this.appointmentService.getAllPatients().subscribe({
      next: (res: any) => {
        this.zone.run(() => {
          this.patients = res?.content ?? [];
          this.cdr.detectChanges();
        });
      }
    });
  }

  goToPage(page: number) {
    if (page < 0 || page >= this.totalPages) return;
    this.currentPage = page;
    this.loadAll();
  }

  editAppointment(a: any) {
    this.editMode = true;
    this.editId = a.id;
    this.appointmentDate = a.appointmentDate;
    this.appointmentTime = a.appointmentTime;
    this.status = a.status;
    this.reason = a.reason;
    this.selectedDoctorId = a.doctor?.id;
    this.selectedPatientId = a.patient?.id;
    window.scrollTo({ top: 0, behavior: 'smooth' });
  }

  cancelEdit() {
    this.editMode = false;
    this.editId = null;
    this.appointmentDate = '';
    this.appointmentTime = '';
    this.reason = '';
    this.status = 'Scheduled';
    this.selectedDoctorId = null;
    this.selectedPatientId = null;
  }

  // ✅ Update status directly from the table dropdown
  updateStatus(a: any) {
    const newStatus = this.rowStatus[a.id];
    const updated = {
      appointmentDate: a.appointmentDate,
      appointmentTime: a.appointmentTime,
      status: newStatus,
      reason: a.reason,
      doctor: { id: a.doctor?.id },
      patient: { id: a.patient?.id }
    };

    this.appointmentService.updateAppointment(a.id, updated).subscribe({
      next: () => {
        a.status = newStatus;
        this.successMsg = `Status updated to "${newStatus}"!`;
        this.cdr.detectChanges();
        setTimeout(() => this.successMsg = '', 3000);
      },
      error: () => {
        // ✅ Revert dropdown on failure
        this.rowStatus[a.id] = a.status;
        this.errorMsg = 'Failed to update status.';
        this.cdr.detectChanges();
      }
    });
  }

  saveAppointment() {
    if (!this.appointmentDate || !this.appointmentTime ||
        !this.selectedDoctorId || !this.selectedPatientId || !this.reason) {
      this.errorMsg = 'Please fill all required fields.';
      return;
    }
    this.errorMsg = '';

    const appointment = {
      appointmentDate: this.appointmentDate,
      appointmentTime: this.appointmentTime,
      status: this.status,
      reason: this.reason,
      doctor: { id: this.selectedDoctorId },
      patient: { id: this.selectedPatientId }
    };

    if (this.editMode && this.editId) {
      this.appointmentService.updateAppointment(this.editId, appointment).subscribe({
        next: () => {
          this.successMsg = 'Appointment updated successfully!';
          this.cancelEdit();
          this.loadAll();
          setTimeout(() => this.successMsg = '', 3000);
        },
        error: () => { this.errorMsg = 'Failed to update appointment.'; }
      });
    } else {
      this.appointmentService.createAppointment(appointment).subscribe({
        next: () => {
          this.successMsg = 'Appointment scheduled successfully!';
          this.cancelEdit();
          this.loadAll();
          setTimeout(() => this.successMsg = '', 3000);
        },
        error: () => { this.errorMsg = 'Failed to schedule appointment.'; }
      });
    }
  }

  deleteAppointment(id: number) {
    if (!confirm('Delete this appointment?')) return;
    this.appointmentService.deleteAppointment(id).subscribe({
      next: () => {
        this.appointments = this.appointments.filter(a => a.id !== id);
        delete this.rowStatus[id];
        this.successMsg = 'Appointment deleted!';
        setTimeout(() => this.successMsg = '', 3000);
      },
      error: () => { this.errorMsg = 'Failed to delete appointment.'; }
    });
  }

  getStatusClass(status: string) {
    if (status === 'Completed') return 'badge-success';
    if (status === 'Cancelled') return 'badge-danger';
    return 'badge-warning';
  }

  get pages(): number[] {
    return Array.from({ length: this.totalPages }, (_, i) => i);
  }
}