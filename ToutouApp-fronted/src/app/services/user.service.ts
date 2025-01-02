import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface User {
  id: number;
  mail: string;
  firstName: string;
  lastName: string;
  country: string;
  city: string;
  street: string;
  postalCode: string;
  active: boolean;
  blocked: boolean;
  role: {
    roleId: number;
    name: string;
  };
}

@Injectable({
  providedIn: 'root',
})
export class UserService {
  private apiUrl = 'http://localhost:8080/user';

  constructor(private http: HttpClient) {}

  getAllUsers(): Observable<User[]> {
    return this.http.get<User[]>(`${this.apiUrl}`);
  }

  blockUser(id: number, block: boolean): Observable<User> {
    return this.http.patch<User>(`${this.apiUrl}/${id}/block?block=${block}`, {});
  }

  updateUser(user: User): Observable<User> {
    return this.http.put<User>(`http://localhost:8080/user/${user.id}`, user);
  }
  

  getUserByEmail(email: string): Observable<User> {
    return this.http.get<User>(`${this.apiUrl}/${email}`);
  }
  
  updateUserFlag(id: number, flag: boolean): Observable<User> {
  return this.http.patch<User>(`http://localhost:8080/user/${id}/flag?flag=${flag}`, {});
}

  
}
