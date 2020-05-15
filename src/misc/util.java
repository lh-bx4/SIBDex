/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package misc;

import java.util.ArrayList;

/**
 *
 * @author Louis
 */
public class util {
    
    public static final int BRACKETS = 12, SBRACKETS=13, CBRACKETS=14;
    
    
    public static String c(ArrayList<String> st, char c) {
        // add errors (empty ?)
        String concat = "";
        for(String s:st) {
            concat+=s+c;
        }
        return concat.substring(0, concat.length()-1);
    }
    public static String c(String[] st, char c) {
        // add errors (empty ?)
        String concat = "";
        for(String s:st) {
            concat+=s+c;
        }
        return concat.substring(0, concat.length()-1);
    }
    public static String e(String s, int q) {
        switch (q) {
            case BRACKETS:
                return "("+s+")";
            case SBRACKETS:
                return "["+s+"]";
            case CBRACKETS:
                return "{"+s+"}";
            default:
                return "("+s+")";
        }
    }
    public static String d(String[] st, char c, int q) {
        return e(c(st, c), q);
    }
}
