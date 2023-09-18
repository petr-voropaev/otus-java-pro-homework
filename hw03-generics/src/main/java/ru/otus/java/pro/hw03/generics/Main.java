package ru.otus.java.pro.hw03.generics;

import ru.otus.java.pro.hw03.generics.box.Box;
import ru.otus.java.pro.hw03.generics.fruit.Apple;
import ru.otus.java.pro.hw03.generics.fruit.Fruit;
import ru.otus.java.pro.hw03.generics.fruit.Orange;

public class Main {
    public static void main(String[] args) {
        Apple apple = new Apple();
        Box<Apple> boxApple = new Box<>();
        boxApple.add(apple);
        boxApple.add(apple);

        Orange orange = new Orange();
        Box<Orange> boxOrange = new Box<>();
        boxOrange.add(orange);

        Box<Fruit> boxFruit = new Box<>();
        boxFruit.add(apple);
        boxFruit.add(orange);

        System.out.println("Comparison result " + boxApple + " and " + boxOrange + ": " + boxApple.compare(boxOrange));
        System.out.println("Comparison result " + boxApple + " and " + boxFruit + ": " + boxApple.compare(boxFruit));
        System.out.println();

        Box<Apple> boxAppleAnother = new Box<>();
        boxAppleAnother.add(apple);
        System.out.println("boxApple: " + boxApple);
        System.out.println("boxAppleAnother: " + boxAppleAnother);

        boxApple.transfer(boxAppleAnother);
        System.out.println("Transfer result boxApple -> boxAppleAnother: " + boxApple + ", " + boxAppleAnother);
        System.out.println();

        System.out.println("boxOrange: " + boxOrange);
        System.out.println("boxFruit: " + boxFruit);
        boxOrange.transfer(boxFruit);
        System.out.println("Transfer result boxOrange -> boxFruit: " + boxOrange + ", " + boxFruit);
    }
}