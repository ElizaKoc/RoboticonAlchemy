/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package roboticonalchemy;

import java.awt.Point;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

/**
 *
 * @author Eliza Kocic 165518
 */
public class Obiekt {
    
    
    
    protected Kierunek direction;
    protected Point position;
    protected Image objectImage;
    protected Poziom level;
    protected int startingPositionX;
    protected int startingPositionY;
    protected boolean isPushable = false;
    
    public Obiekt(int x, int y, Poziom level, String objectImagePath) throws SlickException {
        this.position = new Point(x,y);
        this.objectImage = new Image(objectImagePath);
        adjustDirection();
        this.level = level;
        level.addObject(this);
        this.startingPositionX = x;
        this.startingPositionY = y;
    }
    
    public void setDirection(Kierunek direction) {
        
        this.direction = direction;
    }
    
    public void setPosition(int x, int y) {
        position.move(x, y);
    }
    
    public Kierunek getDirection() {
        return direction;
    }
    
    public Point getPosition() {
        return position;
    }
    
    public void draw() {
        objectImage.draw(position.x, position.y);
    }
    
    public void changeImage(Image image) {
        this.objectImage = image;
    }
    
    public Image getImage() {
        return this.objectImage;
    }
    
    public void setPush(boolean isPushable) {
        this.isPushable = isPushable;
    }
    
    public boolean getPush() {
        return this.isPushable;
    }
    
    public void adjustDirection() {

        switch((int)this.objectImage.getRotation()) {
            case 0:
                setDirection(Kierunek.up);
                break;
            case 90:
                setDirection(Kierunek.right);
                break;
            case 180:
               setDirection(Kierunek.down);
               break;
            case 270:
               setDirection(Kierunek.left);
               break;
            case -90:
                setDirection(Kierunek.left);
                break;
            case -180:
                setDirection(Kierunek.down);
                break;
            case -270:
                setDirection(Kierunek.right);
                break;
            case -360:
                setDirection(Kierunek.up);
                break;
            default:
                break;
        }
    }
    
    public Poziom getLevel() {
      return level;  
    }
    
    public void resetPosition() {
        setPosition(this.startingPositionX, this.startingPositionY);
        setDirection(Kierunek.up);
        objectImage.setRotation(0);
    }
}
