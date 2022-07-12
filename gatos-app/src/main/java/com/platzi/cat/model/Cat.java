package com.platzi.cat.model;

import io.github.cdimascio.dotenv.Dotenv;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Cat {

    Dotenv dotenv = Dotenv.load();
    private String id;
    private String url;
    private String apikey = dotenv.get("API_KEY");
    private String image;
}
