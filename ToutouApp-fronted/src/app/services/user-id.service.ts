import { Injectable } from '@angular/core';
import { KeycloakService } from 'keycloak-angular';
import { AuthService } from './auth.service';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class UserIdService {

  constructor(
    private keycloakService: KeycloakService,
    private authService: AuthService, 
    private http: HttpClient
  ) { }

  private apiUrl = 'http://localhost:8080/user';

  // Méthode pour récupérer l'ID utilisateur depuis l'API
  private getUserIdFromApi(email: string): Observable<string> {
    return this.http.get<string>(`${this.apiUrl}/id`, { params: { email } });
  }


  private async getUserEmail(): Promise<string> {
    const profile = await this.keycloakService.loadUserProfile();
    return profile.email || '';  // Récupérer l'email de l'utilisateur (assurez-vous qu'il est défini dans Keycloak)
  }


  async getUserId(): Promise<string> {

    const users = [
      {
        id: 1,
        mail: 'la227758@student.helha.be',
        lastName: 'Amico',
        firstName: 'Matteo',
        country: 'Belgium',
        city: 'Charleroi',
        street: 'Rue de la Montagne 41',
        postalCode: '6000',
        active: true,
        blocked: false,
        role: { roleId: 2, name: 'USER' },
      },
      {
        id: 3,
        mail: 'samu@gmail.com',
        password: 'samu',
        lastName: 'Samu',
        firstName: 'Paul',
        country: 'Belgium',
        city: 'Charleroi',
        street: 'Rue de la tour 59',
        postalCode: '6000',
        active: true,
        blocked: false,
        role: { id: 2, name: 'USER' },
      },
      {
        id: 2,
        mail: 'user3@example.com',
        password: 'password3',
        lastName: 'Brown',
        firstName: 'Charlie',
        country: 'UK',
        city: 'London',
        street: 'Baker Street',
        postalCode: 'NW1 6XE',
        active: true,
        blocked: false,
        role: { id: 2, name: 'USER' },
      },
    ];

    // Récupérer l'email depuis Keycloak
  const email = await this.getUserEmail();
  if (!email) {
    throw new Error('Email not found in Keycloak');
  }

  // Trouver l'utilisateur dans la base de données simulée
  const user = users.find((user) => user.mail === email);

  if (user) {
    return user.id.toString(); // Retourner l'ID de l'utilisateur
  } else {
    throw new Error(`User with email ${email} not found`);
  }
}
    
}
