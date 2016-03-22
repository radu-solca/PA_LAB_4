package main;

import models.FavouritesManager;
import models.FileManager;

public class Main {

    public static void main(String[] args) {
        FileManager fileManager;
        FavouritesManager favouritesManager;

        fileManager = new FileManager("/audio");
        favouritesManager = new FavouritesManager("/favourites.ser");

        System.out.println(favouritesManager.getFavourites());
    }
}
