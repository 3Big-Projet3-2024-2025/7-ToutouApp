import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class RequestService {

  private baseUrl = 'http://localhost:8080/request';

  constructor(private http: HttpClient) { }

  getAllRequests(): Observable<any> {
    return this.http.get(this.baseUrl); 
  }

  addRequest(request: any): Observable<any> {
    return this.http.post(`${this.baseUrl}`, request); 
  }

  deleteRequest(id: number): Observable<any> {
    return this.http.delete(`${this.baseUrl}/${id}`); 
  }

  modifyRequest(id: number, request: any): Observable<any> {
    return this.http.put(`${this.baseUrl}/${id}`, request); 
  }

  getUserRequest(userId: number): Observable<any>{
    return this.http.get(`${this.baseUrl}/user/${userId}`);
  }

  getRequestById(requestId: string): Observable<any>{
    return this.http.get(`${this.baseUrl}/${requestId}`);
  }
}
