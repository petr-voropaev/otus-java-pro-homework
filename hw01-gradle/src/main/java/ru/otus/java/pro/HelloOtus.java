package ru.otus.java.pro;

import static com.google.common.base.Strings.isNullOrEmpty;

@SuppressWarnings("java:S106")
public class HelloOtus {
    public static void main(String[] args) {
        String str = "Hello Otus!";
        if (!isNullOrEmpty(str)) {
            System.out.println(str);
        }
    }
}
