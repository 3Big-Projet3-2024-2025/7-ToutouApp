import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { User, UserService } from '../../services/user.service';
import { FormBuilder, FormGroup, ReactiveFormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { Validators } from '@angular/forms';


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
      firstName: ['', [Validators.required, Validators.minLength(3)]],
      lastName: ['', [Validators.required, Validators.minLength(3)]],
      mail: [{ value: '', disabled: true }],
      country: ['', [Validators.required]],
      city: ['', [Validators.required]],
      street: ['', [Validators.required]],
      postalCode: ['', [Validators.required, Validators.minLength(4), Validators.pattern('^[0-9]*$')]],
    });
    
    
    
  }

  ngOnInit(): void {
    const email = this.route.snapshot.paramMap.get('id');
    if (email) {
        this.userEmail = email;
        this.loadUser();
    } else {
        console.error("Email not provided in the URL!");
    }
  }

  loadUser(): void {
    this.userService.getUserByEmail(this.userEmail).subscribe({
        next: (user) => {
            this.userForm.patchValue(user);
        },
        error: (err) => {
            console.error('Error while loading user:', err);
            alert('User not found!');
            this.router.navigate(['/users']);
        },
    });
  }

  onSave(): void {
    const updatedUser: User = this.userForm.getRawValue(); // Includes disabled fields
    console.log('Data sent:', updatedUser); // Debugging

    this.userService.updateUser(updatedUser).subscribe({
        next: () => {
            alert('User successfully updated');
            this.router.navigate(['/admin/users']); // Redirect to the user list
        },
        error: (err) => {
            console.error('Error while updating:', err);
            alert('An error occurred while updating.');
        },
    });
  }

  onCancel(): void {
    this.router.navigate(['/admin/users']);
  }

  isInvalid(field: string): boolean {
    const control = this.userForm.get(field);
    return !!(control && control.invalid && control.touched);
  }

  getErrorMessage(field: string): string | null {
    const control = this.userForm.get(field);

    if (!control || !control.errors) return null;

    if (control.errors['required']) {
        return 'This field is required.';
    }
    if (control.errors['minlength']) {
        return `This field must have at least ${control.errors['minlength'].requiredLength} characters.`;
    }
    if (control.errors['pattern']) {
        return 'This field must contain only numbers.';
    }

    return null;
  }
  
}



