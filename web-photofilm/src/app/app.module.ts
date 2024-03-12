import { NgModule } from '@angular/core';
import { CommonModule, DatePipe } from '@angular/common';
import { BrowserModule } from '@angular/platform-browser';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { LoginComponent } from './components/login/login.component';
import { CatalogComponent } from './components/catalog/catalog.component';
import { ProductDetailComponent } from './components/product-detail/product-detail.component';
import { ShoppingCartComponent } from './components/shopping-cart/shopping-cart.component';
import { CheckoutComponent } from './components/checkout/checkout.component';
import { AdminPanelComponent } from './components/admin/admin-panel/admin-panel.component';
import { Error404Component } from './shared/error404/error404.component';
import { HeaderComponent } from './shared/components/header/header.component';
import { HttpClientModule } from '@angular/common/http';
import { ProductService } from './services/product/product.service';
import { CatalogElementComponent } from './components/catalog-element/catalog-element.component';
import { LazyImageComponent } from './shared/components/lazy-image/lazy-image.component';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { RegisterComponent } from './components/register/register.component';
import { LoginModalComponent } from './components/login-modal/login-modal.component';
import { UserService } from './services/user/user.service';
import { AuthService } from './services/auth/auth.service';
import { UserPanelComponent } from './components/user-panel/user-panel.component';
import { RegisterModalComponent } from './components/register-modal/register-modal.component';
import { CookieService } from 'ngx-cookie-service';
import { NewCategoryComponent } from './components/admin/new-category/new-category.component';
import { NewProductComponent } from './components/admin/new-product/new-product.component';
import { UpdateProductComponent } from './components/admin/update-product/update-product.component';
import { UpdateProductStockComponent } from './components/admin/update-product-stock/update-product-stock.component';
import { AddAdminComponent } from './components/admin/add-admin/add-admin.component';
import { CategoryDetailsComponent } from './components/admin/category-details/category-details.component';
import { ProductsTableComponent } from './components/admin/products-table/products-table.component';
import {OrdersComponent} from './components/orders/orders.component';
import {OrderDetailsComponent} from './components/order-details/order-details.component';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { MatPaginatorModule } from '@angular/material/paginator';
import { ResetPasswordComponent } from './components/reset-password/reset-password.component';
import { PasswordModalComponent } from './components/password-modal/password-modal.component';
import { MatSliderModule } from '@angular/material/slider';  

@NgModule({
  declarations: [
    AppComponent,
    LoginComponent,
    RegisterComponent,
    CatalogComponent,
    ProductDetailComponent,
    ShoppingCartComponent,
    CheckoutComponent,
    AdminPanelComponent,
    UserPanelComponent,
    Error404Component,
    HeaderComponent,
    CatalogElementComponent,
    LazyImageComponent,
    LoginModalComponent,
    RegisterModalComponent,
    NewCategoryComponent,
    NewProductComponent,
    UpdateProductComponent,
    UpdateProductStockComponent,
    AddAdminComponent,
    CategoryDetailsComponent,
    ProductsTableComponent,
    OrdersComponent,
    OrderDetailsComponent,
    ResetPasswordComponent,
    PasswordModalComponent

  ],
  imports: [
    CommonModule,
    BrowserModule,
    AppRoutingModule,
    HttpClientModule,
    FormsModule,
    ReactiveFormsModule,
    BrowserAnimationsModule,
    MatPaginatorModule,  
    MatSliderModule
  ],
  exports: [
    LazyImageComponent,
    LoginModalComponent,
    RegisterModalComponent,
    PasswordModalComponent,
    HeaderComponent,
    HeaderComponent,
  ],
  providers: [
    CookieService,
    ProductService,
    UserService,
    AuthService,
    DatePipe
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
