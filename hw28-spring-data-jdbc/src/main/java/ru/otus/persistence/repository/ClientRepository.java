package ru.otus.persistence.repository;

import java.util.List;
import org.springframework.data.repository.ListCrudRepository;
import ru.otus.persistence.entity.ClientEntity;

public interface ClientRepository extends ListCrudRepository<ClientEntity, Long> {

    List<ClientEntity> findAllByOrderByIdAsc();
}
