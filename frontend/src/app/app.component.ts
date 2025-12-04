import { Component } from '@angular/core';
import { FormBuilder, Validators } from '@angular/forms';
import { EmiRequest } from './model/emi-request.model';
import { EmiService } from './service/app.service';


@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {
  title = 'Rabo EMI Calculator';
  result: any = null;
  loading = false;
  serverError: string | null = null;

  emiForm = this.fb.group({
    loanValue: [100000, [Validators.required, Validators.min(0.01)]],
    yearlyInterestRate: [7.5, [Validators.required, Validators.min(0), Validators.max(100)]],
    loanTermYears: [10, [Validators.required, Validators.min(0.01), Validators.max(30)]]
  });

   constructor(private fb: FormBuilder, private emiService: EmiService) {}

   submit() {
  this.serverError = null;
  if (this.emiForm.invalid) {
    this.emiForm.markAllAsTouched();
    return;
  }

  const payload: EmiRequest = {
    loanValue: Number(this.emiForm.controls.loanValue.value!),
    yearlyInterestRate: Number(this.emiForm.controls.yearlyInterestRate.value!),
    loanTermYears: Number(this.emiForm.controls.loanTermYears.value!)
  };

  this.loading = true;
  this.emiService.calculate(payload).subscribe({
    next: (res: any) => {
      this.result = res;
      this.loading = false;
    },
    error: (err: any) => {
      this.loading = false;
      if (err?.error?.details) {
        const details = err.error.details;
        this.serverError = Object.values(details).join('; ');
      } else if (err?.error?.message) {
        this.serverError = err.error.message;
      } else {
        this.serverError = 'Unexpected server error. Please try again later.';
      }
    }
  });
}

  clear() {
    this.result = null;
    this.serverError = null;
    this.emiForm.reset({
      loanValue: 0,
      yearlyInterestRate: 0,
      loanTermYears: 0
    });
  }
}
