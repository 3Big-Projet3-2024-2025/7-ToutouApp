import { Component } from '@angular/core';
import { HeaderComponent } from "../header/header.component";
import { FooterComponent } from "../footer/footer.component";

@Component({
  selector: 'app-personal-profile',
  standalone: true,
  imports: [HeaderComponent, FooterComponent],
  templateUrl: './personal-profile.component.html',
  styleUrl: './personal-profile.component.css'
})
export class PersonalProfileComponent {

}
