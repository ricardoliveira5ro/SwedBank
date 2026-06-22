import { AfterViewInit, Component, ElementRef, inject, OnDestroy, ViewChild } from '@angular/core';
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
export class AccountComponent implements AfterViewInit, OnDestroy {

  httpClient = inject(HttpClient);
  public account?: Account;
  public transactions: Array<Transaction> = [];
  public isLoading: boolean = false;

  @ViewChild('sentinel') sentinel!: ElementRef<HTMLDivElement>;
  @ViewChild('scrollContainer') scrollContainer!: ElementRef<HTMLDivElement>;

  private observer: IntersectionObserver | null = null;
  private page: number = 0;
  private hasMorePages: boolean = true;

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
          this.page = data.page;
          this.hasMorePages = data.page + 1 < data.totalPages;
        }, error: (err) => console.log(err)
      });
  }

  ngAfterViewInit() {
    this.observer = new IntersectionObserver(entries => {
      if (entries[0].isIntersecting && this.hasMorePages && !this.isLoading) {
        this.loadMoreTransactions();
      }
    }, { root: this.scrollContainer.nativeElement });
    this.observer.observe(this.sentinel.nativeElement);
  }

  ngOnDestroy() {
    this.observer?.disconnect();
  }

  loadMoreTransactions() {
    if (this.isLoading || !this.hasMorePages) return;

    const accountId = this.route.snapshot.paramMap.get('accountId');
    this.isLoading = true;
    this.page++;

    this.httpClient.get('http://localhost:8080/api/transactions/' + accountId + '/history?page=' + this.page + '&size=' + 5)
      .subscribe({
        next: (data: any) => {
          console.log(data);
          this.transactions.push(...data.content);
          this.page = data.page;
          this.hasMorePages = data.page + 1 < data.totalPages;
          this.isLoading = false;
        },
        error: (err) => {
          console.log(err);
          this.isLoading = false;
        }
      });
  }
}
