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
import {JumbotronComponent} from './core/jumbotron/jumbotron.component';
import {ProductDetailsComponent} from './product/product-details/product-details.component';
import {UserProfileComponent} from './user/user-profile/user-profile.component';
import {AddProductComponent} from './product/add-product/add-product.component';
import {AuthInterceptor} from "./utils/auth-interceptor";
import {RegisterSuccessComponent} from './user/register-success/register-success.component';
import {ConfirmAccountComponent} from './user/confirm-account/confirm-account.component';
import {CartComponent} from './cart/cart.component';
import {MatInputModule} from "@angular/material/input";
import {MatIconModule} from "@angular/material/icon";
import {SideNavComponent} from './side-nav/side-nav.component';
import {LayoutModule} from '@angular/cdk/layout';
import {MatToolbarModule} from '@angular/material/toolbar';
import {MatButtonModule} from '@angular/material/button';
import {MatSidenavModule} from '@angular/material/sidenav';
import {MatListModule} from '@angular/material/list';
import {MatBadgeModule} from "@angular/material/badge";
import {AddressFormComponent} from './address-form/address-form.component';
import {MatSelectModule} from '@angular/material/select';
import {MatRadioModule} from '@angular/material/radio';
import {MatCardModule} from '@angular/material/card';
import {FlexLayoutModule} from "@angular/flex-layout";
import {MatSnackBarModule} from "@angular/material/snack-bar";
import {MaterialElevationDirective} from "./utils/material-elevation-directive";
import {MatTooltipModule} from "@angular/material/tooltip";
import {MatSlideToggleModule} from "@angular/material/slide-toggle";

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
    ConfirmAccountComponent,
    CartComponent,
    SideNavComponent,
    AddressFormComponent,
    MaterialElevationDirective
  ],
  imports: [
    FormsModule,
    BrowserModule,
    AppRoutingModule,
    FormsModule,
    ReactiveFormsModule,
    BrowserAnimationsModule,
    Ng2Webstorage.forRoot(),
    HttpClientModule,
    MatInputModule,
    MatIconModule,
    LayoutModule,
    MatToolbarModule,
    MatButtonModule,
    MatSidenavModule,
    MatListModule,
    MatBadgeModule,
    MatSelectModule,
    MatRadioModule,
    MatCardModule,
    FlexLayoutModule,
    MatSnackBarModule,
    MatTooltipModule,
    MatSlideToggleModule,
  ],
  providers: [
    {provide: HTTP_INTERCEPTORS, useClass: AuthInterceptor, multi: true},
  ],
  bootstrap: [AppComponent]
})
export class AppModule {
}
