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
    minTime: string = '';



  request = {
    requestId: null,
    requestDate: '',
    startTime: '',
    endTime: '',
    dogName: '',
    photo: '',
    comment: '',
    owner: null, 
    dogCategory: null,
    state: false
  };


  categories = [
    { id: 1, name: 'Big' },
    { id: 2, name: 'Medium' },
    { id: 3, name: 'Small' }
  ];

  ngOnInit(): void {
    this.setMinDate();
    this.setMinTime();
    
  }


  onSubmit(): void {

    if (!this.isEndTimeValid()) {
      console.error('Invalid time range: End time must be later than start time.');
     
      return;
    }

    this.request.state = false;




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
          this.resetForm();
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
      dogCategory: null,
      state: false
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

  setMinTime(): void {
    const now = new Date();
    const hours = String(now.getHours()).padStart(2, '0');
    const minutes = String(now.getMinutes()).padStart(2, '0');
    this.minTime = `${hours}:${minutes}`;
  }

  onDateChange(): void {
    const selectedDate = new Date(this.request.requestDate);
    const today = new Date(this.minDate);

    
    if (selectedDate.toDateString() === today.toDateString()) {
      this.setMinTime();
    } else {
     
      this.minTime = '00:00';
    }
  }

  isEndTimeValid(): boolean {
    const startTime = this.request.startTime;
    const endTime = this.request.endTime;
  
    return !endTime || !startTime || endTime > startTime;
  }
  


  isValidUrl(url: string): boolean {
    try {
      const parsedUrl = new URL(url);
      return ['http:', 'https:'].includes(parsedUrl.protocol) && /\.(jpg|jpeg|png|gif|webp)$/i.test(parsedUrl.pathname);
    } catch (e) {
      return false;
    }
  }
  

}
