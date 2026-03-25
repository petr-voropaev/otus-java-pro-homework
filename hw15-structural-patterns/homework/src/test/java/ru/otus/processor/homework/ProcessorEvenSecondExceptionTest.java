package ru.otus.processor.homework;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import ru.otus.listener.homework.DateTimeProvider;

class ProcessorEvenSecondExceptionTest {

    @Test
    void process_ShouldThrowException() {
        DateTimeProvider timeProvider = Mockito.mock(DateTimeProvider.class);
        when(timeProvider.get()).thenReturn(LocalDateTime.of(2026, 1, 1, 12, 0, 4)); // чётная секунда

        ProcessorEvenSecondException processor = new ProcessorEvenSecondException(timeProvider);

        assertThrows(RuntimeException.class, () -> processor.process(null), "Even second!");
    }

    @Test
    void process_ShouldNotThrowException() {
        DateTimeProvider timeProvider = Mockito.mock(DateTimeProvider.class);
        when(timeProvider.get()).thenReturn(LocalDateTime.of(2026, 1, 1, 12, 0, 5)); // нечётная секунда

        ProcessorEvenSecondException processor = new ProcessorEvenSecondException(timeProvider);

        assertDoesNotThrow(() -> processor.process(null));
    }
}
