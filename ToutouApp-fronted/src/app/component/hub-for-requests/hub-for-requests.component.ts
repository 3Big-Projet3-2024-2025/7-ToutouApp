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
      (data: any) => {
        this.requests = data.filter((request: any) => request.state === false);
        this.sortRequestsByDate();

        this.deleteExpiredRequestsWithoutHelper();
      },
      (error) => {
        console.error('Error while retrieving requests :', error);
      }
     ); 
   })
   .catch(error => {
     console.error("Error while retrieving User id :", error);
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


  evaluateHelper(helperId: any, requestId: any){

    this.router.navigate(['/comment-helper', helperId, requestId]);

  }


  isRequestDatePassed(requestDate: string, requestEndTime: string): boolean {
    const currentDateTime = new Date(); 
    const requestDateTime = new Date(`${requestDate}T${requestEndTime}`); 
    
    return currentDateTime > requestDateTime;
  }


  deleteExpiredRequestsWithoutHelper() {
    const expiredRequests = this.requests.filter(request => 
      request.helper == null && 
      this.isRequestDatePassed(request.requestDate, request.endTime)
    );
  
    expiredRequests.forEach(request => {
      this.requestService.deleteRequest(request.requestId).subscribe({
        next: (response) => {
          console.log(`Expired request ${request.requestId} deleted successfully!`, response);
        },
        error: (error) => {
          console.error(`Error deleting expired request ${request.requestId}:`, error);
        }
      });
    });
    
  }


  goToChat(requestId: number, helperName: string, userName: string): void {
    if (!requestId) {
      console.error('Request ID is missing!');
      return;
    }

    this.router.navigate(['/chat', requestId], { state: { helperName, userName } });
  }
  
  
}
