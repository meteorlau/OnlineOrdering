import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router, RouterModule } from '@angular/router';

@Component({
  selector: 'app-admin-profile',
  standalone: true,
  imports: [CommonModule, RouterModule],
  templateUrl: './admin-profile.component.html'
})
export class AdminProfileComponent implements OnInit {
  username: string | null = null;
  role: string | null = null;

  constructor(private router: Router) {}

  ngOnInit(): void {
    const token = localStorage.getItem('token');
    if (token) {
      try {
        const payload = JSON.parse(atob(token.split('.')[1]));
        this.username = payload.sub || payload.username;

        if (Array.isArray(payload.roles)) {
          this.role = payload.roles[0].replace('ROLE_', '');
        } else {
          this.role = (payload.role || payload.roles || '').replace('ROLE_', '');
        }
      } catch (e) {
        console.error('JWT 解析失敗:', e);
      }
    }
  }

  changePassword(): void {
    this.router.navigate(['/change-password']);
  }
}

