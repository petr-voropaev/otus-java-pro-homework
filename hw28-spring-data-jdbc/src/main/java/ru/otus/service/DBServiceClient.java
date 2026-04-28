package ru.otus.service;

import java.util.List;
import ru.otus.model.Client;

public interface DBServiceClient {

    Client saveClient(Client client);

    List<Client> findAll();
}
