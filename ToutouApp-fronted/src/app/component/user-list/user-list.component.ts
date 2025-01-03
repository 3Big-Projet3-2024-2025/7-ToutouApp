import { Component, OnInit } from '@angular/core';
import { User, UserService } from '../../services/user.service';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';
import { HeaderComponent } from '../header/header.component';
import { FooterComponent } from '../footer/footer.component';
import { FormsModule } from '@angular/forms';
import { NgxPaginationModule } from 'ngx-pagination';

@Component({
  selector: 'app-user-list',
  standalone: true,
  imports: [CommonModule, HeaderComponent, FooterComponent,FormsModule,NgxPaginationModule],
  templateUrl: './user-list.component.html',
  styleUrl: './user-list.component.css'
})
export class UserListComponent implements OnInit {
  users: User[] = [];
  filteredUsers: User[] = [];
  isLoading = true;
  searchTerm: string = '';

  constructor(private userService: UserService, private router: Router) {}
  currentPage: number = 1;


  ngOnInit(): void {
    this.fetchUsers();
  }

  fetchUsers(): void {
    this.isLoading = true;
    this.userService.getAllUsers().subscribe({
      next: (data) => {
        this.users = data;
        this.filteredUsers = data;  // Initialement, tous les utilisateurs sont affichés
        this.isLoading = false;
      },
      error: (err) => {
        console.error('Erreur lors du chargement des utilisateurs', err);
        this.isLoading = false;
      },
    });
  }

  // Fonction de recherche
  onSearch(): void {
    if (this.searchTerm.trim() === '') {
      this.filteredUsers = this.users;  // Si le champ de recherche est vide, on montre tous les utilisateurs
    } else {
      this.filteredUsers = this.users.filter(user =>
        user.lastName.toLowerCase().includes(this.searchTerm.toLowerCase()) // Recherche par lastName
      );
    }
  }

  onToggleBlock(user: User, event: Event): void {
    const isChecked = (event.target as HTMLInputElement).checked;

    this.userService.blockUser(user.id, isChecked).subscribe({
      next: (updatedUser) => {
        user.blocked = updatedUser.blocked;
        alert(`User ${user.firstName} ${user.lastName} has been ${isChecked ? 'blocked' : 'unblocked'}.`);
      },
      error: (err) => {
        console.error('Error while blocking/unblocking user:', err);

        const errorMessage = err.error?.error || 'An error occurred.';
        if (errorMessage === 'Cannot block the last active administrator!') {
          alert('You cannot block the last active administrator.');
        } else {
          alert('An error occurred while blocking/unblocking the user.');
        }

        (event.target as HTMLInputElement).checked = !isChecked;  // Reset checkbox if there is an error
      },
    });
  }

  onEditUser(user: User): void {
    this.router.navigate(['/users/edit', user.mail]);
  }

  onDeleteUser(user: User): void {
    if (confirm(`Are you sure you want to deactivate the user ${user.firstName} ${user.lastName}?`)) {
      this.userService.updateUserFlag(user.id, false).subscribe({
        next: () => {
          alert(`User ${user.firstName} ${user.lastName} has been deactivated.`);
          this.fetchUsers(); // Reloads the list of users
        },
        error: (err) => {
          console.error('Error while deactivating user:', err);
          const errorMessage = err.error?.error || 'An error occurred.';
  
          if (errorMessage === 'Cannot deactivate a user linked to an active request.') {
            alert('Cannot deactivate this user because they are linked to an active request.');
          } else if (errorMessage === 'Cannot deactivate the last active administrator!') {
            alert('You cannot deactivate the last active administrator.');
          } else {
            alert('An error occurred while deactivating the user.');
          }
        },
      });
    }
  }

  
}
