package controllers;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.logging.Level;
import java.util.logging.Logger;
import models.FavManager;
import models.FileManager;

public class ApplicationController {
    
    private final FileManager fileM;
    private final FavManager favM;
    //private View view; TODO

    public ApplicationController(String audioPath, String favouritesPath) {
        this.fileM = new FileManager(audioPath);
        this.favM = new FavManager(favouritesPath);
    }
    
    
    public void start(){
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        String input;
        
        while(true){
            try {
                System.out.print("(" + fileM.getFile("") + ")>");
                input = reader.readLine();
                processCommand(input);
            
            } catch (FileNotFoundException ex) {
                Logger.getLogger(ApplicationController.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException | UnknownCommandException ex) {
                Logger.getLogger(ApplicationController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    
    
    private void processCommand(String command) throws UnknownCommandException{
        String[] aux = command.split(" ");
        command = aux[0];
        String args = aux.length <= 1 ? "" : aux[1];
        
        
        switch(command){
            
            case "cd":
                cd(args);
                break;
            case "list":
                list(args);
                break;
            case "play":
                play(args);
                break;
            case "info":
                info(args);
                break;
            case "find":
                find(args);
                break;
            case "fav":
                fav(args);
                break;
            case "report":
                report(args);
                break;
            case "exit":
                System.exit(0);
                break;
            default:
                throw new UnknownCommandException("Unknown command: " + command);
        }
    }
    
    private void cd(String args){
        try {
            fileM.changeDir(args);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(ApplicationController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
        
    private void list(String args){
        try {
            System.out.println(fileM.list(args));
        } catch (FileNotFoundException ex) {
            Logger.getLogger(ApplicationController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void play(String args){
        try {
            fileM.play(args);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(ApplicationController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void info(String args){
        try {
            System.out.println(fileM.getInfo(args));
        } catch (FileNotFoundException ex) {
            Logger.getLogger(ApplicationController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void find(String args){
        System.out.println(fileM.find(args));
    }
    
    private void fav(String args){
        try {
            File file = fileM.getFile(args);
            favM.addFavourite(file);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(ApplicationController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void report(String args){
        favM.makeReport();
    }

}
