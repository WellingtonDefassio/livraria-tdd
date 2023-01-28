package io.wdefassio.livraria.api.application.exceptions;

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

    public List<String> getErrors() {
        return errors;
    }
}
