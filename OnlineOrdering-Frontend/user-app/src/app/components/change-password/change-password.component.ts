import { Component } from '@angular/core';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { HttpClient } from '@angular/common/http';
import { Router } from '@angular/router';

@Component({
  selector: 'app-change-password',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './change-password.component.html'
})
export class ChangePasswordComponent {
  form: FormGroup;
  successMessage = '';
  errorMessage = '';

  constructor(
    private fb: FormBuilder,
    private http: HttpClient,
    private router: Router
  ) {
    this.form = this.fb.group({
      password: ['', [Validators.required]],
      newPassword: ['', [Validators.required, Validators.minLength(6)]],
      reEnteredPassword: ['', [Validators.required]]
    });
  }

  get password() {
    return this.form.get('password')?.value || '';
  }

  get newPassword() {
    return this.form.get('newPassword')?.value || '';
  }

  get reEnteredPassword() {
    return this.form.get('reEnteredPassword')?.value || '';
  }

  get isTooShort() {
    return this.newPassword.length > 0 && this.newPassword.length < 6;
  }

  get isMismatch() {
    return this.reEnteredPassword.length > 0 && this.newPassword !== this.reEnteredPassword;
  }

  get isSameAsOld() {
    return this.newPassword.length > 0 && this.newPassword === this.password;
  }

  // 🔥 加上角色判斷
  getRoleFromToken(): string {
    const token = localStorage.getItem('token');
    if (!token) return '';

    try {
      const payload = JSON.parse(atob(token.split('.')[1]));
      const roles: string[] = payload.roles;
      if (roles.includes('ROLE_ADMIN')) {
        return 'ADMIN';
      }
      return 'USER';
    } catch (error) {
      console.error('解析 token 失敗:', error);
      return '';
    }
  }

  onSubmit() {
    const { password, newPassword, reEnteredPassword } = this.form.value;

    if (this.form.invalid) {
      this.errorMessage = '請確保所有欄位都正確填寫（新密碼至少6位）';
      return;
    }

    if (this.isSameAsOld) {
      this.errorMessage = '新密碼不能與舊密碼相同';
      return;
    }

    if (this.isMismatch) {
      this.errorMessage = '兩次輸入的新密碼不一致';
      return;
    }

    // 🔥 根據角色選正確 API
    const role = this.getRoleFromToken();
    const endpoint = role === 'ADMIN'
      ? 'http://localhost:8080/admin/change-password'
      : 'http://localhost:8080/user/change-password';

    this.http.post(endpoint, {
      password,
      newPassword,
      reEnteredPassword
    }, { responseType: 'text' }).subscribe({
      next: (response) => {
        this.successMessage = response || '密碼修改成功 ✅';
        this.errorMessage = '';
        this.form.reset();
        setTimeout(() => this.router.navigate(['/profile']), 2000);
      },
      error: (err) => {
        this.errorMessage = err.error?.message || '密碼修改失敗，請稍後再試';
        this.successMessage = '';
      }
    });
  }
}


