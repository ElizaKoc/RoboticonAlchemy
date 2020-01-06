/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package roboticonalchemy;

import java.util.Comparator;

/**
 *
 * @author Eliza Kocic 165518
 */
public class Score {
    
    int score;
    String date;
    String result;
    
    public Score(int instruction, String date) {
        this.score = instruction;
        this.date = date;
        this.result = createScore();
    }
    
    private String createScore() {
        return this.score + " (" + this.date + ") ";
    }
}

class SortByScore implements Comparator<Score> 
{ 
    @Override
    public int compare(Score a, Score b) 
    { 
        return a.score - b.score; 
    } 
} 
