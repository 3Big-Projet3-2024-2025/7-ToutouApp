import { Component, OnInit } from '@angular/core';
import { RatingService } from '../../services/rating.service';
import { RequestService } from '../../services/request.service';
import { ActivatedRoute, Router } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { HeaderComponent } from '../header/header.component';

@Component({
  selector: 'app-comment-helper',
  standalone: true,
  imports: [FormsModule, CommonModule, HeaderComponent],
  templateUrl: './comment-helper.component.html',
  styleUrl: './comment-helper.component.css'
})
export class CommentHelperComponent implements OnInit{

  constructor(private ratingService: RatingService,
    private requestService: RequestService,
    private route: ActivatedRoute,
    private router: Router
  ){}


  rating = {
    ratingId: null,
    ratingValue: '',
    comment: '',
    request: {
      requestId: null as number | null, 
    },
    consumer: {
      id: null as number | null,
    },
  };

  request: any = {};




  ngOnInit(): void {

    const helperId = this.route.snapshot.paramMap.get('helperId');
    const requestId = this.route.snapshot.paramMap.get('requestId');

    if (helperId) {
      this.rating.consumer.id = Number(helperId); 
    }
    if (requestId) {
      this.rating.request.requestId = Number(requestId); 
    }

    if(requestId){
      this.loadRequest(requestId);
    }
    
  }

  loadRequest(requestId: string){
    this.requestService.getRequestById(requestId).subscribe((data) => {
      this.request = data;
    });
  }


  onSubmit(): void {

    this.ratingService.addRating(this.rating).subscribe({
      next: (response) => {
        console.log('comment post with succes', response);
        alert('Evaluation post succefully');
        
        // This part allows changing the request state to true to indicate that the helper has already been evaluated for this request.
        this.request.state = true; 
        this.requestService.modifyRequest(this.request.requestId, this.request).subscribe(
          (response) => {
            console.log('Request state update succesfully', response);
            
          },
          (error) => {
            console.error('Error to update request state', error);
          }
        );
        
        this.router.navigate(['hub-requests']);

      },
      error: (err) => {
        console.error('Error while submitting the review.', err);
        alert('An error ocure try again');
      }
    });
  }

  backToHub(){
    this.router.navigate(['/hub-requests']);
  }




}
