import { Component, OnInit } from '@angular/core';
import { Rating, RatingService } from '../../services/rating.service';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-ratings',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './ratings.component.html',
  styleUrls: ['./ratings.component.css']
})
export class RatingsComponent implements OnInit {
  ratings: Rating[] = []; // Stockage des avis
  negativeRatings: Rating[] = []; // Stockage des avis négatifs

  constructor(private ratingService: RatingService) {}

  ngOnInit(): void {
    this.loadNegativeRatings();
  }

  // Charger les avis négatifs depuis le backend
  loadNegativeRatings(): void {
    this.ratingService.getNegativeRatings().subscribe({
      next: (data) => this.negativeRatings = data, // Affecte les données récupérées
      error: (err) => console.error('Erreur lors du chargement des avis négatifs :', err)
    });
  }
}
