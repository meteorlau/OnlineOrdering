import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({ providedIn: 'root' })
export class OrderService {
  private baseUrl = 'http://localhost:8083/order';

  constructor(private http: HttpClient) {}

  private createAuthHeaders(): HttpHeaders {
    const token = localStorage.getItem('token') || '';
    return new HttpHeaders({
      'Authorization': `Bearer ${token}`
    });
  }

  // 🔥 User 取得自己的訂單列表
  getUserOrders(): Observable<any[]> {
    return this.http.get<any[]>(`${this.baseUrl}/my-orders`, { headers: this.createAuthHeaders() });
  }

  // 🔥 Admin 取得所有訂單列表
  getAllOrdersAsAdmin(): Observable<any[]> {
    return this.http.get<any[]>(`${this.baseUrl}/orders`, { headers: this.createAuthHeaders() });
  }

  // 🔥 查詢單一訂單商品列表（自動根據角色）
  getOrderItemsById(orderId: string): Observable<any[]> {
    const token = localStorage.getItem('token');
    if (!token) {
      throw new Error('No token found');
    }

    const payload = JSON.parse(atob(token.split('.')[1]));
    const roles = payload.roles || [];

    let url = '';
    if (roles.includes('ROLE_ADMIN')) {
      url = `${this.baseUrl}/orders/${orderId}`;
    } else if (roles.includes('ROLE_USER')) {
      url = `${this.baseUrl}/my-orders/${orderId}`;
    } else {
      throw new Error('Unknown role');
    }

    return this.http.get<any[]>(url, { headers: this.createAuthHeaders() });
  }
}



