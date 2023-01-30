package io.wdefassio.livraria.api.application.representation;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LoanDTO {

    private String isbn;
    private String customer;



}
