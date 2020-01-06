/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package roboticonalchemy;

import java.awt.Point;
import java.sql.Time;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.Music;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.Sound;
import org.newdawn.slick.UnicodeFont;
import org.newdawn.slick.gui.TextField;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.state.transition.FadeInTransition;
import org.newdawn.slick.state.transition.FadeOutTransition;
import org.newdawn.slick.tiled.TiledMap;
import static roboticonalchemy.MenuPoziomow.ID;

/**
 *
 * @author Eliza Kocic 165518
 */
public class OknoGry extends BasicGameState{
    
    private StateBasedGame game;
    
    private UnicodeFont myFont;
    
    private Music musicBackground;   
    private Music musicGame;
    private Sound musicButton2;
    
    private Image cursor;
    private Image tableOfCommands;
    private Image buttonEsc;
    private Image buttonStart;
       
    public static final int ID = 5;
    
    private Poziom selectedLevel;
    Komenda command;
    
    private ArrayList<Komenda> commandList;
    private static ArrayList<String> commandTypes;
    private PodsumowaniePoziomu levelSummary;

    private boolean followingCommands = false;
    private boolean selectingCommands = true;
    private boolean notCompleted = false;
  
    private int cursorIndex = 0;
    private int commandIndex = 0;
    private int bottomCommandIndex = 0;
    private int timeSinceLastCommand = 0;
    private int numberOfInstructionsCompleted = 0;
    
    private TextField parameters;
    private static final int TEXT_FIELD_X = 169;
    private static final int TEXT_FIELD_WIDTH = 35;
    private static final int TEXT_FIELD_HEIGHT = 35;
    private static final int MAX_LENGHT_COMMAND_ARG_INPUT = 2;
    
    public OknoGry(UnicodeFont myFont, Music musicBackground, PodsumowaniePoziomu levelSummary, Music musicGame, Sound musicButton2) 
    {
        this.myFont = myFont;
        this.musicBackground = musicBackground;
        this.musicButton2 = musicButton2;
        this.levelSummary = levelSummary;
        this.musicGame = musicGame;        
    }
    
    private int getCurrentTextFieldY() {
        return 150 + (50*cursorIndex);
    }
    
    private int getParamsNum(String commandType) {
        
        int paramsNum = 0; 
        
        switch (commandType) {
            case "obrót":
                paramsNum = Obracanie.paramsNum();
                break;
            case "jedź":
                paramsNum = Ruch.paramsNum();
                break;
            case "powtórz":
                paramsNum = Pentla.paramsNum();
                break;
            default:
                break;
        }
       
        return paramsNum;
    }
    
    private void addCommand(GameContainer gc) throws SlickException {
        
        Komenda command; 
        int paramsNum = getParamsNum(commandTypes.get(cursorIndex));
        
        if(parameters != null) {
            
            int params = 0;
            
            try {
                params = Integer.parseInt(parameters.getText());
            }
            catch(NumberFormatException ex) {
                return;
            }
            
            parameters.deactivate();
            parameters = null;
            
            if(commandTypes.get(cursorIndex).equals("obrót")) {
                if(params > 0) {
                    command = new Obracanie(true);                   
                }  
                else {
                    command = new Obracanie(false); 
                }
            }
            else if(commandTypes.get(cursorIndex).equals("jedź")){
                
                if(params >= 0) {
                    command = new Ruch(64);
                }
                else  {
                    command = new Ruch(-64);
                }            
            }             
            else if(commandTypes.get(cursorIndex).equals("powtórz")) {
                                
                    
                    if(params > 0) {
                        command = new Pentla(this, params);
                    }   
                    else {
                       command = new Pentla(this, 1); 
                    }            
            }
            else {
                if(params >= 0) {
                    command = new Ruch(64);
                }
                else  {
                    command = new Ruch(-64);
                }   
            }
            
            commandList.add(command);
        }      
        else {
            if(paramsNum != 0) {
                setupTextField(gc);
            } 
            else {
                
            }
        }                                         
    }
    
