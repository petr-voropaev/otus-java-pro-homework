package ru.otus.processor.homework;

import ru.otus.listener.homework.DateTimeProvider;
import ru.otus.model.Message;
import ru.otus.processor.Processor;

public class ProcessorEvenSecondException implements Processor {

    private final DateTimeProvider dateTimeProvider;

    public ProcessorEvenSecondException(DateTimeProvider dateTimeProvider) {
        this.dateTimeProvider = dateTimeProvider;
    }

    @Override
    public Message process(Message message) {
        int second = dateTimeProvider.get().getSecond();

        if (second % 2 == 0) {
            throw new RuntimeException("Even second!");
        }

        return message;
    }
}
