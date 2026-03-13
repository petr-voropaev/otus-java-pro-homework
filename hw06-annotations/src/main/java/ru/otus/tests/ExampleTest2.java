package ru.otus.tests;

import ru.otus.annotation.After;
import ru.otus.annotation.Before;
import ru.otus.annotation.Test;

public class ExampleTest2 {

    @Before
    public void setUp1() {
        System.out.println("Before test");
    }

    @Before
    public void setUp2() {
        System.out.println("Before2 test");
    }

    @After
    public void tearDown1() {
        System.out.println("After test");
    }

    @After
    public void tearDown2() {
        System.out.println("After2 test");
    }

    @Test
    public void test1() {
        System.out.println("test1");
    }

    @Test
    public void test2() {
        System.out.println("test2");
    }
}
