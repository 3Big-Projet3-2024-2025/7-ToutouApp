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

  constructor(private router: Router, private authService: AuthService) {}

  navigateToHome() {
    console.log('Logo clicked!');
    this.router.navigateByUrl('/').then(() => {
      window.location.reload(); 
    });
  }
  async ngOnInit(): Promise<void> {
    this.isLoggedIn = await this.authService.isLoggedIn();
  }

  login(): void {
    this.authService.login();
  }

  logout(): void {
    this.authService.logout();
  }

  
}
