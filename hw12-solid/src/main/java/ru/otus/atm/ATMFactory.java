package ru.otus.atm;

import ru.otus.banknote.RubleBanknote;

public class ATMFactory {

    public static ATM createWithBanknote() {
        ATM atm = new ATM();

        atm.deposit(new RubleBanknote(5000), 10);
        atm.deposit(new RubleBanknote(1000), 20);
        atm.deposit(new RubleBanknote(500), 30);
        atm.deposit(new RubleBanknote(100), 50);

        return atm;
    }
}
