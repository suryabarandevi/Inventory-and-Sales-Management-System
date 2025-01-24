import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { ActivatedRoute } from '@angular/router';
import { HttpClient } from '@angular/common/http'; 
import { Company } from '../Models/company.model';


@Component({
  selector: 'app-admin-dashboard',
  templateUrl: './admin-dashboard.component.html',
  styleUrls: ['./admin-dashboard.component.css'],
})
export class AdminDashboardComponent implements OnInit {
  currentView: string = 'manage-users';
  // users: Array<{ name: string; email: string; role: string }> = [];
  
  company: Company | null = null;
  email:string='';
  companyexists:boolean=false;
  isloggedout:boolean=false;
  constructor(
   private router: Router,
    private http: HttpClient, 
    private route: ActivatedRoute,
  ) {}
  
  currentUserEmail: string = '';

  ngOnInit(): void {

    
  }
  toggleSidebar() {
  const sidebar = document.querySelector('.sidebar');
  const menuToggle = document.querySelector('.menu-toggle');
  sidebar?.classList.toggle('active');
  menuToggle?.classList.toggle('active');
}

logout() {
  //this.isDropdownOpen = false;
  this.isloggedout=true;
  fetch('http://localhost:8080/logout', {
    method: 'POST',
    credentials: 'include',
    headers: {
      'Content-Type': 'application/json'
    }
  })
  .then(response => {
    if (response.ok) {
      //this.userName = '';
      console.log("Logout successful");
      this.router.navigate(['/']);
    } else {
      console.error('Logout failed');
    }
  })
  .catch(error => {
    console.error('Error during logout:', error);
  });
}
  
   

  
  // fetchCompanyByEmail(email: string): void {
  //   this.companyService.getCompanyByEmail(email).subscribe({
     
  //     next: (company: Company) => {
  //       this.companyexists=true;
  //       this.company = company;
  //       console.log(this.email)
  //     },
  //     error: () => {
  //       console.error('No company found for the logged-in user');
  //       this.company = null; // Ensure this is null when no company exists
  //     },
  //   });
  // }
  

  // redirectToCompanyForm(): void {
  //   // Navigate to the company form for adding company details
  //   this.router.navigate([`/company-form`]);
  //   console.log('Redirecting to Company Form...');
  // }
  


  
  
}
