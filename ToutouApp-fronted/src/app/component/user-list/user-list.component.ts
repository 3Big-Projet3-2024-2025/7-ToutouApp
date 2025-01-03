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
  imports: [CommonModule, HeaderComponent, FooterComponent, FormsModule, NgxPaginationModule],
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

  // Fetch all users from the backend
  fetchUsers(): void {
    this.isLoading = true;
    this.userService.getAllUsers().subscribe({
      next: (data) => {
        this.users = data;
        this.filteredUsers = data;  // Initially, all users are displayed
        this.isLoading = false;
      },
      error: (err) => {
        console.error('Error while fetching users', err);
        this.isLoading = false;
      },
    });
  }

  // Search functionality
  onSearch(): void {
    if (this.searchTerm.trim() === '') {
      this.filteredUsers = this.users;  // If search field is empty, display all users
    } else {
      this.filteredUsers = this.users.filter(user =>
        user.lastName.toLowerCase().includes(this.searchTerm.toLowerCase()) // Search by lastName
      );
    }
  }

  // Toggle block/unblock user
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

  // Navigate to the user edit page
  onEditUser(user: User): void {
    this.router.navigate(['/users/edit', user.mail]);
  }

  // Deactivate a user
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
