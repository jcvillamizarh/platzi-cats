package com.platzi.cat.service;

import com.google.gson.Gson;

import com.platzi.cat.model.Cat;
import com.platzi.cat.model.FavoriteCat;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;


public class CatService {

    private static final String BASE_URL = "https://api.thecatapi.com/v1/";
    private static final String SEARCH_ENDPOINT = BASE_URL + "images/search";
    private static final String FAVORITE_ENDPOINT = BASE_URL + "favourites";
    private static final String FAVORITE_MENU = "Options: \n"
            + " 1. See other image \n"
            + " 2. Delete favorite \n"
            + " 3. Go back \n";
    private static final String RANDOM_CATS_MENU = "Options: \n"
            + " 1. See other image \n"
            + " 2. Favorite \n"
            + " 3. Go back \n";

    public static void markCatAsFavorite(Cat cat) {
        try {
            OkHttpClient client = new OkHttpClient();
            MediaType mediaType = MediaType.parse("application/json");
            RequestBody body = RequestBody.create(mediaType, "{\n\t\"image_id\":\"" + cat.getId() + "\"\n}");
            Request request = new Request.Builder()
                    .url(FAVORITE_ENDPOINT)
                    .post(body)
                    .addHeader("Content-Type", "application/json")
                    .addHeader("x-api-key", cat.getApikey())
                    .build();
            Response response = client.newCall(request).execute();

            if (!response.isSuccessful()) {
                response.body().close();
            }
        } catch (IOException e) {
            System.out.println(e);
        }
    }

    public static void deleteFavorite(FavoriteCat favoriteCat) {
        try {
            OkHttpClient client = new OkHttpClient();

            Request request = new Request.Builder()
                    .url(FAVORITE_ENDPOINT + favoriteCat.getId() + "")
                    .delete(null)
                    .addHeader("Content-Type", "application/json")
                    .addHeader("x-api-key", favoriteCat.getApikey())
                    .build();

            Response response = client.newCall(request).execute();
            if (!response.isSuccessful()) {
                response.body().close();
            }
        } catch (IOException e) {
            System.out.println(e);
        }
    }

    public static void seeRandomCats() throws IOException {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(SEARCH_ENDPOINT).get().build();
        Response response = client.newCall(request).execute();
        String jsonData = response.body().string();
        if (!response.isSuccessful()) {
            response.body().close();
        }

        jsonData = jsonData.substring(1, jsonData.length());
        jsonData = jsonData.substring(0, jsonData.length() - 1);

        Gson gson = new Gson();
        Cat cat = gson.fromJson(jsonData, Cat.class);
        Image image;
        try {
            URL url = new URL(cat.getUrl());
            HttpURLConnection httpcon = (HttpURLConnection) url.openConnection();
            httpcon.addRequestProperty("User-Agent", "");
            BufferedImage bufferedImage = ImageIO.read(httpcon.getInputStream());
            ImageIcon catImageIcon = new ImageIcon(bufferedImage);
            httpcon.disconnect();
            if (catImageIcon.getIconWidth() > 800) {
                Image background = catImageIcon.getImage();
                Image modified = background.getScaledInstance(800, 600, java.awt.Image.SCALE_SMOOTH);
                catImageIcon = new ImageIcon(modified);
            }

            String[] buttons = {"see other image", "favorite", "go back"};
            String catId = cat.getId();
            String option = (String) JOptionPane.showInputDialog(null, RANDOM_CATS_MENU, catId, JOptionPane.INFORMATION_MESSAGE, catImageIcon, buttons, buttons[0]);

            int selection = -1;

            for (int i = 0; i < buttons.length; i++) {
                if (option.equals(buttons[i])) {
                    selection = i;
                }
            }

            switch (selection) {
                case 0:
                    seeRandomCats();
                    break;
                case 1:
                    markCatAsFavorite(cat);
                    break;
                default:
                    break;
            }

        } catch (IOException e) {
            System.out.println(e);
        } finally {
            response.body().close();
        }

    }

    public static void seeFavoriteCats(String apikey) throws IOException {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(FAVORITE_ENDPOINT)
                .get()
                .addHeader("Content-Type", "application/json")
                .addHeader("x-api-key", apikey)
                .build();

        Response response = client.newCall(request).execute();

        String jsonData = response.body().string();

        if (!response.isSuccessful()) {
            response.body().close();
        }
        Gson gson = new Gson();

        FavoriteCat[] catsArray = gson.fromJson(jsonData, FavoriteCat[].class);
        if (catsArray.length > 0) {
            int min = 1;
            int max = catsArray.length;
            int aleatory = (int) (Math.random() * ((max - min) + 1)) + min;
            int index = aleatory - 1;
            FavoriteCat favoriteCat = catsArray[index];
            Image image;
            try {
                URL url = new URL(favoriteCat.getImage().getUrl());
                HttpURLConnection httpcon = (HttpURLConnection) url.openConnection();
                httpcon.addRequestProperty("User-Agent", "");
                BufferedImage bufferedImage = ImageIO.read(httpcon.getInputStream());
                ImageIcon catImageIcon = new ImageIcon(bufferedImage);
                httpcon.disconnect();
                if (catImageIcon.getIconWidth() > 800) {

                    Image background = catImageIcon.getImage();
                    Image modified = background.getScaledInstance(800, 600, java.awt.Image.SCALE_SMOOTH);
                    catImageIcon = new ImageIcon(modified);
                }

                String[] buttons = {"see other image", "delete favorite", "go back"};
                String catId = favoriteCat.getId();
                String option = (String) JOptionPane.showInputDialog(null, FAVORITE_MENU, catId, JOptionPane.INFORMATION_MESSAGE, catImageIcon, buttons, buttons[0]);

                int selection = -1;

                for (int i = 0; i < buttons.length; i++) {
                    if (option.equals(buttons[i])) {
                        selection = i;
                    }
                }
                switch (selection) {
                    case 0:
                        seeFavoriteCats(apikey);
                        break;
                    case 1:
                        deleteFavorite(favoriteCat);
                        break;
                    default:
                        break;
                }
            } catch (IOException e) {
                System.out.println(e);
            } finally {
                response.body().close();
            }
        }
    }

}
