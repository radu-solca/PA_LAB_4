package models;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import java.beans.XMLDecoder;
import java.beans.XMLEncoder;

import java.util.HashMap;
import java.util.Map;

public class FavManager{
    
    private final List<File> favourites;
    private final File favDir;
    private final File favFile;
    private final File reportFile;

    public FavManager(String favDirPath){
        favDirPath = "./" + favDirPath;
        
        this.favDir = new File(favDirPath);
        if(!favDir.exists()||!favDir.isDirectory()){
            if(!favDir.mkdirs()){
                IOException ex = new IOException("Could not create directory " + favDir.getAbsolutePath());
                Logger.getLogger(FileManager.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        favFile = new File(favDir,"/favourites.xml");
        reportFile = new File(favDir,"/report.html");
        
        favourites = new ArrayList<>();
        loadFavourites();
    }
    
    public void addFavourite(File file){
        favourites.add(file);
        saveFavourites();
    }
    
    public void removeFavourite(File file){
        favourites.remove(file);
        saveFavourites();
    }
    
    public List<File> getFavourites(){
        return new ArrayList<>(favourites);
    }
    
    private void saveFavourites(){
        
        List<String> stringFavs = new ArrayList<>();
        
        favourites.stream().forEach((File f) -> {
            stringFavs.add(f.getPath());
        });
        
        try {
            favFile.createNewFile();
        } catch (IOException ex) {
            Logger.getLogger(FavManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        try(    FileOutputStream fos = new FileOutputStream(favFile);
                BufferedOutputStream bos = new BufferedOutputStream(fos);
                XMLEncoder encoder = new XMLEncoder(bos);) {

            encoder.writeObject(stringFavs);
            
        } catch (IOException ex) {
            Logger.getLogger(FavManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void loadFavourites(){
        List<String> stringFavs = new ArrayList<>();
        
        if(favFile.exists()&&favFile.isFile()){
            try(    FileInputStream fis = new FileInputStream(favFile);
                    BufferedInputStream bis = new BufferedInputStream(fis);
                    XMLDecoder decoder = new XMLDecoder(bis);){
            
            stringFavs = (List<String>) decoder.readObject();
            
            } catch (IOException ex) {
                Logger.getLogger(FavManager.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        favourites.clear();
        stringFavs.stream().forEach((String s) -> {
            favourites.add(new File(s));
        });
    }
    
    /**
     * Writes a html report in favDir/report.html
     */
    public void makeReport(){
        //File currFile = new File(getClass().getResource("report_template.html").getFile());
        //System.err.println(currFile);
        
        Configuration cfg = new Configuration(Configuration.VERSION_2_3_23);
        String templateString = "<html>\n" +
                                "	<body>\n" +
                                "		<ul>\n" +
                                "			<#list favs as fav>\n" +
                                "			    <li>\n" +
                                "			    	<a href=\"${fav}\">${fav}</a>\n" +
                                "			    </li>\n" +
                                "			</#list>\n" +
                                "		</ul> \n" +
                                "	</body>\n" +
                                "</html>";
        try{
            cfg.setClassForTemplateLoading(this.getClass(), "/");
            //Template template = cfg.getTemplate("report_template.html");
            Template template = new Template("templateName", new StringReader(templateString), cfg);

            List<File> cannonicalFavs = new ArrayList<>();
            favourites.stream().forEach((File f) ->{
                try {
                    cannonicalFavs.add(f.getCanonicalFile());
                } catch (IOException ex) {
                    Logger.getLogger(FavManager.class.getName()).log(Level.SEVERE, null, ex);
                }
            });
            
            Map<String, Object> data = new HashMap<>();
            data.put("favs", cannonicalFavs);

            Writer reportWriter = new FileWriter(reportFile);
            template.process(data, reportWriter);
            
        } catch (IOException | TemplateException ex) {
            Logger.getLogger(FavManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public File getFavDir(){
        return favDir;
    }
}
