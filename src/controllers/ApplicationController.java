package controllers;

import java.io.File;
import java.io.FileNotFoundException;
import models.FavManager;
import models.FileManager;
import views.View;

public class ApplicationController {
    
    private final FileManager fileM;
    private final FavManager favM;
    private final View view;

    public ApplicationController(FileManager fileM, FavManager favM, View view) {
        this.fileM = fileM;
        this.favM = favM;
        this.view = view;
    }
    
    public void start(){
        view.start(this);
    }
    
    
    
    public ControllerResponse processCommand(String command){
        String[] aux = command.split(" ");
        command = aux[0];
        String args = aux.length <= 1 ? "" : aux[1];
        
        
        switch(command){
            
            case "cd":
                return cd(args);
            case "list":
                return list(args);
            case "play":
                return play(args);
            case "info":
                return info(args);
            case "find":
                return find(args);
            case "fav":
                return fav(args);
            case "lfav":
                return lfav(args);
            case "report":
                return report(args);
            case "exit":
                System.exit(0);
            default:
                ControllerResponse response = new ControllerResponse(ControllerResponse.returnTypes.EXCEPTION, new UnknownCommandException("Unknown command: " + command));
                return response;
        }
    }
    
    private ControllerResponse cd(String args){
        ControllerResponse response = new ControllerResponse(ControllerResponse.returnTypes.VOID, null);
        try {
            fileM.changeDir(args);
        } catch (FileNotFoundException ex) {
            response = new ControllerResponse(ControllerResponse.returnTypes.EXCEPTION, ex);
        }
        return response;
    }
        
    private ControllerResponse list(String args){
        ControllerResponse response;
        try {
            response = new ControllerResponse(ControllerResponse.returnTypes.FILE_LIST, fileM.list(args));
        } catch (FileNotFoundException ex) {
            response = new ControllerResponse(ControllerResponse.returnTypes.EXCEPTION, ex);
        }
        return response;
    }
    
    private ControllerResponse play(String args){
        ControllerResponse response = new ControllerResponse(ControllerResponse.returnTypes.VOID, null);
        try {
            fileM.play(args);
        } catch (FileNotFoundException ex) {
            response = new ControllerResponse(ControllerResponse.returnTypes.EXCEPTION, ex);
        }
        return response;
    }
    
    private ControllerResponse info(String args){
        ControllerResponse response;
        try {
            response = new ControllerResponse(ControllerResponse.returnTypes.STRING, fileM.getInfo(args));
        } catch (FileNotFoundException ex) {
            response = new ControllerResponse(ControllerResponse.returnTypes.EXCEPTION, ex);
        }
        return response;
    }
    
    private ControllerResponse find(String args){
        ControllerResponse response;
        response = new ControllerResponse(ControllerResponse.returnTypes.FILE_LIST, fileM.find(args));
        return response;
    }
    
    private ControllerResponse fav(String args){
        ControllerResponse response = new ControllerResponse(ControllerResponse.returnTypes.VOID, null);
        try {
            File file = fileM.getFile(args);
            favM.addFavourite(file);
        } catch (FileNotFoundException ex) {
            response = new ControllerResponse(ControllerResponse.returnTypes.EXCEPTION, ex);
        }
        return response;
    }
    
    private ControllerResponse lfav(String args){
        ControllerResponse response;
        response = new ControllerResponse(ControllerResponse.returnTypes.FILE_LIST, favM.getFavourites());
        return response;
    }
    
    private ControllerResponse report(String args){
        ControllerResponse response = new ControllerResponse(ControllerResponse.returnTypes.VOID, null);
        favM.makeReport();
        return response;
    }
    
    public File getWorkDir(){
        return fileM.getWorkDir();
    }

}
