import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

export interface Rating {
  id: number;
  evaluatedUserName: string;
  raterUserName: string;
  ratingValue: number;
  comment: string;
  requestDate: string;
}

@Injectable({
  providedIn: 'root'
})
export class RatingService {

  constructor(private http: HttpClient) { }

  private baseUrl = 'http://localhost:8080/rating';

    getUserRating(userId: number): Observable<any>{
      return this.http.get(`${this.baseUrl}/user/${userId}`);
    }

    deleteRating(id: number): Observable<any> {
      return this.http.delete(`${this.baseUrl}/${id}`); 
    }


    addRating(rating: any): Observable<any> {
      return this.http.post(`${this.baseUrl}`, rating); 
    }

    getAllRatings(): Observable<Rating[]> {
      return this.http.get<Rating[]>(this.baseUrl);
    }

    getNegativeRatings(): Observable<Rating[]> {
      return this.http.get<Rating[]>(`${this.baseUrl}/negative`);
    }
    
}
