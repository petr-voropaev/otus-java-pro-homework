package ru.otus.atm;

import ru.otus.banknote.Banknote;
import ru.otus.operation.ATMOperation;
import ru.otus.operation.WithdrawalResult;

public class ATM {

    private final ATMOperation ATMOperation;

    public ATM(ATMOperation ATMOperation) {
        this.ATMOperation = ATMOperation;
    }

    public void deposit(Banknote banknote, int count) {
        ATMOperation.deposit(banknote, count);
    }

    public WithdrawalResult withdraw(int amount) {
        return ATMOperation.withdraw(amount);
    }

    public int getBalance() {
        return ATMOperation.getBalance();
    }
}
