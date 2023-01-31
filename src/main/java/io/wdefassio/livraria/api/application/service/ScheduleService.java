package io.wdefassio.livraria.api.application.service;

import io.wdefassio.livraria.api.domain.entity.Loan;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ScheduleService {

    private static final String CRON_LATE_LOANS = "0 0 0 1/1 * ?";
    private final LoanService loanService;
    private final EmailService emailService;
    @Value("${mail.message}")
    private String message;


    @Scheduled(cron = CRON_LATE_LOANS)
    public void sendMailToLateLoans() {
        List<Loan> allLateLoans = loanService.getAllLateLoans();
        List<String> emails = allLateLoans.stream().map(Loan::getEmail).toList();
        emailService.sendEmails(message, emails);
    }


}
