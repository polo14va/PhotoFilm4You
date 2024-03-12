import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { LoginModalComponent } from './components/login-modal/login-modal.component';
import { RegisterModalComponent } from './components/register-modal/register-modal.component';
import { CatalogComponent } from './components/catalog/catalog.component';
import { ProductDetailComponent } from './components/product-detail/product-detail.component';
import { ShoppingCartComponent } from './components/shopping-cart/shopping-cart.component';
import { CheckoutComponent } from './components/checkout/checkout.component';
import { AdminPanelComponent } from './components/admin/admin-panel/admin-panel.component';
import { Error404Component } from './shared/error404/error404.component';
import { UserPanelComponent } from './components/user-panel/user-panel.component';
import { NewCategoryComponent } from './components/admin/new-category/new-category.component';
import { NewProductComponent } from './components/admin/new-product/new-product.component';
import { UpdateProductComponent } from './components/admin/update-product/update-product.component';
import { UpdateProductStockComponent } from './components/admin/update-product-stock/update-product-stock.component';
import { AddAdminComponent } from './components/admin/add-admin/add-admin.component';
import { AuthGuard } from './shared/components/auth-guard/auth-guard.component';
import { OrdersComponent } from './components/orders/orders.component';
import { OrderDetailsComponent } from './components/order-details/order-details.component';
import { ChangePasswordComponent } from './components/change-password/change-password.component';
import { PasswordModalComponent } from './components/password-modal/password-modal.component';


const routes: Routes = [
  { path: 'login', component: LoginModalComponent },
  { path: 'register', component: RegisterModalComponent },
  { path: 'catalog', component: CatalogComponent },
  { path: 'product/:id', component: ProductDetailComponent },
  { path: 'shopping-cart', component: ShoppingCartComponent },
  { path: 'shopping-cart/:id', component: ShoppingCartComponent },
  { path: 'orderdetails/:id',  component: OrderDetailsComponent},
  { path: 'user/change-password', component: ChangePasswordComponent},
  { path: 'password', component: PasswordModalComponent},
  { path: 'admin',
    component: AdminPanelComponent,
    children: [
      {path: 'new-category', component: NewCategoryComponent},
      {path: 'new-product', component: NewProductComponent},
      {path: 'update-product', component: UpdateProductComponent},
      {path: 'update-product-stock', component: UpdateProductStockComponent},
      {path: 'add-admin', component: AddAdminComponent}
    ],
    canActivate: [AuthGuard]
  },
  { path: 'user', component: UserPanelComponent, canActivate: [AuthGuard] },
  { path: 'checkout', component: CheckoutComponent, canActivate: [AuthGuard] },
  { path: 'orders',  component: OrdersComponent, canActivate: [AuthGuard]},

  { path: '', redirectTo: '/catalog', pathMatch: 'full' }, // Redirige a 'catalog' como ruta por defecto
  { path: '**', component: Error404Component } // Manejo de ruta no encontrada (404)
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
