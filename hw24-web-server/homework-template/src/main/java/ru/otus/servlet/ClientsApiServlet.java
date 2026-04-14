package ru.otus.servlet;

import com.google.gson.Gson;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.List;
import ru.otus.crm.model.Client;
import ru.otus.crm.service.DBServiceClient;

@SuppressWarnings({"java:S1989"})
public class ClientsApiServlet extends HttpServlet {

    private final transient DBServiceClient dbServiceClient;
    private final transient Gson gson;

    public ClientsApiServlet(DBServiceClient dbServiceClient, Gson gson) {
        this.dbServiceClient = dbServiceClient;
        this.gson = gson;
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json;charset=UTF-8");
        List<Client> dbClients = dbServiceClient.findAll();
        ServletOutputStream out = response.getOutputStream();
        out.print(gson.toJson(dbClients));
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try (var reader = new InputStreamReader(request.getInputStream(), StandardCharsets.UTF_8)) {
            Client client = gson.fromJson(reader, Client.class);
            Client dbClient = dbServiceClient.saveClient(client);
            response.setContentType("application/json;charset=UTF-8");
            response.setStatus(HttpServletResponse.SC_CREATED);
            ServletOutputStream out = response.getOutputStream();
            out.print(gson.toJson(dbClient));
        }
    }
}
