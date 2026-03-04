package ru.otus.service;

import io.grpc.stub.StreamObserver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NumberStreamerImpl extends NumberStreamerServiceGrpc.NumberStreamerServiceImplBase {

    private static final Logger logger = LoggerFactory.getLogger(NumberStreamerImpl.class);

    @Override
    public void getNumberSequence(NumberSequenceRequest request, StreamObserver<NumberResponse> responseObserver) {
        logger.info(
                "Получен запрос [firstValue = {}, lastValue = {}]", request.getFirstValue(), request.getLastValue());

        for (int i = request.getFirstValue() + 1; i <= request.getLastValue(); i++) {
            NumberResponse response = NumberResponse.newBuilder().setValue(i).build();

            responseObserver.onNext(response);
            logger.info("Отправлен ответ [value = {}]", response.getValue());

            sleep();
        }

        responseObserver.onCompleted();
        logger.info("Завершена отправка ответов");
    }

    private static void sleep() {
        try {
            Thread.sleep(2_000);
        } catch (InterruptedException e) {
            logger.error(e.getMessage());
            Thread.currentThread().interrupt();
        }
    }
}
