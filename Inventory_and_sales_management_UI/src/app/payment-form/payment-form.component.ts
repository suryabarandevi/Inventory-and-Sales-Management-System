// payment-form.component.ts
import { Component } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { HttpClient, HttpHeaders } from '@angular/common/http';
@Component({
  selector: 'app-payment-form',
  templateUrl: './payment-form.component.html',
  styleUrls: ['./payment-form.component.css']
})
export class PaymentFormComponent {
  paymentForm: FormGroup;

  constructor(private fb: FormBuilder,private http: HttpClient) {
    
    this.paymentForm = this.fb.group({
      orderId: ['', [Validators.required, Validators.pattern('^[a-zA-Z0-9-]+$')]],
      amount: ['', [Validators.required, Validators.pattern('^[0-9]+(\\.[0-9]{1,2})?$')]],
      cardNumber: ['', [Validators.required, Validators.pattern('^[0-9]{16}$')]],
      cvv: ['', [Validators.required, Validators.pattern('^[0-9]{3}$')]],
    });

  }
  

  onSubmit() {
    if (this.paymentForm.valid) {
      const paymentDetails = this.paymentForm.value;

      this.http.post('http://localhost:8080/api/orders/payment', paymentDetails,{withCredentials:true}).subscribe(
        response => {
          console.log('Payment Response:', response);
          alert('Payment submitted successfully!');
        },
        error => {
          console.error('Payment Error:', error);
          alert('Failed to submit payment. Please try again.');
        }
      );
    } else {
      alert('Please fill all required fields correctly.');
    }
  }
}