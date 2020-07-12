import {BrowserModule} from '@angular/platform-browser';
import {NgModule} from '@angular/core';

import {AppRoutingModule} from './app-routing.module';
import {AppComponent} from './app.component';
import {HeaderComponent} from './header/header.component';
import {FooterComponent} from './core/footer/footer.component';
import {NavComponent} from './core/nav/nav.component';
import {HomeComponent} from './page/home/home.component';
import {LoginComponent} from './auth/login/login.component';
import {RegisterComponent} from './auth/register/register.component';
import {UserComponent} from './user/user/user.component';
import {UserEditComponent} from './user/user-edit/user-edit.component';
import {ForbiddenComponent} from './core/forbidden/forbidden.component';
import {UsersComponent} from './page/users/users.component';
import {OrdersComponent} from './page/orders/orders.component';
import {OrderEditComponent} from './page/order-edit/order-edit.component';
import {FormsModule, ReactiveFormsModule} from "@angular/forms";
import {HttpClientModule} from "@angular/common/http";
import {Ng2Webstorage} from 'ngx-webstorage';
import {ProductListComponent} from './product/product-list/product-list.component';
import {ProductEditComponent} from './product/product-edit/product-edit.component';
import {ProductCardComponent} from './product/product-card/product-card.component';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import {CollapseModule} from "ngx-bootstrap/collapse";
import { JumbotronComponent } from './core/jumbotron/jumbotron.component';

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
    ProductCardComponent,
    JumbotronComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    FormsModule,
    ReactiveFormsModule,
    BrowserAnimationsModule,
    CollapseModule.forRoot(),
    Ng2Webstorage.forRoot(),

    HttpClientModule,

  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule {
}
