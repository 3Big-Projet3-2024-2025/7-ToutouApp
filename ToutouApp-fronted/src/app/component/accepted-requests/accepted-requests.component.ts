import { Component } from '@angular/core';
import { RequestService } from '../../services/request.service';
import { UserIdService } from '../../services/user-id.service';
import { CommonModule } from '@angular/common';
import { HeaderComponent } from "../header/header.component";
import { FooterComponent } from "../footer/footer.component";
import { Router } from '@angular/router';

@Component({
  selector: 'app-accepted-requests',
  standalone: true,
  imports: [CommonModule, HeaderComponent, FooterComponent],
  templateUrl: './accepted-requests.component.html',
  styleUrl: './accepted-requests.component.css'
})
export class AcceptedRequestsComponent {
  acceptedRequests: any[] = [];
  error?: string;

  constructor(
    private requestService: RequestService,
    private userIdService: UserIdService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.loadAcceptedRequests();
  }

  loadAcceptedRequests(): void {
    this.userIdService.getUserId().then((userId) => {
      console.log('User ID:', userId);
  
      this.requestService.getAllRequests().subscribe(
        (requests) => {
          console.log('All requests:', requests);
  
          this.acceptedRequests = requests.filter((request: any) => {
            console.log('Request helper:', request.helper);
            return Number(request.helper?.id) === Number(userId);
          });
  
          console.log('Accepted requests:', this.acceptedRequests);
  
          if (this.acceptedRequests.length === 0) {
            this.error = 'You have not accepted any requests';
          }
        },
        (err) => {
          this.error = 'Error retrieving queries.';
          console.error(err);
        }
      );
    }).catch((err) => {
      this.error = 'Error retrieving user ID.';
      console.error(err);
    });
  }

  deleteRequest(requestId: number): void {
    if (!requestId) {
      console.error('Request ID is missing!');
      return;
    }

    this.requestService.deleteRequest(requestId).subscribe(
      () => {
        this.acceptedRequests = this.acceptedRequests.filter(
          (request) => request.requestId !== requestId
        );
        console.log('Request deleted successfully');
      },
      (err) => {
        this.error = 'Error deleting query.';
        console.error(err);
      }
    );
  }

  goToChat(requestId: number, helperName: string, userName: string): void {
    if (!requestId) {
      console.error('Request ID is missing!');
      return;
    }

    this.router.navigate(['/chat', requestId], { state: { helperName, userName } });
  }
}
