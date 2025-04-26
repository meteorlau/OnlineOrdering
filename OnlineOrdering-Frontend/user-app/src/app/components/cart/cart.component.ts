import { Component, OnInit } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

// 定義一個正確的 item 型別（更安全且清晰）✅
interface CartItem {
  itemId: number;
  name: string;
  price: number;
  quantity: number;
  storeName: string;
  storeLocation: string;
}

@Component({
  selector: 'app-cart',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './cart.component.html'
})
export class CartComponent implements OnInit {
  cartId = this.getUsernameFromToken();
  items: CartItem[] = [];  // ✅ 指定正確型別
  totalPrice = 0;
  errorMessage = '';
  successMessage = '';

  constructor(private http: HttpClient) {}

  ngOnInit(): void {
    this.loadCart();
  }

  loadCart(): void {
    this.http.get<any>(`http://localhost:8082/cart/${this.cartId}`).subscribe({
      next: (res) => {
        this.items = res.items || [];
        this.totalPrice = this.items.reduce((sum, item) => sum + item.price * item.quantity, 0);
        this.errorMessage = '';
      },
      error: () => this.errorMessage = '❌ 無法載入購物車'
    });
  }

  removeItem(itemId: number): void {
    this.http.delete(`http://localhost:8082/cart/${this.cartId}/items/${itemId}`).subscribe({
      next: () => {
        this.successMessage = '✅ 商品已移除';
        this.errorMessage = '';
        this.loadCart();
      },
      error: () => this.errorMessage = '❌ 無法移除商品'
    });
  }

  clearCart(): void {
    this.http.delete(`http://localhost:8082/cart/${this.cartId}`).subscribe({
      next: () => {
        this.successMessage = '🗑️ 購物車已清空';
        this.items = [];
        this.totalPrice = 0;
        this.errorMessage = '';
      },
      error: () => this.errorMessage = '❌ 清空失敗'
    });
  }

  placeOrder(): void {
    if (this.items.length === 0) {
      this.errorMessage = '❌ 購物車是空的，無法下單';
      return;
    }
  
    const token = localStorage.getItem('token');
    if (!token) {
      this.errorMessage = '❌ 未登入或權限不足';
      return;
    }
  
    const orderItems = this.items.map(item => ({
      productId: item.itemId,
      productName: item.name,    // ✅ 加上這行，傳商品名稱！
      quantity: item.quantity,
      unitPrice: item.price,
      storeName: item.storeName,
      storeLocation: item.storeLocation
    }));
    
    const headers = new HttpHeaders({
      'Authorization': `Bearer ${token}`,
      'Content-Type': 'application/json'
    });
  
    this.http.post('http://localhost:8083/order/place-order', orderItems, { headers }).subscribe({
      next: (res) => {
        this.successMessage = '✅ 訂單已成功提交！';
        this.errorMessage = '';
        this.clearCart(); // 成功後清空購物車
      },
      error: (err) => {
        console.error('❌ 下單失敗:', err);
        if (err.status === 403) {
          this.errorMessage = '❌ 權限不足，無法下單';
        } else {
          this.errorMessage = '❌ 訂單提交失敗，請稍後再試';
        }
        this.successMessage = '';
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



