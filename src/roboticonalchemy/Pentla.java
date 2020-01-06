/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package roboticonalchemy;

/**
 *
 * @author Eliza Kocic 165518
 */
public class Pentla extends Komenda{

      private int amount = 0;
      private int commandIndex = 0;
      private OknoGry gameWindow;
    
    public Pentla(OknoGry gameWindow, int amount) {
        this.gameWindow = gameWindow;
        this.amount = amount;        
    }    
        
    @Override
    public void proceed(Robot player) {
        
        Komenda command;
        
        if(amount < 1) {
             gameWindow.setCommandIndex(gameWindow.getCommandIndex() + 1);
            return;
        } 
        
        command = gameWindow.getNextCommand();
        command.proceed(player);
        gameWindow.setCommandIndex(gameWindow.getCommandIndex() - 1);
        amount--;       
    }
  
    @Override
    public String getName() {
        return "powtórz x" + Integer.toString(amount);
    }
    
    public static String getTitle() {      
        return "powtórz";
    }
    
    public static int paramsNum() {      
        return 1;
    }

    @Override
    public boolean isLoop() {
        return true;
    }
    
}
