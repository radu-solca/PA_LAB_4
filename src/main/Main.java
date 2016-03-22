package main;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import models.FavouritesManager;
import models.FileManager;
import org.apache.tika.exception.TikaException;
import org.xml.sax.SAXException;

public class Main {

    public static void main(String[] args) {
        FileManager fileManager;
        FavouritesManager favouritesManager;
        try {

            fileManager = new FileManager("/audio");
            favouritesManager = new FavouritesManager("/favourites.ser");
            
            favouritesManager.addFavourite(fileManager.getFile("boonika.mp3"));
            System.out.println(favouritesManager.getFavourites());
            
            favouritesManager.addFavourite(fileManager.getFile("YEAH/yeah.wav"));
            System.out.println(favouritesManager.getFavourites());
            
            favouritesManager.removeFavourite(fileManager.getFile("boonika.mp3"));
            System.out.println(favouritesManager.getFavourites());
            
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        } catch (/*SAXException | TikaException |*/ IOException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
