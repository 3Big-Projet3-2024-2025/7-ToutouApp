import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { AuthService } from '../../services/auth.service'; // Import du AuthService

@Component({
  selector: 'app-header',
  standalone: true,
  imports: [],
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.css'],
})
export class HeaderComponent {
  constructor(private router: Router, private authService: AuthService) {}

  navigateToHome() {
    console.log('Logo clicked!');
    this.router.navigateByUrl('/').then(() => {
      window.location.reload(); // Force le rechargement de la page si on est déjà dessus
    });
  }

  // Méthode pour déconnecter l'utilisateur
  logout() {
    console.log('Déconnexion en cours...');
    this.authService.logout();
  }
}
