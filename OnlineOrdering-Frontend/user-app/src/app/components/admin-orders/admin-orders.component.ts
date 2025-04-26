import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router, RouterModule } from '@angular/router';
import { OrderService } from '../../services/order.service';
import { HttpClient, HttpHeaders } from '@angular/common/http';

@Component({
  selector: 'app-admin-orders',
  standalone: true,
  imports: [CommonModule, RouterModule],
  templateUrl: './admin-orders.component.html'
})
export class AdminOrdersComponent implements OnInit {
  orders: any[] = [];

  constructor(
    private orderService: OrderService,
    private router: Router,
    private http: HttpClient
  ) {}

  ngOnInit(): void {
    this.loadAllOrders();
  }

  loadAllOrders(): void {
    this.orderService.getAllOrdersAsAdmin().subscribe({
      next: (data: any[]) => {
        // 🔥 按 createdAt 字段倒序排列
        this.orders = data.sort((a, b) => new Date(b.createdAt).getTime() - new Date(a.createdAt).getTime());
      },
      error: (err: any) => console.error('載入訂單失敗:', err)
    });
  }
  

  viewOrderDetails(orderId: string): void {
    this.router.navigate(['/admin/orders', orderId]);
  }

  markOrderAsCompleted(orderId: string): void {
    const token = localStorage.getItem('token');
    const headers = new HttpHeaders({ 'Authorization': `Bearer ${token}` });

    this.http.post(`http://localhost:8083/order/${orderId}/complete`, {}, { headers, responseType: 'text' }).subscribe({
      next: () => {
        alert('✅ 訂單標記為完成');
        this.loadAllOrders(); // 重新載入訂單列表
      },
      error: () => {
        alert('❌ 標記失敗，請稍後再試');
      }
    });
  }

  cancelOrder(orderId: string): void {
    const token = localStorage.getItem('token');
    const headers = new HttpHeaders({ 'Authorization': `Bearer ${token}` });

    this.http.post(`http://localhost:8083/order/${orderId}/cancel`, {}, { headers, responseType: 'text' }).subscribe({
      next: () => {
        alert('✅ 訂單已取消');
        this.loadAllOrders(); // 重新載入訂單列表
      },
      error: () => {
        alert('❌ 取消失敗，請稍後再試');
      }
    });
  }
}



