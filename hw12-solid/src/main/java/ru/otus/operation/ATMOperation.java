package ru.otus.operation;

import ru.otus.banknote.Banknote;

public interface ATMOperation {

    int getBalance();

    void deposit(Banknote banknote, int count);

    WithdrawalResult withdraw(int amount);
}
