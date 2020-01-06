/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package roboticonalchemy;

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

/**
 *
 * @author Eliza Kocic 165518
 */
public class Pomoc extends BasicGameState {
    
    private StateBasedGame game;
    
    private UnicodeFont myFont;
    private Sound musicButton2;
    private Music musicBackground;
    
    public static final int ID = 3;
    
    private Image help; 
    private Image buttonBack;   
    private Image button;
    
    public Pomoc(UnicodeFont myFont, Sound musicButton2, Music musicBackground) 
    {
        this.myFont = myFont;
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
               
        help = new Image("..\\images\\help.png");
        
        buttonBack = new Image ("..\\images\\buttons\\buttonBackSmaller2.png");
        button = new Image("..\\images\\trybikGold.png");
    }

    @Override
    public void update(GameContainer gc, StateBasedGame game, int i) throws SlickException {
               
        if(gc.getInput().isKeyPressed(Input.KEY_ENTER)) {
            
            musicButton2.play(1, (float)0.75);           
            game.enterState(MenuGlowne.ID, new FadeOutTransition(Color.black), new FadeInTransition(Color.black));              
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
        help.draw(0, 0, 1920, 1080);       

        buttonBack.draw(10, 950, (float)0.35);        
        button.draw(470, 970, (float)0.09);
        button.draw(70, 970, (float)0.09);                                                  
    }
}
