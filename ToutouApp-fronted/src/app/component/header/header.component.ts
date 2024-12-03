import { Component } from '@angular/core';
import { Router } from '@angular/router';

@Component({
  selector: 'app-header',
  standalone: true,
  imports: [],
  templateUrl: './header.component.html',
  styleUrl: './header.component.css'
})
export class HeaderComponent {

  constructor(private router:Router){}

  navigateToHome() {
    console.log('Logo clicked!');
    this.router.navigateByUrl('/').then(() => {
      window.location.reload();  // force le rechargement de la page si je suis déjà dessus 
    });
  }
  
}
