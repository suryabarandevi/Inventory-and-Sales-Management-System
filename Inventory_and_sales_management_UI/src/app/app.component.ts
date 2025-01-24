import { Component } from '@angular/core';

import { Router } from '@angular/router';
import { ActivatedRoute } from '@angular/router';

import { HttpClient } from '@angular/common/http'; 

// import {CompanyService} from '../app/services/company.service';

// import { CurrentUserService } from '../app/services/currentUserService';
@Component({
  selector: 'app-root', 
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {
  title = 'inventory-management';

  showhomepage:boolean=false;
  constructor(
    private router: Router,
     // private authService: AuthService,
     private http: HttpClient, 
     private route: ActivatedRoute,
    //  private companyService: CompanyService,
    //  private currentUserService: CurrentUserService
    
     // private authGuard: AuthGuard
   ) {}
 
  redirectToCompanyForm(): void {
    
    this.router.navigate([`/company-form`]);
    console.log('Redirecting to Company Form...');
  }
  
  logout() {
    //this.isDropdownOpen = false;
    this.showhomepage=true;
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
