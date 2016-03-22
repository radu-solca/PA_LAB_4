package controllers;

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
    
    public void processCommand(String command) throws UnknownCommandException{
        String[] parsedCommand = command.split(" ");
        
        switch(parsedCommand[0]){
            
            case "cd":
                cd(parsedCommand);
                break;
            case "list":
                list(parsedCommand);
                break;
            case "play":
                play(parsedCommand);
                break;
            case "info":
                info(parsedCommand);
                break;
            case "find":
                find(parsedCommand);
                break;
            case "fav":
                fav(parsedCommand);
                break;
            case "report":
                report(parsedCommand);
                break;
            default:
                throw new UnknownCommandException("Unknown command: " + parsedCommand[0]);
        }
    }
    
    private void cd(String[] args){
    }
        
    private void list(String[] args){
    }
    
    private void play(String[] args){
    }
    
    private void info(String[] args){
    }
    
    private void find(String[] args){
    }
    
    private void fav(String[] args){
    }
    
    private void report(String[] args){
    }

}
