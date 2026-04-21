package ru.otus.persistence.entity;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceCreator;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Getter
@Setter
@Table("phone")
public class PhoneEntity {

    @Id
    @Column("id")
    private Long id;

    @Column("number")
    private String number;

    @Column("client_id")
    private Long clientId;

    public PhoneEntity(String number, Long clientId) {
        this(null, number, clientId);
    }

    @PersistenceCreator
    public PhoneEntity(Long id, String number, Long clientId) {
        this.id = id;
        this.number = number;
        this.clientId = clientId;
    }
}
