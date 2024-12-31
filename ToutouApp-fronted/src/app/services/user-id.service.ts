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
        id: 18,
        mail: 'marie@gmail.com',
        lastName: 'Madeleine',
        firstName: 'Marie',
        country: 'Australia',
        city: 'Sydney',
        street: 'Porto 3',
        postalCode: '78000',
        active: true,
        blocked: false,
        role: { roleId: 1, name: 'USER' },
      },
      {
        id: 15,
        mail: 'jean1@gmail.com',
        lastName: 'jean',
        firstName: 'jean',
        country: 'jean',
        city: 'jean',
        street: 'jean 123',
        postalCode: '5885',
        active: true, 
        blocked: false,
        role: { id: 1, name: 'USER' },
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

  getUserByEmail(email: string): Observable<any> {
    return this.http.get<any>(`${this.apiUrl}/${email}`);
  }

    updateUser(user: any): Observable<any> {
      return this.http.put(`${this.apiUrl}/${user.id}`, user);
    }
  
    deleteUser(id: number): Observable<void> {
      return this.http.delete<void>(`${this.apiUrl}/${id}`);
    }

    getUserReviews(email: string): Observable<string[]> {
      return this.http.get<string[]>(`${this.apiUrl}/${email}/reviews`);
    }
}
