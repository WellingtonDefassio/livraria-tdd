package io.wdefassio.livraria.api.exceptions;

import org.springframework.validation.BindingResult;

import java.util.ArrayList;
import java.util.List;

public class ApiError {

    private List<String> errors;
    public ApiError(BindingResult bindingResult) {
        this.errors = new ArrayList<>();
        bindingResult.getAllErrors().forEach(e -> {
            errors.add(e.getDefaultMessage());
        });
    }

    public ApiError(String message) {
        this.errors = new ArrayList<>();
        errors.add(message);
    }

    public List<String> getErrors() {
        return errors;
    }
}
