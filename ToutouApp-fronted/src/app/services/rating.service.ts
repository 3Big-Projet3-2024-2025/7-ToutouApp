import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class RatingService {

  constructor(private http: HttpClient) { }

  private baseUrl = 'http://localhost:8080/rating';


    getUserRating(userId: number): Observable<any>{
      return this.http.get(`${this.baseUrl}/user/${userId}`);
    }


    deleteRequest(id: number): Observable<any> {
      return this.http.delete(`${this.baseUrl}/${id}`); 
    }


    addRequest(request: any): Observable<any> {
      return this.http.post(`${this.baseUrl}`, request); 
    }














}
