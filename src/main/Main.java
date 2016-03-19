package main;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import models.FavouritesManager;
import models.FileManager;

public class Main {

    public static void main(String[] args) {
        FileManager fileManager;
        FavouritesManager favouritesManager;
        try {

            fileManager = new FileManager("/audio");
            favouritesManager = new FavouritesManager("/favourites.ser");
            
            fileManager.DEBUG_print();
            System.out.println(fileManager.list(""));
            
        } catch (IOException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
