import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class NominatimService {
  private baseUrl: string = 'https://nominatim.openstreetmap.org/search';

  constructor(private http: HttpClient) {}

  getCoordinates(address: string): Observable<any> {
    const params = {
      q: address,
      format: 'json',
      addressdetails: '1',
      limit: '1'
    };
    return this.http.get<any>(this.baseUrl, { params });
  }
}
