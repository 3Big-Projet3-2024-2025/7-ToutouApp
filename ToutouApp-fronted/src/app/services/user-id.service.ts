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

  
  private getUserIdFromApi(email: string): Observable<string> {
    return this.http.get<string>(`${this.apiUrl}/id`, { params: { email } });
  }


  private async getUserEmail(): Promise<string> {
    const profile = await this.keycloakService.loadUserProfile();
    return profile.email || ''; 
  }


  async getUserId(): Promise<string> {


    
  const email = await this.getUserEmail();
  if (!email) {
    throw new Error('Email not found in Keycloak');
  }

  const users = await this.getAllUsers().toPromise();

  
  const user = users.find((user: any) => user.mail === email);

  if (user) {
    return user.id.toString(); 
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

    getAllUsers(): Observable<any> {
      return this.http.get(this.apiUrl);
    }
    
}