    private void removeCommand()  {

        if(commandList.size() > 15 && bottomCommandIndex > 0) {
            commandList.remove(cursorIndex + bottomCommandIndex + 1);
        }
        else {
            commandList.remove(cursorIndex + bottomCommandIndex);
        }        
    }
    
    private void followCommands(){
        followingCommands = true;
    }
    
    private void resetState() {
        
        notCompleted = false;
        bottomCommandIndex = 0;
        cursorIndex = 0;
        selectingCommands = true;
        commandIndex = 0;
        commandList.clear();
        followingCommands = false;
        numberOfInstructionsCompleted = 0;
        selectedLevel.resetObjects();
    }
    
    private void endGame() {
        levelSummary.setScore(numberOfInstructionsCompleted ,commandList.size(), selectedLevel);
        numberOfInstructionsCompleted = 0;
    }
    
    private void backToLevelMenu() {
        game.enterState(MenuPoziomow.ID, new FadeOutTransition(Color.black), new FadeInTransition(Color.black));
        musicButton2.play(1, (float)0.75);
    }
    
    public void setLevel(Poziom selectedLevel) {
        this.selectedLevel = selectedLevel;
    }
    
    public void handleInput(GameContainer gc) {
        
    }
    
    @Override
    public void enter(GameContainer container, StateBasedGame game) throws SlickException {
        
        musicGame.loop(1, (float)0.5);
        super.enter(container, game);
        container.getInput().clearKeyPressedRecord();
       
        selectedLevel.loadMap();
    }
    
    @Override
    public void leave(GameContainer container, StateBasedGame game) throws SlickException {
        musicBackground.loop(1, (float)0.5);
        resetState();
    }
    
    @Override
    public int getID() {
      return ID;
    }

    @Override
    public void init(GameContainer gc, StateBasedGame game) throws SlickException {
        
        this.game = game;      
        
        tableOfCommands = new Image("..\\images\\table5.png");
        cursor = new Image("..\\images\\cursor3.png");
        buttonEsc = new Image("..\\images\\buttons\\esc.png");
        buttonStart = new Image("..\\images\\buttons\\start.png");
        
        commandList = new ArrayList<>();
        commandTypes = new ArrayList<>();
        commandTypes.add("obrót");
        commandTypes.add("jedź");
        commandTypes.add("powtórz");            
    }
    
    private void adjustIndex() {
        int listSize = getListSize();
        if(cursorIndex >= listSize) {
            if(listSize == 0) {
                cursorIndex = 0;
                selectingCommands = true;
            }
            else {
                cursorIndex = listSize - 1;
            }           
        }
    }
    
    private void setupTextField(GameContainer gc){
        parameters = new TextField(gc, myFont, TEXT_FIELD_X, getCurrentTextFieldY(), TEXT_FIELD_WIDTH, TEXT_FIELD_HEIGHT);       
        parameters.setFocus(true);
        parameters.setMaxLength(MAX_LENGHT_COMMAND_ARG_INPUT);
    }

    private int getListSize() {
        int listSize = 0;
        if(selectingCommands) {
            listSize = commandTypes.size();
        }
        else {
            listSize = commandList.size();
        }
        return listSize;
    }
    
