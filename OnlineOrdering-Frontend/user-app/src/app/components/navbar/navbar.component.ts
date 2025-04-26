import { Component, OnInit } from '@angular/core';
import { Router, RouterModule } from '@angular/router';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-navbar',
  standalone: true,
  imports: [CommonModule, RouterModule],
  templateUrl: './navbar.component.html',
  styleUrls: ['./navbar.component.css']
})
export class NavbarComponent implements OnInit {
  username: string | null = null;
  role: string | null = null;       
  roles: string[] = [];             

  constructor(private router: Router) {}

  ngOnInit(): void {
    const token = localStorage.getItem('token');
    if (token) {
      try {
        const payload = JSON.parse(atob(token.split('.')[1]));

        this.username = payload.sub || payload.username;

        if (Array.isArray(payload.roles)) {
          this.roles = payload.roles.map((r: string) => r.replace('ROLE_', ''));
        } else if (payload.role) {
          this.roles = [payload.role.replace('ROLE_', '')];
        }

        this.role = this.roles[0] || null;

        console.log('👤 使用者:', this.username);
        console.log('🎭 角色:', this.role);
      } catch (e) {
        console.error('❌ JWT 解析失敗:', e);
      }
    }
  }

  isLoggedIn(): boolean {
    return !!localStorage.getItem('token');
  }

  logout(): void {
    localStorage.removeItem('token');
    this.router.navigate(['/login']);
  }
}