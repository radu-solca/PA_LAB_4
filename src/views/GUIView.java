/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package views;

import controllers.ApplicationController;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.*;


public class GUIView extends JFrame implements View, TreeSelectionListener{

    private ApplicationController controller;
    private JScrollPane leftPane;
    private JScrollPane rightPane;
    private JTree fileTree;
    private final TreePopupMenu popup = new TreePopupMenu();
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

        try {
            Node root = new Node();
            Node audioRoot = new Node();
            Node favRoot = new Node();
            
            audioRoot.setUserObject(controller.getWorkDir().getCanonicalFile());
            audioRoot.setName(controller.getWorkDir().getName());
            buildFileTree(audioRoot);
            
            favRoot.setUserObject(controller.getFavDir().getCanonicalFile());
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
            tree.addMouseListener(mouseListener);
            
            return tree;
        } catch (IOException ex) {
            Logger.getLogger(GUIView.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
    private void buildFileTree(Node root){
        List<File> list = (List<File>)controller.processCommand("list","").getReturnValue();
        list.stream().forEach((f) -> {
            
            try {
                Node node = new Node();
                node.setUserObject(f.getCanonicalFile());
                node.setName(f.getName());
                
                root.add(node);
                if (f.isDirectory()) {
                    controller.processCommand("cd",f.getName());
                    buildFileTree(node);
                    controller.processCommand("cd","..");
                }
            } catch (IOException ex) {
                Logger.getLogger(GUIView.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
    }
    
    private void buildFavTree(Node root){
        List<File> list = (List<File>) controller.processCommand("lfav","").getReturnValue();
        list.stream().forEach((f) -> {
            try {
                Node node = new Node();
                node.setUserObject(f.getCanonicalFile());
                node.setName(f.getName());
                root.add(node);
            } catch (IOException ex) {
                Logger.getLogger(GUIView.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
    }

    @Override
    public void valueChanged(TreeSelectionEvent e) {
        if(e.getNewLeadSelectionPath() != null){
            Node node = (Node)e.getNewLeadSelectionPath().getLastPathComponent();
            //System.out.println (node.toString());
            File nodeFile = (File)node.getUserObject();
            
            if (nodeFile.isFile()) {
                
                JTextArea textArea = buildFileDataTextArea(nodeFile);
                this.rightPane.setViewportView(textArea);
            }
            else if(nodeFile.isDirectory()){
                JTable table = buildDirDataTable(nodeFile);
                this.rightPane.setViewportView(table);
            }
        }
    }
    
    private JTextArea buildFileDataTextArea(File file){
        Map<String,String> metaData = (Map<String,String>) controller.processCommand("info",file.getAbsolutePath()).getReturnValue();
        String stringData = "";

        //add metadata in stringData:
        stringData = metaData.entrySet().stream().map((entry) -> entry.getKey() + ": " + entry.getValue() + "\n").reduce(stringData, String::concat);

        JTextArea displayData = new JTextArea(stringData);
        return displayData;
    }
    
    private JTable buildDirDataTable(File dir){
        List<Object> tableNames = new ArrayList<>();
        tableNames.add("Title");
        tableNames.add("Artist");
        tableNames.add("Album");
        tableNames.add("ReleaseDate");
        tableNames.add("Genre");
        List<List<Object>> tableData = new ArrayList<>();

        List<File> files = (List<File>) controller.processCommand("list", dir.getPath()).getReturnValue();

        files.stream().filter((file) -> (file.isFile())).map((file) -> ((Map<String,String>) controller.processCommand("info", file.getPath()).getReturnValue())).map((metaData) -> {
            List<Object> row = new ArrayList<>();
            metaData.entrySet().stream().forEach((entry) -> {
                row.add(entry.getValue());
            });
            return row;
        }).forEach((row) -> {
            tableData.add(row);
        });
        
        Object[] tempNames = tableNames.toArray(); // return Object[]

        Object[][] tempData = new Object[tableData.size()][]; 

        int i = 0;
        for (List<Object> next : tableData) {
            tempData[i++] = next.toArray(new Object[next.size()]); // return Object[][]
        }

        JTable table = new JTable(tempData, tempNames);
        return table;
    }
    
    
    //listeners:
    private final MouseListener mouseListener = new MouseAdapter() {

        @Override
        public void mouseReleased(MouseEvent e) {
            
            //int selRow = fileTree.getRowForLocation(e.getX(), e.getY());
            
            
            TreePath selPath = fileTree.getPathForLocation(e.getX(), e.getY());
            if(selPath != null){
                System.out.println(selPath);

                File subject = (File)((Node)selPath.getLastPathComponent()).getUserObject();
                if(e.getButton() == MouseEvent.BUTTON3 && subject.isFile()){
                    System.out.println("Detected Mouse Right Click!");

                    popup.setSubject(subject);
                    popup.show(leftPane, e.getX(), e.getY());

                }
            }
        }
    };
    
    private class TreePopupMenu extends JPopupMenu{

    private File subject;
    public void setSubject(File subject){
        this.subject = subject;
    }
    
    public TreePopupMenu() {
        JMenuItem addFav = new JMenuItem("Add to favourites");
        JMenuItem play = new JMenuItem("Play");
        JMenuItem search = new JMenuItem("Search on the web");
        
        addFav.addActionListener((ActionEvent e) -> {
            controller.processCommand("fav", subject.getAbsolutePath());
            fileTree = buildTree();
            leftPane.setViewportView(fileTree);
        });
        
        this.add(addFav);
        
        play.addActionListener((ActionEvent e) -> {
            controller.processCommand("play", subject.getAbsolutePath());
        });
        
        this.add(play);
        
        search.addActionListener((ActionEvent e) -> {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        });
        
        this.add(search);
    }
    
    
}
}
