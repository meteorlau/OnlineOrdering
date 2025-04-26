import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { CommonModule } from '@angular/common';
import { HttpClient } from '@angular/common/http';

@Component({
  selector: 'app-admin-store-detail',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './admin-store-detail.component.html'
})
export class AdminStoreDetailComponent implements OnInit {
  store: any;
  products: any[] = [];
  errorMessage = '';

  constructor(private route: ActivatedRoute, private http: HttpClient) {}

  ngOnInit(): void {
    this.route.paramMap.subscribe(params => {
      const id = params.get('storeId');
      if (id) {
        this.http.get<any>(`http://localhost:8081/store-admin/stores/${id}`).subscribe({
          next: data => {
            this.store = data.store;
            this.products = data.availableProducts;
          },
          error: () => this.errorMessage = '無法載入商鋪詳情'
        });
      }
    });
  }  
}

