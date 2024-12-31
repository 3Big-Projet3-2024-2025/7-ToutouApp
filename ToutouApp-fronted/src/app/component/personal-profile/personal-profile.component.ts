import { Component, OnInit } from '@angular/core';
import { HeaderComponent } from "../header/header.component";
import { FooterComponent } from "../footer/footer.component";
import { UserIdService } from '../../services/user-id.service';
import { AuthService } from '../../services/auth.service';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';
import { KeycloakService } from 'keycloak-angular';
import { RatingService } from '../../services/rating.service';
import { RequestService } from '../../services/request.service';

@Component({
  selector: 'app-personal-profile',
  standalone: true,
  imports: [HeaderComponent, FooterComponent, FormsModule, CommonModule],
  templateUrl: './personal-profile.component.html',
  styleUrls: ['./personal-profile.component.css']
})
export class PersonalProfileComponent implements OnInit {
  user: any = {};
  email: string = '';
  reviews: any[] = [];
  showMessage: boolean = false;
  hasRequests: boolean = false;

  constructor(
    private authService: AuthService,
    private userService: UserIdService,
    private router: Router,
    private keycloakService: KeycloakService,
    private ratingService: RatingService,
    private requestService: RequestService 
  ) { }

  async ngOnInit(): Promise<void> {
    try {
      this.email = await this.authService.getUserEmail();
  
      this.userService.getUserByEmail(this.email).subscribe({
        next: (userData) => {
          this.user = userData;
          console.log('Loaded user:', this.user);
  
          this.ratingService.getUserRating(this.user.id).subscribe({
            next: (data) => {
              this.reviews = data.map((review: { request: { owner: { firstName: any; lastName: any; }; }; }) => ({
                ...review,
                reviewerName: `${review.request.owner.firstName} ${review.request.owner.lastName}`,
              }));
              console.log('Loaded Ratings:', this.reviews);
            },
            error: (err) => console.error('Error loading reviews', err)
          });

          this.requestService.getUserRequest(this.user.id).subscribe({
            next: (data) => {
              this.hasRequests = data.length > 0;
            },
            error: (err) => console.error('Error loading user queries', err)
          });
        },
        error: (err) => console.error('Error loading user data', err)
      });
  
    } catch (error) {
      console.error('Error retrieving user email:', error);
    }
  }
  
  saveChanges(): void {
    if (!this.user.id) {
      console.error("User ID is missing");
      alert("User ID is missing");
      return;
    }
  
    this.userService.updateUser(this.user).subscribe({
      next: () => {
        this.showMessage = true;
        setTimeout(() => {
          this.showMessage = false;
          this.router.navigate(['/']);
        }, 2000);
      },
      error: (err) => {
        console.error('Error saving changes', err);
        alert('An error occurred while saving your information.');
      }
    });
  }
  
  deleteAccount(): void {
    if (this.hasRequests) {
      alert("You cannot delete your account because you have active requests.");
      return;
    }

    const confirmation = window.confirm("Are you sure you want to delete your account ? This action cannot be undone.");
    
    if (confirmation) {
      this.userService.deleteUser(this.user.id).subscribe({
        next: () => {
          this.keycloakService.logout(window.location.origin).then(() => {
            this.router.navigate(['/']);
          });
        },
        error: (err) => {
          console.error('Error deleting account', err);
          alert('An error occurred while deleting your account.');
        }
      });
    }
  }
}
