import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { User, UserService } from '../../services/user.service';
import { FormBuilder, FormGroup, ReactiveFormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-user-edit',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './user-edit.component.html',
  styleUrl: './user-edit.component.css'
})
export class UserEditComponent implements OnInit {
  userForm: FormGroup;
  userEmail!: string;

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private userService: UserService,
    private fb: FormBuilder
  ) {
    this.userForm = this.fb.group({
      id: [null], 
      firstName: [''],
      lastName: [''],
      mail: [''],
      country: [''],
      city: [''],
      street: [''],
      postalCode: [''],
    });
    
  }

  ngOnInit(): void {
    const email = this.route.snapshot.paramMap.get('id'); // Utilisez 'id' pour capturer l'email
    if (email) {
      this.userEmail = email;
      this.loadUser();
    } else {
      console.error("Email non fourni dans l'URL !");
    }
  }

  loadUser(): void {
    this.userService.getUserByEmail(this.userEmail).subscribe({
      next: (user) => {
        this.userForm.patchValue(user); 
      },
      error: (err) => {
        console.error('Erreur lors du chargement de l\'utilisateur :', err);
        alert('Utilisateur introuvable !');
        this.router.navigate(['/users']);
      },
    });
  }
  

  onSave(): void {
    const updatedUser: User = { ...this.userForm.value }; // Inclut toutes les données du formulaire, y compris l'ID
    this.userService.updateUser(updatedUser).subscribe({
      next: () => {
        alert('Utilisateur mis à jour avec succès');
        this.router.navigate(['/admin/users']); // Redirige vers la liste
      },
      error: (err) => {
        console.error('Erreur lors de la mise à jour :', err);
        alert('Une erreur est survenue lors de la mise à jour.');
      },
    });
  }
  

  onCancel(): void {
    this.router.navigate(['/admin/users']);
  }
}

