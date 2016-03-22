/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package models;

import java.util.ArrayList;
import java.util.List;
import views.View;

/**
 *
 * @author NASA
 */
public abstract class AbstractViewableModel implements Model {
    private final List<View> views;

    public AbstractViewableModel() {
        views = new ArrayList<>();
    }
    
    @Override
    public void addView(View view){
        views.add(view);
    }
    
    @Override
    public void removeView(View view){
        views.remove(view);
    }
    
    private void notifyViews(){
        views.stream().forEach((view) -> {
            view.update(this);
        });
    }
}
