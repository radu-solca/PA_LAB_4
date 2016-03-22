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

/**
 *
 * @author NASA
 */
public class FavouritesManager implements Serializable {
    
    private Set<File> favourites;
    private final File storage;

    public FavouritesManager(String storage){
        storage = "./" + storage;
        this.storage = new File(storage);
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
            this.storage.createNewFile();
        } catch (IOException ex) {
            Logger.getLogger(FavouritesManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        try(    FileOutputStream fos = new FileOutputStream(storage);
                ObjectOutputStream oos = new ObjectOutputStream(fos);) {

            oos.writeObject(favourites);
            
        } catch (IOException ex) {
            Logger.getLogger(FavouritesManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
    private void loadFavourites(){
        if(storage.exists()&&storage.isFile()){
            try(    FileInputStream fis = new FileInputStream(storage);
                    ObjectInputStream ois = new ObjectInputStream(fis)  ){
            
            favourites = (Set<File>) ois.readObject();
            
            } catch (IOException | ClassNotFoundException ex) {
                Logger.getLogger(FavouritesManager.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
