/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package roboticonalchemy;

import java.util.ArrayList;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.Music;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.Sound;
import org.newdawn.slick.UnicodeFont;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.state.transition.FadeInTransition;
import org.newdawn.slick.state.transition.FadeOutTransition;
import static roboticonalchemy.MenuKampanii.ID;

/**
 *
 * @author Eliza Kocic 165518
 */
public class MenuPoziomow extends BasicGameState {
    
    private StateBasedGame game;
    
    private UnicodeFont myFont;
    private Sound musicButton;
    private Sound musicButton2;
    private Music musicBackground;
    
    public static final int ID = 4;
    
    private Image campaignsBackground; //żródło: https://www.goodfon.com/wallpaper/stimpank-oboi-chasy-stil.html
    private Image descriptionBackground;
    private Image levelImage;
    
    private Image buttonBack; 
    private Image buttonRank;
    private Image buttonCancel;
    private Image buttonChoice;
    private Image button;
    
    
    private int selectedObjectIndex = 1;    
    
    private OknoGry gameWindow;
    private PodsumowaniePoziomu levelSummary;
    private Poziom selectedLevel;
    
    private Kampania campaign;
    
    private boolean chooseLevel = false;
    private boolean enterPerformedForLevel = false;
    private boolean isSelect = false;
    
    private int levelCursorIndex = 0;
    private int index = 0;
    private int chooseLevelIndex = 0;     
    private int selectCampaingIndex = 0;
    private int confirmCampaignIndex = 0;
    private int backCursorIndex = 1;
    
    public MenuPoziomow(UnicodeFont myFont, Sound musicButton, Sound musicButton2, Music musicBackground, OknoGry gameWindow, PodsumowaniePoziomu levelSummary) 
    {
        this.myFont = myFont;
        this.musicButton = musicButton;
        this.musicButton2 = musicButton2;
        this.levelSummary = levelSummary;
        this.musicBackground = musicBackground;
        this.gameWindow = gameWindow;
    }
    
    public void setCampaign(Kampania selectedCampaign) {
        
        campaign = selectedCampaign;        
    }
    
    private void drawLevelList() {
        
        Image logo;
        index = 1;         

        for(Poziom level : campaign.getLevels()){
            logo = level.getLogo();
            logo.draw(115, 250 + (160*index), (float)0.35);
            index++;
        }
    }
    
    private void goToGameWindow() {
        gameWindow.setLevel(selectedLevel);
        game.enterState(OknoGry.ID, new FadeOutTransition(Color.black), new FadeInTransition(Color.black));
    }
    
    private void getSelectedLevelId() {
    selectedLevel = campaign.getLevels().get(levelCursorIndex - 1); 
    }
    
    private void drawLevelDescription() {
        
        String levelName = selectedLevel.getLevelName();
        String levelOfDifficulty = selectedLevel.getLevelOfDifficulty();
        String description = selectedLevel.getDescription();
        
        myFont.drawString(800, 200, "Nazwa poziomu: " + levelName , org.newdawn.slick.Color.yellow);
        myFont.drawString(800, 300, "Poziom trudności: " + levelOfDifficulty , org.newdawn.slick.Color.yellow);
        myFont.drawString(800, 400, description , org.newdawn.slick.Color.yellow);
    }
    
    @Override
    public void enter(GameContainer container, StateBasedGame game) throws SlickException {
        super.enter(container, game);
        container.getInput().clearKeyPressedRecord();
        
        enterPerformedForLevel = false;
        chooseLevelIndex = 0;
        isSelect = false;
        campaign.loadLevelsList();
    }
    
    @Override
    public int getID() {
      return ID;
    }

    @Override
    public void init(GameContainer gc, StateBasedGame game) throws SlickException {
        
        this.game = game;      
    
        levelImage = new Image("..\\images\\buttons\\level.png");
        campaignsBackground = new Image("..\\images\\campaigns.jpg");
        descriptionBackground = new Image("..\\images\\description.png");
               
        buttonBack = new Image ("..\\images\\buttons\\buttonBackSmaller2.png");
        buttonRank = new Image("..\\images\\buttons\\ranking.png");
        buttonCancel = new Image ("..\\images\\buttons\\buttonCancel.png");
        buttonChoice = new Image ("..\\images\\buttons\\buttonChoice.png");
        button = new Image("..\\images\\trybikGold.png");
    }

