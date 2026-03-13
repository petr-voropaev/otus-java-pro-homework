package ru.otus;

import ru.otus.runner.TestRunner;

public class Main {

    public static void main(String[] args) {
        new TestRunner().runTests("ru.otus.tests.ExampleTest", "ru.otus.tests.ExampleTest2");
    }
}
