
import { Component } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { ToastrService } from 'ngx-toastr';

interface Customers {
  id: number;
  firstName: string;
  lastName: string;
  email: string;
  address: string;
  city: string;
  state: string;
 
  // companyId: number;
}

// interface Customer {
//   id: number;
//   firstName: string;
//   lastName: string;
//   customerEmail: string;
//   address: string;
//   city: string;
//   state: string;
//   companyId: number;
// }

// interface OrderMapping {
//   id: number;
//   orderId: string;
//   customerId: number;
//   companyId: number;
//   status: string;
//   productPrice: number;
//   date: string;
//   customerName?: string;
// }

@Component({
  selector: 'app-customers',
  templateUrl: './customers.component.html',
  styleUrls: ['./customers.component.css']
})
export class CustomersComponent {
  
  // isFormOpen: boolean = false;
  existingCustomers: Customers[] = [];
  customerForm: FormGroup;
  isFormOpen: boolean =false;
  isEditMode: boolean =false;
  editingCustomerId: any;
  customersPage:number=1;
  // orderMappings: OrderMapping[] = [];
 
  constructor(
    private http: HttpClient,
    private toastr: ToastrService,
    private fb: FormBuilder
  ) {
    this.customerForm = this.fb.group({
      firstName: ['', Validators.required],
      lastName: ['', Validators.required],
      email: ['', [Validators.required, Validators.email]],
      address: ['', Validators.required],
      city: ['', Validators.required],
      state: ['', Validators.required]
    });
  }

  ngOnInit() {
    this.fetchCustomers();
    // this.fetchOrderMapping();
  }

  fetchCustomers() {
    this.http.get<Customers[]>('http://localhost:8080/api/customers/get', {
      withCredentials: true
    }).subscribe({
      next: (response: any) => {
        console.log('Fetched Customers:', response);
        this.existingCustomers = response;
      },
      error: (error: any) => {
        this.toastr.error('Unable to fetch customers.');
        console.error('Error fetching customers:', error);
      }
    });
  }
  // openForm() {
  //   this.isFormOpen = true;
  // }

  closeForm() {
    this.isFormOpen = false;
    this.customerForm.reset();
  }

  openForm() {
    this.isEditMode = false;
    this.isFormOpen = true;
    this.customerForm.reset();
  }

  editCustomer(customer: any) {
    this.isEditMode = true;
    this.isFormOpen = true;
    this.editingCustomerId = customer.id;
    this.customerForm.patchValue(customer);
  }

  saveCustomer() {
    if (this.isEditMode) {
      const updatedCustomer = { id: this.editingCustomerId, ...this.customerForm.value };
      this.http.put(`http://localhost:8080/api/customers/update/${this.editingCustomerId}`, updatedCustomer,{ withCredentials: true}).subscribe(
        (response:any) => {
          this.toastr.success('Customer updated successfully');
          this.fetchCustomers();
          this.closeForm();
        },
        (error:any) => {
          console.error('Error updating customer:', error);
          this.toastr.error('Failed to update customer');
        }
      );
    } else {
      this.http.post('http://localhost:8080/api/customers/create', this.customerForm.value,{ withCredentials: true}).subscribe(
        (response:any) => {
          this.toastr.success('Customer added successfully');
          this.fetchCustomers();
          this.closeForm();
        },
        (error:any) => {
          console.error('Error creating customer:', error);
          this.toastr.error('Customer with the given Email Already Exists');
        }
      );
    }
  }

  deleteCustomer(id: number) {
   

    this.http.delete<boolean>(`http://localhost:8080/api/customers/${id}`, {withCredentials: true}).subscribe({
      next: (response: boolean) => {
        this.toastr.success('Customer deleted successfully!');
        this.fetchCustomers();
      },
      error: (error: any) => {
        console.error('Error deleting customer:', error);
        this.toastr.error("Can't Delete old customer");
      }
    });
  }
 
  // fetchOrderMapping() {
  //   this.http.get<OrderMapping[]>('http://localhost:8080/api/orders/ordermapping/get', {
  //     withCredentials: true
  //   }).subscribe({
  //     next: (response) => {
  //       console.log('Fetched order mappings:', response);
  //       this.orderMappings = response.map(order => {
  //         const customer = this.existingCustomers.find(c => c.id === order.customerId);
          
  //         let customerName = 'Unknown Customer';  
  //         if (customer) {
  //           customerName = `${customer.firstName} ${customer.lastName}`;
  //         }
    
         
  //         return {
  //           ...order,  
  //           customerName: customerName
  //         };
  //       });
      
  //       this.orderMappings.sort((a, b) => 
  //         new Date(b.date).getTime() - new Date(a.date).getTime()
  //       );
        
  //       console.log('Processed order mappings:', this.orderMappings);
  //     },
  //     error: (error) => console.error('Error fetching order mappings:', error)
  //   });
  // }

}
