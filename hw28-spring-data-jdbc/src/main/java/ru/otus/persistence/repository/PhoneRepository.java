package ru.otus.persistence.repository;

import java.util.List;
import org.springframework.data.repository.ListCrudRepository;
import ru.otus.persistence.entity.PhoneEntity;

public interface PhoneRepository extends ListCrudRepository<PhoneEntity, Long> {

    List<PhoneEntity> findAllByClientIdOrderByIdAsc(Long clientId);
}
