package ru.otus.java.pro.hw03.generics.box;

import ru.otus.java.pro.hw03.generics.fruit.Fruit;

import java.util.ArrayList;
import java.util.List;

public class Box<T extends Fruit> {
    private final List<T> fruits = new ArrayList<>();

    public void add(T fruit) {
        if (fruit == null) {
            return;
        }

        fruits.add(fruit);
    }

    public int weight() {
        int boxWeight = 0;

        for (T fruit : fruits) {
            boxWeight += fruit.getWeight();
        }

        return boxWeight;
    }

    public boolean compare(Box<?> another) {
        if (another == null) {
            return false;
        }

        return Math.abs(this.weight() - another.weight()) < 0.0001;
    }

    public void transfer(Box<? super T> another) {
        if (this.equals(another)) {
            return;
        }

        another.fruits.addAll(this.fruits);
        this.fruits.clear();
    }

    @Override
    public String toString() {
        return "Box{" +
                "fruits=" + fruits +
                '}';
    }
}
