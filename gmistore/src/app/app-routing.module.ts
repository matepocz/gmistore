import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import {HomeComponent} from "./page/home/home.component";
import {LoginComponent} from "./page/login/login.component";
import {UsersComponent} from "./page/users/users.component";
import {UserEditComponent} from "./page/user-edit/user-edit.component";
import {OrderEditComponent} from "./page/order-edit/order-edit.component";
import {ProductsComponent} from "./page/products/products.component";
import {ProductEditComponent} from "./page/product-edit/product-edit.component";
import {RegisterComponent} from "./page/register/register.component";
import {OrdersComponent} from "./page/orders/orders.component";
import {ForbiddenComponent} from "./page/forbidden/forbidden.component";


const routes: Routes = [
  {
    path: '',
    component:HomeComponent
  },
  {
    path: 'login',
    component:LoginComponent
  },
  {
    path: 'register',
    component:RegisterComponent
  },
  {
    path: 'users',
    component:UsersComponent
  },
  {
    path: 'user/edit/:id',
    component:UserEditComponent
  },
  {
    path: 'products',
    component:ProductsComponent
  },
  {
    path: 'products/edit/:id',
    component:ProductEditComponent
  },
  {
    path: 'orders',
    component:OrdersComponent
  },
  {
    path: 'order/edit/:id',
    component:OrderEditComponent
  },
  {
    path: 'forbidden',
    component:ForbiddenComponent
  },
  {
    path: '**',
    redirectTo:'',
  }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
