import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { HeaderComponent } from './header/header.component';
import { FooterComponent } from './footer/footer.component';
import { NavComponent } from './page/nav/nav.component';
import { HomeComponent } from './page/home/home.component';
import { LoginComponent } from './auth/login/login.component';
import { RegisterComponent } from './auth/register/register.component';
import { UserComponent } from './page/user/user.component';
import { UserEditComponent } from './page/user-edit/user-edit.component';
import { ForbiddenComponent } from './page/forbidden/forbidden.component';
import { UsersComponent } from './page/users/users.component';
import { OrdersComponent } from './page/orders/orders.component';
import { ProductsComponent } from './page/products/products.component';
import { ProductEditComponent } from './page/product-edit/product-edit.component';
import { OrderEditComponent } from './page/order-edit/order-edit.component';
import {FormsModule, ReactiveFormsModule} from "@angular/forms";
import {RouterModule} from "@angular/router";
import {HttpClientModule} from "@angular/common/http";

@NgModule({
  declarations: [
    AppComponent,
    HeaderComponent,
    FooterComponent,
    NavComponent,
    HomeComponent,
    LoginComponent,
    RegisterComponent,
    UserComponent,
    UserEditComponent,
    ForbiddenComponent,
    UsersComponent,
    OrdersComponent,
    ProductsComponent,
    ProductEditComponent,
    OrderEditComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    FormsModule,
    ReactiveFormsModule,
    RouterModule.forRoot([
      {path: 'register', component: RegisterComponent}
    ]),
    HttpClientModule
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
