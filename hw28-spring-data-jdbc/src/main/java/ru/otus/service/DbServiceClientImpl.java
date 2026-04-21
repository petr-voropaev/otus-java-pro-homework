package ru.otus.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.model.Address;
import ru.otus.model.Client;
import ru.otus.model.Phone;
import ru.otus.persistence.entity.AddressEntity;
import ru.otus.persistence.entity.ClientEntity;
import ru.otus.persistence.entity.PhoneEntity;
import ru.otus.persistence.repository.AddressRepository;
import ru.otus.persistence.repository.ClientRepository;
import ru.otus.persistence.repository.PhoneRepository;

@Service
@RequiredArgsConstructor
public class DbServiceClientImpl implements DBServiceClient {

    private static final Logger log = LoggerFactory.getLogger(DbServiceClientImpl.class);

    private final ClientRepository clientRepository;
    private final AddressRepository addressRepository;
    private final PhoneRepository phoneRepository;

    @Override
    @Transactional
    public Client saveClient(Client client) {
        var addressEntity = new AddressEntity(client.getAddress().getStreet(), client.getId());
        var phoneEntities = client.getPhones() == null
                ? List.<PhoneEntity>of()
                : client.getPhones().stream()
                        .map(phone -> new PhoneEntity(phone.getNumber().trim(), client.getId()))
                        .toList();
        var clientEntity = new ClientEntity(client.getName().trim(), addressEntity, phoneEntities);
        var savedClientEntity = clientRepository.save(clientEntity);
        var savedClientModel = mapClient(savedClientEntity);
        log.info("Saved client: {}", savedClientModel);

        return savedClientModel;
    }

    @Override
    @Transactional
    public List<Client> findAll() {
        var clients = clientRepository.findAllByOrderByIdAsc();

        if (clients.isEmpty()) {
            return List.of();
        }

        var clientList = clients.stream().map(this::mapClient).toList();
        log.info("Clients: {}", clientList);

        return clientList;
    }

    private Client mapClient(ClientEntity clientEntity) {
        var addressEntity =
                addressRepository.findByClientId(clientEntity.getId()).orElse(null);
        var phoneEntities = phoneRepository.findAllByClientIdOrderByIdAsc(clientEntity.getId());
        var address = addressEntity == null ? null : new Address(addressEntity.getId(), addressEntity.getStreet());
        var phones = phoneEntities == null
                ? List.<Phone>of()
                : phoneEntities.stream()
                        .map(phoneEntity -> new Phone(phoneEntity.getId(), phoneEntity.getNumber()))
                        .toList();

        return new Client(clientEntity.getId(), clientEntity.getName(), address, phones);
    }
}
