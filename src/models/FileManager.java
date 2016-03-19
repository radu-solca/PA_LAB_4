package models;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.*;


public class FileManager{
    
    private File currentDir; //can only be a directory.

    public FileManager() {
        currentDir = new File(".");
    } 
    
    // directory methods:
    
    /**
     * This method changes the current working directory to the path specified.
     * Note that the path is relative to the current working directory.
     * 
     * In case that the path specified does not lead to a directory, it will throw a FileNotFoundException.
     * 
     * @param path
     * @throws FileNotFoundException 
     */
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
    
    /**
     * This method returns a list of all (supported) audio files in the directory at the given path.
     * Note that the path is relative to the current working directory.
     * 
     * If the path is a null string then it will list the audio files in the current working directory.
     * In case that the path specified does not lead to a directory, it will throw a FileNotFoundException.
     * 
     * @param path
     * @return
     * @throws FileNotFoundException 
     */
    public List<File> list(String path) throws FileNotFoundException {
        List<File> list = new ArrayList<>();
        
        File selectedDir = new File(currentDir,path);
        if(!selectedDir.exists()){
            throw new FileNotFoundException("\"" + selectedDir.getAbsolutePath() + "\" does not exist.");
        }
        else
        if(!selectedDir.isDirectory()){
            throw new FileNotFoundException("\"" + selectedDir.getAbsolutePath() + "\" is not a directory.");
        }
        else{
            //copy all files in selectedDir that match audioFilenameFilter to list;
            list.addAll(Arrays.asList(selectedDir.listFiles(audioFilenameFilter)));
        }
        
        return list;
    }
    
    
    // file methods:
    
    public void getInfo(String path) throws FileNotFoundException {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    public List<File> find(Pattern pattern) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    
    // directory/file methods:
    
    public File getFile(String path) throws FileNotFoundException {
        throw new UnsupportedOperationException("Not supported yet.");
    }
        
    
    //auxiliary methods:
    
    private final FilenameFilter audioFilenameFilter = (File dir, String name) -> {
        String regex = ".(mp3|wav|flac)\\z";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(name);
        
        return matcher.find();
    };

    
    public void DEBUG_print(){
        System.out.println(currentDir.getPath());
        //System.out.println(Arrays.toString(currentDir.list()));
    }

}
