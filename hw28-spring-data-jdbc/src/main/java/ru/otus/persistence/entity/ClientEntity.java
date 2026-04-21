package ru.otus.persistence.entity;

import java.util.List;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceCreator;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.MappedCollection;
import org.springframework.data.relational.core.mapping.Table;

@Getter
@Setter
@Table("client")
public class ClientEntity {

    @Id
    @Column("id")
    private Long id;

    @Column("name")
    private String name;

    @MappedCollection(idColumn = "client_id")
    private AddressEntity address;

    @MappedCollection(idColumn = "client_id", keyColumn = "order_column")
    private List<PhoneEntity> phones;

    public ClientEntity(String name, AddressEntity address, List<PhoneEntity> phones) {
        this(null, name, address, phones);
    }

    @PersistenceCreator
    public ClientEntity(Long id, String name, AddressEntity address, List<PhoneEntity> phones) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.phones = phones;
    }
}
