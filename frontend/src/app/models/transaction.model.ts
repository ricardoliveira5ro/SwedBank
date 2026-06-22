export class Transaction {
    transactionId: string;
    amount: number;
    balanceAfter: number;
    type: string;
    date: Date;

    constructor(transactionId: string, amount: number, balanceAfter: number, type: string, date: Date) {
        this.transactionId = transactionId;
        this.amount = amount;
        this.balanceAfter = balanceAfter;
        this.type = type;
        this.date = date;
    }
}