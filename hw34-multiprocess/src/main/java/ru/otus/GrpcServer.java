package ru.otus;

import io.grpc.Server;
import io.grpc.ServerBuilder;
import java.io.IOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.service.NumberStreamerImpl;

public class GrpcServer {

    private static final Logger logger = LoggerFactory.getLogger(GrpcServer.class);
    private static final int PORT = 8190;

    private Server server;

    public static void main(String[] args) throws IOException, InterruptedException {
        GrpcServer server = new GrpcServer();
        server.start();
        server.awaitTermination();
    }

    private void start() throws IOException {
        server = ServerBuilder.forPort(PORT)
                .addService(new NumberStreamerImpl())
                .build()
                .start();

        logger.info("Сервер запущен на порту " + PORT);
    }

    private void awaitTermination() throws InterruptedException {
        if (server != null) {
            server.awaitTermination();
        }
    }
}
