import { Component, OnInit } from '@angular/core';
import { RequestService } from '../../services/request.service';
import { ActivatedRoute, Router } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { HeaderComponent } from '../header/header.component';

@Component({
  selector: 'app-edit-request',
  standalone: true,
  imports: [FormsModule,CommonModule,HeaderComponent],
  templateUrl: './edit-request.component.html',
  styleUrl: './edit-request.component.css'
})
export class EditRequestComponent implements OnInit{

  constructor(private requestService: RequestService, private router: ActivatedRoute, private route: Router){}

  request: any = {};
  dogCategories: any [] = [];


  categories = [
    { id: 1, name: 'Big' },
    { id: 2, name: 'Medium' },
    { id: 3, name: 'Small' }
  ];


  ngOnInit(): void {
    const requestId = this.router.snapshot.paramMap.get('id');

    if(requestId){
      this.loadRequest(requestId);
    }
  }


  loadRequest(requestId: string){
    this.requestService.getRequestById(requestId).subscribe((data) => {
      this.request = data;
    });
  }


  onSubmit() {
    // Ici, tu enverras les données mises à jour au backend
    this.requestService.modifyRequest(this.request.requestId, this.request).subscribe(
      (response) => {
        console.log('Requête mise à jour avec succès', response);
        window.alert('Your modifications have been saved successfully.');
        this.route.navigate(['/hub-requests']);
      },
      (error) => {
        console.error('Erreur lors de la mise à jour de la requête', error);
      }
    );
  }

  cancelModif(){
    this.route.navigate(['/hub-requests']);
  }

  

}
