/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package roboticonalchemy;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import java.lang.Object;
import org.newdawn.slick.Image;
import org.newdawn.slick.UnicodeFont;

import org.lwjgl.openal.AL;
import org.newdawn.slick.Color;
import org.newdawn.slick.Input;
import org.newdawn.slick.Music;
import org.newdawn.slick.Sound;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.state.transition.FadeInTransition;
import org.newdawn.slick.state.transition.FadeOutTransition;

/**
 *
 * @author Eliza Kocic 165518 
 */
public class MenuGlowne extends BasicGameState{

    /**
     * @param gamename
     */  
    private StateBasedGame game;
    
    private UnicodeFont myFont;
    private Sound musicButton;
    private Sound musicButton2;
    private Music musicBackground;
    
    private Image buttonExit;
    
    public static final int ID = 1;
    
    private Image menu;  //źródło: http://eskipaper.com/steampunk-wallpaper-1.html
    private Image logo;
    private Image button;
    private Image buttonCampaign;
    private Image buttonHelp;
   
    
    private int selectedObjectIndex = 1;   
    
    public MenuGlowne(UnicodeFont myFont, Sound musicButton, Sound musicButton2, Music musicBackground)
    {
        this.myFont = myFont;
        this.musicButton = musicButton;
        this.musicButton2 = musicButton2;
        this.musicBackground = musicBackground;
    }
    
    @Override
    public void enter(GameContainer container, StateBasedGame game) throws SlickException {      
        
        super.enter(container, game);
        container.getInput().clearKeyPressedRecord();
    }
    
    @Override
    public int getID() {
      return ID;
   }

    @Override
    public void init(GameContainer gc, StateBasedGame game) throws SlickException {
        
        this.game = game;
        
        myFont.loadGlyphs();
        
        logo = new Image("..\\images\\RoboticonAlchemy-logo-transpsrent-bg3.png");
        menu = new Image("..\\images\\menu.jpg");
        button = new Image("..\\images\\trybikGold.png");
        buttonCampaign = new Image("..\\images\\buttons\\buttonCampaign.png");
        buttonHelp = new Image("..\\images\\buttons\\buttonHelp.png");   
        buttonExit = new Image ("..\\images\\buttons\\buttonExit.png");      
    }

    @Override
    public void update(GameContainer gc, StateBasedGame game, int i) throws SlickException {
        
        if(gc.getInput().isKeyPressed(Input.KEY_DOWN)) {
            if(selectedObjectIndex == 3){
                selectedObjectIndex = 1;
            }
            else {
                selectedObjectIndex = selectedObjectIndex + 1;
            } 
            
            musicButton.play(1, (float)0.75);
        }
        else if(gc.getInput().isKeyPressed(Input.KEY_UP)) {
            if(selectedObjectIndex == 1){
                selectedObjectIndex = 3;
            }
            else {
                selectedObjectIndex = selectedObjectIndex - 1;
            }  
            
            musicButton.play(1, (float)0.75);
        }
        else if(gc.getInput().isKeyPressed(Input.KEY_ENTER)) {
            
            musicButton2.play(1, (float)0.75);
            
            if(selectedObjectIndex == 1){
                game.enterState(MenuKampanii.ID, new FadeOutTransition(Color.black), new FadeInTransition(Color.black));
            }
            else if(selectedObjectIndex == 2){
                game.enterState(Pomoc.ID, new FadeOutTransition(Color.black), new FadeInTransition(Color.black));
            }
            else if(selectedObjectIndex == 3) {
                AL.destroy();
                System.exit(0);
            }
        }
        else if(gc.getInput().isKeyPressed(Input.KEY_SPACE)) {
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
        
        menu.draw(0, 0, 1920, 1070);               
        
        logo.draw(140, 60, (float)1.2);
        
        buttonCampaign.draw(480, 500, (float)0.3);
        buttonHelp.draw(480, 675, (float)0.3);
        buttonExit.draw(480, 850, (float)0.3);
        
        button.draw(1270,(365+(selectedObjectIndex*175)), (float)0.15);
        button.draw(580,(365+(selectedObjectIndex*175)), (float)0.15);
    }    
}
