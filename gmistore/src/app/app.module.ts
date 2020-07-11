import {BrowserModule} from '@angular/platform-browser';
import {NgModule} from '@angular/core';

import {AppRoutingModule} from './app-routing.module';
import {AppComponent} from './app.component';
import {HeaderComponent} from './header/header.component';
import {FooterComponent} from './footer/footer.component';
import {NavComponent} from './page/nav/nav.component';
import {HomeComponent} from './page/home/home.component';
import {LoginComponent} from './auth/login/login.component';
import {RegisterComponent} from './auth/register/register.component';
import {UserComponent} from './page/user/user.component';
import {UserEditComponent} from './page/user-edit/user-edit.component';
import {ForbiddenComponent} from './page/forbidden/forbidden.component';
import {UsersComponent} from './page/users/users.component';
import {OrdersComponent} from './page/orders/orders.component';
import {OrderEditComponent} from './page/order-edit/order-edit.component';
import {FormsModule, ReactiveFormsModule} from "@angular/forms";
import {RouterModule} from "@angular/router";
import {HttpClientModule} from "@angular/common/http";
import {Ng2Webstorage} from 'ngx-webstorage';
import { ProductListComponent } from './product/product-list/product-list.component';
import { ProductEditComponent } from './product/product-edit/product-edit.component';
import { ProductCardComponent } from './product/product-card/product-card.component';

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
    OrderEditComponent,
    ProductListComponent,
    ProductEditComponent,
    ProductCardComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    FormsModule,
    ReactiveFormsModule,
    Ng2Webstorage.forRoot(),
    RouterModule.forRoot([
      {path: '', component: HomeComponent},
      {path: 'login',component: LoginComponent},
      {path: 'register',component: RegisterComponent},
      {path: 'users',component: UsersComponent},
      {path: 'user/edit/:id',component: UserEditComponent},
      {path: 'orders',component: OrdersComponent},
      {path: 'product-list',component: ProductListComponent},
      {path: 'product/edit/:id',component: ProductEditComponent},
      {path: 'order/edit/:id',component: OrderEditComponent},
      {path: 'forbidden',component: ForbiddenComponent},
      {path: '**',redirectTo: '',}
    ]),
    HttpClientModule
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule {
}
