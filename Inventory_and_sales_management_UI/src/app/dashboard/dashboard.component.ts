import { Component, OnInit } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';

import { Router } from '@angular/router';
import { Chart } from 'chart.js/auto';
import { ToastrService } from 'ngx-toastr';

interface Customer {
  id: number;
  firstName: string;
  lastName: string;
  customerEmail: string;
  address: string;
  city: string;
  state: string;
  // companyId: number;
}

interface OrderMapping {
  id: number;
  orderId: string;
  customerId: number;
  // companyId: number;
  status: string;
  productPrice: number;
  date: string;
  customerName?: string;
}

@Component({
  selector: 'app-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.css']
})
export class DashboardComponent implements OnInit {
  // salesData: any[] = [];
  salesChart: any;
  existingCustomers: Customer[] = [];
  // companyId: string = "";
  orderMappings: OrderMapping[] = [];
 
  currentUser = {
    name: '',
    role: ''

  };
  inventoryData: {
    labels: string[];
    datasets: {
      label: string;
      data: number[];
      backgroundColor: string[];
      borderColor: string[];
      borderWidth: number;
    }[];
  } = {
    labels: [],
    datasets: [],
  };
  SalesData: {
    labels: string[];
    datasets: {
      label: string;
      data: number[];
      backgroundColor: string;
      borderColor: string;
      borderWidth: number;
    }[];
  } = {
    labels: [],
    datasets: [],
  };
  

  // noOfProducts:[]=[];
  stats = [
    { title: 'Total Orders', value: '0'},
    { title: 'Total Revenue', value: '₹0'},
    { title: 'Products', value: '0' },
    { title: 'Low Stock Items', value: '0' }
  ];

  recentOrders = [];
  lowStockProducts:any[]= [];
  chart: any;
  inventoryChart: any;
  companyId: any;

  constructor(private http: HttpClient,private router:Router,private toastr:ToastrService) {}

  ngOnInit(): void {
    this.fetchTotalOrders();
    this.fetchCustomers();
    this.fetchOrderMapping()
    this.fetchTotalProducts();
    this.fetchLowStockProducts(); 
    this.fetchProducts();
    this.fetchSalesData(); 
    this.fetchTotalRevenue();
  }
  
  fetchTotalOrders(): void {
    this.http.get<{ Name: string; email: string; Role: string; Orders: string }>('http://localhost:8080/api/orders/noOfOrders', {withCredentials: true})
      .subscribe(
        (response: any) => {
          console.log(response);        
          this.currentUser.name = response.Name;
          this.currentUser.role = response.Role;
          this.stats[0].value = response.Orders;
        },
        (error: any) => {
          console.error('Error fetching total orders:', error);
        }
      );
  }
  
  // H:Map<String,String>;
  fetchTotalProducts(): void {
    this.http.get<{ Products: string }>('http://localhost:8080/api/products/noOfProducts', { withCredentials: true })
      .subscribe(
        (response: any) => {
          console.log(response);
  
          this.stats[2].value = response.Products;
        },
        (error: any) => {
        
          console.error('Error fetching total products:', error);
        }
      );
  }
  
  fetchLowStockProducts(): void {
    this.http.get(`http://localhost:8080/api/products/lowStockProducts`,{withCredentials:true}).subscribe(
      (response: any) => {
        console.log(response);
        this.lowStockProducts=response;
        console.log(this.lowStockProducts)
        if(this.lowStockProducts.length>0){
          this.toastr.info('Check the Low Stock Products')
        }
        // this.companyId=response[0].companyId;
        //   console.log(response.companyId)
        //   this.fetchOrderMapping();
        // if(this.lowStockProducts.length<5){
        //   alert("Stock is low")
        // }
        this.stats[3].value=String(this.lowStockProducts.length)
       
       
      },
      (error: any) => {
        this.toastr.info("No Products Found")
        // alert("There are no Products Found")
      }
    );
    }
  
  route(routing:string){
    this.router.navigate([routing])
  }
  fetchProducts(): void {
    this.http
      .get<any[]>('http://localhost:8080/api/products/getProducts', {
       
        withCredentials: true,
      })
      .subscribe(
        (response: any[]) => {
          console.log('Fetched Products:', response);

          
          const productNames = response.map((product) => product.name);
          const productQuantities = response.map((product) => product.quantity);

          this.inventoryData = {
            labels: productNames,
            datasets: [
              {
                label: 'Inventory Levels',
                data: productQuantities,
                backgroundColor: this.generateColors(productNames.length, 0.2),
                borderColor: this.generateColors(productNames.length, 1),
                borderWidth: 1,
              },
            ],
          };

          
          if (this.inventoryChart) {
            this.inventoryChart.data = this.inventoryData;
            this.inventoryChart.update();
          } else {
            this.renderInventoryChart();
          }
        },
        (error: any) => {
          // alert("There are no Products Found")
          console.error('Error fetching Products:', error);

        }
      );
  }

