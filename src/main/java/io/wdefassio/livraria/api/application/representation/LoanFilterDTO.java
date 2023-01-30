package io.wdefassio.livraria.api.application.representation;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoanFilterDTO {
    private String isbn;
    private String customer;



}
