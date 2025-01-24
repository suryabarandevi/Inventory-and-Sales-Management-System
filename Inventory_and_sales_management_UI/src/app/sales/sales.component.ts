import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Component, ElementRef, OnInit, ViewChild } from '@angular/core';
import { Chart, ChartConfiguration } from 'chart.js/auto';

@Component({
  selector: 'app-sales',
  templateUrl: './sales.component.html',
  styleUrls: ['./sales.component.css'],
})
export class SalesComponent  {
  salesChart: any;
  inventoryChart: any;
  chartData: any[] = [];
  // labels: string[] = [];
  chart: any;

 
  // selectedFilter: string = 'day';
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
  
  // chart: Chart<"bar", any[], string>;

  constructor(private http: HttpClient) {}

  public barChartLegend = true;

  ngOnInit(): void {
    this.fetchProducts(); 
    this.fetchSalesData("2025");
    
  //  this.fetchOrderMapping();
    
  }
  // ngAfterViewInit(): void {
   
  //   const canvas = document.getElementById('salesChart') as HTMLCanvasElement;
  //   const ctx = canvas.getContext('2d');
  //   if (ctx) {
  //     this.salesChart = new Chart(ctx, this.getChartConfig([], [])); 
  //   }
  // }
  // getChartConfig(labels: string[], data: number[]): ChartConfiguration {
  //   return {
  //     type: 'line',
  //     data: {
  //       labels: labels,
  //       datasets: [
  //         {
  //           label: 'Revenue',
  //           data: data,
  //           backgroundColor: '#4CAF50',
  //           borderColor: '#388E3C',
  //           hoverBackgroundColor: '#66BB6A',
  //           hoverBorderColor: '#2E7D32',
  //           borderWidth: 1,
  //         }
  //       ],
  //     },
  //     options: {
  //       responsive: true,
  //       scales: {
  //         x: {
  //           beginAtZero: true,
  //         },
  //         y: {
  //           beginAtZero: true,
  //         },
  //       },
  //       plugins: {
  //         legend: {
  //           display: true,
  //           position: 'top',
  //         },
  //       },
  //     },
  //   };
  // }
  

  // updateChart(labels: string[], data: number[]): void {
  //   if (this.salesChart) {
  //     this.salesChart.data.labels = labels;
  //     this.salesChart.data.datasets[0].data = data;
  //     this.salesChart.update();
  //   }
  // }
  
  fetchOrderMapping() {
    this.http.get<any>(`http://localhost:8080/api/orders/ordermapping/get`, {
      withCredentials: true
    }).subscribe(
      (response:any) => {
        console.log(response)
        // this.filteredOrders = response;
       
      },
      (error:any) => console.error('Error fetching order mappings:', error)
    );
  }

  

  fetchProducts(): void {
    const headers = new HttpHeaders({
      'Content-Type': 'application/json',
    });

    this.http
      .get<any[]>('http://localhost:8080/api/products/getProducts', {
        headers: headers,
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
  fetchSalesData(year:string): void {
    this.http.get<any>(`http://localhost:8080/api/orders/sales/data/${year}`, { withCredentials: true }).subscribe({
    next: (data: any) => {
      // this.salesData = data;
      // console.log(this.salesData);
      console.log(data[1].revenue)
      // const currentDate = new Date();
      // const currentDay = currentDate.getDay()-1;
     
      const labels = ['Jan', 'Feb', 'Mar', 'Apr', 'May', 'Jun', 'Jul', 'Aug', 'Sep', 'Oct', 'Nov', 'Dec'];

  
      const orderCounts = [
        data[1].revenue,
        data[2].revenue,
        data[3].revenue,
        data[4].revenue,
        data[5].revenue,
        data[6].revenue,
        data[7].revenue,
        data[8].revenue,
        data[9].revenue,
        data[10].revenue,
        data[11].revenue,
        data[12].revenue
      
      ];
      this.SalesData = {
        labels: labels,
        datasets: [
          {
            label: 'Revenue',
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
      // this.updateChart(labels, orderCounts);
    },
    error: (err:any) => {

      console.error('Error fetching sales data', err);
    }
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
}
