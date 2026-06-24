import { HttpClient } from '@angular/common/http';
import { Component, inject } from '@angular/core';
import { RouterLink } from '@angular/router';
import { Account } from '../models/account.model';
import { DebitCreditExchangeComponent } from "../debit-credit-exchange/debit-credit-exchange.component";
@Component({
  selector: 'app-home',
  standalone: true,
  imports: [RouterLink, DebitCreditExchangeComponent],
  templateUrl: './home.component.html',
  styleUrl: './home.component.css'
})
export class HomeComponent {

  httpClient = inject(HttpClient);
  public accounts: Array<Account> = [];

  ngOnInit() {
    this.fetchAccounts();
  }

  fetchAccounts() {
    this.httpClient.get('http://localhost:8080/api/accounts/all/a1b2c3d4-e5f6-7890-abcd-ef1234567890')
      .subscribe({
        next: (data: any) => {
          console.log(data);
          this.accounts = data;
        }, error: (err) => console.log(err)
      });
  }
}
