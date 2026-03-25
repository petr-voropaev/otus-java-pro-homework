package ru.otus.listener.homework;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import ru.otus.listener.Listener;
import ru.otus.model.Message;
import ru.otus.model.ObjectForMessage;

public class HistoryListener implements Listener, HistoryReader {

    private final Map<Long, Message> messageHistory = new HashMap<>();

    @Override
    public void onUpdated(Message msg) {
        if (msg != null) {
            ObjectForMessage field13 = new ObjectForMessage();
            field13.setData(msg.getField13().getData().stream().toList());
            Message message = msg.toBuilder().field13(field13).build();
            messageHistory.put(msg.getId(), message);
        }
    }

    @Override
    public Optional<Message> findMessageById(long id) {
        return Optional.ofNullable(messageHistory.get(id));
    }
}
