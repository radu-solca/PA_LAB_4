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
            
            fileManager.DEBUG_print();
//            fileManager.DEBUG_print();
            System.out.println(fileManager.list(""));
//            
//            System.out.println(fileManager.getInfo("yeah.wav"));
//            //fileManager.play("yeah.wav");
//          
            System.out.println(fileManager.getInfo("/TEST.mp3"));
            fileManager.DEBUG_print();
            System.out.println(fileManager.find("Hide & Seek"));
            fileManager.DEBUG_print();
                    
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SAXException | TikaException | IOException ex) {
        Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
