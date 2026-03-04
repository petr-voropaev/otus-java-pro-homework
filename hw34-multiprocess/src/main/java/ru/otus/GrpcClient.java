package ru.otus;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.service.NumberResponse;
import ru.otus.service.NumberSequenceRequest;
import ru.otus.service.NumberStreamerServiceGrpc;

public class GrpcClient {

    private static final Logger logger = LoggerFactory.getLogger(GrpcClient.class);
    private static final String SERVER_HOST = "localhost";
    private static final int SERVER_PORT = 8190;

    private final ManagedChannel channel;
    private final NumberStreamerServiceGrpc.NumberStreamerServiceStub asyncStub;

    private int currentValue = 0;
    private final AtomicInteger serverValueLast = new AtomicInteger(0);
    private final AtomicBoolean serverValueUsed = new AtomicBoolean(false);

    public static void main(String[] args) throws InterruptedException {
        GrpcClient client = new GrpcClient(SERVER_HOST, SERVER_PORT);

        client.requestNumberSequence(0, 30);
        client.updateCurrentValue();

        client.shutdown();
    }

    public GrpcClient(String host, int port) {
        this.channel =
                ManagedChannelBuilder.forAddress(host, port).usePlaintext().build();
        this.asyncStub = NumberStreamerServiceGrpc.newStub(channel);
    }

    private void shutdown() {
        channel.shutdown();
    }

    public void requestNumberSequence(int firstValue, int lastValue) {
        NumberSequenceRequest request = NumberSequenceRequest.newBuilder()
                .setFirstValue(firstValue)
                .setLastValue(lastValue)
                .build();

        logger.info("Отправка запроса на сервер [firstValue = {}, lastValue = {}]", firstValue, lastValue);

        asyncStub.getNumberSequence(request, new StreamObserver<>() {

            @Override
            public void onNext(NumberResponse response) {
                logger.info("serverValue: {}", response.getValue());
                serverValueLast.set(response.getValue());
                serverValueUsed.set(false);
            }

            @Override
            public void onError(Throwable t) {
                logger.error("Ошибка при получении ответа от сервера: {}", t.getMessage());
            }

            @Override
            public void onCompleted() {
                logger.info("Сервер завершил отправку ответов");
            }
        });
    }

    public void updateCurrentValue() throws InterruptedException {
        for (int i = 0; i <= 50; i++) {

            if (serverValueUsed.get()) {
                currentValue = currentValue + 1;
            } else {
                currentValue = currentValue + serverValueLast.get() + 1;
                serverValueUsed.set(true);
            }

            logger.info("currentValue: {}", currentValue);
            Thread.sleep(1_000);
        }
    }
}
