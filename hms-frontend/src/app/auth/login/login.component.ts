import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { HttpClient } from '@angular/common/http';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './login.component.html'
})
export class Login {
  username = '';
  password = '';
  errorMsg = '';
  loading = false;
  showPassword = false;  // ✅ toggle state

  constructor(private http: HttpClient, private router: Router) {}

  login() {
    this.loading = true;
    this.errorMsg = '';
    this.http.post<any>('http://localhost:8080/api/auth/login', {
      username: this.username,
      password: this.password
    }).subscribe({
      next: (res) => {
        localStorage.setItem('token', res.token);
        this.router.navigate(['/dashboard']);
      },
      error: (err) => {
        this.errorMsg = err.error || 'Login failed. Check your credentials.';
        this.loading = false;
      }
    });
  }
}