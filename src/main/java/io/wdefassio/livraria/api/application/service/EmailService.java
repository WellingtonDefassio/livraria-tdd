package io.wdefassio.livraria.api.application.service;

import java.util.List;

public interface EmailService {
    void sendEmails(String message, List<String> emails);

}
