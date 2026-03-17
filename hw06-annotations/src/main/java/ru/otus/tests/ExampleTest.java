package ru.otus.tests;

import ru.otus.annotation.After;
import ru.otus.annotation.Before;
import ru.otus.annotation.Test;

public class ExampleTest {

    @Before
    public void setUp() {
        System.out.println("Before test");
    }

    @After
    public void tearDown() {
        System.out.println("After test");
    }

    @Test
    public void testSuccess() {
        System.out.println("testSuccess");
    }

    @Test
    public void testFailure() {
        System.out.println("testFailure");
        throw new RuntimeException("Error!");
    }

    @Test
    public void testSuccess2() {
        System.out.println("testSuccess2");
    }
}
