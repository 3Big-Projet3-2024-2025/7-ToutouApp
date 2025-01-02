import { Component } from '@angular/core';
import { RequestService } from '../../services/request.service';
import { UserIdService } from '../../services/user-id.service';
import { CommonModule } from '@angular/common';
import { HeaderComponent } from "../header/header.component";
import { FooterComponent } from "../footer/footer.component";

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
    private userIdService: UserIdService
  ) {}

  ngOnInit(): void {
    this.loadAcceptedRequests();
  }

  loadAcceptedRequests(): void {
    this.userIdService.getUserId().then((userId) => {
      console.log('User ID:', userId);  // Affichez l'ID utilisateur ici
  
      this.requestService.getAllRequests().subscribe(
        (requests) => {
          console.log('All requests:', requests);  // Affichez toutes les requêtes
  
          // Utilisez Number() pour vous assurer que les valeurs comparées sont bien des nombres
          this.acceptedRequests = requests.filter((request: any) => {
            console.log('Request helper:', request.helper);  // Affichez l'objet helper de chaque requête
            return Number(request.helper?.id) === Number(userId);
          });
  
          console.log('Accepted requests:', this.acceptedRequests);  // Vérifiez les requêtes filtrées
  
          if (this.acceptedRequests.length === 0) {
            this.error = 'You have not accepted any requests';
          }
        },
        (err) => {
          this.error = 'Erreur lors de la récupération des requêtes.';
          console.error(err);
        }
      );
    }).catch((err) => {
      this.error = 'Erreur lors de la récupération de l\'ID utilisateur.';
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
        // Suppression réussie, mettez à jour la liste des requêtes acceptées
        this.acceptedRequests = this.acceptedRequests.filter(
          (request) => request.requestId !== requestId
        );
        console.log('Request deleted successfully');
      },
      (err) => {
        this.error = 'Erreur lors de la suppression de la requête.';
        console.error(err);
      }
    );
  }
  
}
