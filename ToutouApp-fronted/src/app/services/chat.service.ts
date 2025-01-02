import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Chat } from '../component/chat/chat.component';

@Injectable({
  providedIn: 'root'
})

export class ChatService {
  private baseUrl = 'http://localhost:8080/chats';

  constructor(private http: HttpClient) {}

  createChat(requestId: number): Observable<Chat> {
      return this.http.post<Chat>(`http://localhost:8080/chats/create?requestId=${requestId}`, {});
  }

  getChatByRequest(requestId: number): Observable<any> {
    return this.http.get(`${this.baseUrl}/chats/request/${requestId}`);
  }
}
