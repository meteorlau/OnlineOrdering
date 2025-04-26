import { Component, OnInit, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';

@Component({
  selector: 'app-profile',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './profile.component.html'
})
export class ProfileComponent implements OnInit {
  username: string | null = null;
  role: string | null = null;

  constructor(private cdr: ChangeDetectorRef, private router: Router) {}

  ngOnInit(): void {
    const token = localStorage.getItem('token');
    if (token) {
      try {
        const payload = JSON.parse(atob(token.split('.')[1]));
        this.username = payload.sub || payload.username;
        this.role = payload.role || payload.roles || null;
        this.cdr.detectChanges();
      } catch (e) {
        console.error('JWT 解析失敗:', e);
      }
    }
  }

  changePassword() {
    this.router.navigate(['/change-password']);
  }
}





