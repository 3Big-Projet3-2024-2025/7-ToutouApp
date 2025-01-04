import { Component, AfterViewInit, OnInit } from '@angular/core';
import { FooterComponent } from '../footer/footer.component';
import { HeaderComponent } from '../header/header.component';
import { AuthService } from '../../services/auth.service';
import { User } from '../../user.model';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-home-page',
  standalone: true,
  imports: [FooterComponent, HeaderComponent,CommonModule],
  templateUrl: './home-page.component.html',
  styleUrls: ['./home-page.component.css'],
})
export class HomePageComponent implements AfterViewInit, OnInit {
  userFullName: string = ''; // Variable to store the full name
  userdb: User | null = null;

  constructor(private authService: AuthService) {}

  async ngOnInit(): Promise<void> {
    try {
      // Create the user in the database if it does not exist
      await this.authService.createUser();
  
      // Attempt to fetch user details from the database
      this.userdb = await this.authService.getUserEmailOnly();
  
      if (!this.userdb) {
        // If user is not in the database, fallback to Keycloak profile
        console.log('User not found in the database. Using Keycloak profile details.');
        const fullName = await this.authService.getUserFullName();
        const [firstName, lastName] = fullName.split(' ');
  
        // Create a temporary user object for display
        this.userdb = {
          firstName: firstName || '',
          lastName: lastName || '',
          email: '', // Placeholder since it's not fetched
        } as unknown as User;
      }
  
      console.log('User details:', this.userdb);
    } catch (error) {
      console.error('Error during user creation or loading:', error);
    }
  }
  
  ngAfterViewInit(): void {
    // Select all elements to animate
    const elements = document.querySelectorAll('.animate-on-scroll');

    // Configure the observer
    const observer = new IntersectionObserver(
      (entries) => {
        entries.forEach((entry) => {
          if (entry.isIntersecting) {
            // Add a visible class if the element is visible
            entry.target.classList.add('visible');
            observer.unobserve(entry.target); // Stop observing after the animation
          }
        });
      },
      {
        threshold: 0.4, // Trigger the animation when 40% of the element is visible
      }
    );

    // Observe each element
    elements.forEach((el) => observer.observe(el));
  }

  
}