package io.wdefassio.livraria.api.exceptions;

public class BookAlreadyLoanException extends RuntimeException {
    public BookAlreadyLoanException() {
        super("book already loan");
    }
}
