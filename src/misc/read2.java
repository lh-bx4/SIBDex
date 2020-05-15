/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package misc;

import java.util.Scanner;

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
}
