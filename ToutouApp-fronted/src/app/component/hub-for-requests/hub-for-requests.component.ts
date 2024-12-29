import { Component, OnInit } from '@angular/core';
import { RequestService } from '../../services/request.service';
import { UserIdService } from '../../services/user-id.service';
import { FormsModule } from '@angular/forms';
import { CommonModule, DatePipe } from '@angular/common';
import { ActivatedRoute, Route, Router, RouterModule } from '@angular/router';
import { HeaderComponent } from '../header/header.component';
import { NgxPaginationModule } from 'ngx-pagination';

@Component({
  selector: 'app-hub-for-requests',
  standalone: true,
  imports: [FormsModule,CommonModule,HeaderComponent, DatePipe, NgxPaginationModule],
  templateUrl: './hub-for-requests.component.html',
  styleUrl: './hub-for-requests.component.css'
})
export class HubForRequestsComponent implements OnInit{

  constructor(private requestService: RequestService, private userIdService: UserIdService, private router: Router){}


  requests: any[] = [];
  selectedRequest: any = null;
  page: number = 1;


  ngOnInit(): void {
    this.loadUserRequest();
    
  }



  loadUserRequest(){
   this.userIdService.getUserId()
   .then(userIdString => {
     const userId = parseInt(userIdString, 10); 

     this.requestService.getUserRequest(userId).subscribe(
      (data) => {
        this.requests = data;
        this.sortRequestsByDate();
      },
      (error) => {
        console.error('Erreur lors de la récupération des requêtes :', error);
      }
     ); 
   })
   .catch(error => {
     console.error("Erreur lors de la récupération de l'ID utilisateur :", error);
   });
  }


  editRequest(requestId: number) {
    this.router.navigate(['/edit-request', requestId]);
  }

  deleteRequest(requestId: number, requestHelper: any){

    const confirmation = window.confirm('Are you sure you want to delete this request? This action cannot be undone.');

    if (confirmation && requestHelper == null) {
      
      this.requestService.deleteRequest(requestId).subscribe({
        next: (response) => {
          console.log('Request deleted successfully!', response);
          window.alert('Request deleted successfully!');
          
          this.loadUserRequest(); 
        },
        error: (error) => {
          console.error('Error deleting request:', error);
          window.alert('Error deleting the request. Please try again.');
        }
      });
    } else {

      console.log('Request deletion cancelled');
      if(requestHelper){
        window.alert('You cannot delete this request because someone has already agreed to help you.');
      }
      
    }

  }


  createRequest(){
    this.router.navigate(['/post-request']);
  }


  sortRequestsByDate() {

    this.requests.sort((a, b) => {
      const dateA = new Date(a.requestDate);
      const dateB = new Date(b.requestDate);

      return dateA.getTime() - dateB.getTime();
    });
  }


  seeHelperProfile(helperId: any){

    this.router.navigate(['/helper-profile', helperId]);


  }



}
