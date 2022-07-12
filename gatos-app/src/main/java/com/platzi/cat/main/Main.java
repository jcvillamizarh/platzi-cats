package com.platzi.cat.main;

import com.platzi.cat.model.Cat;
import com.platzi.cat.service.CatService;
import com.sun.scenario.effect.impl.sw.sse.SSEBlend_SRC_OUTPeer;

import javax.swing.*;
import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException {
        int menuOption = -1;
        String[] buttons = {"1. See cats", "2. See favourites", "3. exit"};
        do {
            String option = (String) JOptionPane.showInputDialog(null, "Cats Java", "Main Menu", JOptionPane.INFORMATION_MESSAGE, null, buttons, buttons[0]);
            for (int i = 0; i < buttons.length; i++) {
                if (option.equals(buttons[i])) {
                    menuOption = i;
                }
            }
            switch (menuOption) {
                case 0:
                    CatService.seeRandomCats();
                    break;
                case 1:
                    Cat cat = new Cat();
                    CatService.seeFavoriteCats(cat.getApikey());
                    break;
                default:
                    break;
            }
        } while (menuOption != 2);

    }
}
