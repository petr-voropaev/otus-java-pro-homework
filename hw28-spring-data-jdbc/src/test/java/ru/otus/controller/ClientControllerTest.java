package ru.otus.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import ru.otus.model.Phone;
import ru.otus.persistence.repository.AddressRepository;
import ru.otus.persistence.repository.ClientRepository;
import ru.otus.persistence.repository.PhoneRepository;
import ru.otus.service.DBServiceClient;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class ClientControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private DBServiceClient dbServiceClient;

    @Autowired
    private PhoneRepository phoneRepository;

    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private ClientRepository clientRepository;

    @BeforeEach
    void setUp() {
        phoneRepository.deleteAll();
        addressRepository.deleteAll();
        clientRepository.deleteAll();
    }

    @Test
    void check_getClients() throws Exception {
        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Добро пожаловать на стартовую страницу")));

        mockMvc.perform(get("/clients"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Список клиентов")));
    }

    @Test
    void check_postClients() throws Exception {
        mockMvc.perform(post("/clients")
                        .param("name", "Name")
                        .param("address", "Address")
                        .param("phones", "1, 2"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/clients"));

        var clients = dbServiceClient.findAll();
        assertThat(clients).hasSize(1);
        assertThat(clients.getFirst().getName()).isEqualTo("Name");
        assertThat(clients.getFirst().getAddress().getStreet()).isEqualTo("Address");
        assertThat(clients.getFirst().getPhones()).extracting(Phone::getNumber).containsExactly("1", "2");
    }
}
