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
            alert(`User ${user.firstName} ${user.lastName} has been ${isChecked ? 'blocked' : 'unblocked'}.`);
        },
        error: (err) => {
            console.error('Error while blocking/unblocking user:', err);

            // Retrieve the error message sent by the backend
            const errorMessage = err.error?.error || 'An error occurred.';

            // Check if this is the error for the last active administrator
            if (errorMessage === 'Cannot block the last active administrator!') {
                alert('You cannot block the last active administrator.');
            } else {
                alert('An error occurred while blocking/unblocking the user.');
            }

            // Reset the toggle state to reflect the actual status
            (event.target as HTMLInputElement).checked = !isChecked;
        },
    });
}

  

  onEditUser(user: User): void {
    this.router.navigate(['/users/edit', user.mail]);
  }

  onDeleteUser(user: User): void {
    if (confirm(`Are you sure you want to delete the user ${user.firstName} ${user.lastName}?`)) {
        this.userService.updateUserFlag(user.id, false).subscribe({
            next: () => {
                alert(`User ${user.firstName} ${user.lastName} has been deleted.`);
                this.fetchUsers();
            },
            error: (err) => {
                console.error('Error while deleting the user:', err);

                // Ensure the error is retrieved correctly
                const errorMessage = err.error?.error || 'An unknown error occurred';

                // Handle specific error message
                if (errorMessage === 'Cannot remove the last active administrator!') {
                    alert('You cannot delete the last active administrator.');
                } else {
                    alert('An error occurred while deleting the user.');
                }
            },
        });
    }
}

}





