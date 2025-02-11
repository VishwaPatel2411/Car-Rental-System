import { HttpClient } from '@angular/common/http';
import { Component } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { catchError, of } from 'rxjs';

@Component({
  selector: 'app-forgot-password',
  templateUrl: './forgot-password.component.html',
  styleUrl: './forgot-password.component.scss'
})
export class ForgotPasswordComponent {
  forgotpasswordForm!: FormGroup;
  otpForm!: FormGroup;
  newPasswordForm!: FormGroup;
  otpSent: boolean = false;  // To control the visibility of OTP form
  otpVerify:boolean=false;
  email: string = '';         // To store the email entered by the user
  otp: string = '';           // OTP generated and sent to email

  constructor(
    private fb: FormBuilder,
    private http: HttpClient,
    private router: Router,
    
  ) { }

  ngOnInit(): void {
    // Initialize the form group with a form control for registeredEmail
    this.forgotpasswordForm = this.fb.group({
      registeredEmail: ['', [Validators.required, Validators.email]] // Added email validation
    });

      // Initialize OTP form
      this.otpForm = this.fb.group({
        otp: ['', [Validators.required, Validators.pattern('^[0-9]{4}$')]] // OTP validation
      });

      this.newPasswordForm = this.fb.group({
        newPassword: ['', [Validators.required, Validators.minLength(6)]], // Password validation
        confirmPassword: ['', [Validators.required, Validators.minLength(6)]]
      });
    }
  

  // Send OTP function
  sendOtp() {
    if (this.forgotpasswordForm.invalid) {
      // Mark all fields as touched to show validation errors
      this.forgotpasswordForm.markAllAsTouched();
      return;
    }

    // Prepare the payload for the request
    const email = this.forgotpasswordForm.value.registeredEmail;
    
    // Make the HTTP POST request to send OTP to the backend
    this.http.post('http://localhost:8080/api/auth/send-otp', { email },{ responseType: 'text' })
      .pipe(
        catchError(error => {
          // Handle error if API request fails
          console.error('Error sending OTP:', error);
          alert('Failed to send OTP. Please try again.');
          return of(null);  // Return an empty observable to prevent further processing
        })
      )
      .subscribe(response => {
        if (response) {
          alert('OTP sent successfully to ' + email);
          this.otpSent = true;
        this.email = email;  // Store email for later use in OTP verificatio
        }
      });
  }

  // Getter for form control access in the template
  get registeredEmail() {
    return this.forgotpasswordForm.get('registeredEmail');
  }

// Verify OTP function
verifyOtp() {
  if (this.otpForm.invalid) {
    this.otpForm.markAllAsTouched();
    return;
  }

  const enteredOtp = this.otpForm.value.otp;

  // Call backend to verify OTP
  this.http.post('http://localhost:8080/api/auth/validate-otp', { email: this.email, otp: enteredOtp },{ responseType: 'text' })
    .pipe(
      catchError(error => {
        console.error('Error verifying OTP:', error);
        alert('OTP verification failed. Please try again.');
        return of(null);  // Return an empty observable to prevent further processing
      })
    )
    .subscribe(response => {
      if (response && response === "OTP is valid.") {
        alert('OTP verified successfully.');
        this.otpVerify =true;
      } else {
        alert('OTP verification failed. Please try again.');
      }
    });
    
}
get otpControl() {
  return this.otpForm.get('otp');
}




// Update password
updatePassword() {
  console.log("updatepassword()");
 
  const newPassword = this.newPasswordForm.value.newPassword;

  console.log("updatepassword()-2");

  // Call backend API to update password
  this.http.post('http://localhost:8080/api/auth/update-password', {

    email: this.email,
    newPassword: newPassword
  }, { responseType: 'text' })
    .pipe(
      catchError(error => {
        console.error('Error updating password:', error);
        alert('Failed to update password. Please try again.');
        return of(null);
      })
    )
    .subscribe(response => {
      if (response && response === "Password updated successfully.") {
        alert('Password updated successfully!');
        this.newPasswordForm.reset();  // Reset form after success
        this.router.navigateByUrl('/login');
      } else {
        alert('Password update failed. Please try again.');
      }
    });


}}
