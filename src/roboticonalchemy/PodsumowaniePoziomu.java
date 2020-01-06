/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package roboticonalchemy;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.Music;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.UnicodeFont;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

import static roboticonalchemy.OknoGry.ID;

import java.io.PrintStream;
import java.io.File;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;
import org.newdawn.slick.Color;
import org.newdawn.slick.Sound;
import org.newdawn.slick.state.transition.FadeInTransition;
import org.newdawn.slick.state.transition.FadeOutTransition;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 *
 * @author Eliza Kocic 165518
 */
public class PodsumowaniePoziomu extends BasicGameState{
    
    
    private StateBasedGame game;
    
    private UnicodeFont myFont;
    private Music musicBackground;
    private Sound musicButton2;
            
    private Image descriptionBackground;
    private Image character;
    private Image characterDarker;
    private Image rankImage;
    private Image buttonBack;
    private Image button;
    
    public static final int ID = 6;
    
    private Poziom level;
    private int numberOfInstructionsCompleted;
    private int instructionLength;
    
    private ArrayList<Score> numberOfInstructionsCompletedList;
    private ArrayList<Score> instructionLengthList;
    
    private final DateTimeFormatter dtfDate = DateTimeFormatter.ofPattern("yyyy/MM/dd"); 
    private final DateTimeFormatter dtfTime = DateTimeFormatter.ofPattern("HH : mm : ss");
    private String date;
    
    private boolean isNewScore = false;
    
    public PodsumowaniePoziomu(UnicodeFont myFont, Music musicBackground, Sound musicButton2) 
    {
        this.myFont = myFont;
        this.musicBackground = musicBackground;
        this.musicButton2 = musicButton2;
    }
    
    //Shows the level ranking from the level menu.
    public void loadScoresFromMenuLevel(Poziom selectedLevel) {
        
        this.level = selectedLevel;
        loadHighScores();
        isNewScore = false;
    }
    
    public void setScore(int numberOfInstructionsCompleted, int instructionLength, Poziom level) {
        
        this.numberOfInstructionsCompleted = numberOfInstructionsCompleted;
        this.instructionLength = instructionLength;  
        this.level = level;
        
        isNewScore = true;
                
        LocalDateTime now = LocalDateTime.now();  
        date = dtfDate.format(now) + "  [" + dtfTime.format(now) + "]";        
        
        loadHighScores();
        
        numberOfInstructionsCompletedList.add(new Score(numberOfInstructionsCompleted, date));
        instructionLengthList.add(new Score(instructionLength, date));
        
        sortByScore(numberOfInstructionsCompletedList);
        sortByScore(instructionLengthList);
        
        if(numberOfInstructionsCompletedList.size() >= 10) {
            numberOfInstructionsCompletedList = new ArrayList<>(numberOfInstructionsCompletedList.subList(0, 10));
        }
        if(instructionLengthList.size() >= 10) {
            instructionLengthList = new ArrayList<>(instructionLengthList.subList(0, 10));
        }
        
        saveHighScores(numberOfInstructionsCompletedList, instructionLengthList);
    }
    
    //Sorts scores.
    private void sortByScore(ArrayList<Score> array){
        Collections.sort(array, new SortByScore());
    }
     
    // Loads scores from set directory to scores list.
    private void loadHighScores() {
        numberOfInstructionsCompletedList.clear(); 
        instructionLengthList.clear(); 
        try {
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = documentBuilderFactory.newDocumentBuilder();
            Document document = builder.parse( new File( "..\\scores\\" + Integer.toString(level.getCampaignIdForSelectedLevel()) + "\\" + Integer.toString(level.getLevelId()) + ".xml") );
            Element rootElement = document.getDocumentElement();
            validateFile(rootElement);

            Element numberOfInstructionsCompleted = (Element) rootElement.getElementsByTagName("numberOfInstructionsCompleted").item(0);
            NodeList instrCompleted = numberOfInstructionsCompleted.getElementsByTagName("score");
            
            for (int i = 0; i < instrCompleted.getLength(); i++) {
                Element childNode = (Element) instrCompleted.item(i);
                numberOfInstructionsCompletedList.add( new Score ( Integer.parseInt(childNode.getAttribute("Wynik")), childNode.getAttribute("Data")) );
            }
            
            Element instructionLength = (Element) rootElement.getElementsByTagName("instructionLength").item(0);
            NodeList instrLength = instructionLength.getElementsByTagName("score");
            
            for (int i = 0; i < instrLength.getLength(); i++) {
                Element childNode = (Element) instrLength.item(i);
                instructionLengthList.add( new Score ( Integer.parseInt(childNode.getAttribute("Wynik")), childNode.getAttribute("Data")) );
            }
        } catch (Exception ex) {
                ex.printStackTrace();
        }
    }

    private void validateFile(Element rootElement) throws Exception  {
            if (!rootElement.getNodeName().equals("highScores")) {
                    throw new Exception("Not a file containing high scores");
            }
    }
    
