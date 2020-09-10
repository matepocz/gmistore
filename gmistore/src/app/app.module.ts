import {BrowserModule, Title} from '@angular/platform-browser';
import {NgModule} from '@angular/core';
import {FormsModule, ReactiveFormsModule} from "@angular/forms";

import {AppRoutingModule} from './app-routing.module';
import {AppComponent} from './app.component';
import {FooterComponent} from './components/footer/footer.component';
import {HomeComponent} from './components/home/home.component';
import {LoginComponent} from './components/user/login/login.component';
import {RegisterComponent} from './components/user/register/register.component';
import {UserEditComponent} from './components/user/user-edit/user-edit.component';
import {ForbiddenComponent} from './components/forbidden/forbidden.component';
import {HTTP_INTERCEPTORS, HttpClientModule} from "@angular/common/http";
import {Ng2Webstorage} from 'ngx-webstorage';
import {ProductListComponent} from './components/product/product-list/product-list.component';
import {ProductCardComponent} from './components/product/product-card/product-card.component';
import {BrowserAnimationsModule} from '@angular/platform-browser/animations';
import {ProductDetailsComponent} from './components/product/product-details/product-details.component';
import {UserProfileComponent} from './components/user/user-profile/user-profile.component';
import {ProductFormComponent} from './components/product/product-form/product-form.component';
import {AuthInterceptor} from "./utils/auth-interceptor";
import {RegisterSuccessComponent} from './components/user/register-success/register-success.component';
import {ConfirmAccountComponent} from './components/user/confirm-account/confirm-account.component';
import {CartComponent} from './components/cart/cart.component';
import {MatInputModule} from "@angular/material/input";
import {MatIconModule} from "@angular/material/icon";
import {SideNavComponent} from './components/side-nav/side-nav.component';
import {LayoutModule} from '@angular/cdk/layout';
import {MatToolbarModule} from '@angular/material/toolbar';
import {MatButtonModule} from '@angular/material/button';
import {MatSidenavModule} from '@angular/material/sidenav';
import {MatListModule} from '@angular/material/list';
import {MatBadgeModule} from "@angular/material/badge";
import {MatSelectModule} from '@angular/material/select';
import {MatRadioModule} from '@angular/material/radio';
import {MatCardModule} from '@angular/material/card';
import {FlexLayoutModule} from "@angular/flex-layout";
import {MatSnackBarModule} from "@angular/material/snack-bar";
import {MaterialElevationDirective} from "./utils/material-elevation-directive";
import {MatTooltipModule} from "@angular/material/tooltip";
import {MatSlideToggleModule} from "@angular/material/slide-toggle";
import {MatProgressSpinnerModule} from "@angular/material/progress-spinner";
import {MatExpansionModule} from "@angular/material/expansion";
import {MatSliderModule} from "@angular/material/slider";
import {MatProgressBarModule} from "@angular/material/progress-bar";
import {StarRatingModule} from "angular-star-rating";
import {AddProductReviewComponent} from './components/add-product-review/add-product-review.component';
import {NotFoundComponent} from './components/not-found/not-found.component';
import {MatCheckboxModule} from "@angular/material/checkbox";
import {AdminNavComponent} from './components/admin/admin-nav/admin-nav.component';
import {AdminDashboardComponent} from './components/admin/admin-dashboard/admin-dashboard.component';
import {DragDropModule} from "@angular/cdk/drag-drop";
import {MatCarouselModule} from '@ngmodule/material-carousel';
import {MatDialogModule} from '@angular/material/dialog';
import {MatMenuModule} from "@angular/material/menu";
import {MatGridListModule} from "@angular/material/grid-list";
import {GdprDialogComponent} from './components/gdpr-dialog/gdpr-dialog.component';
import {PrivacyComponent} from './components/privacy/privacy.component';
import {CookiesPrivacyComponent} from './components/cookies-privacy/cookies-privacy.component'
import {AdminProductComponent} from "./components/admin/admin-product/admin-product.component";
import {CheckoutComponent} from './components/checkout/checkout.component';
import {MatStepperModule} from "@angular/material/stepper";
import {AdminUserComponent} from './components/admin/admin-user/admin-user-list/admin-user.component';
import {ChartsModule} from 'ng2-charts';
import {MatTableModule} from "@angular/material/table";
import {MatSortModule} from "@angular/material/sort";
import {MatPaginatorModule} from "@angular/material/paginator";
import {AdminUserFormComponent} from './components/admin/admin-user/admin-user-form/admin-user-form.component';
import {ShippingAddressComponent} from './components/admin/admin-user/admin-user-form/shipping-address/shipping-address.component';
import {BillingAddressComponent} from './components/admin/admin-user/admin-user-form/billing-address/billing-address.component';
import {MatChipsModule} from "@angular/material/chips";

@NgModule({
  declarations: [
    AppComponent,
    FooterComponent,
    HomeComponent,
    LoginComponent,
    RegisterComponent,
    UserEditComponent,
    ForbiddenComponent,
    ProductListComponent,
    ProductCardComponent,
    ProductDetailsComponent,
    UserProfileComponent,
    ProductFormComponent,
    RegisterSuccessComponent,
    ConfirmAccountComponent,
    CartComponent,
    SideNavComponent,
    MaterialElevationDirective,
    AddProductReviewComponent,
    NotFoundComponent,
    GdprDialogComponent,
    PrivacyComponent,
    CookiesPrivacyComponent,
    NotFoundComponent,
    AdminDashboardComponent,
    AdminNavComponent,
    AdminProductComponent,
    AdminUserComponent,
    AdminUserFormComponent,
    ShippingAddressComponent,
    BillingAddressComponent,
    CheckoutComponent
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
        StarRatingModule.forRoot(),
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
        MatProgressSpinnerModule,
        MatExpansionModule,
        MatSliderModule,
        MatProgressBarModule,
        MatCheckboxModule,
        MatGridListModule,
        MatMenuModule,
        DragDropModule,
        MatCarouselModule.forRoot(),
        MatDialogModule,
        MatMenuModule,
        MatStepperModule,
        ChartsModule,
        MatTableModule,
        MatSortModule,
        MatPaginatorModule,
        MatChipsModule,

    ],
  providers: [
    {provide: HTTP_INTERCEPTORS, useClass: AuthInterceptor, multi: true},
    Title,
  ],
  bootstrap: [AppComponent]
})
export class AppModule {
}
