package io.wdefassio.livraria.api.exceptions;

public class LoanNotFoundException extends RuntimeException {
    public LoanNotFoundException() {
        super("loan not found");
    }
}
