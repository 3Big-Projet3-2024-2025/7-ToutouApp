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

      this.userdb = await this.authService.getUserEmailOnly();

      console.log('Détails de l\'utilisateur récupéré depuis l\'API:', this.userdb);

      // Load the full name after login
      this.userFullName = await this.authService.getUserFullName();

      console.log('User full name loaded:', this.userFullName);
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