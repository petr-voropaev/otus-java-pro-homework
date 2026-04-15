package ru.otus.servlet;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import ru.otus.crm.model.Address;
import ru.otus.crm.model.Client;
import ru.otus.crm.model.Phone;
import ru.otus.crm.service.DBServiceClient;
import ru.otus.services.TemplateProcessor;

@SuppressWarnings({"java:S1989"})
public class ClientsServlet extends HttpServlet {

    private static final String CLIENTS_PAGE_TEMPLATE = "clients.html";
    private static final String PARAM_NAME = "name";
    private static final String PARAM_ADDRESS = "address";
    private static final String PARAM_PHONES = "phones";

    private final transient TemplateProcessor templateProcessor;
    private final transient DBServiceClient dbServiceClient;

    public ClientsServlet(TemplateProcessor templateProcessor, DBServiceClient dbServiceClient) {
        this.templateProcessor = templateProcessor;
        this.dbServiceClient = dbServiceClient;
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Map<String, Object> paramsMap = new HashMap<>();
        paramsMap.put("clients", dbServiceClient.findAll());

        response.setContentType("text/html;charset=UTF-8");
        response.getWriter().println(templateProcessor.getPage(CLIENTS_PAGE_TEMPLATE, paramsMap));
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String nameValue = request.getParameter(PARAM_NAME);
        if (nameValue != null && !nameValue.isBlank()) {
            Client client = new Client(nameValue);

            String addressValue = request.getParameter(PARAM_ADDRESS);
            if (addressValue != null && !addressValue.isBlank()) {
                client.setAddress(new Address(null, addressValue, client));
            }

            String phonesValue = request.getParameter(PARAM_PHONES);
            if (phonesValue != null && !phonesValue.isBlank()) {
                List<Phone> phones = Arrays.stream(phonesValue.split(","))
                        .map(phone -> new Phone(null, phone, client))
                        .toList();
                client.setPhones(phones);
            }

            dbServiceClient.saveClient(client);
        }
        response.sendRedirect("/clients");
    }
}
