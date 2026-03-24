package ru.otus.atm;

import ru.otus.operation.ATMService;

public class ATMFactory {

    public static ATM createWithBanknote() {
        return new ATM(new ATMService());
    }
}
