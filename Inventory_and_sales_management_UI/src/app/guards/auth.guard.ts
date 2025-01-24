import { HttpClient } from '@angular/common/http';
import { inject } from '@angular/core';
import { CanActivateFn ,Router} from '@angular/router';

export const AuthGuard: CanActivateFn = async (route, state) => {
  const http = inject(HttpClient);
  const router = inject(Router);
  interface RouteData {
    role?: string; 
  }
    const AUTH_STATUS_URL = 'http://localhost:8080/api/validate-session';
  
    try {
      const response = await http.get<any>(AUTH_STATUS_URL, { withCredentials: true }).toPromise();
  
      if (response.isAuthenticated) {
        const userRole = response.role;
        const requiredRole = (route.data as RouteData).role;  
  
        console.log(userRole)
        console.log(requiredRole)
  
    
        if (userRole.toUpperCase() === requiredRole) {
            console.log("equal");
            return true;  
        }
        else {
          console.log('Role mismatch. Redirecting...');
          await router.navigate(['']);  
          return false;  
        }
      } else {
        console.log('User not authenticated. Redirecting...');
        await router.navigate(['/']);  
        return false;  
      }
    } catch (error) {
      console.error('Error checking authentication status:', error);
      await router.navigate(['/']); 
      return false; 
    }
};

