import { Component, Input, OnInit } from '@angular/core';
import { ChatService } from '../../services/chat.service';
import { ActivatedRoute, Router } from '@angular/router';
import { MessageService } from '../../services/message.service';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { HeaderComponent } from "../header/header.component";
import { FooterComponent } from "../footer/footer.component";
import { UserIdService } from '../../services/user-id.service';

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
  helperName: string = '';
  userName: string = '';

  constructor(
    private chatService: ChatService,
    private messageService: MessageService,
    private route: ActivatedRoute,
    private router: Router,
    private userService: UserIdService
  ) { }

  ngOnInit(): void {
    const navigation = this.router.getCurrentNavigation();
    const state = navigation?.extras.state as { helperName: string, userName: string };

    if (state) {
      this.helperName = state.helperName;
      this.userName = state.userName;
    }

    this.route.paramMap.subscribe(params => {
      this.requestId = +params.get('requestId')!;
      this.loadChat();
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
    });
  }

  sendMessage(): void {
    if (this.newMessage.trim()) {
      this.messageService.sendMessage(this.chat?.chatId!, this.newMessage).subscribe(() => {
        if (this.chat) {
          this.loadMessages(this.chat.chatId);
          this.newMessage = '';
        }
      });
    }
  }
}
