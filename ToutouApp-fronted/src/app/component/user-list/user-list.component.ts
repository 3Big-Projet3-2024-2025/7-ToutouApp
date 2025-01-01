import { Component, OnInit } from '@angular/core';
import { User, UserService } from '../../services/user.service';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';

@Component({
  selector: 'app-user-list',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './user-list.component.html',
  styleUrl: './user-list.component.css'
})
export class UserListComponent implements OnInit {
  users: User[] = [];
  isLoading = true;

  constructor(private userService: UserService, private router: Router) {}

  ngOnInit(): void {
    this.fetchUsers();
  }

  fetchUsers(): void {
    this.isLoading = true;
    this.userService.getAllUsers().subscribe({
      next: (data) => {
        this.users = data;
        this.isLoading = false;
      },
      error: (err) => {
        console.error('Erreur lors du chargement des utilisateurs', err);
        this.isLoading = false;
      },
    });
  }

  onToggleBlock(user: User, event: Event): void {
    const isChecked = (event.target as HTMLInputElement).checked;
    this.userService.blockUser(user.id, isChecked).subscribe({
      next: (updatedUser) => {
        user.blocked = updatedUser.blocked;
      },
      error: (err) => {
        console.error('Erreur lors du blocage/déblocage', err);
      },
    });
  }

  onEditUser(user: User): void {
    this.router.navigate(['/users/edit', user.mail]);
  }
  
  
}





