package ru.otus.listener.homework;

import java.time.LocalDateTime;

@FunctionalInterface
public interface DateTimeProvider {

    LocalDateTime get();
}
