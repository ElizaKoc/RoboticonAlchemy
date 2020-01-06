/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package roboticonalchemy;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.List;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

/**
 *
 * @author Eliza Kocic 165518
 */
public class Kampania {
    
    //description info
    private String campaignName;
    private String description;
    
    private final int campaignId;
    private final ArrayList<Poziom> levels;
    
    private final Image logoCampaign;
    
    public Kampania(int campaignId) throws SlickException{

        this.campaignId = campaignId;
        this.levels = new ArrayList<>();
        loadDescription();
        loadLevelsList();
        this.logoCampaign = new Image("..\\campaigns\\" + Integer.toString(campaignId) + "\\logo.png");       
    }
    
    private void loadDescription(){
        
        try{
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = documentBuilderFactory.newDocumentBuilder();
            Document document = builder.parse( new File( "..\\campaigns\\" + Integer.toString(campaignId) + "\\description.xml") );
            Element rootElement = document.getDocumentElement();
            validateFile(rootElement);
         
            this.campaignName = rootElement.getAttribute("CampaignName");
            this.description = rootElement.getAttribute("Description");
            
        } catch (Exception ex) {
                ex.printStackTrace();
        }
        
    }
    
    private void validateFile(Element rootElement) throws Exception  {
        if (!rootElement.getNodeName().equals("campaign")) {
                throw new Exception("Not a file containing campaigns");
        }
    }
    
    public String getCampaignName(){
        return campaignName;
    }
    
    public String getDescription(){
        return description;
    }
    
    public int getCampaignId(){
        return campaignId;
    }
    
    public ArrayList<Poziom> getLevels(){
        return levels;
    }
    
    public Image getLogo(){
        return logoCampaign;
    }
    
    public void loadLevelsList() {
        
        levels.clear(); 
        try {
            String[] levelIds = loadLevelIdList();
            
            for(String levelId : levelIds) {
                levels.add(new Poziom(Integer.parseInt(levelId), this));
            }                                           
        } catch (Exception ex) {
                ex.printStackTrace();
        }
    }
    
    private String[] loadLevelIdList() {
        
        String path = "..\\campaigns\\" + Integer.toString(campaignId) + "\\levels\\";
        
        File file = new File(path);
        String[] directories = file.list(new FilenameFilter() {
          @Override
          public boolean accept(File current, String name) {
            return new File(current, name).isDirectory();
          }
        });
        
        return directories;
    }
}
