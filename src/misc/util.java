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
    
    public static final int BRACKETS = 12, SBRACKETS=13, CBRACKETS=14, SPIKES=15;
    
    /**
     * Concat : concat ArrayList <st> in one String separated by <c> char.
     * @param st
     * @param c
     * @return Concatenated string.
     */
    public static String c(ArrayList<String> st, String c) {
        // add errors (empty ?)
        String concat = "";
        for(String s:st) {
            concat+=s+c;
        }
        return concat.substring(0, concat.length()-1);
    }
    
    public static String c(String[] st, String c) {
        // add errors (empty ?)
        String concat = "";
        for(String s:st) {
            concat+=s+c;
        }
        return concat.substring(0, concat.length()-1);
    }
    
    /**
     * Enclose : enclose <s> between <q> type of brackets.
     * @param s String to enclose
     * @param q Type of bracket - see util.static
     * @return enclosed string.
     */
    public static String e(String s, int q) {
        switch (q) {
            case BRACKETS:
                return "("+s+")";
            case SBRACKETS:
                return "["+s+"]";
            case CBRACKETS:
                return "{"+s+"}";
            case SPIKES:
                return "<"+s+">";
            default:
                return "("+s+")";
        }
    }
    public static String d(String[] st, String c, int q) {
        return e(c(st, c), q);
    }
    
    /**
     * Concat, then enclose.
     * @param st
     * @param c
     * @param q
     * @return 
     */
    public static String d(ArrayList<String> st, String c, int q) {
        return e(c(st, c), q);
    }
    
    /**
     * Format the string <S> to correct PSQL format <format> 
     * @param S 
     * @param format 
     */
    public static String f(String S, String format) {
        //System.out.println(format);
        switch (format) {
            case "varchar":
                return "'"+S+"'";
            case "int4":
                // if int, keep only digits
                return S.replaceAll("[^\\d.]", "");
            default:
                return S;
        }
    }
}