    @Override
    public void update(GameContainer gc, StateBasedGame game, int i) throws SlickException {      
        
        adjustIndex();        
        
        if(parameters != null && ((parameters.getText().length() > 1) || !(parameters.getText().equals("-"))) ) {
            try {
                Integer.parseInt(parameters.getText());
            }
            catch (NumberFormatException ex) {
                parameters.setText("");
            } 
        }        
        
        if(gc.getInput().isKeyPressed(Input.KEY_SPACE)) {
            if(musicGame.playing()){
                musicGame.pause();               
            }
            else {
                musicGame.resume();
            }
        }
        
        if(parameters == null) {
            
            if(gc.getInput().isKeyPressed(Input.KEY_UP)) { 
                if(cursorIndex == 0){
                    if(!selectingCommands) {
                        if(bottomCommandIndex == 0) {

                            if(commandList.size() > 15) {

                                cursorIndex = 14;
                                bottomCommandIndex = commandList.size() - 15;
                            } 
                            else {
                                 cursorIndex = getListSize() - 1;
                            }
                        }
                        else {
                            bottomCommandIndex = bottomCommandIndex - 1;
                        } 
                    }
                    else {
                        cursorIndex = getListSize() - 1;
                    }
                }
                else {   

                    cursorIndex = cursorIndex - 1;                                   
                }               
            }

            if(gc.getInput().isKeyPressed(Input.KEY_DOWN)) {
                
                if(!selectingCommands) {
                    
                    if(cursorIndex + bottomCommandIndex == getListSize() - 1){

                        cursorIndex = 0;                   
                        bottomCommandIndex = 0;
                    }
                    else {   

                        if(cursorIndex < 14) {
                            cursorIndex = cursorIndex + 1;
                        }                                      
                        else{
                            bottomCommandIndex = bottomCommandIndex + 1;
                        }                    
                    } 
                }
                else {
                    if(cursorIndex == getListSize() - 1) {
                        cursorIndex = 0;
                    }
                    else {
                        cursorIndex = cursorIndex + 1;
                    }
                }
            }
        
            if(gc.getInput().isKeyPressed(Input.KEY_RIGHT)) { 
                if(selectingCommands) {
                    
                   if(commandList.size() != 0) {
                        selectingCommands = false;
                        cursorIndex = 0; 
                        bottomCommandIndex = 0;
                    } 
                }                
            }

            if(gc.getInput().isKeyPressed(Input.KEY_LEFT)) { 
                
                if(!selectingCommands) {
                    selectingCommands = true;
                    cursorIndex = 0;
                    bottomCommandIndex = 0;
                }
            }
            
            if(!notCompleted) {
              if(gc.getInput().isKeyPressed(Input.KEY_S)) {
                    selectingCommands = false;
                    cursorIndex = 0;
                    bottomCommandIndex = 0;
                    followCommands();                
                }  
            }            
        }
        
        if(gc.getInput().isKeyPressed(Input.KEY_R)) { 
            if(notCompleted) {
                resetState();
            } 
            gc.getInput().clearKeyPressedRecord();
        }
        
        if(gc.getInput().isKeyPressed(Input.KEY_ESCAPE)) { 
            resetState();
            backToLevelMenu();
        }
        
        if(gc.getInput().isKeyPressed(Input.KEY_C)) { 
           commandIndex = 0;
           commandList.clear();
           followingCommands = false;
        }
           
        if(!notCompleted) {
            if(gc.getInput().isKeyPressed(Input.KEY_ENTER)) {  

                if(selectingCommands) {
                    addCommand(gc);
                    if(commandList.size() - bottomCommandIndex > 15) {
                        bottomCommandIndex = bottomCommandIndex + 1;
                    }
                }
                else {
                    if(commandList.size() > 15 && bottomCommandIndex > 0) {
                        bottomCommandIndex = bottomCommandIndex - 1; 
                    }
                    removeCommand();                               
                }

                gc.getInput().clearKeyPressedRecord();
            }   
        }                           
        
        if(followingCommands) {          
            timeSinceLastCommand += i;
            if(timeSinceLastCommand > 1000) {
               timeSinceLastCommand = timeSinceLastCommand - 1000;
               if(commandIndex < commandList.size()){
                    if(commandList.get(commandIndex).isLoop()) {
                        if(commandList.size() > commandIndex + 1) {
                            if(!(commandList.get(commandIndex + 1).isLoop())) {
                                commandList.get(commandIndex).proceed(selectedLevel.getPlayer());                                
                                numberOfInstructionsCompleted++;           
                                commandIndex++;
                            }
                            else {
                                notCompleted = true;
                            }
                            
                        }
                        else {
                            notCompleted = true;
                        }                        
                    }
                    else {
                       
                        if(commandIndex != 0 && commandIndex != 1) {
                            if(commandList.get(commandIndex - 2).isLoop()) {
                                if(cursorIndex < 14) {
                                    cursorIndex = cursorIndex + 2;
                                }
                                else {
                                    bottomCommandIndex = bottomCommandIndex + 2;
                                }
                            }
                        }
                                              
                        commandList.get(commandIndex).proceed(selectedLevel.getPlayer());
                        if(cursorIndex < 14) {
                            cursorIndex++;
                        }                               
                        else {
                            bottomCommandIndex++;
                        }                       
                        numberOfInstructionsCompleted++;           
                        commandIndex++;
                    }                                           
                }                
                else {                    
                    if(selectedLevel.isObjectiveCompleted()) {
                        endGame();
                        game.enterState(PodsumowaniePoziomu.ID, new FadeOutTransition(Color.black), new FadeInTransition(Color.black));                       
                    }
                    else {
                        notCompleted = true;                                                                    
                    }
                }
            }
        }
        /*else {
            handleInput(gc);
        }*/
    }
    
