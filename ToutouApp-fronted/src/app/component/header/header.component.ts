import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { AuthService } from '../../services/auth.service'; // Import du AuthService
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-header',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.css'],
})
export class HeaderComponent implements OnInit {
  isLoggedIn = false;
  isAdmin = false;

  constructor(private router: Router, private authService: AuthService) {}

  navigateToHome() {
    console.log('Logo clicked!');
    this.router.navigateByUrl('/').then(() => {
      window.location.reload(); 
    });
  }

  async ngOnInit(): Promise<void> {
    try {
      this.isLoggedIn = await this.authService.isLoggedIn();

      if (this.isLoggedIn) {
        this.isAdmin = await this.authService.isAdmin(); // Vérifiez si l'utilisateur est admin
      }
    } catch (error) {
      console.error('Erreur lors de l\'initialisation du header:', error);
    }
  }


  login(): void {
    this.authService.login();
  }

  logout(): void {
    this.authService.logout();
  }

  seeRequests(){
    this.router.navigate(['/hub-requests'])
  }

  seeMap(){
    this.router.navigate(['/map'])              
  }

  seeProfile(){
    this.router.navigate(['/personal-profile'])
  }

  seeAdminPage(): void {
    this.router.navigate(['/admin/users']);
  }
  
}
