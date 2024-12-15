import { Component } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { KeycloakService } from 'keycloak-angular';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [RouterOutlet],
  templateUrl: './app.component.html',
  styleUrl: './app.component.css'
})
export class AppComponent {


  title(title: any) {
    throw new Error('Method not implemented.');
  }
  constructor(private keycloakService: KeycloakService) {}

  async login(): Promise<void> {
    await this.keycloakService.login({
      redirectUri: window.location.origin, // Redirige vers l'URL racine après connexion
    });
  }
}
