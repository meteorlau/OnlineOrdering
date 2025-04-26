import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { HttpClient } from '@angular/common/http';

@Component({
  selector: 'app-admin-stock',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './admin-stock.component.html'
})
export class AdminStockComponent {
  stockDTO = {
    storeId: null,
    productId: null,
    stock: null,
    unitPrice: null
  };

  successMessage = '';
  errorMessage = '';

  constructor(private http: HttpClient) {}

  updateStock(): void {
    this.http.post('http://localhost:8081/product-admin/update-stock&price', this.stockDTO, { responseType: 'text' })
      .subscribe({
        next: res => {
          this.successMessage = res;
          this.errorMessage = '';
        },
        error: () => {
          this.successMessage = '';
          this.errorMessage = '更新庫存或價格失敗';
        }
      });
  }
}

