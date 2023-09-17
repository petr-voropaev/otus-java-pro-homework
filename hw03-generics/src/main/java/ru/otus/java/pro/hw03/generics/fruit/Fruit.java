package ru.otus.java.pro.hw03.generics.fruit;

import java.util.Objects;

public abstract class Fruit {
    private final int weight;

    public Fruit(int weight) {
        this.weight = weight;
    }

    public int getWeight() {
        return weight;
    }

    @Override
    public String toString() {
        return "Fruit{" +
                "type=" + this.getClass().getSimpleName() +
                ", weight=" + weight +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Fruit fruit = (Fruit) o;
        return weight == fruit.weight;
    }

    @Override
    public int hashCode() {
        return Objects.hash(weight);
    }
}
