package ru.otus.cell;

import ru.otus.banknote.Banknote;

public class BanknoteCell implements Cell {

    private final Banknote banknote;
    private int count;

    public BanknoteCell(Banknote banknote, int count) {
        this.banknote = banknote;
        this.count = count;
    }

    @Override
    public Banknote getBanknote() {
        return banknote;
    }

    @Override
    public int getCount() {
        return count;
    }

    @Override
    public void add(int count) {
        this.count += count;
    }

    @Override
    public void remove(int count) {
        this.count -= count;
    }

    @Override
    public int getAvailableAmount() {
        return banknote.getDenomination() * count;
    }
}
