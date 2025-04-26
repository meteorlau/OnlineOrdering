import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { CommonModule } from '@angular/common';
import { OrderService } from '../../services/order.service';
import { HttpClient } from '@angular/common/http';

@Component({
  selector: 'app-order-detail',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './order-detail.component.html'
})
export class OrderDetailComponent implements OnInit {
  orderItems: any[] = [];  // ✅ 這裡是商品列表
  orderId: string = '';
  errorMessage = '';
  successMessage = '';

  constructor(
    private route: ActivatedRoute,
    private orderService: OrderService,
    private http: HttpClient
  ) {}

  ngOnInit(): void {
    this.route.paramMap.subscribe(params => {
      const id = params.get('orderId');
      if (id) {
        this.orderId = id;
        this.loadOrderItems();
      }
    });
  }

  loadOrderItems(): void {
    this.orderService.getOrderItemsById(this.orderId).subscribe({
      next: (data) => this.orderItems = data,
      error: () => this.errorMessage = '無法載入訂單商品'
    });
  }

  markAsCompleted(): void {
    this.http.post(`http://localhost:8083/order/${this.orderId}/complete`, {}, { responseType: 'text' }).subscribe({
      next: () => {
        this.successMessage = '✅ 訂單已標記為完成';
        this.loadOrderItems(); // reload 商品
      },
      error: () => {
        this.errorMessage = '❌ 更新訂單狀態失敗';
      }
    });
  }

  markAsCancelled(): void {
    this.http.post(`http://localhost:8083/order/${this.orderId}/cancel`, {}, { responseType: 'text' }).subscribe({
      next: () => {
        this.successMessage = '✅ 訂單已標記為取消';
        this.loadOrderItems(); // reload 商品
      },
      error: () => {
        this.errorMessage = '❌ 更新訂單狀態失敗';
      }
    });
  }
}





