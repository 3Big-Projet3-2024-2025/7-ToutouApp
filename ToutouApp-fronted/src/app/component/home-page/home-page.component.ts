import { Component, AfterViewInit, OnInit } from '@angular/core';
import { FooterComponent } from '../footer/footer.component';
import { HeaderComponent } from '../header/header.component';
import { AuthService } from '../../services/auth.service';

@Component({
  selector: 'app-home-page',
  standalone: true,
  imports: [FooterComponent, HeaderComponent],
  templateUrl: './home-page.component.html',
  styleUrls: ['./home-page.component.css'],
})
export class HomePageComponent implements AfterViewInit, OnInit {
  userFullName: string = ''; // Variable pour stocker le nom complet

  constructor(private authService: AuthService) {}

  async ngOnInit(): Promise<void> {
    try {
      // Crée l'utilisateur dans la base de données s'il n'existe pas
      await this.authService.createUser();

      // Charge le nom complet après la connexion
      this.userFullName = await this.authService.getUserFullName();

      console.log('Nom complet de l’utilisateur chargé :', this.userFullName);
    } catch (error) {
      console.error('Erreur lors de la création ou du chargement de l’utilisateur :', error);
    }
  }

  ngAfterViewInit(): void {
    // Sélectionne tous les éléments à animer
    const elements = document.querySelectorAll('.animate-on-scroll');

    // Configure l'observateur
    const observer = new IntersectionObserver(
      (entries) => {
        entries.forEach((entry) => {
          if (entry.isIntersecting) {
            // Ajoute une classe visible si l'élément est visible
            entry.target.classList.add('visible');
            observer.unobserve(entry.target); // Arrête d'observer après l'animation
          }
        });
      },
      {
        threshold: 0.4, // Déclenche l'animation lorsque 40% de l'élément est visible
      }
    );

    // Observe chaque élément
    elements.forEach((el) => observer.observe(el));
  }
}
