import { Component, OnInit } from '@angular/core';
import { RatingService } from '../../services/rating.service';
import { ActivatedRoute, Router } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { HeaderComponent } from '../header/header.component';
import { NgxPaginationModule } from 'ngx-pagination';

@Component({
  selector: 'app-helper-profile',
  standalone: true,
  imports: [FormsModule, CommonModule, HeaderComponent, NgxPaginationModule],
  templateUrl: './helper-profile.component.html',
  styleUrl: './helper-profile.component.css'
})
export class HelperProfileComponent implements OnInit{

  constructor(private ratingService: RatingService, private router: ActivatedRoute, ){}

  helperRatings: any[] = [];
  page: number = 1;



  ngOnInit(): void {

    const helperId = this.router.snapshot.paramMap.get('helperId');

    if (helperId) {
      
      const numericHelperId = Number(helperId); //convert helperId(string) into a number type

      if (!isNaN(numericHelperId)) {
        this.loadHelperRatings(numericHelperId);
      } else {
        console.error('Invalid HelperId: not a number');
      }
    }
  }



  loadHelperRatings(helperId: number){

    this.ratingService.getUserRating(helperId).subscribe(
      (data) => {
        this.helperRatings = data; 
      },
      (error) => {
        console.error('Error loading helper ratings', error);
      }
    );
  }

  get totalRatings(): number {
    return this.helperRatings.length;
  }


  get averageRating(): number {
    if (this.helperRatings.length === 0) {
      return 0;
    }
    const total = this.helperRatings.reduce((sum, rating) => sum + rating.ratingValue, 0);
    return total / this.helperRatings.length;
  }


}
