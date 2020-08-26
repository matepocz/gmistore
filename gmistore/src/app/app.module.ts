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
import {ProductEditComponent} from './components/product/product-edit/product-edit.component';
import {ProductCardComponent} from './components/product/product-card/product-card.component';
import {BrowserAnimationsModule} from '@angular/platform-browser/animations';
import {ProductDetailsComponent} from './components/product/product-details/product-details.component';
import {UserProfileComponent} from './components/user/user-profile/user-profile.component';
import {AddProductComponent} from './components/product/add-product/add-product.component';
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
import {DragDropModule} from "@angular/cdk/drag-drop";

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
    ProductEditComponent,
    ProductCardComponent,
    ProductDetailsComponent,
    UserProfileComponent,
    AddProductComponent,
    RegisterSuccessComponent,
    ConfirmAccountComponent,
    CartComponent,
    SideNavComponent,
    MaterialElevationDirective,
    AddProductReviewComponent,
    NotFoundComponent
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
        DragDropModule,

    ],
  providers: [
    {provide: HTTP_INTERCEPTORS, useClass: AuthInterceptor, multi: true},
    Title,
  ],
  bootstrap: [AppComponent]
})
export class AppModule {
}
