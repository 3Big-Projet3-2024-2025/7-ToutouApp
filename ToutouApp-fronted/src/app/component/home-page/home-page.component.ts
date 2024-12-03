import { Component, AfterViewInit } from '@angular/core';
import { FooterComponent } from '../footer/footer.component';
import { HeaderComponent } from '../header/header.component';

@Component({
  selector: 'app-home-page',
  standalone: true,
  imports: [FooterComponent,HeaderComponent],
  templateUrl: './home-page.component.html',
  styleUrls: ['./home-page.component.css']
})
export class HomePageComponent implements AfterViewInit {
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
        threshold: 0.4, // Déclenche l'animation lorsque 20% de l'élément est visible
      }
    );

    // Observe chaque élément
    elements.forEach((el) => observer.observe(el));
  }
}

