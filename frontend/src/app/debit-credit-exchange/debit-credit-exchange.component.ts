import { Component, EventEmitter, inject, Input, Output} from '@angular/core';
import { Account } from '../models/account.model';
import { HttpClient } from '@angular/common/http';

@Component({
  selector: 'app-debit-credit-exchange',
  standalone: true,
  imports: [],
  templateUrl: './debit-credit-exchange.component.html',
  styleUrl: './debit-credit-exchange.component.css'
})
export class DebitCreditExchangeComponent {
  @Input({ required: true }) accounts: Account[] = [];

  isDebit = true;
  sourceAccountId = '';
  httpClient = inject(HttpClient);

  @Output() submitted = new EventEmitter<void>();

  get filteredTargets(): Account[] {
    return this.accounts.filter(a => a.accountId !== this.sourceAccountId);
  }

  onDebitOrCredit(accountSelect: HTMLSelectElement, amountInput: HTMLInputElement) {
    this.httpClient.post(`http://localhost:8080/api/transactions/${accountSelect.value}/${this.isDebit ? 'debit' : 'credit'}`, { amount: amountInput.value })
      .subscribe({
        next: (data: any) => {
          console.log(data);
          accountSelect.value = '';
          amountInput.value = '';
          this.isDebit = true;
          this.submitted.emit();
        }, error: (err) => console.log(err)
      });
  }

  onExchange(sourceSelect: HTMLSelectElement, targetSelect: HTMLSelectElement, amountInput: HTMLInputElement) {
    this.httpClient.post(`http://localhost:8080/api/transactions/exchange`, {
      sourceAccount: sourceSelect.value,
      targetAccount: targetSelect.value,
      amount: amountInput.value
    }).subscribe({
      next: (data: any) => {
        console.log(data);
        sourceSelect.value = '';
        targetSelect.value = '';
        amountInput.value = '';
        this.sourceAccountId = '';
        this.submitted.emit();
      }, error: (err) => console.log(err)
    });
  }
}
