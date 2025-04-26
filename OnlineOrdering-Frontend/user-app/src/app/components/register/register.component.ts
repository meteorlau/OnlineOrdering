import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { UserService } from '../../services/user.service';

@Component({
  selector: 'app-register',
  standalone: true,
  imports: [FormsModule, CommonModule],
  templateUrl: './register.component.html'
})
export class RegisterComponent {
  username = '';
  password = '';
  role = 'USER';
  successMessage = '';
  errorMessage = '';

  constructor(private userService: UserService) {}

  register() {
    this.successMessage = '';
    this.errorMessage = '';

    // 檢查欄位是否為空
    if (!this.username || !this.password) {
      this.errorMessage = '請填寫帳號與密碼';
      return;
    }

    // 檢查密碼長度
    if (this.password.length < 6) {
      this.errorMessage = '密碼長度至少需為 6 位字元';
      return;
    }

    const user = { username: this.username, password: this.password };

    this.userService.register(user, this.role).subscribe({
      next: res => {
        this.successMessage = res;
        this.username = '';
        this.password = '';
        this.role = 'USER';
      },
      error: err => {
        this.errorMessage = '註冊失敗，請重試';
      }
    });
  }
}




