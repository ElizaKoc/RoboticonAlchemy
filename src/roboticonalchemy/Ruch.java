/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package roboticonalchemy;

import java.awt.Point;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.Sound;

/**
 *
 * @author Eliza Kocic 165518
 */
public class Ruch extends Komenda{
        
    private int distance;
 
    private Sound movement;
    private Sound collide;
    
    public Ruch(int distance) throws SlickException{
       this.distance = distance;
       this.movement = new Sound("..\\music\\move2.wav");
       this.collide = new Sound("..\\music\\rotate.wav");
    }
    
    @Override
    public void proceed(Robot player) {
                        
        Point modified = new Point(player.getPosition());
        Point position = player.getPosition();
        Poziom level = player.getLevel();
              
        int deltaX = 0;
        int deltaY = 0;
        
        switch(player.getDirection()) {
            case up:
                deltaY = - distance;
                break;
            case down:
                deltaY = distance;
                break;
            case right:
                deltaX =  distance;
                break;
            case left:
                deltaX =  - distance;
                break;
            default:
                break;
        }
        
        modified.x = position.x + deltaX;
        modified.y = position.y + deltaY;
        
        Obiekt tileObject = level.getTileObject(modified.x, modified.y);
                
        if(tileObject != null) {

            int tileToPushX = modified.x + deltaX;
            int tileToPushY = modified.y + deltaY;
            
            if(tileObject.getPush() && level.tileIsPassable(tileToPushX, tileToPushY) && level.getTileObject(tileToPushX, tileToPushY) == null) {
                move(player, modified.x, modified.y);
                tileObject.setPosition(tileToPushX, tileToPushY);
            }
            else{
                collide();
            }
        }       
        else if(level.tileIsPassable(modified.x, modified.y)) { 
            move(player, modified.x, modified.y);
        }
        else {
            collide();
        }
    }

    private void move(Robot player, int x, int y) {
        movement.play(1, (float)0.75);                  
        player.setPosition(x, y);
    }
    
    private void collide() {
        collide.play(1, (float)0.75);
    }
    
    @Override
    public String getName() {
        
         switch(distance) {
            case 64:
                return "jedź : 1";
            case -64:
                return "jedź : -1";
            default:
                return "";
         }
    }
    
    public static String getTitle() {      
        return "jedź";
    }
    
    public static int paramsNum() {      
        return 1;
    }

    @Override
    public boolean isLoop() {
        return false;
    }
    
}
