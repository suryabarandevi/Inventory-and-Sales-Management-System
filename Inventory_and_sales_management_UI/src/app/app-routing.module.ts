

import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
// import { LoginComponent } from './new/login.component';
import { HomePageComponent } from './home-page/home-page.component';
// import { SelectRoleComponent } from './select-role/select-role.component';
import { StaffDashboardComponent } from './staff-dashboard/staff-dashboard.component';
import { AdminDashboardComponent } from './admin-dashboard/admin-dashboard.component';
import { ManagerDashboardComponent } from './manager-dashboard/manager-dashboard.component';
import { AuthGuard } from './guards/auth.guard'; 
import {CompanyFormComponent} from './company-form/company-form.component'
import {AboutComponent} from './about/about.component'
import { ProfilesComponent } from './profiles/profiles.component';
import { ProductsComponent } from './products/products.component';
import { CategoriesComponent } from './categories/categories.component';
import { OrdersComponent } from './orders/orders.component';
import { CustomersComponent } from './customers/customers.component';
import { OrderMappingComponent } from './order-mapping/order-mapping.component';
import { DashboardComponent } from './dashboard/dashboard.component';
import { SalesComponent } from './sales/sales.component';
import { PaymentFormComponent } from './payment-form/payment-form.component';
const routes: Routes = [
  // { path: '', component: LoginComponent }, 
  { path: '', component: HomePageComponent }, 
 
  // { 
  //   path: 'staff-dashboard', 
  //   component: StaffDashboardComponent, 
  //   canActivate: [AuthGuard],
  //   data: { role: 'STAFF' } 
  // },
  { 
    path: 'admin-dashboard', 
    component: AdminDashboardComponent, 
    canActivate: [AuthGuard],data: { role: 'ADMIN' },
    children: [ 
    { path: 'products',
    component: ProductsComponent,},
  { path: 'about', component: AboutComponent, },
  { path: 'profiles', component: ProfilesComponent, canActivate: [AuthGuard],data: { role: 'ADMIN' }},
  { path: 'orders', component: OrdersComponent, canActivate: [AuthGuard],data: { role: 'ADMIN' }},
  { path: 'customers', component: CustomersComponent, canActivate: [AuthGuard],data: { role: 'ADMIN' } },
  { path: 'orders-mapping', component: OrderMappingComponent , canActivate: [AuthGuard],data: { role: 'ADMIN' }},
  { path: 'dashboard', component: DashboardComponent , canActivate: [AuthGuard],data: { role: 'ADMIN' }},
  { path: 'sales', component: SalesComponent, canActivate: [AuthGuard],data: { role: 'ADMIN' } },
  { path: '', component: DashboardComponent, canActivate: [AuthGuard],data: { role: 'ADMIN' } },
 
      
    ], 
  },
  { 
    path: 'manager-dashboard', 
    component: ManagerDashboardComponent, 
    canActivate: [AuthGuard],
    data: { role: 'MANAGER' },
    children: [
    { path: 'products',
    component: ProductsComponent,},
  { path: 'about', component: AboutComponent, canActivate: [AuthGuard],
  data: { role: 'MANAGER' }},
  { path: 'profiles', component: ProfilesComponent, canActivate: [AuthGuard],
  data: { role: 'MANAGER' }},
  { path: 'orders', component: OrdersComponent, canActivate: [AuthGuard],
  data: { role: 'MANAGER' }},
  { path: 'customers', component: CustomersComponent },
  { path: 'orders-mapping', component: OrderMappingComponent, canActivate: [AuthGuard],
  data: { role: 'MANAGER' } },
  // { path: '', component: DashboardComponent },
  { path: 'sales', component: SalesComponent, canActivate: [AuthGuard],
  data: { role: 'MANAGER' } },
  { path: 'dashboard', component: DashboardComponent , canActivate: [AuthGuard],
  data: { role: 'MANAGER' }},
  { path: '', component: DashboardComponent, canActivate: [AuthGuard],
  data: { role: 'MANAGER' } },
 
 
      
    ], 
  },
  
  { path: 'company-form', component: CompanyFormComponent },

  {path:'payment-form',component:PaymentFormComponent},

  // { path: 'about', component: AboutComponent,canActivate: [AuthGuard], pathMatch:"full"},
 { path: '**', redirectTo: '', pathMatch: 'full' },
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule],
})
export class AppRoutingModule {}

