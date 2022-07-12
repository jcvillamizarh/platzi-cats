package com.platzi.cat.model;

import io.github.cdimascio.dotenv.Dotenv;
import lombok.Data;

@Data
public class FavoriteCat {
    Dotenv dotenv = Dotenv.load();

    private String id;
    private String imageId;
    private String apikey = dotenv.get("API_KEY");
    private ImageX image;

}
