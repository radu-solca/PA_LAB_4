package models;

import java.awt.Desktop;
import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.*;
import org.apache.tika.exception.*;
import org.apache.tika.metadata.*;
import org.apache.tika.parser.*;
import org.apache.tika.sax.*;
import org.xml.sax.SAXException;


public class FileManager{
    
    private File currentDir; //can only be a directory.

    /**
     * Initializes the application's working directory with the provided path.
     * Note that the path is relative to the application's root directory.
     * 
     * If the provided directory does not exist, the function will create it. If this fails, a IOException is thrown.
     * 
     * @param path
     * @throws IOException 
     */
    public FileManager(String path) throws IOException {
        
        path = "./" + path;
        
        currentDir = new File(path);
        if(!currentDir.exists()||!currentDir.isDirectory()){
            if(!currentDir.mkdirs()){
                throw new IOException("Could not create directory " + currentDir.getAbsolutePath());
            }
        }
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
    
    public String getInfo(String path) throws FileNotFoundException, IOException, SAXException, TikaException {
        File file = new File(currentDir, path);
        String info = "";
               
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
            
            info += "Title: " + (metadata.get("title") == null ? "N/A" : metadata.get("title")) + "\n";
            info += "Genre: " + (metadata.get("xmpDM:genre") == null ? "N/A" : metadata.get("xmpDM:genre")) + "\n";
            info += "Artist: " + (metadata.get("xmpDM:artist") == null ? "N/A" : metadata.get("xmpDM:artist")) + "\n";
            info += "Album: " + (metadata.get("xmpDM:album") == null ? "N/A" : metadata.get("xmpDM:album")) + "\n";
            info += "Release Date: " + (metadata.get("xmpDM:releaseDate") == null ? "N/A" : metadata.get("xmpDM:releaseDate")) + "\n";
            
        }
        
        return info;
    }
    
    public void play(String path) throws IOException{
        File file = new File(currentDir, path);  
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
            desktop.open(file);
        }
    }
    
    public List<File> find(Matcher matcher) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    
    // directory/file methods:
    
    public File getFile(String path) throws FileNotFoundException {
        throw new UnsupportedOperationException("Not supported yet.");
    }
        
    
    //auxiliary methods:
    
    private final FilenameFilter audioFilenameFilter = (File dir, String name) -> {
        String regex = ".([mM][pP]3|[wW][aA][vV]|[fF][lL][aA][cC])\\z";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(name);
        
        return matcher.find();
    };

    
    public void DEBUG_print(){
        System.out.println(currentDir.getAbsolutePath());
        //System.out.println(Arrays.toString(currentDir.list()));
    }

}
