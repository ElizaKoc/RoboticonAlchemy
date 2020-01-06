/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package roboticonalchemy;

import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.Sound;

/**
 *
 * @author Eliza Kocic 165518
 */
public class Obracanie extends Komenda{
    
    private boolean clockWise;
    private final float rotation = (float)90;
    private Image robot;
    private Sound rotate;
    
    public Obracanie(boolean clockWise) throws SlickException{
       this.clockWise = clockWise; 
       this.rotate = new Sound("..\\music\\move2.wav");
    }

    @Override
    public void proceed(Robot player) {
        
        robot = player.getImage();
        
        if(clockWise) {
            rotate.play(1, (float)1);
            robot.setRotation(robot.getRotation() + rotation);
        }
        else {
            rotate.play(1, (float)1);
            robot.setRotation(robot.getRotation() - rotation);
        }   
        
        player.changeImage(robot);
        player.adjustDirection();
    } 

    @Override
    public String getName() {
        if(clockWise){
            return "obrót : 1";
        }
        else {
            return "obrót : 0";
        }
        
    }
    
    public static String getTitle() {      
        return "obrót";
    }
    
    public static int paramsNum() {      
        return 1;
    }

    @Override
    public boolean isLoop() {
        return false;
    }
    
}
