package ru.otus.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class Client {

    private Long id;
    private String name;
    private Address address;
    private List<Phone> phones = new ArrayList<>();

    public Client(Long id, String name, Address address, List<Phone> phones) {
        this.id = id;
        this.name = name;
        setAddress(address);
        setPhones(phones);
    }

    public void setAddress(Address address) {
        this.address = address == null ? null : new Address(address.getId(), address.getStreet(), this);
    }

    public void setPhones(List<Phone> phones) {
        this.phones = phones == null
                ? new ArrayList<>()
                : phones.stream()
                        .filter(Objects::nonNull)
                        .map(phone -> new Phone(phone.getId(), phone.getNumber(), this))
                        .toList();
    }

    @Override
    public String toString() {
        return "Client{" + "id=" + id + ", name='" + name + '\'' + '}';
    }
}
