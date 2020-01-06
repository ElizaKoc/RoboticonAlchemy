/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package roboticonalchemy;

//import static jdk.internal.org.objectweb.asm.Type.getObjectType;
import java.awt.Point;
import java.io.File;
import java.util.ArrayList;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.tiled.TiledMap;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 *
 * @author Eliza Kocic 165518
 */
public class Poziom {
    
    private String levelName;
    private String levelOfDifficulty;
    private String description;
       
    private final Image logoLevel;
    
    private final int levelId;
    private final Kampania campaign;
    private final ArrayList<Obiekt> objectsList;
    private final ArrayList<Obiekt> crates;
    
    private TiledMap map;
    private Robot player;
    
    private int groupID = 0;
    private int objectID = 0;
    
    public Poziom(int levelId, Kampania campaign) throws SlickException{
        
        this.objectsList = new ArrayList<>();
        this.crates = new ArrayList<>();
        this.levelId = levelId;
        this.campaign = campaign;
        this.logoLevel = new Image( getLevelDir() + "logo.png");
        loadDescription();
    }
    
    private String getLevelDir() {
        return "..\\campaigns\\" + Integer.toString(campaign.getCampaignId()) + "\\levels\\" + Integer.toString(levelId) + "\\";
    }
    
    private void loadDescription(){
        
        try{
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = documentBuilderFactory.newDocumentBuilder();
            Document document = builder.parse( new File( getLevelDir() + "description.xml") );
            Element rootElement = document.getDocumentElement();
            validateFile(rootElement);
         
            this.levelName = rootElement.getAttribute("LevelName");
            this.description = rootElement.getAttribute("Description");
            this.levelOfDifficulty = rootElement.getAttribute("LevelOfDifficulty");
            
        } catch (Exception ex) {
                ex.printStackTrace();
        }
        
    }
    
    private void validateFile(Element rootElement) throws Exception  {
        if (!rootElement.getNodeName().equals("level")) {
            throw new Exception("Not a file containing levels");
        }
    }
    
    public Robot getPlayer() {
        return player;
    }
    
    private void addPlayer(int objectID) throws SlickException{
         
        int x = map.getObjectX(groupID, objectID)  + 448;
        int y = map.getObjectY(groupID, objectID);

        player = new Robot(x, y, this);         
               
    }
    
    private void addObjects() throws SlickException{
        
        int objectsGroupID = 0;
        int objectsInGroup = map.getObjectCount(objectsGroupID);
        
        
        for (int objectID = 0; objectID < objectsInGroup; objectID++) {
            
            String objectType = map.getObjectType(objectsGroupID, objectID);
            
            if(objectType.equals("Robot")){
                addPlayer(objectID);
            }
            else if(objectType.equals("Crate")) {
                
                int x = map.getObjectX(groupID, objectID)  + 448;
                int y = map.getObjectY(groupID, objectID);
                
                Obiekt crate = new Obiekt(x, y, this, "..\\images\\crate.png");
                crate.setPush(true);
                crates.add(crate);
            }
        }
    }
    
    public int getCampaignIdForSelectedLevel() {
        return campaign.getCampaignId();
    }
    
    public String getLevelName() {
        return levelName;
    } 
      
    public int getLevelId() {
        return levelId;
    }   
     
    public String getLevelOfDifficulty() {
        return levelOfDifficulty;
    }
    
    public Image getLogo(){
        return logoLevel;
    }
    
    public String getDescription(){
        return description;
    }
      
    public TiledMap getMap() {
        return map;
    }
        
    public void loadMap() throws SlickException{  
       map = new TiledMap(getLevelDir() + "map\\poziom.tmx");
       addObjects();
    }
    
    public void renderMap() {
        map.render(448,0);
    }
    
    private boolean playerStandingOnFinishPoint() {
        Point p = player.getPosition();
        int x = (int)p.getX();
        int y = (int)p.getY();
        boolean isObjectiveCompleted = false;
        int tileID = map.getTileId((x-448)/64, y/64, 0);
        String completed = map.getTileProperty(tileID, "finish", "false");
        if(completed.equals("true")){
            isObjectiveCompleted = true;           
        }
        return isObjectiveCompleted;
    }
    
    private boolean isPlayerInFrontOfTable() {
        
        Point p = player.getPosition();
        int x = (int)p.getX();
        int y = (int)p.getY();
        int tileID;
        
        switch(player.getDirection()) {
            case up:
                tileID = map.getTileId((x-448)/64, (y-64)/64, 1);
                break;
            case down:
                tileID = map.getTileId((x-448)/64, (y+64)/64, 1);
                break;
            case right:
                tileID = map.getTileId((x-384)/64, y/64, 1);
                break;
            case left:
                tileID = map.getTileId((x-512)/64, y/64, 1);
                break;
            default:
                return false;
        }

        String completed = map.getTileProperty(tileID, "Table", "false");
        
        if(completed.equals("false")){
            return false;           
        }
        return true;
    }
    
    private boolean allCratesOnPoints() {
        
        if(crates.size() == 0) {
            return false;
        }
        
        for(Obiekt crate : crates) {
            Point p = crate.getPosition();
            int x = (int)p.getX();
            int y = (int)p.getY();
            int tileID = map.getTileId((x-448)/64, y/64, 0);
            String completed = map.getTileProperty(tileID, "CrateFinish", "false");
            if(completed.equals("false")){
                return false;           
            }
        }
        return true;
    }
    
    public boolean isObjectiveCompleted() {
        return allCratesOnPoints() || playerStandingOnFinishPoint() || isPlayerInFrontOfTable();
    }
    
    public boolean tileIsPassable(int x, int y) {
        boolean isPassable = false;      
        
        if(((x-448)/64 < map.getWidth()) && ((x-448)/64 >= 0)) {
            if((y/64 < map.getHeight()) && (y/64 >= 0)) {
                
                int tileID = map.getTileId((x-448)/64, y/64, 1);
                String passable = map.getTileProperty(tileID, "Collidable", "false");
                if(passable.equals("false")){
                    isPassable = true;           
                }
            }            
        }       
        return isPassable;
    }
    
    public Obiekt getTileObject(int x, int y) {
        
        for(Obiekt object : objectsList) {
            Point positionObject = object.getPosition();
            if(x == positionObject.getX() && y == positionObject.getY()) {
                return object;
            }
        }
        return null;
    }
    
    public void addObject(Obiekt object) {
        this.objectsList.add(object);
    }
    
    public void drawObjects() {
        for(Obiekt object : objectsList) {
            object.draw();
        }
    }
    
    public void resetObjects() {
        for(Obiekt object : objectsList) {
            object.resetPosition();
        }
    }
}
