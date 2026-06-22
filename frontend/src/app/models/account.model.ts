export class Account {
    accountId: string;
    balance: number;
    currency: string;

    constructor(accountId: string, balance: number, currency: string) {
        this.accountId = accountId;
        this.balance = balance;
        this.currency = currency;
    }
}