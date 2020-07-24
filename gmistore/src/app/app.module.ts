import {BrowserModule} from '@angular/platform-browser';
import {NgModule} from '@angular/core';
import {FormsModule, ReactiveFormsModule} from "@angular/forms";

import {AppRoutingModule} from './app-routing.module';
import {AppComponent} from './app.component';
import {HeaderComponent} from './header/header.component';
import {FooterComponent} from './core/footer/footer.component';
import {NavComponent} from './core/nav/nav.component';
import {HomeComponent} from './page/home/home.component';
import {LoginComponent} from './user/login/login.component';
import {RegisterComponent} from './user/register/register.component';
import {UserEditComponent} from './user/user-edit/user-edit.component';
import {ForbiddenComponent} from './core/forbidden/forbidden.component';
import {UsersComponent} from './page/users/users.component';
import {OrdersComponent} from './page/orders/orders.component';
import {OrderEditComponent} from './page/order-edit/order-edit.component';
import {HTTP_INTERCEPTORS, HttpClientModule} from "@angular/common/http";
import {Ng2Webstorage} from 'ngx-webstorage';
import {ProductListComponent} from './product/product-list/product-list.component';
import {ProductEditComponent} from './product/product-edit/product-edit.component';
import {ProductCardComponent} from './product/product-card/product-card.component';
import {BrowserAnimationsModule} from '@angular/platform-browser/animations';
import {CollapseModule} from "ngx-bootstrap/collapse";
import {JumbotronComponent} from './core/jumbotron/jumbotron.component';
import {ProductDetailsComponent} from './product/product-details/product-details.component';
import {UserProfileComponent} from './user/user-profile/user-profile.component';
import {AddProductComponent} from './product/add-product/add-product.component';
import {AuthInterceptor} from "./utils/auth-interceptor";
import {RegisterSuccessComponent} from './user/register-success/register-success.component';
import {ConfirmAccountComponent} from './user/confirm-account/confirm-account.component';

@NgModule({
  declarations: [
    AppComponent,
    HeaderComponent,
    FooterComponent,
    NavComponent,
    HomeComponent,
    LoginComponent,
    RegisterComponent,
    UserEditComponent,
    ForbiddenComponent,
    UsersComponent,
    OrdersComponent,
    OrderEditComponent,
    ProductListComponent,
    ProductEditComponent,
    ProductCardComponent,
    JumbotronComponent,
    ProductDetailsComponent,
    UserProfileComponent,
    AddProductComponent,
    RegisterSuccessComponent,
    ConfirmAccountComponent
  ],
  imports: [
    FormsModule,
    BrowserModule,
    AppRoutingModule,
    FormsModule,
    ReactiveFormsModule,
    BrowserAnimationsModule,
    CollapseModule.forRoot(),
    Ng2Webstorage.forRoot(),
    HttpClientModule,

  ],
  providers: [
    { provide: HTTP_INTERCEPTORS, useClass: AuthInterceptor, multi: true },
  ],
  bootstrap: [AppComponent]
})
export class AppModule {
}
