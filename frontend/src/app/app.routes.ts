import { Routes } from '@angular/router';
import { HomeComponent } from './home/home.component';
import { AccountComponent } from './account/account.component';
import { TransactionComponent } from './transaction/transaction.component';

export const routes: Routes = [
    {
        path: '',
        component: HomeComponent
    },
    {
        path: 'account/:accountId',
        component: AccountComponent
    },
    {
        path: 'transaction/:transactionId',
        component: TransactionComponent
    }
];
