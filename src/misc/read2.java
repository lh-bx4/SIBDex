/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package misc;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Louis
 */
public class read2 {
    public static String pS() {
        System.out.print(">>> ");
        return S();
    }
    
    public static String S() {
        return (new Scanner(System.in)).nextLine();
    } 
    
    public static void Running() {
        String[] symbol = new String[] {"|","/","-","\\"};
        int i=0;
        while (true) {
            System.out.write('\r');
            System.out.print(symbol[i=(i+1)%symbol.length]);
        }
    }
    public static ArrayList<Boolean> ArrowSelector(ArrayList<String> st, boolean Unique, boolean start) {
        System.out.println("TRUE, false. Move with <>, toggle with $, end with :. It is possible to chian operators.");
        ArrayList<Boolean> bt = new ArrayList<Boolean>();
        for (String st1 : st) bt.add(start);
	boolean stop=false;
	int i=0;
        while(!stop) {
            System.out.write('\r');
            for (String s:st) {
                System.out.print((bt.get(st.indexOf(s))?s.toUpperCase():s.toLowerCase())+" ");
            }
            System.out.print("["+st.get(i)+"]{<$>}:");
            String in = S();
            int k=0;
            // while my we havn't reached the lase char of string
            for(char c:in.toCharArray()) {
                switch (c) {
                    case '<':
                        i=(i-1)%st.size();
                        System.out.print("<"+st.get(i));
                        break;
                    case '>':
                        i=(i+1)%st.size();
                        System.out.print(">"+st.get(i));
                        break;
                    case '$':
                        if (Unique) {
                            bt.replaceAll((t) -> {return false;});
                            bt.set(i, true);
                        } else {
                            bt.set(i, !bt.get(i));
                        }
                        System.out.print("$"+st.get(i));
                        break;
                    case ':':
                        stop=true;
                        System.out.write('\r');
                        return bt;
                    default:
                        System.out.print(":Unknown action. Try q, s, d, \\n.");
                }
            }
            System.out.print("&");
        }
        return bt;
    }
}
