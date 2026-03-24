package ru.otus.banknote;

public class RubleBanknote implements Banknote {

    private final int denomination;

    public RubleBanknote(int denomination) {
        this.denomination = denomination;
    }

    @Override
    public int getDenomination() {
        return denomination;
    }

    @Override
    public String toString() {
        return "RubleBanknote{" + "denomination=" + denomination + '}';
    }
}
