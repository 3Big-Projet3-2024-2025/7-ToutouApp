import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { RequestService } from '../../services/request.service';
import { UserIdService } from '../../services/user-id.service';

@Component({
  selector: 'app-post-form-request',
  standalone: true,
  imports: [FormsModule,CommonModule],
  templateUrl: './post-form-request.component.html',
  styleUrl: './post-form-request.component.css'
})
export class PostFormRequestComponent implements OnInit{

  constructor(public requestService: RequestService, private userIdService: UserIdService){}

  request = {
    requestId: null,
    requestDate: '',
    startTime: '',
    endTime: '',
    dogName: '',
    photo: '',
    comment: '',
    owner: null, 
    dogCategory: null 
  };


  categories = [
    { id: 1, name: 'Big' },
    { id: 2, name: 'Medium' },
    { id: 3, name: 'Small' }
  ];

  ngOnInit(): void {
    
  }


  onSubmit(): void {
    this.userIdService.getUserId().then((userId) => {
      const transformedRequest = {
        ...this.request,
        owner: { id: userId },
        dogCategory: { dogCategoryId: this.request.dogCategory }
      };
  
      console.log('Request to send:', transformedRequest);
  
      this.requestService.addRequest(transformedRequest).subscribe({
        next: (response) => {
          console.log('Request sent successfully!', response);
          this.resetForm(); // Réinitialise le formulaire après un succès
        },
        error: (error) => {
          console.error('Error sending request:', error);
        }
      });
    }).catch((error) => {
      console.error('Error fetching user ID:', error);
    });
  }
  

  resetForm(): void {
    this.request = {
      requestId: null,
      requestDate: '',
      startTime: '',
      endTime: '',
      dogName: '',
      photo: '',
      comment: '',
      owner: null,
      dogCategory: null
    };
  }

}
