import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {HomeComponent} from "./components/home/home.component";
import {LoginComponent} from "./components/user/login/login.component";
import {RegisterComponent} from "./components/user/register/register.component";
import {UserEditComponent} from "./components/user/user-edit/user-edit.component";
import {ProductListComponent} from "./components/product/product-list/product-list.component";
import {ProductEditComponent} from "./components/product/product-edit/product-edit.component";
import {ForbiddenComponent} from "./components/forbidden/forbidden.component";
import {ProductDetailsComponent} from "./components/product/product-details/product-details.component";
import {AddProductComponent} from "./components/product/add-product/add-product.component";
import {RegisterSuccessComponent} from "./components/user/register-success/register-success.component";
import {ConfirmAccountComponent} from "./components/user/confirm-account/confirm-account.component";
import {UserProfileComponent} from "./components/user/user-profile/user-profile.component";
import {CartComponent} from "./components/cart/cart.component";
import {AddProductReviewComponent} from "./components/add-product-review/add-product-review.component";
import {NotFoundComponent} from "./components/not-found/not-found.component";
import {AdminDashboardComponent} from "./components/admin/admin-dashboard/admin-dashboard.component";
import {AdminProductComponent} from "./components/admin/admin-product/admin-product.component";
import {AdminUserComponent} from "./components/admin/admin-user/admin-user.component";
import {SideNavComponent} from "./components/side-nav/side-nav.component";
import {AdminNavComponent} from "./components/admin/admin-nav/admin-nav.component";


const routes: Routes = [
  {path: '', component: HomeComponent},
  {path: 'login', component: LoginComponent},
  {path: 'register', component: RegisterComponent},
  {path: 'register-success', component: RegisterSuccessComponent},
  {path: 'confirm-account', component: ConfirmAccountComponent},
  {path: 'user/my-account', component: UserProfileComponent},
  {path: 'user/edit', component: UserEditComponent},
  {path: 'add-product', component: AddProductComponent},
  {path: 'product-list', component: ProductListComponent},
  {path: 'product/:slug', component: ProductDetailsComponent},
  {path: 'edit-product/:slug', component: ProductEditComponent},
  {path: 'add-review/:slug', component: AddProductReviewComponent},
  {path: 'forbidden', component: ForbiddenComponent},
  {path: 'cart', component: CartComponent},
  {path: 'admin', component: AdminNavComponent,
    children: [
      { path: 'dashboard',component: AdminDashboardComponent},
      { path: 'product',component: AdminProductComponent},
      { path: 'user',component: AdminUserComponent},
    ]},
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
    ProductEditComponent,
    ForbiddenComponent
  ];
}
