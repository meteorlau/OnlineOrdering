import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { CommonModule } from '@angular/common';
import { HttpClient } from '@angular/common/http';

@Component({
  selector: 'app-admin-product-detail',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './admin-product-detail.component.html'
})
export class AdminProductDetailComponent implements OnInit {
  product: any;
  stores: any[] = [];
  errorMessage = '';

  constructor(private route: ActivatedRoute, private http: HttpClient) {}

  ngOnInit(): void {
    this.route.paramMap.subscribe(params => {
      const id = params.get('productId');
      if (id) {
        this.http.get<any>(`http://localhost:8081/product-admin/products/${id}`).subscribe({
          next: data => {
            this.product = data.product;
            this.stores = data.availableStores;
          },
          error: () => this.errorMessage = '無法載入商品詳情'
        });
      }
    });
  }  
}

