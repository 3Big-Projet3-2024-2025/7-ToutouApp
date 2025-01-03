import { Component, OnInit } from '@angular/core';
import { Rating, RatingService } from '../../services/rating.service';
import { CommonModule } from '@angular/common';
import { FooterComponent } from '../footer/footer.component';
import { HeaderComponent } from '../header/header.component';

@Component({
  selector: 'app-ratings',
  standalone: true,
  imports: [CommonModule, FooterComponent, HeaderComponent],
  templateUrl: './ratings.component.html',
  styleUrls: ['./ratings.component.css']
})
export class RatingsComponent implements OnInit {
  negativeRatings: Rating[] = []; // List of negative reviews
  sortOrder: 'asc' | 'desc' = 'asc'; // Sorting order
  selectedComment: string | null = null; // Selected comment for the modal

  constructor(private ratingService: RatingService) {}

  ngOnInit(): void {
    this.loadNegativeRatings();
  }

  // Load negative reviews from the backend
  loadNegativeRatings(): void {
    this.ratingService.getNegativeRatings().subscribe({
      next: (data) => {
        this.negativeRatings = data;
      },
      error: (err) => console.error('Error loading negative reviews:', err)
    });
  }

  // Toggle between ascending and descending order
  toggleSortOrder(): void {
    this.sortOrder = this.sortOrder === 'asc' ? 'desc' : 'asc';

    this.negativeRatings.sort((a, b) => {
      const dateA = new Date(a.requestDate).getTime();
      const dateB = new Date(b.requestDate).getTime();

      return this.sortOrder === 'asc' ? dateA - dateB : dateB - dateA;
    });
  }

  // Delete a review
  deleteRating(rating: Rating): void {
    if (confirm(`Are you sure you want to delete this review?`)) {
      this.ratingService.deleteRating(rating.id).subscribe({
        next: () => {
          this.negativeRatings = this.negativeRatings.filter(r => r.id !== rating.id);
          alert('Review successfully deleted!');
        },
        error: (err) => {
          console.error('Error deleting the review:', err);
          alert('Failed to delete the review. Please try again.');
        }
      });
    }
  }

  // Display a full comment in the modal
  showFullComment(comment: string): void {
    this.selectedComment = comment;
  }

  // Close the modal
  closeModal(): void {
    this.selectedComment = null;
  }
}
