/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package roboticonalchemy;

import java.io.File;
import java.io.FilenameFilter;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.Music;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.Sound;
import org.newdawn.slick.UnicodeFont;
import org.newdawn.slick.font.effects.ColorEffect;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.state.transition.FadeInTransition;
import org.newdawn.slick.state.transition.FadeOutTransition;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

/**
 *
 * @author Eliza Kocic 165518
 */
public class MenuKampanii extends BasicGameState{
    
    private StateBasedGame game;
    
    private UnicodeFont myFont;
    private Sound musicButton;
    private Sound musicButton2;
    private Music musicBackground;   
    
    private Image buttonBack; 
    private Image buttonCancel;
    private Image buttonChoice;
    private Image campaignImage;    
       
    private Image campaignsBackground; //żródło: https://www.goodfon.com/wallpaper/stimpank-oboi-chasy-stil.html
    private Image descriptionBackground;
    private Image button;
    
    public static final int ID = 2;
    
    private ArrayList<Kampania> campaigns;
    private Kampania selectedCampaign;   
    
    private MenuPoziomow levelMenu;
    
    private boolean chooseCampaign = false;
    private boolean enterPerformedForCampaign = false;
    private boolean isSelect = false;
    
    private int campaignCursorIndex = 0;
    private int index = 0;
    private int chooseCampaignIndex = 0;    
    private int selectCampaingIndex = 0;
    private int confirmCampaignIndex = 0;
    private int backCursorIndex = 1;
        
    public MenuKampanii(UnicodeFont myFont, Sound musicButton, Sound musicButton2, Music musicBackground, MenuPoziomow levelMenu) 
    {
        this.myFont = myFont;
        this.musicButton = musicButton;
        this.musicButton2 = musicButton2;
        this.musicBackground = musicBackground;
        this.campaigns = null;
        this.levelMenu = levelMenu;
    }
    
    private String[] loadCampaignIdList() {
        
        String path = "..\\campaigns\\";
        
        File file = new File(path);
        String[] directories = file.list(new FilenameFilter() {
          @Override
          public boolean accept(File current, String name) {
            return new File(current, name).isDirectory();
          }
        });
        
        return directories;
    }
    
    private void loadCampaignList() {
 
        campaigns.clear(); 
        try {
            String[] campaignIds = loadCampaignIdList();
            
            for(String campaignId : campaignIds) {
                campaigns.add(new Kampania(Integer.parseInt(campaignId)));
            }                                           
        } catch (Exception ex) {
                ex.printStackTrace();
        }
    }
    
    private void goToLevelMenu() {
        levelMenu.setCampaign(selectedCampaign);
        game.enterState(MenuPoziomow.ID, new FadeOutTransition(Color.black), new FadeInTransition(Color.black));
    }
    
    private void drawCampaignList() {

        Image logo;
        index = 1;         

        for(Kampania campaign : campaigns){
            logo = campaign.getLogo();
            logo.draw(115, 250 + (160*index), (float)0.35);
            index++;
        }
    }
     
    private void drawCampaignDescription() {
        
        String name;
        String description;
        name = selectedCampaign.getCampaignName();
        description = selectedCampaign.getDescription();
        myFont.drawString(800, 200, name , org.newdawn.slick.Color.yellow);
        myFont.drawString(800, 300, description , org.newdawn.slick.Color.yellow);
    }
    
    private void getSelectedCampaignId() {
        selectedCampaign = campaigns.get(campaignCursorIndex - 1); 
    }
    
    @Override
    public void enter(GameContainer container, StateBasedGame game) throws SlickException {
        super.enter(container, game);
        container.getInput().clearKeyPressedRecord();
        
        enterPerformedForCampaign = false;
        chooseCampaignIndex = 0;
        isSelect = false;
        loadCampaignList();
    }
    
    @Override
    public int getID() {
      return ID;
    }

    @Override
    public void init(GameContainer gc, StateBasedGame game) throws SlickException {
        
        this.game = game;      

        campaigns = new ArrayList<>();
        
        campaignImage = new Image ("..\\images\\buttons\\campaign.png");
        campaignsBackground = new Image("..\\images\\campaigns.jpg");
        descriptionBackground = new Image("..\\images\\description.png");
        
        buttonBack = new Image ("..\\images\\buttons\\buttonBackSmaller2.png");
        buttonCancel = new Image ("..\\images\\buttons\\buttonCancel.png");
        buttonChoice = new Image ("..\\images\\buttons\\buttonChoice.png");
        button = new Image("..\\images\\trybikGold.png");      
    }
    
