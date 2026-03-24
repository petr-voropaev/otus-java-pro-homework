package ru.otus.operation;

import java.util.Map;
import ru.otus.banknote.Banknote;

public class WithdrawalResult {

    private final Map<Banknote, Integer> banknotes;
    private final String errorMessage;

    private WithdrawalResult(Map<Banknote, Integer> banknotes, String errorMessage) {
        this.banknotes = banknotes;
        this.errorMessage = errorMessage;
    }

    public static WithdrawalResult success(Map<Banknote, Integer> banknotes) {
        return new WithdrawalResult(banknotes, null);
    }

    public static WithdrawalResult failure(String errorMessage) {
        return new WithdrawalResult(null, errorMessage);
    }

    public Map<Banknote, Integer> getBanknotes() {
        return banknotes;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}
