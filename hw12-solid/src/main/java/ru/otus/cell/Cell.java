package ru.otus.cell;

import ru.otus.banknote.Banknote;

public interface Cell {

    Banknote getBanknote();

    int getCount();

    void add(int count);

    void remove(int count);

    int getAvailableAmount();
}
