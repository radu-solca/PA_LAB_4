/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package views;

import models.AbstractViewableModel;

/**
 *
 * @author NASA
 */
public interface View {
    
    void update(AbstractViewableModel model);
}
