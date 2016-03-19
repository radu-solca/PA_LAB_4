package models;

import java.io.*;
import java.util.Arrays;
import java.util.List;
import java.util.regex.*;


public class FileManager{
    
    private File currentDir; //can only be a directory.

    public FileManager() {
        currentDir = new File(".");
    } 
    
    public void changeDir(String path) throws FileNotFoundException {
        File aux = new File(currentDir, path);
        if(!aux.exists()){
            throw new FileNotFoundException("\"" + aux.getAbsolutePath() + "\" does not exist.");
        }
        else
        if(!aux.isDirectory()){
            throw new FileNotFoundException("\"" + aux.getAbsolutePath() + "\" is not a directory.");
        }
        else{
            this.currentDir = aux;
        }
    }

    public void getInfo(String path) throws FileNotFoundException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    public File getFile(String path) throws FileNotFoundException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    public List<File> find(Pattern pattern) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
        
    public void DEBUG_print(){
        System.out.println(currentDir.getPath());
        
        System.out.println(Arrays.toString(currentDir.list()));
    }

}
