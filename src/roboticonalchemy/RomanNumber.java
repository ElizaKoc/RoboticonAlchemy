/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package roboticonalchemy;

import java.util.TreeMap;

/**
 *
 * @author Eliza Kocic 165518
 */
public class RomanNumber {
    
    private final static TreeMap<Integer, String> map = new TreeMap<Integer, String>();

    static {

        map.put(1000, "M");
        map.put(900, "CM");
        map.put(500, "D");
        map.put(400, "CD");
        map.put(100, "C");
        map.put(90, "XC");
        map.put(50, "L");
        map.put(40, "XL");
        map.put(20, "XX");
        map.put(19, "XIX");
        map.put(18, "XVIII");
        map.put(17, "XVII");
        map.put(16, "XVI");
        map.put(15, "XV");
        map.put(14, "XIV");
        map.put(13, "XIII");
        map.put(12, "XII");
        map.put(11, "XI");
        map.put(10, "X");
        map.put(9, "IX");
        map.put(8, "VIII");
        map.put(7, "VII");
        map.put(6, "VI");
        map.put(5, "V");
        map.put(4, "IV");
        map.put(3, "III");
        map.put(2, "II");
        map.put(1, "I");

    }

    public final static String toRoman(int number) {
        int l =  map.floorKey(number);
        if ( number == l ) {
            return map.get(number);
        }
        return map.get(l) + toRoman(number-l);
    }
}
