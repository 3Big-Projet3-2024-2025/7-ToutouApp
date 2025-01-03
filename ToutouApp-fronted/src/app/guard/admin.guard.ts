import { Injectable } from '@angular/core';
import { CanActivate, Router } from '@angular/router';
import { AuthService } from '../services/auth.service';

@Injectable({
  providedIn: 'root',
})
export class AdminGuard implements CanActivate {
  constructor(private authService: AuthService, private router: Router) {}

  async canActivate(): Promise<boolean> {
    try {
      const isLoggedIn = await this.authService.isLoggedIn();

      if (!isLoggedIn) {
        // If the user is not logged in, redirect to the home page
        this.router.navigate(['/']);
        return false;
      }

      const isAdmin = await this.authService.isAdmin();

      if (!isAdmin) {
        // If the user is not an admin, redirect to an error or home page
        this.router.navigate(['/']);
        return false;
      }

      // The user is logged in and is an admin
      return true;
    } catch (error) {
      console.error('Error while checking admin rights:', error);
      this.router.navigate(['/']);
      return false;
    }
  }
}
