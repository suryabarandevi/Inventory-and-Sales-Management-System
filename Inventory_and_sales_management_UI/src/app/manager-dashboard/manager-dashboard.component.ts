// manager-dashboard.component.ts
import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { ActivatedRoute } from '@angular/router';

import { HttpClient } from '@angular/common/http'; // Import HttpClient for backend logout
// import { AuthService } from '../services/auth.service'; // Assuming AuthService is responsible for session management
import {AuthGuard} from '../guards/auth.guard'
// import {CompanyService} from '../services/company.service';
import { Company } from '../Models/company.model';
// import { CurrentUserService } from '../services/currentUserService';

@Component({
  selector: 'app-manager-dashboard',
  templateUrl: 'manager-dashboard.component.html',
  styleUrls:['manager-dashboard.component.css']
})
export class ManagerDashboardComponent {

  currentView: string = 'manage-users';
  users: Array<{ name: string; email: string; role: string }> = [];
  // reportData: number[] = [65, 59, 80, 81, 56, 55, 40];
  // reportLabels: string[] = ['Jan', 'Feb', 'Mar', 'Apr', 'May', 'Jun', 'Jul'];
  company: Company | null = null;
  email:string='';
  companyexists:boolean=false;
  isloggedout:boolean=false;
  constructor(
   private router: Router,
    // private authService: AuthService,
    private http: HttpClient, 
    private route: ActivatedRoute,
    // private companyService: CompanyService,
    // private currentUserService: CurrentUserService
    // Inject HttpClient here for making HTTP requests
    // private authGuard: AuthGuard
  ) {}
  
  currentUserEmail: string = '';

  ngOnInit(): void {
    // this.fetchCurrentUserEmail();
   // this.getCompanyDetails();
    
  }

  
    // fetchCurrentUserEmail(): void {
    //   this.currentUserService.getCurrentUserEmail().subscribe(
    //     (email:any) => {
    //       this.currentUserEmail = email;
    //       console.log(email)
    //     },
    //     (error:any) => {
    //       console.error('Error fetching user email:', error);
    //     }
    //   );
    // }
  

  fetchUsers() {
    
  }

  getCompanyDetails(){

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
  

  redirectToCompanyForm(): void {
    // Navigate to the company form for adding company details
    this.router.navigate([`/company-form`]);
    console.log('Redirecting to Company Form...');
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


}
