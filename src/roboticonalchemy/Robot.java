/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package roboticonalchemy;

import org.newdawn.slick.SlickException;

/**
 *
 * @author Eliza Kocic 165518
 */
public class Robot extends Obiekt{    
    
    public Robot(int x, int y, Poziom level) throws SlickException {
        super(x, y, level, "..\\images\\robot.png");
    }               
}
