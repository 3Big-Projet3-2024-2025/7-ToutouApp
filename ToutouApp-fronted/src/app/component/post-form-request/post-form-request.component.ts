import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { RequestService } from '../../services/request.service';
import { UserIdService } from '../../services/user-id.service';
import { HeaderComponent } from '../header/header.component';
import { Router } from '@angular/router';

@Component({
  selector: 'app-post-form-request',
  standalone: true,
  imports: [FormsModule,CommonModule,HeaderComponent],
  templateUrl: './post-form-request.component.html',
  styleUrl: './post-form-request.component.css'
})
export class PostFormRequestComponent implements OnInit{

  constructor(
    public requestService: RequestService,
    private userIdService: UserIdService,
    private router: Router
    ){}

    minDate: string = '';



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
    this.setMinDate();
    
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
          window.alert('Request created successfully')
          this.resetForm(); // Réinitialise le formulaire après un succès
          this.router.navigate(['/hub-requests']);
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


  backToHub(){
    this.router.navigate(['/hub-requests']);
  }

  setMinDate(): void {
    const today = new Date();
    const year = today.getFullYear();
    const month = String(today.getMonth() + 1).padStart(2, '0');
    const day = String(today.getDate()).padStart(2, '0');
    this.minDate = `${year}-${month}-${day}`;
  }

}
