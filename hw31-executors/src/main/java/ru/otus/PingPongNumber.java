package ru.otus;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PingPongNumber {
    private static final Logger logger = LoggerFactory.getLogger(PingPongNumber.class);
    private volatile String last = "Поток 2";
    private int number = 1;
    private boolean increasing = true;

    public static void main(String[] args) {
        PingPongNumber pingPongNumber = new PingPongNumber();
        new Thread(() -> pingPongNumber.action("Поток 1")).start();
        new Thread(() -> pingPongNumber.action("Поток 2")).start();
    }

    private synchronized void action(String threadName) {
        while (!Thread.currentThread().isInterrupted()) {
            try {
                while (last.equals(threadName)) {
                    wait();
                }

                printNumber(threadName);
                sleep();
            } catch (InterruptedException ex) {
                Thread.currentThread().interrupt();
            }
        }
    }

    private void printNumber(String threadName) {
        logger.info("{}: {}", threadName, number);

        if ("Поток 2".equals(threadName)) {
            if (increasing) {
                number++;
                if (number == 10) {
                    increasing = false;
                }
            } else {
                number--;
                if (number == 1) {
                    increasing = true;
                }
            }
        }

        last = threadName;
        notifyAll();
    }

    private static void sleep() {
        try {
            Thread.sleep(1_000);
        } catch (InterruptedException e) {
            logger.error(e.getMessage());
            Thread.currentThread().interrupt();
        }
    }
}