  generateColors(count: number, alpha: number): string[] {
    return Array.from({ length: count }, () =>
      `rgba(${Math.floor(Math.random() * 255)}, 
             ${Math.floor(Math.random() * 255)}, 
             ${Math.floor(Math.random() * 255)}, ${alpha})`
    );
  }

 

  renderInventoryChart() {
    if (this.inventoryChart) {
      this.inventoryChart.destroy(); 
    }

    this.inventoryChart = new Chart('inventoryChart', {
      type: 'bar',
      data: this.inventoryData,
      options: {
        responsive: true,
        plugins: {
          legend: {
            position: 'top',
          },
          tooltip: {
            enabled: true,
          },
        },
      },
    });
  }
  renderSalesChart() {
    if (this.salesChart) {
      this.salesChart.destroy(); 
    }

    this.salesChart = new Chart('salesChart', {
      type: 'line',
      data: this.SalesData,
      options: {
        responsive: true,
        plugins: {
          legend: {
            position: 'top',
          },
          tooltip: {
            enabled: true,
          },
        },
      },
    });
  }
  fetchSalesData(): void {
    this.http.get<any>('http://localhost:8080/api/orders/sales/data', { withCredentials: true })
      .subscribe(
        (data: any) => {
          console.log(data);
  
          const labels = ['Monday', 'Tuesday', 'Wednesday', 'Thursday', 'Friday', 'Saturday', 'Sunday'];
  
          const orderCounts = [
            data[0],
            data[1],
            data[2],
            data[3],
            data[4],
            data[5],
            data[6],
          ];
  
          this.SalesData = {
            labels: labels,
            datasets: [
              {
                label: 'No of Sales',
                data: orderCounts,
                backgroundColor: '#4CAF50',
                borderColor: '#388E3C',
                borderWidth: 1,
              },
            ],
          };
  
          if (this.salesChart) {
            this.salesChart.data = this.SalesData;
            this.salesChart.update();
          } else {
            this.renderSalesChart();
          }
        },
        (error: any) => {
          console.error('Error fetching sales data', error);
        }
      );
  }
  
  


  fetchTotalRevenue() {
    this.http.get<string>('http://localhost:8080/api/orders/getRevenue', {
      withCredentials: true
    }).subscribe(
      (response:string) => {
        // this.stats[1].value="₹ "+response;
        const revenue = Number(response); 
      const formattedRevenue = new Intl.NumberFormat('en-IN', {
        style: 'currency',
        currency: 'INR',
        maximumFractionDigits: 2
      }).format(revenue); 

      this.stats[1].value = formattedRevenue; 
      },
      (error:any) => {
        // alert("Total Revenue not Found")
        console.error('Error fetching customers:', error)}
    );
  }



  fetchCustomers() {
    return new Promise<void>((resolve) => {
      this.http.get<Customer[]>('http://localhost:8080/api/customers/get', {
        withCredentials: true
      }).subscribe({
        next: (response:any) => {
          console.log('Fetched Customers:', response);
          this.existingCustomers = response;
          // if (response.length > 0) {
          //   // this.companyId = response[0].companyId.toString();
          // }
          // resolve();
        },
        error: (error:any) => {
          console.error('Error fetching Customers:', error);
          resolve();
        }
      });
    });
  }

  fetchOrderMapping() {
    this.http.get<OrderMapping[]>('http://localhost:8080/api/orders/ordermapping/get', {
      withCredentials: true
    }).subscribe(
      (response: any) => {
        console.log('Fetched order mappings:', response);
        
        this.orderMappings = response.map((order: any) => {
          const customer = this.existingCustomers.find(c => c.id === order.customerId);
          
          let customerName = 'Unknown Customer';  
          if (customer) {
            customerName = `${customer.firstName} ${customer.lastName}`;
          }
  
          return {
            ...order,  
            customerName: customerName
          };
        });
      
        this.orderMappings.sort((a, b) => 
          new Date(b.date).getTime() - new Date(a.date).getTime()
        );
        
        console.log('Processed order mappings:', this.orderMappings);
      },
      (error: any) => {
        console.error('Error fetching order mappings:', error);
      }
    );
  }
  
  }




