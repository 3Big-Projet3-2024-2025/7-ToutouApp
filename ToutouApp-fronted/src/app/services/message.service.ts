import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})

export class MessageService {
  private baseUrl = 'http://localhost:8080/messages';

  constructor(private http: HttpClient) { }

  getMessagesByChat(chatId: number): Observable<any> {
    return this.http.get(`${this.baseUrl}/chat/${chatId}`);
  }

  sendMessage(chatId: number, content: string): Observable<any> {
    return this.http.post(`${this.baseUrl}/send/${chatId}`, { content });
  }

  markAsRead(messageId: number): Observable<any> {
    return this.http.patch(`${this.baseUrl}/read/${messageId}`, {});
  }
}
