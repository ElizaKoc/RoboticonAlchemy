/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package roboticonalchemy;

import java.awt.Font;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import java.lang.Object;
import org.newdawn.slick.Image;
import org.newdawn.slick.TrueTypeFont;
import org.newdawn.slick.UnicodeFont;
import org.newdawn.slick.font.effects.ColorEffect;
import org.newdawn.slick.font.effects.*;

import java.util.Scanner;
import javax.swing.JFrame;
import org.newdawn.slick.Input;
import org.newdawn.slick.Music;
import org.newdawn.slick.Sound;
import org.newdawn.slick.state.StateBasedGame;
//import org.newdawn.slick.tests.states.TestState2;


/**
 *
 * @author Eliza Kocic 165518
 */
public class RoboticonAlchemy extends StateBasedGame{

    /**
     * @param gamename
     */
    
    private UnicodeFont myFont;
    private String glyphs = "AaĄąBbCcĆćDdEeĘęFfGgHhIiJjKkLlŁłMmNnŃńOoÓóPpRrSsŚśTtUuWwYyZzŹźŻżVvXxQq1234567890-.'\",!*:;_/()[]"; 
    
    private Sound musicButton;
    private Sound musicButton2;
    private Music musicBackground;     
    private Music musicGame;
        
    public RoboticonAlchemy(String gamename) throws SlickException
    {
        super(gamename);
            
        java.awt.Font awtFont = new java.awt.Font("Georgia", java.awt.Font.PLAIN, 30);
        myFont = new UnicodeFont(awtFont);
        myFont.addGlyphs(glyphs);
        myFont.getEffects().add(new ColorEffect(java.awt.Color.YELLOW));
        
        musicBackground = new Music("..\\music\\TheGentleman.ogg");
        musicBackground.loop(1, (float)0.5);       
        musicGame = new Music("..\\music\\Darkmor.ogg");       
        musicButton = new Sound("..\\music\\select.wav");
        musicButton2 = new Sound("..\\music\\clank.wav");        
    }
    
    @Override
    public void initStatesList(GameContainer container) throws SlickException {
     addState(new MenuGlowne(myFont, musicButton, musicButton2, musicBackground));    
     addState(new Pomoc(myFont, musicButton2, musicBackground));     
     PodsumowaniePoziomu levelSummary = new PodsumowaniePoziomu(myFont, musicBackground, musicButton2);
     OknoGry gameWindow = new OknoGry(myFont, musicBackground, levelSummary, musicGame, musicButton2);
     MenuPoziomow levelMenu = new MenuPoziomow(myFont, musicButton, musicButton2, musicBackground, gameWindow, levelSummary);
     addState(new MenuKampanii(myFont, musicButton, musicButton2, musicBackground, levelMenu));
     addState(levelMenu);
     addState(gameWindow);
     addState(levelSummary);
    }
    
    public static void main(String[] args) {
        
        try
            {
                AppGameContainer appgc;
                appgc = new AppGameContainer(new RoboticonAlchemy("Roboticon Alchemy"));
                appgc.setDisplayMode(1920, 1080, true);
                appgc.start();
            }
            catch (SlickException ex)
            {
                Logger.getLogger(RoboticonAlchemy.class.getName()).log(Level.SEVERE, null, ex);
            }
        
    }
    
}
