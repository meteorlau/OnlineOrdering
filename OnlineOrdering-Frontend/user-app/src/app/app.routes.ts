import { Routes } from '@angular/router';
import { authGuard } from './guards/auth.guard';

import { LoginComponent } from './components/login/login.component';
import { RegisterComponent } from './components/register/register.component';
import { ProfileComponent } from './components/profile/profile.component';
import { OrdersComponent } from './components/orders/orders.component';
import { OrderDetailComponent } from './components/order-detail/order-detail.component';
import { ChangePasswordComponent } from './components/change-password/change-password.component';
import { NavbarComponent } from './components/navbar/navbar.component';
import { AdminProfileComponent } from './components/admin-profile/admin-profile.component';
import { AdminStoreComponent } from './components/admin-store/admin-store.component';
import { AdminProductComponent } from './components/admin-product/admin-product.component';
import { AdminStockComponent } from './components/admin-stock/admin-stock.component';
import { AdminProductDetailComponent } from './components/admin-product-detail/admin-product-detail.component';
import { AdminStoreDetailComponent } from './components/admin-store-detail/admin-store-detail.component';
import { AdminOrdersComponent } from './components/admin-orders/admin-orders.component'; // ✅ 新增 Admin 訂單總覽
import { MenuComponent } from './components/menu/menu.component';
import { CartComponent } from './components/cart/cart.component';

export const routes: Routes = [
  // 公開頁面
  { path: 'login', component: LoginComponent },
  { path: 'register', component: RegisterComponent },

  // 登入後頁面
  {
    path: '',
    component: NavbarComponent,
    canActivate: [authGuard],
    children: [
      // 👤 使用者端
      { path: 'profile', component: ProfileComponent },
      { path: 'orders', component: OrdersComponent },
      { path: 'orders/:orderId', component: OrderDetailComponent },
      { path: 'change-password', component: ChangePasswordComponent },
      { path: 'menu', component: MenuComponent },
      { path: 'cart', component: CartComponent },

      // 👑 管理員端
      { path: 'admin/profile', component: AdminProfileComponent },
      { path: 'admin/stores', component: AdminStoreComponent },
      { path: 'admin/products', component: AdminProductComponent },
      { path: 'admin/stock', component: AdminStockComponent },
      { path: 'admin/products/:productId', component: AdminProductDetailComponent },
      { path: 'admin/stores/:storeId', component: AdminStoreDetailComponent },
      { path: 'admin/orders', component: AdminOrdersComponent },            // ✅ Admin 訂單總覽
      { path: 'admin/orders/:orderId', component: OrderDetailComponent }, // ✅ Admin 單筆訂單詳情

      // 預設導向
      { path: '', redirectTo: 'profile', pathMatch: 'full' }
    ]
  },

  // fallback
  { path: '**', redirectTo: 'login' }
];


