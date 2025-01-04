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
  minDate: string = '';
  minTime: string = '';


  categories = [
    { id: 1, name: 'Big' },
    { id: 2, name: 'Medium' },
    { id: 3, name: 'Small' }
  ];


  ngOnInit(): void {

    this.setMinDate();
    this.setMinTime();

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

    if (!this.isEndTimeValid()) {
      console.error('Invalid time range: End time must be later than start time.');
     
      return;
    }
    

    this.requestService.modifyRequest(this.request.requestId, this.request).subscribe(
      (response) => {
        console.log('Requête mise à jour avec succès', response);
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


  isValidUrl(url: string): boolean {
    try {
      const parsedUrl = new URL(url);
      return (
        /\.(jpg|jpeg|png|gif|webp)$/i.test(parsedUrl.pathname) || 
        /^data:image\/(jpeg|png|gif|webp);base64,/i.test(parsedUrl.href)
    );
    } catch (e) {
      return false;
    }
  }


  setMinDate(): void {
    const today = new Date();
    const year = today.getFullYear();
    const month = String(today.getMonth() + 1).padStart(2, '0');
    const day = String(today.getDate()).padStart(2, '0');
    this.minDate = `${year}-${month}-${day}`;
  }

  setMinTime(): void {
    const now = new Date();
    const hours = String(now.getHours()).padStart(2, '0');
    const minutes = String(now.getMinutes()).padStart(2, '0');
    this.minTime = `${hours}:${minutes}`;
  }

  onDateChange(): void {
    const selectedDate = new Date(this.request.requestDate);
    const today = new Date(this.minDate);

    
    if (selectedDate.toDateString() === today.toDateString()) {
      this.setMinTime();
    } else {
     
      this.minTime = '00:00';
    }
  }

  isEndTimeValid(): boolean {
    const startTime = this.request.startTime;
    const endTime = this.request.endTime;
  
    return !endTime || !startTime || endTime > startTime;
  }

  

}
