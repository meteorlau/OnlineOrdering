import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { HttpClient } from '@angular/common/http';
import { Router, RouterModule } from '@angular/router';

@Component({
  selector: 'app-admin-product',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterModule],
  templateUrl: './admin-product.component.html'
})
export class AdminProductComponent implements OnInit {
  products: any[] = [];
  errorMessage = '';
  newProduct = {
    name: '',
    description: '',
    imageUrl: ''
  };

  constructor(private http: HttpClient, private router: Router) {}

  ngOnInit(): void {
    this.loadProducts();
  }

  loadProducts(): void {
    this.http.get<any[]>('http://localhost:8081/product-admin/products').subscribe({
      next: data => this.products = data,
      error: () => this.errorMessage = '無法載入產品資料'
    });
  }

  addProduct(): void {
    const { name, description, imageUrl } = this.newProduct;
    if (!name || !imageUrl) {
      this.errorMessage = '請填寫完整的產品資訊';
      return;
    }

    this.http.post('http://localhost:8081/product-admin/add-product', this.newProduct).subscribe({
      next: () => {
        this.newProduct = { name: '', description: '', imageUrl: '' };
        this.loadProducts();
        this.errorMessage = '';
      },
      error: () => this.errorMessage = '新增產品失敗'
    });
  }

  viewProductDetails(productId: number): void {
    this.router.navigate([`/admin/products/${productId}`]);
  }
}
