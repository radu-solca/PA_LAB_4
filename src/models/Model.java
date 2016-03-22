/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package models;

import views.View;

/**
 *
 * @author NASA
 */
public interface Model {
    
    public void addView(View view);
    
    public void removeView(View view);
}
