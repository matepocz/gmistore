import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {HomeComponent} from "./page/home/home.component";
import {LoginComponent} from "./auth/login/login.component";
import {RegisterComponent} from "./auth/register/register.component";
import {UsersComponent} from "./page/users/users.component";
import {UserEditComponent} from "./page/user-edit/user-edit.component";
import {OrdersComponent} from "./page/orders/orders.component";
import {ProductListComponent} from "./product/product-list/product-list.component";
import {ProductEditComponent} from "./product/product-edit/product-edit.component";
import {OrderEditComponent} from "./page/order-edit/order-edit.component";
import {ForbiddenComponent} from "./page/forbidden/forbidden.component";

const routes: Routes = [
  {path: '', component: HomeComponent},
  {path: 'home', component: HomeComponent},
  {path: 'login', component: LoginComponent},
  {path: 'register', component: RegisterComponent},
  {path: 'users', component: UsersComponent},
  {path: 'user/edit/:id', component: UserEditComponent},
  {path: 'orders', component: OrdersComponent},
  {path: 'order/edit/:id', component: OrderEditComponent},
  {path: 'product-list', component: ProductListComponent},
  {path: 'product/edit/:id', component: ProductEditComponent},
  {path: 'forbidden', component: ForbiddenComponent},
  {path: '**', redirectTo: '',}
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
    UsersComponent,
    UserEditComponent,
    OrdersComponent,
    ProductListComponent,
    ProductEditComponent,
    OrderEditComponent,
    ForbiddenComponent
  ];
}
