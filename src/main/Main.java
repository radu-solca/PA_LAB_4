package main;

import controllers.ApplicationController;
import models.FavManager;
import models.FileManager;
import views.ConsoleView;
import views.View;
import views.GUIView;

public class Main {

    public static void main(String[] args) {
        
        FileManager fileM = new FileManager("audio");
        FavManager favM = new FavManager("favourites");
        View view = new GUIView();
        ApplicationController controller = new ApplicationController(fileM, favM, view);
        controller.start();
        
    }
    
}
