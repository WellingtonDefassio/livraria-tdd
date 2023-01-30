package io.wdefassio.livraria.api.application.representation;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ReturnedLoanDTO {
    private Boolean returned;


    public ReturnedLoanDTO(Boolean returned) {
        this.returned = returned;
    }
}