    @Override
    public void update(GameContainer gc, StateBasedGame game, int i) throws SlickException {
        
        if(!isSelect) {
            
            if(gc.getInput().isKeyPressed(Input.KEY_DOWN)) {    
            
                if(chooseLevel){
                    if(levelCursorIndex < index - 1) {
                        levelCursorIndex = levelCursorIndex + 1;
                    }   
                    else if(levelCursorIndex >= index - 1) {
                        chooseLevel = false;
                        backCursorIndex = 1;
                    }                                 
                }
                else {
                    chooseLevel = true;
                    levelCursorIndex = 1;
                    backCursorIndex = 0;
                } 

                musicButton.play(1, (float)0.75);
            }
            
            else if(gc.getInput().isKeyPressed(Input.KEY_UP)) {

                if(backCursorIndex == 1){
                    chooseLevel = true;
                    levelCursorIndex = index - 1;
                    backCursorIndex = 0;
                }
                else if (levelCursorIndex == 1) {
                    chooseLevel = false;
                    backCursorIndex = 1;
                }
                else {
                    levelCursorIndex = levelCursorIndex - 1;              
                }  

                musicButton.play(1, (float)0.75);
            }
            
            else if(gc.getInput().isKeyPressed(Input.KEY_ENTER)) {
            
                musicButton2.play(1, (float)0.75);

                if(chooseLevel) {
                    enterPerformedForLevel = true;  
                    chooseLevelIndex = 2;
                    isSelect = true;
                }
                else {
                   if(backCursorIndex == 1){

                        game.enterState(MenuKampanii.ID, new FadeOutTransition(Color.black), new FadeInTransition(Color.black));
                        enterPerformedForLevel = false;
                    } 
                }
            }               
        }       
        else {
            if(gc.getInput().isKeyPressed(Input.KEY_ENTER)) {
                
                musicButton2.play(1, (float)0.75);
                
                if(chooseLevelIndex == 2) {
                    goToGameWindow();                    
                }
                else if(chooseLevelIndex == 1) {
                    
                    enterPerformedForLevel = false;
                    chooseLevelIndex = 0;
                    isSelect = false;
                }
                else if(chooseLevelIndex == 3) {
                    levelSummary.loadScoresFromMenuLevel(selectedLevel);
                    game.enterState(PodsumowaniePoziomu.ID, new FadeOutTransition(Color.black), new FadeInTransition(Color.black));
                }
            }
            
            else if(gc.getInput().isKeyPressed(Input.KEY_RIGHT)) {
                
                if(chooseLevelIndex == 1) {                                       
                    chooseLevelIndex = 2;
                }
                else if(chooseLevelIndex == 2) {
                    chooseLevelIndex = 1;
                }
                
                musicButton.play(1, (float)0.75);
            }
            
            else if(gc.getInput().isKeyPressed(Input.KEY_LEFT)) {
                
                if(chooseLevelIndex == 2) {                                       
                    chooseLevelIndex = 1;
                }
                else if(chooseLevelIndex == 1) {
                    chooseLevelIndex = 2;
                }
                
                musicButton.play(1, (float)0.75);
            }
            
            else if(gc.getInput().isKeyPressed(Input.KEY_UP)) {
                
                musicButton.play(1, (float)0.75);
                
                if(chooseLevelIndex < 3){
                    chooseLevelIndex = 3;
                }    
                else {
                    chooseLevelIndex = 2;
                }
            }
            
            else if(gc.getInput().isKeyPressed(Input.KEY_DOWN)) {
                
                musicButton.play(1, (float)0.75);
                
                if(chooseLevelIndex == 3) {
                    chooseLevelIndex = 2;
                } 
                else {
                    chooseLevelIndex = 3;
                }
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
        levelImage.draw(70, 150, (float)0.35);
        descriptionBackground.draw(720, 90, 1160, 840);   
        
        drawLevelList();
                             
        buttonBack.draw(70, 950, (float)0.35);        
        
        if(!chooseLevel) {
            button.draw(530, 970, (float)0.09);
            button.draw(130, 970, (float)0.09);
        }
        else {
                        
            if(enterPerformedForLevel) {
                
                buttonRank.draw(800, 740, (float)0.35);
                buttonCancel.draw(790, 950, (float)0.30);
                buttonChoice.draw(1340, 950, (float)0.30);                
                
                if(chooseLevelIndex == 3) {
                    button.draw(860, 760, (float)0.085);
                    button.draw(1260, 760, (float)0.085);
                }
                else {
                    button.draw(290 + (550*chooseLevelIndex), 965, (float)0.085);
                    button.draw(630 + (550*chooseLevelIndex), 965, (float)0.085);
                }              
            }
            else {
                button.draw(50, 260 + (160*levelCursorIndex), (float)0.125);
                button.draw(590, 260 + (160*levelCursorIndex), (float)0.125);
            }
            
            getSelectedLevelId();
            drawLevelDescription();
        }                                           
    }
}
