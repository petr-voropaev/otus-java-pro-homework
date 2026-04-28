package ru.petrelevich.service;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.petrelevich.domain.Message;

public interface DataStore {
    String ROOM_1408_ID = "1408";

    Mono<Message> saveMessage(Message message);

    Flux<Message> loadMessages(String roomId);

    Flux<Message> loadMessagesNot1408();
}
