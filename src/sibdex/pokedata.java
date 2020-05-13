/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sibdex;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import misc.util;
import static misc.util.*;

/**
 *
 * @author Louis
 */
public abstract class pokedata {
    public static final int INSERT = 1, UPDATE = 2, DELETE = 3, PLAYER = 4;
            
    private final int type;
    
    protected pokedata(int type) {
        this.type=type;
    }
    
    public String send(String[] s) {
        return util.c(s, ' ')+";";
    }
    
    public void exec(Connection c, String q) {
        try {
            Statement stmt = c.createStatement();
            ResultSet rs = stmt.executeQuery(q);
        } catch (SQLException ex) {
            Logger.getLogger(pokedata.class.getName()).log(Level.INFO, null, ex);
        }
    }
    
    /**
     * Insert to database
     */
    public static class pdI extends pokedata {
        private final String tName;
        private final String[] tCols;
        private final String[][] tVals;
        
        public pdI(String tn, String[] tc, String[][] tv) {
            super(INSERT);
            this.tName=tn;
            this.tCols=tc;
            this.tVals=tv;
        }
                
        public String send() {
            // TODO see if all lines have the right format
            boolean t1;
            if (t1=tCols.length!=0) {
                if (tCols.length!=tVals[0].length) System.err.println("Different valueq length while inserting.");
            } else {
                System.err.println("Empty tCols is deprecated");
            }
            
            ArrayList<String> t = new ArrayList<>();
            for(String[] r:tVals) {
                t.add(util.d(r,',', BRACKETS));
            }
            
            return super.send(new String[] 
                {
                    "INSERT",
                    "INTO",
                    tName,
                    t1?util.e(util.c(tCols,','), BRACKETS):"",
                    "VALUES",
                    util.c(t,',')
                }
            );
        }   
    }
    
    /**
     * Update database
     */
    public static final class pdU extends pokedata {    
        
        public pdU(int type) {
            super(UPDATE);
        }
        
    }
    
    /**
     * Delete from database
     */
    public static final class pdD extends pokedata {    
        
        public pdD(int type) {
            super(DELETE);
        }
        
    }
    
    /**
     * Add an access to database
     */
    public static final class pdP extends pokedata {    
        
        public pdP(int type) {
            super(PLAYER);
        }
        
    }
}
