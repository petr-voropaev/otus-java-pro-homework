package ru.otus;

import ru.otus.proxy.IocLogging;

public class Demo {

    public static void main(String[] args) {
        new Demo().action();
    }

    public void action() {
        TestLoggingInterface proxy = IocLogging.createProxy(new TestLogging());
        proxy.calculation(6);
        proxy.calculation(1, 2);
        proxy.calculation(1, 2, "test");
    }
}
