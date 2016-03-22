package main;

import java.io.FileNotFoundException;
import java.util.logging.Level;
import java.util.logging.Logger;
import models.FavManager;
import models.FileManager;

public class Main {

    public static void main(String[] args) {
        
        FileManager fileManager;
        FavManager favouritesManager;
        fileManager = new FileManager("/audio");
        favouritesManager = new FavManager("/favourites");
        System.out.println(favouritesManager.getFavourites());
        favouritesManager.makeReport();
    }
}
