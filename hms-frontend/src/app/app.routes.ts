import { Routes } from '@angular/router';
import { Login } from './auth/login/login.component';
import { authGuard } from './auth.guard';
import { Dashboard } from './dashboard/dashboard.component';
import { PatientComponent } from './patient/patient.component';
import { DoctorComponent } from './doctor/doctor.component';
import { AppointmentComponent } from './appointment/appointment.component';

export const routes: Routes = [
  { path: '', component: Login },
  { path: 'login', component: Login },
  { path: 'dashboard', component: Dashboard, canActivate: [authGuard] },
  { path: 'patients', component: PatientComponent, canActivate: [authGuard] },
  { path: 'doctors', component: DoctorComponent, canActivate: [authGuard] },
  { path: 'appointments', component: AppointmentComponent, canActivate: [authGuard] }
];
