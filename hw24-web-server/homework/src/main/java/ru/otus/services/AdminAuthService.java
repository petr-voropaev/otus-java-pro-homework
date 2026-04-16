package ru.otus.services;

public interface AdminAuthService {
    boolean authenticate(String login, String password);
}
