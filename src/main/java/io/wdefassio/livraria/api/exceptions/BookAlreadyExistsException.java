package io.wdefassio.livraria.api.exceptions;

public class BookAlreadyExistsException extends RuntimeException {
    public BookAlreadyExistsException() {
        super("isbn already exists");
    }
}
