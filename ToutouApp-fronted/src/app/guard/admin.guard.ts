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
        // Si l'utilisateur n'est pas connecté, redirection vers la page d'accueil
        this.router.navigate(['/']);
        return false;
      }

      const isAdmin = await this.authService.isAdmin();

      if (!isAdmin) {
        // Si l'utilisateur n'est pas admin, redirection vers une page d'erreur ou d'accueil
        this.router.navigate(['/']);
        return false;
      }

      // L'utilisateur est connecté et admin
      return true;
    } catch (error) {
      console.error('Erreur lors de la vérification des droits admin:', error);
      this.router.navigate(['/']);
      return false;
    }
  }
}
