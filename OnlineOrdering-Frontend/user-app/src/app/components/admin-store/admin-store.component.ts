import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { HttpClient } from '@angular/common/http';
import { Router, RouterModule } from '@angular/router';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-admin-store',
  standalone: true,
  imports: [CommonModule, RouterModule, FormsModule],
  templateUrl: './admin-store.component.html'
})
export class AdminStoreComponent implements OnInit {
  stores: any[] = [];
  errorMessage = '';
  newStore = {
    name: '',
    location: ''
  };

  constructor(private http: HttpClient, private router: Router) {}

  ngOnInit(): void {
    this.loadStores();
  }

  loadStores(): void {
    this.http.get<any[]>('http://localhost:8081/store-admin/stores').subscribe({
      next: data => this.stores = data,
      error: () => this.errorMessage = '無法載入商鋪資料'
    });
  }

  addStore(): void {
    if (!this.newStore.name || !this.newStore.location) {
      this.errorMessage = '請填寫完整的商鋪資訊';
      return;
    }

    this.http.post('http://localhost:8081/store-admin/add-store', this.newStore).subscribe({
      next: () => {
        this.newStore = { name: '', location: '' };
        this.loadStores();
        this.errorMessage = '';
      },
      error: () => this.errorMessage = '新增商鋪失敗'
    });
  }

  deactivateStore(storeId: number): void {
    this.http.post(`http://localhost:8081/store-admin/stores/${storeId}/deactivate`, {}, { responseType: 'text' })
      .subscribe({
        next: () => {
          const store = this.stores.find(s => s.id === storeId);
          if (store) store.isActive = false;
        },
        error: () => this.errorMessage = '停用商鋪失敗'
      });
  }

  viewStoreDetails(storeId: number): void {
    this.router.navigate([`/admin/stores/${storeId}`]);
  }
}


