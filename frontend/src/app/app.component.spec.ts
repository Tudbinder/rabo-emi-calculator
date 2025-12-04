import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ReactiveFormsModule } from '@angular/forms';
import { of, throwError } from 'rxjs';
import { AppComponent } from './app.component';
import { EmiService } from './service/app.service';
import { EmiRequest } from './model/emi-request.model';
import { EmiResponse } from './model/emi-response.model';

class MockEmiService {
  calculate(request: EmiRequest) {
    return of({
      emi: 1000,
      totalPayment: 12000,
      totalInterest: 2000,
      monthlyRate: 0.01,
      tenureMonths: 12
    } as EmiResponse);
  }
}

describe('AppComponent', () => {
  let component: AppComponent;
  let fixture: ComponentFixture<AppComponent>;
  let emiService: EmiService;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [AppComponent],
      imports: [ReactiveFormsModule],
      providers: [{ provide: EmiService, useClass: MockEmiService }]
    }).compileComponents();

    fixture = TestBed.createComponent(AppComponent);
    component = fixture.componentInstance;
    emiService = TestBed.inject(EmiService);
    fixture.detectChanges();
  });

  it('should create the app', () => {
    expect(component).toBeTruthy();
  });

  it('should have default form values', () => {
    expect(component.emiForm.value).toEqual({
      loanValue: 100000,
      yearlyInterestRate: 7.5,
      loanTermYears: 10
    });
  });

  it('should call EmiService.calculate and set result on submit', () => {
    spyOn(emiService, 'calculate').and.callThrough();
    component.emiForm.setValue({ loanValue: 12000, yearlyInterestRate: 10, loanTermYears: 1 });
    component.submit();
    expect(emiService.calculate).toHaveBeenCalled();
    expect(component.result).toEqual({
      emi: 1000,
      totalPayment: 12000,
      totalInterest: 2000,
      monthlyRate: 0.01,
      tenureMonths: 12
    });
    expect(component.loading).toBe(false);
  });

  it('should set serverError on error response', () => {
    spyOn(emiService, 'calculate').and.returnValue(throwError({ error: { message: 'Server error' } }));
    component.emiForm.setValue({ loanValue: 12000, yearlyInterestRate: 10, loanTermYears: 1 });
    component.submit();
    expect(component.serverError).toBe('Server error');
    expect(component.loading).toBe(false);
  });

  it('should mark form as touched and not submit if invalid', () => {
    component.emiForm.setValue({ loanValue: null, yearlyInterestRate: null, loanTermYears: null });
    component.submit();
    expect(component.emiForm.touched).toBe(true);
    expect(component.result).toBeNull();
  });

  it('should clear result and serverError on clear()', () => {
    component.result = { emi: 1000 };
    component.serverError = 'Some error';
    component.clear();
    expect(component.result).toBeNull();
    expect(component.serverError).toBeNull();
    expect(component.emiForm.value).toEqual({ loanValue: 0, yearlyInterestRate: 0, loanTermYears: 0 });
  });
});
