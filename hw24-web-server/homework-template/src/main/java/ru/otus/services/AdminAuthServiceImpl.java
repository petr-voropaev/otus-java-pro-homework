package ru.otus.services;

public class AdminAuthServiceImpl implements AdminAuthService {

    private final String login;
    private final String password;

    public AdminAuthServiceImpl(String login, String password) {
        this.login = login;
        this.password = password;
    }

    @Override
    public boolean authenticate(String login, String password) {
        return this.login.equals(login) && this.password.equals(password);
    }
}
