package ru.otus.controller;

import java.util.Arrays;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import ru.otus.controller.request.ClientRequest;
import ru.otus.model.Address;
import ru.otus.model.Client;
import ru.otus.model.Phone;
import ru.otus.service.DBServiceClient;

@Controller
public class ClientController {

    private final DBServiceClient dbServiceClient;

    public ClientController(DBServiceClient dbServiceClient) {
        this.dbServiceClient = dbServiceClient;
    }

    @GetMapping("/")
    public String index() {
        return "index";
    }

    @GetMapping("/clients")
    public String clients(Model model) {
        model.addAttribute("clients", dbServiceClient.findAll());
        model.addAttribute("clientRequest", new ClientRequest());
        return "clients";
    }

    @PostMapping("/clients")
    public String saveClient(@ModelAttribute ClientRequest clientRequest) {
        var clientName =
                clientRequest.getName() == null ? "" : clientRequest.getName().trim();
        var addressValue = clientRequest.getAddress() == null
                ? ""
                : clientRequest.getAddress().trim();
        var address = addressValue.isBlank() ? null : new Address(null, clientRequest.getAddress());

        var phones = Arrays.stream((clientRequest.getPhones() == null ? "" : clientRequest.getPhones()).split(","))
                .map(String::trim)
                .map(phone -> new Phone(null, phone))
                .toList();

        dbServiceClient.saveClient(new Client(null, clientName, address, phones));
        return "redirect:/clients";
    }
}
