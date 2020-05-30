
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import misc.read2;
import org.postgresql.util.PSQLException;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Louis
 */
public class test {
    public static void main(String[] args) {
        String[] st = (new String[] {"choix1", "choix2", "choix3"});
        ArrayList<String> choix = new ArrayList<>();
        for(String s:st) choix.add(s);
        read2.ArrowSelector(choix,true,false);
        
    }
}
