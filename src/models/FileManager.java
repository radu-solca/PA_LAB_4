package models;

import java.awt.Desktop;
import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.*;
import org.apache.tika.exception.*;
import org.apache.tika.metadata.*;
import org.apache.tika.parser.*;
import org.apache.tika.sax.*;
import org.xml.sax.SAXException;


public class FileManager{
    
    private File workDir; //can only be a directory.

    /**
     * Initializes the application's working directory with the provided path.
     * Note that the path is relative to the application's root directory.
     * 
     * If the provided directory does not exist, the function will create it.
     * 
     * @param path
     */
    public FileManager(String path){
        
        path = "./" + path;
        
        workDir = new File(path);
        if(!workDir.exists()||!workDir.isDirectory()){
            if(!workDir.mkdirs()){
                IOException ex = new IOException("Could not create directory " + workDir.getAbsolutePath());
                Logger.getLogger(FileManager.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    } 
    
    // directory methods:
    public File getWorkDir(){
        return workDir;
    }
    
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
        File aux = new File(workDir, path);
        if(!aux.exists()){
            throw new FileNotFoundException("\"" + aux.getAbsolutePath() + "\" does not exist.");
        }
        else
        if(!aux.isDirectory()){
            throw new FileNotFoundException("\"" + aux.getAbsolutePath() + "\" is not a directory.");
        }
        else{
            this.workDir = aux;
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
        
        File selectedDir = new File(workDir,path);
                
        if(!selectedDir.exists()){
            throw new FileNotFoundException("\"" + selectedDir.getAbsolutePath() + "\" does not exist.");
        }
        else
        if(!selectedDir.isDirectory()){
            throw new FileNotFoundException("\"" + selectedDir.getAbsolutePath() + "\" is not a directory.");
        }
        else{
            //copy all files in selectedDir that match audioFilenameFilter to list;
            list.addAll(Arrays.asList(selectedDir.listFiles((File dir, String name) -> {
                String regex = ".([mM][pP]3|[wW][aA][vV]|[fF][lL][aA][cC])\\z";
                Pattern pattern = Pattern.compile(regex);
                Matcher matcher = pattern.matcher(name);
                
                File file = new File(dir, name);
                
                return matcher.find() || file.isDirectory();
            })));
        }
        
    return list;    
    }
    
    
    // file methods:
    
    /**
     * This method extracts the meta-data from an audio file at a given path.
     * 
     * @param path The path of the desired file, relative to the current working directory.
     * @return A String containing the meta-data such as title and artist.
     * @throws FileNotFoundException
     */
    public Map<String,String> getInfo(String path) throws FileNotFoundException{
        File file = new File(workDir, path);
        Map<String,String> info = new LinkedHashMap<>();
               
        try(FileInputStream inputstream = new FileInputStream(file)) {
            if(!file.exists()){
                throw new FileNotFoundException("\"" + file.getAbsolutePath() + "\" does not exist.");
            }
            else
            if(!file.isFile()){
                throw new FileNotFoundException("\"" + file.getAbsolutePath() + "\" is not a file.");
            }
            
            //Parser method parameters
            Parser parser = new AutoDetectParser();
            BodyContentHandler handler = new BodyContentHandler();
            Metadata metadata = new Metadata();
            ParseContext context = new ParseContext();

            parser.parse(inputstream, handler, metadata, context);            
            
            info.put( "title", (metadata.get("title") == null ? "N/A" : metadata.get("title")) + "\n");            
            info.put( "artist", (metadata.get("xmpDM:artist") == null ? "N/A" : metadata.get("xmpDM:artist")) + "\n");
            info.put( "album", (metadata.get("xmpDM:album") == null ? "N/A" : metadata.get("xmpDM:album")) + "\n");
            info.put( "releaseDate", (metadata.get("xmpDM:releaseDate") == null ? "N/A" : metadata.get("xmpDM:releaseDate")) + "\n");
            info.put( "genre", (metadata.get("xmpDM:genre") == null ? "N/A" : metadata.get("xmpDM:genre")) + "\n");
            

        } catch (IOException | SAXException | TikaException ex) {
            Logger.getLogger(FileManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return info;
    }
    
    /**
     * This method will play the file at a given path using the operating system's default application.
     * @param path The path to the desired file, relative to the current working directory.s 
     * @throws java.io.FileNotFoundException 
     */
    public void play(String path) throws FileNotFoundException{
        File file = new File(workDir, path);  
        if(!file.exists()){
            throw new FileNotFoundException("\"" + file.getAbsolutePath() + "\" does not exist.");
        }
        else
        if(!file.isFile()){
            throw new FileNotFoundException("\"" + file.getAbsolutePath() + "\" is not a file.");
        }
        
        Desktop desktop = null;
        
        if (Desktop.isDesktopSupported()) {
            desktop = Desktop.getDesktop();
        }
        if (desktop != null){
            try {
                desktop.open(file);
            } catch (IOException ex) {
                Logger.getLogger(FileManager.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    /**
     * Searches the current directory, and recursively searches any directories that it encounters on the way, for files matching the criteria.
     * Returns a file if and only if the criteria String is found in either it's name, or meta-data.
     * 
     * @param criteria
     * @return A list of files matching the criterion. 
     */
    public List<File> find(String criteria){
        String regex = criteria;
        Pattern pattern = Pattern.compile(regex);
        
        List<File> results = new ArrayList<>();
        
        recursiveFind(pattern, results);
        return results;
    }
    
    private void recursiveFind(Pattern pattern, List<File> results){
        
        String[] items = workDir.list();
                
        for(String item : items){
            File file = new File(workDir, item);
            if(file.isFile()){
                // check if the file matches the search.
                Matcher nameMatcher = pattern.matcher(file.getName());
                Matcher metadataMatcher;
                
                String metadata = null;
                try {
                    metadata = getInfo(item).toString();
                } catch (FileNotFoundException ex) {
                    Logger.getLogger(FileManager.class.getName()).log(Level.SEVERE, null, ex);
                }
                
                metadataMatcher = pattern.matcher(metadata);
                
                if(nameMatcher.find() || metadataMatcher.find()){
                    results.add(file);
                }
            }
            else
            if(file.isDirectory()){
                //try again with the next directory.
                
                File currentDirSave = new File(workDir.getPath());
                workDir = file;
                recursiveFind(pattern,results);
               
                workDir = currentDirSave; //revert to old current dir.
            }
        }
        
    }
    
    
    // directory/file methods:
    /**
     * 
     * @param path
     * @return The file at the specified path
     * @throws FileNotFoundException 
     */
    public File getFile(String path) throws FileNotFoundException{
        File file = new File(workDir, path);
               
        if(!file.exists()){
            throw new FileNotFoundException("\"" + file.getAbsolutePath() + "\" does not exist.");
        }
        return file;
    }
}
