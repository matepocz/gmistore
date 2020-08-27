import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {HomeComponent} from "./components/home/home.component";
import {LoginComponent} from "./components/user/login/login.component";
import {RegisterComponent} from "./components/user/register/register.component";
import {UserEditComponent} from "./components/user/user-edit/user-edit.component";
import {ProductListComponent} from "./components/product/product-list/product-list.component";
import {ForbiddenComponent} from "./components/forbidden/forbidden.component";
import {ProductDetailsComponent} from "./components/product/product-details/product-details.component";
import {ProductFormComponent} from "./components/product/product-form/product-form.component";
import {RegisterSuccessComponent} from "./components/user/register-success/register-success.component";
import {ConfirmAccountComponent} from "./components/user/confirm-account/confirm-account.component";
import {UserProfileComponent} from "./components/user/user-profile/user-profile.component";
import {CartComponent} from "./components/cart/cart.component";
import {AddProductReviewComponent} from "./components/add-product-review/add-product-review.component";
import {NotFoundComponent} from "./components/not-found/not-found.component";
import {AdminDashboardComponent} from "./components/admin/admin-dashboard/admin-dashboard.component";
import {AdminNavComponent} from "./components/admin/admin-nav/admin-nav.component";
import {PrivacyComponent} from "./components/privacy/privacy.component";
import {CookiesPrivacyComponent} from "./components/cookies-privacy/cookies-privacy.component";


const routes: Routes = [
  {path: '', component: HomeComponent},
  {path: 'login', component: LoginComponent},
  {path: 'register', component: RegisterComponent},
  {path: 'register-success', component: RegisterSuccessComponent},
  {path: 'confirm-account', component: ConfirmAccountComponent},
  {path: 'user/my-account', component: UserProfileComponent},
  {path: 'user/edit', component: UserEditComponent},
  {path: 'add-product', component: ProductFormComponent},
  {path: 'product-list', component: ProductListComponent},
  {path: 'product/:slug', component: ProductDetailsComponent},
  {path: 'edit-product/:slug', component: ProductFormComponent},
  {path: 'add-review/:slug', component: AddProductReviewComponent},
  {path: 'forbidden', component: ForbiddenComponent},
  {path: 'cart', component: CartComponent},
  {path: 'not-found', component: NotFoundComponent,},
  {path: 'privacy', component: PrivacyComponent,},
  {path: 'cookies-privacy', component: CookiesPrivacyComponent,},
  {path: 'admin', component: AdminNavComponent},
  {path: 'admin/dashboard', component: AdminDashboardComponent},
  {path: '**', component: NotFoundComponent,}
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]

})
export class AppRoutingModule {
  static routableComponents = [
    HomeComponent,
    LoginComponent,

    RegisterComponent,
    UserEditComponent,
    ProductListComponent,
    ForbiddenComponent
  ];
}