    @Override
    public void update(GameContainer gc, StateBasedGame game, int i) throws SlickException {
            
        if(!isSelect) {
            
            if(gc.getInput().isKeyPressed(Input.KEY_DOWN)) {    
            
                if(chooseCampaign){
                    if(campaignCursorIndex < index - 1) {
                        campaignCursorIndex = campaignCursorIndex + 1;
                    }   
                    else if(campaignCursorIndex >= index - 1) {
                        chooseCampaign = false;
                        backCursorIndex = 1;
                    }                                 
                }
                else {
                    chooseCampaign = true;
                    campaignCursorIndex = 1;
                    backCursorIndex = 0;
                } 

                musicButton.play(1, (float)0.75);
            }
            
            else if(gc.getInput().isKeyPressed(Input.KEY_UP)) {

                if(backCursorIndex == 1){
                    chooseCampaign = true;
                    campaignCursorIndex = index - 1;
                    backCursorIndex = 0;
                }
                else if (campaignCursorIndex == 1) {
                    chooseCampaign = false;
                    backCursorIndex = 1;
                }
                else {
                    campaignCursorIndex = campaignCursorIndex - 1;              
                }  

                musicButton.play(1, (float)0.75);
            }
            
            else if(gc.getInput().isKeyPressed(Input.KEY_ENTER)) {
            
                musicButton2.play(1, (float)0.75);

                if(chooseCampaign) {
                    enterPerformedForCampaign = true;  
                    chooseCampaignIndex = 2;
                    isSelect = true;
                }
                else {
                   if(backCursorIndex == 1){
                        game.enterState(MenuGlowne.ID, new FadeOutTransition(Color.black), new FadeInTransition(Color.black));
                        enterPerformedForCampaign = false;
                    } 
                }
            }               
        }       
        else {
            if(gc.getInput().isKeyPressed(Input.KEY_ENTER)) {
                
                musicButton2.play(1, (float)0.75);
                
                if(chooseCampaignIndex == 2) {                                                           
                    goToLevelMenu();                     
                }
                else if(chooseCampaignIndex == 1) {
                    
                    enterPerformedForCampaign = false;
                    chooseCampaignIndex = 0;
                    isSelect = false;
                }
            }
            
            else if(gc.getInput().isKeyPressed(Input.KEY_RIGHT)) {
                
                if(chooseCampaignIndex == 1) {                                       
                    chooseCampaignIndex = 2;
                }
                else if(chooseCampaignIndex == 2) {
                    chooseCampaignIndex = 1;
                }
                
                musicButton.play(1, (float)0.75);
            }
            
            else if(gc.getInput().isKeyPressed(Input.KEY_LEFT)) {
                
                if(chooseCampaignIndex == 2) {                                       
                    chooseCampaignIndex = 1;
                }
                else if(chooseCampaignIndex == 1) {
                    chooseCampaignIndex = 2;
                }
                
                musicButton.play(1, (float)0.75);
            }
        }
        
        if(gc.getInput().isKeyPressed(Input.KEY_SPACE)) {
            if(musicBackground.playing()){
                musicBackground.pause();
            }
            else {
                musicBackground.resume();
            }
        }
    }

    @Override
    public void render(GameContainer gc, StateBasedGame game, Graphics g) throws SlickException
    {    
        campaignsBackground.draw(0, 0, 1920, 1080);
        campaignImage.draw(70, 150, (float)0.35);
        descriptionBackground.draw(720, 90, 1160, 840);   
        
        drawCampaignList();

        buttonBack.draw(70, 950, (float)0.35);        
        
        if(!chooseCampaign) {
            button.draw(530, 970, (float)0.09);
            button.draw(130, 970, (float)0.09);
        }
        else {
                        
            if(enterPerformedForCampaign) {

                buttonCancel.draw(790, 950, (float)0.30);
                buttonChoice.draw(1340, 950, (float)0.30);
                
                button.draw(290 + (550*chooseCampaignIndex), 965, (float)0.085);
                button.draw(630 + (550*chooseCampaignIndex), 965, (float)0.085);
            }
            else {
                button.draw(50, 260 + (160*campaignCursorIndex), (float)0.125);
                button.draw(590, 260 + (160*campaignCursorIndex), (float)0.125);
            }
            
            getSelectedCampaignId();
            drawCampaignDescription();
        }                                         
    }   
}
