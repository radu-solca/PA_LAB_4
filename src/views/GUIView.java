/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package views;

import controllers.ApplicationController;
import java.io.File;
import java.util.List;
import javax.swing.*;
import javax.swing.tree.*;

/**
 *
 * @author NASA
 */
public class GUIView extends JFrame implements View {

    JTree fileTree;
    ApplicationController controller;
    
    @Override
    public void start(ApplicationController controller){
        this.controller = controller;
        
        SwingUtilities.invokeLater(()->{
        
            this.setTitle("AudioLibraryManager");
            this.setDefaultCloseOperation(EXIT_ON_CLOSE);
            this.setSize(500, 300);

            
            JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
                    new JScrollPane(buildTree()), 
                    new JScrollPane());
            
            splitPane.setOneTouchExpandable(false);
            splitPane.setDividerLocation(250);
            
            this.add(splitPane);

            this.setVisible(true);
        });
        
    }
    
    private JTree buildTree(){
        DefaultMutableTreeNode root = new DefaultMutableTreeNode(controller.getWorkDir().getName());
        root.setUserObject(controller.getWorkDir());
        recursiveBuildTree(root);
        JTree tree = new JTree(root);
        
        tree.setShowsRootHandles(true);
        tree.setRootVisible(false);
        tree.setCellRenderer(new SystemIconRenderer());
        
        return tree;
    }
    
    private void recursiveBuildTree(DefaultMutableTreeNode root){
        List<File> list = (List<File>)controller.processCommand("list").getReturnValue();
        list.stream().forEach((f) -> {
            
//            ImageIcon imageIcon = (ImageIcon) fsv.getSystemIcon(f);
//            DefaultTreeCellRenderer renderer = new DefaultTreeCellRenderer();
//            renderer.setLeafIcon(imageIcon);
            
            DefaultMutableTreeNode node = new DefaultMutableTreeNode(f.getName());
            node.setUserObject(f);
            
            root.add(node);
            if (f.isDirectory()) {
                controller.processCommand("cd " + f.getName());
                recursiveBuildTree(node);
                controller.processCommand("cd ..");
            }
        });
    }
    
    
}
