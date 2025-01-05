import { Component, Input, OnInit, ViewChild } from '@angular/core';
import { ChatService } from '../../services/chat.service';
import { ActivatedRoute, Router } from '@angular/router';
import { MessageService } from '../../services/message.service';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { HeaderComponent } from "../header/header.component";
import { FooterComponent } from "../footer/footer.component";
import { UserIdService } from '../../services/user-id.service';
import { RequestService } from '../../services/request.service';

interface User {
  id: number;
  name: string;
}

interface Request {
  owner: User;
  helper: User;
}

export interface Chat {
  chatId: number;
  request: Request;
  createdAt: string;
  chatFlag: boolean;
}

@Component({
  selector: 'app-chat',
  standalone: true,
  imports: [CommonModule, FormsModule, HeaderComponent, FooterComponent],
  templateUrl: './chat.component.html',
  styleUrls: ['./chat.component.css']
})
export class ChatComponent implements OnInit {
  chat: Chat | undefined;
  messages: any[] = [];
  newMessage: string = '';
  @Input() requestId!: number;
  helperName: any;
  userName: any;
  request: any;
  userId?: any;

  constructor(
    private chatService: ChatService,
    private messageService: MessageService,
    private route: ActivatedRoute,
    private router: Router,
    private userService: UserIdService,
    private requestService: RequestService
  ) { }

  ngOnInit(): void {
    this.route.paramMap.subscribe(params => {
      this.requestId = +params.get('requestId')!;
      this.loadChat();
      this.loadRequest();
    });
  }

  loadChat(): void {
    this.chatService.getChatByRequest(this.requestId).subscribe((chat: Chat) => {
      this.chat = chat;
      this.loadMessages(chat.chatId);
    });
  }

  loadMessages(chatId: number): void {
    this.messageService.getMessagesByChat(chatId).subscribe(messages => {
      this.messages = messages;

      this.markMessagesAsRead();
    });
  }

  sendMessage(): void {
    if (this.newMessage.trim().length > 60) {
      alert('Your message is too long. Please limit your message to 60 characters or less.');
      this.newMessage = '';
      return;
    }

    if (this.newMessage.trim()) {
      this.messageService.sendMessage(this.chat?.chatId!, this.newMessage).subscribe(() => {
        if (this.chat) {
          this.loadMessages(this.chat.chatId);
          this.newMessage = '';
        }
      });
    }
  }

  loadRequest(): void {
    this.requestService.getRequestById(this.requestId.toString()).subscribe((request: Request) => {
      this.request = request;
      
      this.helperName = this.request.helper.lastName;
      this.userName = this.request.owner.lastName;
    }, (error) => {
      console.error('Error retrieving query:', error);
    });
  }

  markMessagesAsRead(): void {
    if (this.messages.length > 0) {
      this.messages.forEach(message => {
        this.messageService.markAsRead(message.messageId).subscribe(() => {
        });
      });
    }
  }
}
