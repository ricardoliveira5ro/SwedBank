import { AfterViewInit, Component, ElementRef, inject, OnDestroy, ViewChild } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Account } from '../models/account.model';
import { HttpClient } from '@angular/common/http';
import { Transaction } from '../models/transaction.model';
import { Chart, LineController, CategoryScale, LinearScale, PointElement, LineElement } from 'chart.js';

Chart.register(LineController, CategoryScale, LinearScale, PointElement, LineElement);

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
  private chartInstance: Chart | null = null;

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
          this.updateChart();
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
    this.chartInstance?.destroy();
  }

  private updateChart() {
    const ctx = document.getElementById('myChart') as HTMLCanvasElement;
    if (!ctx || this.transactions.length === 0) return;

    this.chartInstance?.destroy();

    const sorted = [...this.transactions].sort(
      (a, b) => new Date(a.date).getTime() - new Date(b.date).getTime()
    );

    this.chartInstance = new Chart(ctx, {
      type: 'line',
      data: {
        labels: sorted.map(t => new Date(t.date).toLocaleString('en-US', { month: 'short', day: 'numeric', hour: 'numeric', minute: '2-digit', second: '2-digit' })),
        datasets: [{
          label: 'Balance',
          data: sorted.map(t => t.balanceAfter),
          borderColor: '#3b82f6',
          backgroundColor: 'rgba(59, 130, 246, 0.1)',
          fill: false,
          tension: 0.1
        }]
      },
      options: {
        responsive: true,
        scales: {
          x: { title: { display: true, text: 'Date' } },
          y: { title: { display: true, text: 'Balance' }, beginAtZero: false }
        }
      }
    });
  }

  formatDate(date: Date): string {
    return new Date(date).toLocaleString('en-US', {
      month: 'short', day: 'numeric',
      hour: 'numeric', minute: '2-digit', second: '2-digit'
    });
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
          this.updateChart();
        },
        error: (err) => {
          console.log(err);
          this.isLoading = false;
        }
      });
  }
}
