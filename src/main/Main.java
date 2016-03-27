package main;

import controllers.ApplicationController;
import models.FavManager;
import models.FileManager;
import views.ConsoleView;

public class Main {

    public static void main(String[] args) {
        
        FileManager fileM = new FileManager("/audio");
        FavManager favM = new FavManager("/favourites");
        ConsoleView view = new ConsoleView();
        ApplicationController controller = new ApplicationController(fileM, favM, view);
        controller.start();
        
    }
    
}
