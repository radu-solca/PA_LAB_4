/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package views;

import controllers.ApplicationController;
import java.io.File;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import javax.swing.*;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.*;

/**
 *
 * @author NASA
 */
public class GUIView extends JFrame implements View, TreeSelectionListener{

    private ApplicationController controller;
    private JScrollPane leftPane;
    private JScrollPane rightPane;
    private JTree fileTree;
    private JTable dirTable;
    private JLabel fileLabel;
    
    @Override
    public void start(ApplicationController controller){
        this.controller = controller;
        
        SwingUtilities.invokeLater(()->{ //good practice for threads apparently.
        
            this.setTitle("Visual Audio Library");
            this.setDefaultCloseOperation(EXIT_ON_CLOSE);
            this.setSize(500, 300);
            
            leftPane = new JScrollPane();
            rightPane = new JScrollPane();
            fileTree = buildTree();

            leftPane.setViewportView(fileTree);
                        
            JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, leftPane, rightPane);
            
            splitPane.setOneTouchExpandable(false);
            splitPane.setDividerLocation(250);
            
            this.add(splitPane);

            this.setVisible(true);
        });
        
    }
    
    private JTree buildTree(){
        Node root = new Node();
        Node audioRoot = new Node();
        Node favRoot = new Node();
        
        audioRoot.setUserObject(controller.getWorkDir());
        audioRoot.setName(controller.getWorkDir().getName());
        buildFileTree(audioRoot);
        
        favRoot.setUserObject(controller.getFavDir());
        favRoot.setName(controller.getFavDir().getName());
        buildFavTree(favRoot);
        
        root.setUserObject(new File("."));
        root.add(audioRoot);
        root.add(favRoot);
        
        JTree tree = new JTree(root);
                
        tree.setShowsRootHandles(true);
        tree.setRootVisible(false);
        tree.setCellRenderer(new SystemIconRenderer());
        
        tree.addTreeSelectionListener(this);
        
        return tree;
    }
    
    private void buildFileTree(Node root){
        List<File> list = (List<File>)controller.processCommand("list").getReturnValue();
        list.stream().forEach((f) -> {
            
            Node node = new Node();
            node.setUserObject(f);
            node.setName(f.getName());
            
            root.add(node);
            if (f.isDirectory()) {
                controller.processCommand("cd " + f.getName());
                buildFileTree(node);
                controller.processCommand("cd ..");
            }
        });
    }
    
    private void buildFavTree(Node root){
        List<File> list = (List<File>) controller.processCommand("lfav").getReturnValue();
        list.stream().forEach((f) -> {
            Node node = new Node();
            node.setUserObject(f);
            node.setName(f.getName());
            root.add(node);
        });
    }

    @Override
    public void valueChanged(TreeSelectionEvent e) {
        if(e.getNewLeadSelectionPath() != null){
            Node node = (Node)e.getNewLeadSelectionPath().getLastPathComponent();
            //System.out.println (node.toString());
            File nodeFile = (File)node.getUserObject();
            
           
            
            if (nodeFile.isFile()) {
                Map<String,String> metaData = (Map<String,String>) controller.processCommand("info " + nodeFile.getName()).getReturnValue();
                String stringData = "";
                
                for(Map.Entry<String,String> entry : metaData.entrySet()){
                    stringData += entry.getKey() + ": " + entry.getValue() + "\n";
                }
                
                JTextArea displayData = new JTextArea(stringData);
                this.rightPane.setViewportView(displayData);
            }
            else if(nodeFile.isDirectory()){
                
            }
        }
    }
}



//tree.addTreeSelectionListener(new TreeSelectionListener() {
//            @Override
//            public void valueChanged(TreeSelectionEvent e) {
//                Node node = (Node) tree.getLastSelectedPathComponent();
//                /* if nothing is selected */
//                if (node == null) return;
//                /* retrieve the node that was selected */
//                Object nodeInfo = node.getUserObject();
//                if (((File)nodeInfo).isFile()) {
//                    String info = ((Map<String,String>)controller.processCommand("info "+((File)nodeInfo).getName()).getReturnValue()).toString();
//                    JTextArea textArea = new JTextArea(info);
//                    GUIView.this.rightPane.setViewportView(textArea);
//                }
//                else if(((File)nodeInfo).isDirectory()){
//                    Map<String,String> info = ((Map<String,String>)controller.processCommand("info "+((File)nodeInfo).getName()).getReturnValue());
//                    
//);
//                    List<String> columnNames
//                    columnNames = new ArrayList<>({"title","genre","album","artist","releaseDate"};
//                    List<List<String>> data;
//                    for(Map.Entry<String,String> entry : info.entrySet()){
//                        Object[] aux;
//                        
//                    }
//                }
//            }
//        });