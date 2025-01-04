import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { AuthService } from '../../services/auth.service';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-header',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.css'],
})
export class HeaderComponent implements OnInit {
  isLoggedIn = false; // Tracks if the user is logged in
  isAdmin = false; // Tracks if the logged-in user has admin privileges

  constructor(private router: Router, private authService: AuthService) {}

  // Navigates to the home page when the logo is clicked and refreshes the page
  navigateToHome() {
    console.log('Logo clicked!');
    this.router.navigateByUrl('/').then(() => {
      window.location.reload(); 
    });
  }

  // Runs on component initialization to check login status and admin privileges
  async ngOnInit(): Promise<void> {
    try {
      this.isLoggedIn = await this.authService.isLoggedIn();

      if (this.isLoggedIn) {
        this.isAdmin = await this.authService.isAdmin(); // Checks if the user is an admin
      }
    } catch (error) {
      console.error('Error during header initialization:', error);
    }
  }

  // Initiates the login process
  login(): void {
    this.authService.login();
  }

  // Logs the user out and clears their session
  logout(): void {
    this.authService.logout();
  }

  // Navigates to the Requests Hub page
  seeRequests() {
    this.router.navigate(['/hub-requests']);
  }

  // Navigates to the map page
  seeMap() {
    this.router.navigate(['/map']);
  }

  // Navigates to the user's personal profile page
  seeProfile() {
    this.router.navigate(['/personal-profile']);
  }

  // Navigates to the admin page for managing users (visible only to admin users)
  seeAdminPage(): void {
    this.router.navigate(['/admin/users']);
  }
  
  // Navigates to the "My Services" page
  seeMyServices() {
    this.router.navigate(['/my-services']);
  }

  showAdminMenu = false; // Tracks the visibility of the admin dropdown menu


  // Toggles the visibility of the admin menu
  toggleAdminMenu(show: boolean): void {
    this.showAdminMenu = show;
  }

  // Navigates to a specified path and hides the admin menu after navigation
  navigateTo(path: string): void {
    this.router.navigate([`/${path}`]);
    this.showAdminMenu = false; // Hide the menu after navigation
  }
}
