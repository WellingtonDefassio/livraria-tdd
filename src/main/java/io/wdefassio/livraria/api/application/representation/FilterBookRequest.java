package io.wdefassio.livraria.api.application.representation;

import lombok.Data;

@Data
public class FilterBookRequest {

    private String author;
    private String title;

}
