/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package models;

import java.io.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import java.util.HashMap;
import java.util.Map;


/**
 *
 * @author NASA
 */
public class FavManager implements Serializable {
    
    private Set<File> favourites;
    private final File favDir;
    private final File favFile;
    private final File reportFile;

    public FavManager(String favDirPath){
        favDirPath = "./" + favDirPath;
        
        this.favDir = new File(favDirPath);
        if(!favDir.exists()||!favDir.isDirectory()){
            if(!favDir.mkdirs()){
                IOException ex = new IOException("Could not create directory " + favDir.getAbsolutePath());
                Logger.getLogger(FileManager.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        favFile = new File(favDir,"/favourites.ser");
        reportFile = new File(favDir,"/report.html");
        
        favourites = new HashSet<>();
        loadFavourites();
    }
    
    public void addFavourite(File file){
        favourites.add(file);
        saveFavourites();
    }
    
    public void removeFavourite(File file){
        favourites.remove(file);
        saveFavourites();
    }
    
    public List<File> getFavourites(){
        return new ArrayList<>(favourites);
    }
    
    private void saveFavourites(){
        
        try {
            favFile.createNewFile();
        } catch (IOException ex) {
            Logger.getLogger(FavManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        try(    FileOutputStream fos = new FileOutputStream(favFile);
                ObjectOutputStream oos = new ObjectOutputStream(fos);) {

            oos.writeObject(favourites);
            
        } catch (IOException ex) {
            Logger.getLogger(FavManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void loadFavourites(){
        
        if(favFile.exists()&&favFile.isFile()){
            try(    FileInputStream fis = new FileInputStream(favFile);
                    ObjectInputStream ois = new ObjectInputStream(fis)  ){
            
            favourites = (Set<File>) ois.readObject();
            
            } catch (IOException | ClassNotFoundException ex) {
                Logger.getLogger(FavManager.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    /**
     * Writes a html report in favDir/report.html
     */
    public void makeReport(){
        Configuration cfg = new Configuration(Configuration.VERSION_2_3_23);
          
        
        try{
            cfg.setDirectoryForTemplateLoading(this.favDir);
            Template template = cfg.getTemplate("report_template.html");
            
            Map<String, Object> data = new HashMap<>();
            data.put("favs", favourites);

            Writer reportWriter = new FileWriter(reportFile);
            template.process(data, reportWriter);
            
        } catch (IOException | TemplateException ex) {
            Logger.getLogger(FavManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
