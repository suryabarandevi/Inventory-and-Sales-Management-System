
import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { HttpClientModule } from '@angular/common/http';

import { HomePageComponent } from './home-page/home-page.component';

import { StaffDashboardComponent } from './staff-dashboard/staff-dashboard.component';
import { AdminDashboardComponent } from './admin-dashboard/admin-dashboard.component';
import { ManagerDashboardComponent } from './manager-dashboard/manager-dashboard.component';
import { CompanyFormComponent } from './company-form/company-form.component';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { AboutComponent } from './about/about.component';
import { RouterModule } from '@angular/router';
import { ProfilesComponent } from './profiles/profiles.component';
import { ProductsComponent } from './products/products.component';
import { CategoriesComponent } from './categories/categories.component';
import { OrdersComponent } from './orders/orders.component';
import { CustomersComponent } from './customers/customers.component';
import { OrderMappingComponent } from './order-mapping/order-mapping.component';
import { DashboardComponent } from './dashboard/dashboard.component';
import { SalesComponent } from './sales/sales.component';
import { PaymentFormComponent } from './payment-form/payment-form.component';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations'; 
import { ToastrModule } from 'ngx-toastr';
import { NgxPaginationModule } from 'ngx-pagination';




@NgModule({
  declarations: [
    AppComponent,
    HomePageComponent,
    // LoginComponent,
    // SelectRoleComponent,
    StaffDashboardComponent,
    AdminDashboardComponent,
    ManagerDashboardComponent,
    CompanyFormComponent,
    AboutComponent,
    ProfilesComponent,
    ProductsComponent,
    CategoriesComponent,
    OrdersComponent,
    CustomersComponent,
    OrderMappingComponent,
    DashboardComponent,
    SalesComponent,
    PaymentFormComponent,
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    HttpClientModule, 
    ReactiveFormsModule,
    RouterModule,
    FormsModule,
    BrowserAnimationsModule, 
    ToastrModule.forRoot({
      timeOut: 2000, 
      positionClass: 'toast-top-right', 
      preventDuplicates: true, 
    }), 
    NgxPaginationModule 
  ],
    
   


  bootstrap: [AppComponent],
})
export class AppModule {}
