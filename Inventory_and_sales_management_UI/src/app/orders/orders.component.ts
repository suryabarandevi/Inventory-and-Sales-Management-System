
import { Component, OnInit } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { FormsModule } from '@angular/forms';

import { ToastrService } from 'ngx-toastr';

interface Product {
  id: number;
  name: string;
  price: number;
  quantity: number;
  companyId: number;
  adminId: number;
}

interface OrderItem {
  productId: number;
  name: string;
  price: number;
  quantity: number;
  total: number;
}

@Component({
  selector: 'app-orders',
  templateUrl: './orders.component.html',
  styleUrls: ['./orders.component.css'],
})
export class OrdersComponent implements OnInit {
  existingProducts: any[] = [];
  orderItems: OrderItem[] = [];
  orderStatus: string = 'Pending';
  orderDate: Date = new Date();
  existingOrders: any[] = [];
  companyid: Number = 0;
  orderDetails: any[] = [];
  orderId: string = "";
  orderStatuses = ['Pending', 'In Progress', 'Shipped', 'Delivered'];
  currentPage: number = 1;
  
  productPage: number = 1; 
  

  filteredProducts: Product[] = [];
  filteredOrders: any[] = [];
  productSearch: string = '';
  orderSearch: string = '';

  constructor(private http: HttpClient,private toastr:ToastrService) {}

  ngOnInit(): void {
    this.fetchProducts();
  }


  filterProducts(): void {
    if (!this.productSearch) {
      this.filteredProducts = this.existingProducts;
    } else {
      this.filteredProducts = this.existingProducts.filter(product =>
        product.name.toLowerCase().includes(this.productSearch.toLowerCase())
      );
    }
  }

  filterOrders(): void {
    if (!this.orderSearch) {
      this.filteredOrders = this.existingOrders;
    } else {
      this.filteredOrders = this.existingOrders.filter(order =>
        order.orderId.toString().toLowerCase().includes(this.orderSearch.toLowerCase())
      );
    }
  }

  updateStatus(id: string, status: string) {
    const headers = new HttpHeaders({
      'Content-Type': 'application/json',
    });

    const options = {
      headers: headers,
      withCredentials: true,
    };

    this.http.put<boolean>(`http://localhost:8080/api/orders/updateStatus/${id}/${status}`, options).subscribe(
      (response: boolean) => {
        if (response) {
          this.toastr.success('Status Updated Successfully')
          //alert('Order Details updated successfully!');
        } else {
          alert('Failed to update Order details. Please try again.');
        }
      },
      (error: any) => {
        console.error('Error updating Order details:', error);
        alert('An error occurred while updating the Order details.');
      }
    );
  }

  fetchProducts(): void {
    const headers = new HttpHeaders({
      'Content-Type': 'application/json',
    });

    this.http
      .get<Product[]>('http://localhost:8080/api/products/getProducts', {
        headers,
        withCredentials: true,
      })
      .subscribe({
        next: (response:any) => {
          console.log("products"+response)
          this.existingProducts = response;
          this.filteredProducts = response; 
          this.companyid = this.existingProducts[0]?.adminId;
          this.fetchOrders();
        },
        error: (error:any) => console.error('Error fetching Products:', error)
      });
  }

  fetchOrders(): void {
    const headers = new HttpHeaders({
      'Content-Type': 'application/json',
    });

    this.http
      .get<any[]>(`http://localhost:8080/api/orders/company`, {
        headers,
        withCredentials: true,
      })
      .subscribe({
        next: (response:any) => {
          this.existingOrders = response;
          this.filteredOrders = response; 
        },
        error: (error:any) => console.error('Error fetching Orders:', error)
      });
  }

  addToOrder(product: Product): void {
    const existingItem = this.orderItems.find((item) => item.productId === product.id);
    if (existingItem) {
      existingItem.quantity++;
      existingItem.total = existingItem.quantity * existingItem.price;
    } else {
      this.orderItems.push({
        productId: product.id,
        name: product.name,
        price: product.price,
        quantity: 1,
        total: product.price,
      });
    }
  }

  incrementQuantity(item: OrderItem): void {
    item.quantity++;
    item.total = item.quantity * item.price;
  }

  decrementQuantity(item: OrderItem): void {
    if (item.quantity > 1) {
      item.quantity--;
      item.total = item.quantity * item.price;
    } else {
      this.removeItem(item);
    }
  }

  removeItem(item: OrderItem): void {
    this.orderItems = this.orderItems.filter((i) => i.productId !== item.productId);
  }

  getTotalPrice(): number {
    let totalPrice = 0;
    
    for (let i = 0; i < this.orderItems.length; i++) {
      totalPrice += this.orderItems[i].total;
    }
    
    return totalPrice;
  }

  createOrder(): void {
    const orderPayload = {
      status: this.orderStatus,
      date: this.orderDate,
      items: this.orderItems.map((item) => ({
        productId: item.productId,
        productName: item.name,
        productPrice: item.price,
        companyId: this.existingProducts.find((p) => p.id === item.productId)?.companyId || null,
        quantity: item.quantity,
      })),
    };
    console.log(orderPayload);

    const headers = new HttpHeaders({
      'Content-Type': 'application/json',
    });

    this.http
      .post('http://localhost:8080/api/orders/create', orderPayload, {
        headers: headers,
        withCredentials: true,
      })
      .subscribe(
        (response: any) => {
          console.log('Order created successfully:', response);
          this.toastr.success('Order Created Successfully')
         
          this.fetchOrders();
          this.resetOrder();
        },
        (error: any) => {
          console.error('Error creating order:', error);
          this.toastr.error('Unable to Create Order as required quantity is not available')
        }
      );
  }

  getOrderDetails(): void {
    const headers = new HttpHeaders({
      'Content-Type': 'application/json',
    });

    this.http
      .get<any[]>(`http://localhost:8080/api/orders/company/${this.orderId}`, {
        headers: headers,
        withCredentials: true,
      })
      .subscribe(
        (response: any[]) => {
          console.log('Fetched Orders:', response);
          this.orderDetails = response;
        },
        (error: any) => {
          console.error('Error fetching Orders:', error);
        }
      );
  }

  resetOrder(): void {
    this.orderItems = [];
    this.orderStatus = 'Pending';
    this.orderDate = new Date();
  }

  delete(id:number){
   
      const headers = new HttpHeaders({
        'Content-Type': 'application/json',
      });
  
      const options = {
        headers: headers,
        withCredentials: true, 
      };
  
      this.http.delete<boolean>(`http://localhost:8080/api/orders/delete/${id}`, options).subscribe(
        (response: boolean) => {
          this.toastr.success('Order Cancelled Successfully')
         
          this.fetchOrders(); 
        },
        (error: any) => {
          console.error('Error deleting Order:', error);
          alert('An error occurred while trying to delete the Order. Please try again.');
        }
      );
    }
  
}