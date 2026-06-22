import { Component, inject } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Account } from '../models/account.model';
import { HttpClient } from '@angular/common/http';
import { Transaction } from '../models/transaction.model';

@Component({
  selector: 'app-account',
  standalone: true,
  imports: [],
  templateUrl: './account.component.html',
  styleUrl: './account.component.css'
})
export class AccountComponent {

  httpClient = inject(HttpClient);
  public account?: Account;
  public transactions: Array<Transaction> = [];
  public page: number = 0;

  constructor(private route: ActivatedRoute) {}

  ngOnInit() {
    const accountId = this.route.snapshot.paramMap.get('accountId');

    this.httpClient.get('http://localhost:8080/api/accounts/' + accountId)
      .subscribe({
        next: (data: any) => {
          console.log(data);
          this.account = data;
        }, error: (err) => console.log(err)
      });

    this.httpClient.get('http://localhost:8080/api/transactions/' + accountId + '/history')
      .subscribe({
        next: (data: any) => {
          console.log(data);
          this.transactions = data.content;
        }, error: (err) => console.log(err)
      });
  }

  loadMoreTransactions() {
    const accountId = this.route.snapshot.paramMap.get('accountId');
    this.page++;

    this.httpClient.get('http://localhost:8080/api/transactions/' + accountId + '/history?page=' + this.page)
      .subscribe({
        next: (data: any) => {
          console.log(data);
          this.transactions.push.apply(this.transactions, data.content);
        }, error: (err) => console.log(err)
      });
  }
}
