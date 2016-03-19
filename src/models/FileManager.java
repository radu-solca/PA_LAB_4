package models;

import java.io.File;
import java.util.Arrays;


public class FileManager {
    
    private File selectedItem; //the selected item - can be a directory or a file.

    public FileManager() {
        selectedItem = new File(".");
    } 
    
    public void cd(String path){
        this.selectedItem = new File(selectedItem, path);
    }
    
    public void DEBUG_print(){
        System.out.println(selectedItem.getPath());
        
        System.out.println(Arrays.toString(selectedItem.list()));
    }
}
