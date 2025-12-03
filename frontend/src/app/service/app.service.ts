import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface EmiRequest {
  loanValue: number;
  yearlyInterestRate: number;
  loanTermYears: number;
}

export interface EmiResponse {
  emi: number;
  totalPayment: number;
  totalInterest: number;
  monthlyRate: number;
  tenureMonths: number;
}

@Injectable({ providedIn: 'root' })
export class EmiService {
  // Change this if backend is on different host/port
  private apiUrl = 'http://localhost:8080/api/emi';

  constructor(private http: HttpClient) {}

  calculate(request: EmiRequest): Observable<EmiResponse> {
    return this.http.post<EmiResponse>(this.apiUrl, request);
  }
}