    //Saves scores to XML file.
    private void saveHighScores(ArrayList<Score> numberOfInstructionsCompletedList, ArrayList<Score> instructionLengthList) {
        try {
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            documentBuilderFactory.setNamespaceAware(true);
            DocumentBuilder builder = documentBuilderFactory.newDocumentBuilder();
            Document document = builder.newDocument();
                        
            Element root = document.createElement("highScores");           
                        
            Element numberOfInstructionsCompletedScoresElement = document.createElement("numberOfInstructionsCompleted");
            Element instructionLengthScoresElement = document.createElement("instructionLength");                            
                
            for(Score score: numberOfInstructionsCompletedList) {
                 
                Element scoreElement = document.createElement("score");
                scoreElement.setAttribute( "Wynik", Integer.toString(score.score) );
                scoreElement.setAttribute( "Data", score.date );
                numberOfInstructionsCompletedScoresElement.appendChild(scoreElement);
            }
            
            for(Score score: instructionLengthList) {
                 
                Element scoreElement = document.createElement("score");
                scoreElement.setAttribute( "Wynik", Integer.toString(score.score) );
                scoreElement.setAttribute( "Data", score.date );
                instructionLengthScoresElement.appendChild(scoreElement);
            }
            
            root.appendChild(numberOfInstructionsCompletedScoresElement);
            root.appendChild(instructionLengthScoresElement);
            document.appendChild(root);

            DOMSource source = new DOMSource(document);
            PrintStream printStream = new PrintStream("..\\scores\\" + Integer.toString(level.getCampaignIdForSelectedLevel()) + "\\" + Integer.toString(level.getLevelId()) + ".xml");
            StreamResult result = new StreamResult(printStream);
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            transformer.transform(source, result); 

        } catch (Exception ex) {
            ex.printStackTrace();
        }	
    }
    
    private void drawRank(ArrayList<Score> score, int x) {
        for(int scoreIndex = 0; scoreIndex < score.size(); scoreIndex++) {
            String scoreToString = score.get(scoreIndex).result;
            String number = RomanNumber.toRoman(scoreIndex + 1);
            //String number = Integer.toString(scoreIndex + 1);
            
            switch(scoreIndex) {
                case 0:
                    myFont.drawString(x, 355 + (50*scoreIndex), number + ". " + scoreToString, org.newdawn.slick.Color.green);
                    break;
                case 1:
                    myFont.drawString((x - 10), 355 + (50*scoreIndex), number + ". " + scoreToString, org.newdawn.slick.Color.green);
                    break;
                default:
                    myFont.drawString((x - 20), 355 + (50*scoreIndex), number + ". " + scoreToString, org.newdawn.slick.Color.green);
                    break;
            }
        }
    }
    
    @Override
    public void enter(GameContainer container, StateBasedGame game) throws SlickException {
        
        super.enter(container, game);
        container.getInput().clearKeyPressedRecord();       
    }
    
    @Override
    public void leave(GameContainer container, StateBasedGame game) throws SlickException {

    }
    
    @Override
    public int getID() {
      return ID;
    }

    @Override
    public void init(GameContainer gc, StateBasedGame game) throws SlickException {
        
        this.game = game;      

        descriptionBackground = new Image("..\\images\\description2.png");
        character = new Image("..\\images\\rankGuy.png");
        characterDarker = new Image("..\\images\\rankGuyDarker.png");
        rankImage = new Image("..\\images\\buttons\\ranking.png");
        
        buttonBack = new Image("..\\images\\buttons\\buttonBackSmaller2.png");
        button = new Image("..\\images\\trybikGold.png");
       
        numberOfInstructionsCompletedList = new ArrayList<Score>();
        instructionLengthList = new ArrayList<Score>();
    }

    @Override
    public void update(GameContainer gc, StateBasedGame game, int i) throws SlickException {
               
        if(gc.getInput().isKeyPressed(Input.KEY_SPACE)) {
            if(musicBackground.playing()){
                musicBackground.pause();
            }
            else {
                musicBackground.resume();
            }
        } 
        
        else if(gc.getInput().isKeyPressed(Input.KEY_ENTER)) {
            game.enterState(MenuPoziomow.ID, new FadeOutTransition(Color.black), new FadeInTransition(Color.black));
            musicButton2.play(1, (float)0.75);
        }
    }

    @Override
    public void render(GameContainer gc, StateBasedGame game, Graphics g) throws SlickException
    {    
        descriptionBackground.draw(0, 0, 1920, 1080, Color.darkGray);
        
        rankImage.draw(170, 50, (float)0.5);
                
        buttonBack.draw(70, 950, (float)0.35);        
        button.draw(530, 970, (float)0.09);
        button.draw(130, 970, (float)0.09);
        
        if(isNewScore) {
            
            characterDarker.draw(1200, 150, (float)0.95, Color.darkGray);
            myFont.drawString(1435, 400,"TWÓJ WYNIK", org.newdawn.slick.Color.yellow);
            myFont.drawString(1350, 450,"Liczba wszystkich instrukcji: " + Integer.toString(numberOfInstructionsCompleted), org.newdawn.slick.Color.yellow);
            myFont.drawString(1375, 500,"Długość listy instrukcji: " + Integer.toString(instructionLength), org.newdawn.slick.Color.yellow);
            
            myFont.drawString(100, 250,"Liczba wszystkich wykonanych instrukcji: ", org.newdawn.slick.Color.green); 
            drawRank(numberOfInstructionsCompletedList, 120);
            myFont.drawString(700, 250, "Długość listy instrukcji: ", org.newdawn.slick.Color.green);   
            drawRank(instructionLengthList, 720);
        }
        else {
            
            character.draw(1200, 150, (float)0.95);
            
            myFont.drawString(100, 250,"Liczba wszystkich wykonanych instrukcji: ", org.newdawn.slick.Color.green); 
            drawRank(numberOfInstructionsCompletedList, 120);
            myFont.drawString(700, 250, "Długość listy instrukcji: ", org.newdawn.slick.Color.green);   
            drawRank(instructionLengthList, 720);
        }
    }
}
