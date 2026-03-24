package ru.otus;

import ru.otus.atm.ATM;
import ru.otus.atm.ATMFactory;
import ru.otus.banknote.RubleBanknote;
import ru.otus.operation.WithdrawalResult;

public class Main {

    public static void main(String[] args) {
        ATM atm = ATMFactory.createWithBanknote();
        System.out.println("Изначальный баланс: " + atm.getBalance());

        System.out.println("Снятие: 7800 руб");
        WithdrawalResult result = atm.withdraw(7800);
        System.out.println("Банкноты:" + result.getBanknotes());
        System.out.println("Баланс: " + atm.getBalance());

        System.out.println("Снятие: 10 руб");
        result = atm.withdraw(10);
        System.out.println("Ошибка: " + result.getErrorMessage());

        System.out.println("Внесение: 1000 руб в кол-ве 3 шт");
        atm.deposit(new RubleBanknote(1000), 3);
        System.out.println("Баланс: " + atm.getBalance());
    }
}
