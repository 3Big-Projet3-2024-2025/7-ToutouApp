import { Injectable } from '@angular/core';
import { KeycloakService } from 'keycloak-angular';
import { jwtDecode } from 'jwt-decode';
import { HttpClient } from '@angular/common/http';

@Injectable({
  providedIn: 'root',
})
export class AuthService {
  private apiUrl = 'http://localhost:8080/user';
  initializeUser: any;

  constructor(private keycloakService: KeycloakService, private http: HttpClient) {}

  // Retrieve the list of existing emails
  getAllEmails(): Promise<string[]> {
    return this.http.get<string[]>(`${this.apiUrl}/emails`).toPromise().then((res) => res || []);
  }

  // Create a user
  async createUser(): Promise<void> {
    try {
      const profile = await this.keycloakService.loadUserProfile();

      // Default values if fields are missing
      const email: string = profile.email || 'unknown@email.com';
      const firstName: string = profile.firstName || 'Unknown';
      const lastName: string = profile.lastName || 'Unknown';

      // Decode the Access Token to obtain additional information
      const token = await this.getAccessToken();
      const decodedToken: any = jwtDecode(token);

      const country: string = decodedToken?.country || 'N/A';
      const city: string = decodedToken?.city || 'N/A';
      const street: string = decodedToken?.street || 'N/A';
      const postalCode: string = decodedToken?.postal_code || 'N/A';

      // Check if the email exists in the database
      const emails = await this.getAllEmails();

      if (!emails.includes(email)) {
        const user = { email, firstName, lastName, country, city, street, postalCode };

        // API call to create the user
        this.http.post(`${this.apiUrl}/create`, user).subscribe({
          next: (res) => console.log('User successfully created', res),
          error: (err) => console.error('Error creating the user', err),
        });
      } else {
        console.log('User already exists with this email:', email);
      }
    } catch (error) {
      console.error('Error creating the user:', error);
    }
  }

  // Retrieve the user's first name (given_name) and last name (family_name)
  async getUserFullName(): Promise<string> {
    const profile = await this.keycloakService.loadUserProfile();
    const firstName = profile.firstName || '';
    const lastName = profile.lastName || '';
    return `${firstName} ${lastName}`;
  }

  login(): Promise<void> {
    return this.keycloakService.login();
  }

  logout(): void {
    this.keycloakService.logout(window.location.origin);
  }

  isLoggedIn(): Promise<boolean> {
    return Promise.resolve(this.keycloakService.isLoggedIn());
  }

  async getUsername(): Promise<string> {
    const profile = await this.keycloakService.loadUserProfile();
    return profile.firstName || '';
  }

  // Method to retrieve the Access Token
  async getAccessToken(): Promise<string> {
    const token = await this.keycloakService.getToken();
    console.log('Access Token:', token);  
    return token || '';
  }
  

  // Decode the JWT Token
  async getDecodedAccessToken(): Promise<any> {
    const token = await this.getAccessToken();
    return jwtDecode(token);
  }
}
