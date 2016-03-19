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
import java.util.List;

/**
 *
 * @author NASA
 */
public class FavouritesManager implements Serializable {
    
    private final List<File> favourites;
    private final File storage;

    public FavouritesManager(String storage) throws IOException {
        storage = "./" + storage;
        
        this.storage = new File(storage);

        this.storage.createNewFile();
        
        favourites = new ArrayList<>();
    }
    
    public void addFavourite(File file){
        favourites.add(file);
    }
    
    public List<File> getFavourites(){
        return new ArrayList<>(favourites);
    }
}
