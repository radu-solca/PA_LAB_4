/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package views;

import controllers.ApplicationController;
import controllers.ControllerResponse;
import controllers.UnknownCommandException;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author NASA
 */
public class ConsoleView implements View{

    private ApplicationController controller;
    
    @Override
    public void start(ApplicationController controller) {
        this.controller = controller;
        
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        String input;
        
        while(true){

            try {
                System.out.print("(" + controller.getWorkDir() + ")>");
                input = reader.readLine();
                ControllerResponse response = controller.processCommand(input);
                handleResponse(response);
            } catch (IOException ex) {
                Logger.getLogger(ConsoleView.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    private void handleResponse(ControllerResponse response){
        switch(response.getReturnType()){
            case EXCEPTION:
                Exception ex = (Exception) response.getReturnValue();
                System.out.println(ex.getMessage());
                break;
            case STRING:
                String msg = (String) response.getReturnValue();
                System.out.println(msg);
                break;
            case FILE_LIST:
                List<File> list = (List<File>) response.getReturnValue();
                System.out.println(list);
                break;
        }
    }
}