    public void setCommandIndex(int commandIndexFromLoop) {
        commandIndex = commandIndexFromLoop;
    }
    
    public int getCommandIndex() {
        return commandIndex;
    }
    
    public int getNumberOfInstructionsCompleted() {
        return numberOfInstructionsCompleted;
    }
    
    public Komenda getNextCommand() {
        return commandList.get(commandIndex + 1);
    }
    
    private void drawCommandList() {       
                
        for(int commandIndex = 0; commandIndex + bottomCommandIndex < commandList.size() && commandIndex < 15; commandIndex++){
            Komenda command = commandList.get(commandIndex + bottomCommandIndex);
            String name = command.getName();
            if(this.commandIndex == commandIndex + bottomCommandIndex && followingCommands) {
                myFont.drawString(265, 150 + (50*commandIndex), name, org.newdawn.slick.Color.red);
            }
            else {
                myFont.drawString(265, 150 + (50*commandIndex), name, org.newdawn.slick.Color.yellow);
            } 
        }
        
    }
    
    private void drawCommandTypes() {
        
      String title = "";
      for(int commandIndex = 0; commandIndex < commandTypes.size(); commandIndex++){
            String command = commandTypes.get(commandIndex);
            if(command.equals("obrót")) {
                title = Obracanie.getTitle();
            }
            else if(command.equals("jedź")) {
                title = Ruch.getTitle();
            }
            else if(command.equals("powtórz")) {
                title = Pentla.getTitle();
            }
            
            myFont.drawString(61, 150 + (50*commandIndex), title, org.newdawn.slick.Color.yellow);            
        }
    }
    
    @Override
    public void render(GameContainer gc, StateBasedGame game, Graphics g) throws SlickException
    {    
        selectedLevel.renderMap(); 
           
        tableOfCommands.draw(0, 0);
        buttonEsc.draw(60,980, (float)0.35);
        buttonStart.draw(264, 980, (float)0.35);
        
        selectedLevel.drawObjects();  
        
        drawCommandList();
        drawCommandTypes();
        
        if(!followingCommands || notCompleted) {
            if(selectingCommands) {
                cursor.draw(32, 155 + (50*cursorIndex));
            }
            else {           
                cursor.draw(240, 155 + (50*cursorIndex));
            }
        }
                
        if(parameters != null) {
            parameters.render(gc, g);
        }
        
        if(notCompleted) {
            myFont.drawString(758, 510,"WCIŚNIJ \"R\", ABY ZRESTARTOWAĆ POZIOM", org.newdawn.slick.Color.yellow);
        }
    }   
}
