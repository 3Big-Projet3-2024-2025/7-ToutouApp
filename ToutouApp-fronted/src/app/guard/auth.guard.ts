import { Injectable } from '@angular/core';
import { CanActivate, Router } from '@angular/router';
import { AuthService } from '../services/auth.service';

@Injectable({
  providedIn: 'root',
})
export class AuthGuard implements CanActivate {
  constructor(private authService: AuthService, private router: Router) {}

  async canActivate(): Promise<boolean> {
    try {
      const isLoggedIn = await this.authService.isLoggedIn();
      if (!isLoggedIn) {
        this.router.navigate(['/']);
        return false;
      }

      // Récupérer l'utilisateur pour vérifier les conditions supplémentaires
      const user = await this.authService.getUserEmailOnly();
      if (!user) {
        // Si l'utilisateur n'existe pas, rediriger
        this.router.navigate(['/']);
        return false;
      }

      // Vérifier les conditions : userFlag et isBlocked
      if (!user.active || user.blocked) {
        console.warn('Accès refusé : Utilisateur supprimé ou bloqué');
        this.authService.logout();
        return false;
      }

      // L'utilisateur est autorisé
      return true;
    } catch (error) {
      console.error('Erreur dans le guard AuthGuard:', error);
      this.router.navigate(['/']);
      return false;
    }
  }
}
