package io.wdefassio.livraria;

import io.wdefassio.livraria.api.application.service.EmailService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.Arrays;
import java.util.List;

@SpringBootApplication
@EnableScheduling
public class LivrariaApplication {

    @Bean
    public ModelMapper modelMapper(){
        return new ModelMapper();
    }

//    @Bean
//    public CommandLineRunner runner(){
//        return args -> {
//            List<String> emails  = Arrays.asList("113f0d886a-b5bbe9@inbox.mailtrap.io", "well.defassio@gmail.com");
//            emailService.sendEmails("Testando serviço de emails", emails);
//            System.out.println("Emails enviados");
//        };
//    }


    @Scheduled(cron = "0 0 0 1 1 1")
    public void testeAgendamentoTarefas() {

    }
    public static void main(String[] args) {
        SpringApplication.run(LivrariaApplication.class, args);
    }

}
