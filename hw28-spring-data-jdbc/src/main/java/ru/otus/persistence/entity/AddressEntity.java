package ru.otus.persistence.entity;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceCreator;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Getter
@Setter
@Table("address")
public class AddressEntity {

    @Id
    @Column("id")
    private Long id;

    @Column("street")
    private String street;

    @Column("client_id")
    private Long clientId;

    public AddressEntity(String address, Long clientId) {
        this(null, address, clientId);
    }

    @PersistenceCreator
    public AddressEntity(Long id, String street, Long clientId) {
        this.id = id;
        this.street = street;
        this.clientId = clientId;
    }
}
