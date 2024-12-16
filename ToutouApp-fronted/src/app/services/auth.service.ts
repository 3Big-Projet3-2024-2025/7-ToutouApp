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

  // Récupérer la liste des emails existants
  getAllEmails(): Promise<string[]> {
    return this.http.get<string[]>(`${this.apiUrl}/emails`).toPromise().then((res) => res || []);
  }

  // Créer un utilisateur
  async createUser(): Promise<void> {
    try {
      const profile = await this.keycloakService.loadUserProfile();

      // Valeurs par défaut si des champs sont absents
      const email: string = profile.email || 'unknown@email.com';
      const firstName: string = profile.firstName || 'Unknown';
      const lastName: string = profile.lastName || 'Unknown';

      // Décoder l'Access Token pour obtenir les autres informations
      const token = await this.getAccessToken();
      const decodedToken: any = jwtDecode(token);

      const country: string = decodedToken?.country || 'N/A';
      const city: string = decodedToken?.city || 'N/A';
      const street: string = decodedToken?.street || 'N/A';
      const postalCode: string = decodedToken?.postal_code || 'N/A';

      // Vérifier si l'email existe dans la base de données
      const emails = await this.getAllEmails();

      if (!emails.includes(email)) {
        const user = { email, firstName, lastName, country, city, street, postalCode };

        // Appel à l'API pour créer l'utilisateur
        this.http.post(`${this.apiUrl}/create`, user).subscribe({
          next: (res) => console.log('Utilisateur créé avec succès', res),
          error: (err) => console.error("Erreur lors de la création de l'utilisateur", err),
        });
      } else {
        console.log('Utilisateur déjà existant avec cet email : ', email);
      }
    } catch (error) {
      console.error('Erreur lors de la création de l\'utilisateur :', error);
    }
  }

  // Récupère le prénom (given_name) et le nom de famille (family_name) de l'utilisateur
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

  // Méthode pour récupérer l'Access Token
  async getAccessToken(): Promise<string> {
    const token = await this.keycloakService.getToken();
    console.log('Access Token:', token);  
    return token || '';
  }
  

  // Décoder le Token JWT
  async getDecodedAccessToken(): Promise<any> {
    const token = await this.getAccessToken();
    return jwtDecode(token);
  }
}
