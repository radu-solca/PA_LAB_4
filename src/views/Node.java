/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package views;

import javax.swing.tree.DefaultMutableTreeNode;

/**
 *
 * @author NASA
 */
public class Node extends DefaultMutableTreeNode{
    String name;
    
    /**
     *
     * @param newName
     */
    public void setName(String newName){
        this.name = newName;
    }
    
    @Override
    public String toString(){
        return name;
    }
}
