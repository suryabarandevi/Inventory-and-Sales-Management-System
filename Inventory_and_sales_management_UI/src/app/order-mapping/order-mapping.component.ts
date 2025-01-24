import { Component, OnInit } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { ToastrService } from 'ngx-toastr';
interface Customer {
  id: number;
  firstName: string;
  lastName: string;
  email: string;
  // companyId: string;
  address: string;
  city: string;
  state: string;
}

interface Order {
  id: string;
  orderId: string;
  productName: string;
  productPrice: number;
  quantity: number;
  status: string;
  date: Date;
  customerId?: number;
}

interface OrderMapping {
  id: number;
  orderId: string;
  customerId: number;
  date: string;
  // companyId: string;
  status:string;
}

interface MappedOrderData {
  id:number;
  order: Order;
  customer: Customer;
  orderStatus:string;
}

@Component({
  selector: 'app-order-mapping',
  templateUrl: './order-mapping.component.html',
  styleUrls: ['./order-mapping.component.css']
})
export class OrderMappingComponent implements OnInit {
  orders: Order[] = [];
  customers: Customer[] = [];
  selectedOrder: Order | null = null;
  selectedCustomer: Customer | null = null;
  companyId: string = "";
  orderMappings: OrderMapping[] = [];
  mappedOrders: MappedOrderData[] = [];
  
  orderSearchTerm: string = '';
  customerSearchTerm: string = '';
  filteredOrders: Order[] = [];
  filteredCustomers: Customer[] = [];
  filteredMappedOrders: MappedOrderData[] = [];

  isMapping: boolean = false;
  isSendingInvoice: boolean = false;
  currentPage: number = 1;
  constructor(private http: HttpClient,private toastr:ToastrService) {}

  ngOnInit() {

    this.fetchCustomers();
    this.fetchOrders();
  }

 
  filterOrders() {
    if (!this.orderSearchTerm.trim()) {
      this.filteredOrders = this.orders.filter(order =>
        !this.orderMappings.some(mapping => mapping.orderId === order.orderId)
      );
      return;
    }

    const searchTermLower = this.orderSearchTerm.toLowerCase();
    this.filteredOrders = this.orders.filter(order =>
      !this.orderMappings.some(mapping => mapping.orderId === order.orderId) &&
      (order.orderId.toLowerCase().includes(searchTermLower))
       
    );
  }

  filterCustomers() {
    if (!this.customerSearchTerm.trim()) {
      this.filteredCustomers = this.customers;
      return;
    }

    const searchTermLower = this.customerSearchTerm.toLowerCase();
    this.filteredCustomers = this.customers.filter(customer =>
      
      customer.email.toLowerCase().includes(searchTermLower) 
      
    );
  }

  filterMappedOrders() {
    if (!this.orderSearchTerm.trim()) {
      this.filteredMappedOrders = this.mappedOrders;
      return;
    }

    const searchTermLower = this.orderSearchTerm.toLowerCase();
    this.filteredMappedOrders = this.mappedOrders.filter(mappedOrder =>
      mappedOrder.order.orderId.toLowerCase().includes(searchTermLower)
      
    );
  }

  fetchOrderMapping() {
    this.http.get<OrderMapping[]>(`http://localhost:8080/api/orders/ordermapping/get`, {
      withCredentials: true
    }).subscribe(
      (response:any) => {
        console.log(response)
        this.orderMappings = response;
        this.processMappedOrders();
        this.orderMappings.sort((a, b) => 
        new Date(b.date).getTime() - new Date(a.date).getTime()
      );
      
      },
      (error:any) => console.error('Error fetching order mappings:', error)
    );
  }

  processMappedOrders() {
    this.mappedOrders = [];
    const customerMap = new Map(this.customers.map(customer => [customer.id, customer]));
    const orderMap = new Map(this.orders.map(order => [order.orderId, order]));
    
    this.orderMappings.forEach(mapping => {
      const customer = customerMap.get(mapping.customerId);
      const order = orderMap.get(mapping.orderId);
      const orderStatus=mapping.status;
      const id=mapping.id;
      
      
      
      if (customer && order) {
        this.mappedOrders.push({
          id:id,
          order: order,
          customer: customer,
          orderStatus: orderStatus,
         
        });
      }
    });

   
    this.filteredMappedOrders = this.mappedOrders;
    const mappedOrderIds = new Set(this.orderMappings.map(m => m.orderId));
    this.filteredOrders = this.orders.filter(order => !mappedOrderIds.has(order.orderId));
    this.filteredCustomers = this.customers;
  }

  fetchOrders() {
    this.http.get<Order[]>(`http://localhost:8080/api/orders/company`, {
      withCredentials: true
    }).subscribe(
      (response:any) => {
        console.log(response)
        this.orders = response;
        // this.filteredOrders = response;
        this.fetchOrderMapping();
      },
      (error:any) => console.error('Error fetching orders:', error)
    );
  }

  fetchCustomers() {
    this.http.get<Customer[]>('http://localhost:8080/api/customers/get', {
      withCredentials: true
    }).subscribe(
      (response:any) => {
        console.log(response);
        console.log(this.customers)
        this.customers = response;
        this.filteredCustomers = response;
        // this.companyId = this.customers[0].companyId;
        
      },
      (error:any) => console.error('Error fetching customers:', error)
    );
  }

  selectOrder(order: Order) {
    this.selectedOrder = order;
  }

  selectCustomer(customer: Customer) {
    this.selectedCustomer = customer;
  }

  mapOrderToCustomer() {
    if (!this.selectedOrder || !this.selectedCustomer) return;
    this.isMapping = true;
    
    const mapping = {
      orderId: this.selectedOrder.orderId,
      customerId: this.selectedCustomer.id,
      // companyId: this.companyId
    };

    this.http.post('http://localhost:8080/api/orders/ordermapping', mapping, {
      withCredentials: true
    }).subscribe({
      next: (response: any) => {
        this.toastr.success('Order Mapped Successfully')
        console.log('Order mapped successfully:', response);
        this.fetchOrders();
        this.selectedOrder = null;
        this.selectedCustomer = null;
        // alert("Order Mapped Successfully");
      },
      error: (error: any) => {
        console.log(mapping)
        console.error('Error mapping order:', error);
        alert("Failed to map order. Please try again.");
      },
      complete: () => {
        this.isMapping = false;
      }
    });
  }

  sendInvoice(id:number,email: string, orderId: string): void {
    this.isSendingInvoice = true;
    
    const headers = new HttpHeaders({
      'Content-Type': 'application/json'
    });

    this.http.put(`http://localhost:8080/api/orders/ordermapping/sendInvoice/${id}/${email}/${orderId}`, null, {
      headers,
      withCredentials: true,
    }).subscribe({
      next: (response: any) => {
        this.toastr.success('Invoice Sent Successfully')
        this.fetchOrderMapping();
        // alert(`Invoice successfully sent`);
      },
      error: (error:any) => {
        console.error('Error sending invoice:', error);
        this.toastr.error('error sending Invoice')
        // alert("Failed to send invoice. Please try again.");
      },
      complete: () => {
        this.isSendingInvoice = false;
      }
    });
  }
}

