import { Component, OnInit } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-menu',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './menu.component.html'
})
export class MenuComponent implements OnInit {
  keyword = '';
  rawData: any[] = []; // 從後端獲得的原始資料 [{ product, availableStores }]
  products: any[] = []; // 拍平結構後的 [{ ...product, availableStores }]
  filteredProducts: any[] = [];
  selectedStore: { [productId: number]: any } = {};
  quantityMap: { [productId: number]: number } = {};
  errorMessage = '';
  successMessage = '';

  constructor(private http: HttpClient) {}

  ngOnInit(): void {
    this.loadProducts();
  }

  loadProducts(): void {
    this.http.get<any[]>('http://localhost:8081/menu/products').subscribe({
      next: data => {
        this.rawData = data;
        this.products = data.map(d => ({ ...d.product, availableStores: d.availableStores }));
        this.filteredProducts = [...this.products];
      },
      error: () => this.errorMessage = '❌ 無法載入菜單資料'
    });
  }

  filterProducts(): void {
    const lowerKeyword = this.keyword.toLowerCase();
    this.filteredProducts = this.products.filter(p =>
      p.name.toLowerCase().includes(lowerKeyword)
    );
  }

  addToCart(product: any): void {
    const store = this.selectedStore[product.id];
    const quantity = this.quantityMap[product.id];
    const username = this.getUsernameFromToken();
  
    if (!store || quantity == null) {
      this.errorMessage = '請選擇供應商與數量';
      return;
    }
  
    if (quantity <= 0 || quantity > store.stock) {
      this.errorMessage = '請選擇有效數量';
      return;
    }
  
    const cartItem = {
      itemId: product.id,
      quantity: quantity,
      storeName: store.store?.name || '未命名商鋪', // ✅ 新增
      storeLocation: store.store?.location || '無地址資料' // ✅ 新增
    };
  
    this.http.post(`http://localhost:8082/cart/${username}/items`, cartItem).subscribe({
      next: () => {
        this.successMessage = '✅ 已加入購物車！';
        this.errorMessage = '';
      },
      error: () => {
        this.successMessage = '';
        this.errorMessage = '加入購物車失敗';
      }
    });  
  }
  
  getUsernameFromToken(): string {
    const token = localStorage.getItem('token');
    if (!token) return 'anonymous';
  
    try {
      const payload = JSON.parse(atob(token.split('.')[1]));
      return payload.sub || payload.username || 'anonymous';
    } catch (e) {
      return 'anonymous';
    }
  }
}



