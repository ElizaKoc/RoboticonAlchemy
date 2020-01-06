/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package roboticonalchemy;

import static jdk.nashorn.internal.codegen.CompilerConstants.className;

/**
 *
 * @author Eliza Kocic 165518
 */
public abstract class Komenda {
    
    public abstract void proceed(Robot player);

    public abstract String getName();
    
    public abstract boolean isLoop();
    
    public static String getTitle() {
        return "";
    }
    
    public static int paramsNum() {
        return 0;
    }
}
