/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package views;

import controllers.ApplicationController;

/**
 *
 * @author NASA
 */
public interface View {
    
    /**
     *
     * @param controller
     */
    void start(ApplicationController controller);
}
