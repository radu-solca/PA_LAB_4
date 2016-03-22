/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package models;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 *
 * @author NASA
 */
public class FavouritesManager implements Serializable {
    
    private final Set<File> favourites;
    private final File storage;

    public FavouritesManager(String storage){
        storage = "./" + storage;
        this.storage = new File(storage);
        favourites = new HashSet<>();
        
        loadFavourites();
    }
    
    public void addFavourite(File file) throws IOException{
        favourites.add(file);
        saveFavourites();
    }
    
    public void removeFavourite(File file) throws IOException{
        favourites.remove(file);
        saveFavourites();
    }
    
    public List<File> getFavourites(){
        return new ArrayList<>(favourites);
    }
    
    private void saveFavourites() throws IOException{
        this.storage.createNewFile();
    }
    
    private void loadFavourites(){
        
    }
}
