package main;

import controllers.ApplicationController;

public class Main {

    public static void main(String[] args) {
        
        ApplicationController controller = new ApplicationController("/audio", "/favourites");
        controller.start();
        
    }
    
}
