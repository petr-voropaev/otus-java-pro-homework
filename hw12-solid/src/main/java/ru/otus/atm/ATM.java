package ru.otus.atm;

import ru.otus.banknote.Banknote;
import ru.otus.operation.ATMOperation;
import ru.otus.operation.Operation;
import ru.otus.operation.WithdrawalResult;

public class ATM {
    private final Operation operation;

    public ATM() {
        this.operation = new ATMOperation();
    }

    public void deposit(Banknote banknote, int count) {
        operation.deposit(banknote, count);
    }

    public WithdrawalResult withdraw(int amount) {
        return operation.withdraw(amount);
    }

    public int getBalance() {
        return operation.getBalance();
    }
}
