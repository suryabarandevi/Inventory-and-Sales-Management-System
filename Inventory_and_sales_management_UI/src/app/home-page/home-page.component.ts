import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { firstValueFrom } from 'rxjs';

import { HttpClient } from '@angular/common/http'; 
@Component({
  selector: 'app-home-page',
  templateUrl: './home-page.component.html',
  styleUrls: ['./home-page.component.css'],
})
export class HomePageComponent {
 
  constructor(private router: Router,private http: HttpClient) {}
  isauth:boolean=false;
  login() {
    
    window.location.href = 'http://localhost:8080/oauth2/authorization/google';
    // window.location.href ='http://localhost:8080/oauth/google';
  }

  ngOnInit():void{
    // if(this.isauth){
    // this.checkSession();
    // }
    console.log(this.router.url)
    this.checkSession()
  }

  
  checkSession(): void {
    this.http.get<{ isAuthenticated: boolean; email: string | null; role: string }>(
      'http://localhost:8080/api/validate-session', { withCredentials: true }
    ).subscribe(
      (response: { isAuthenticated: boolean; role: string }) => {
        console.log("check session is called");
        console.log(response);
        const role = response.role.toLowerCase(); 
        if (response.isAuthenticated) {
          // this.isauth=response.isAuthenticated
         
          switch (role) {
            case 'admin':
              this.router.navigate(['/admin-dashboard']);
              break;
            case 'manager':
              this.router.navigate(['/manager-dashboard']);
              break;
            // case 'staff':
            //   this.router.navigate(['/staff-dashboard']);
            //   break;
            default:
              console.warn('Unknown role:', role);
              this.router.navigate(['']);
          }
        } else {
          
        
          this.router.navigate([""]);
          
        }
      },
      (error: any) => {
        console.error('Error checking session:', error);
      
        if (error.status === 0) {
        
          console.error("CORS or network error");
          this.router.navigate(['']);
        } else {
          this.router.navigate(['']);
        }
      }
    );
  }

 
}
