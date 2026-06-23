import { Component, inject } from '@angular/core';
import { Transaction } from '../models/transaction.model';
import { HttpClient } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import jsPDF from 'jspdf';
import autoTable from 'jspdf-autotable';

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

  exportTransaction(transaction: Transaction | undefined) {
    if (!transaction) return;

    const doc = new jsPDF();
    doc.setFontSize(16);
    doc.text("Transaction Overview", 14, 15);
    doc.setFontSize(12);
    const headers = [["Amount", "Type", "Date", "Balance"]];
    const data = [[transaction.amount,  transaction.type, this.formatDate(transaction.date), transaction.balanceAfter]];
    autoTable(doc, {
      head: headers,
      body: data,
      startY: 20,
    });
    doc.save(transaction.transactionId + ".pdf");
  }
}
