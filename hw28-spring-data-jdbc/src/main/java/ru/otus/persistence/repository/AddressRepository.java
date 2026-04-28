package ru.otus.persistence.repository;

import java.util.Optional;
import org.springframework.data.repository.ListCrudRepository;
import ru.otus.persistence.entity.AddressEntity;

public interface AddressRepository extends ListCrudRepository<AddressEntity, Long> {

    Optional<AddressEntity> findByClientId(Long clientId);
}
