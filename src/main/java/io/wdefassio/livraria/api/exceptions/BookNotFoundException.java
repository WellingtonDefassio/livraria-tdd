package io.wdefassio.livraria.api.exceptions;

public class BookNotFoundException extends RuntimeException {

    public BookNotFoundException() {
        super("book not found");
    }
}
