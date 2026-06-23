import { Component, inject } from '@angular/core';
import { Transaction } from '../models/transaction.model';
import { HttpClient } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';

@Component({
  selector: 'app-transaction',
  standalone: true,
  imports: [],
  templateUrl: './transaction.component.html',
  styleUrl: './transaction.component.css'
})
export class TransactionComponent {
  
  httpClient = inject(HttpClient);
  public transaction?: Transaction;

  constructor(private route: ActivatedRoute) {}

  ngOnInit() {
    const transactionId = this.route.snapshot.paramMap.get('transactionId');

    this.httpClient.get('http://localhost:8080/api/transactions/' + transactionId)
      .subscribe({
        next: (data: any) => {
          console.log(data);
          this.transaction = data;
        }, error: (err) => console.log(err)
      });
  }

  formatDate(date: Date | undefined): string {
    return new Date(date || Date.now()).toLocaleString('en-US', {
      month: 'short', day: 'numeric',
      hour: 'numeric', minute: '2-digit', second: '2-digit'
    });
  }
}
