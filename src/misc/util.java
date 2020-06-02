/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package misc;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Array;
import java.sql.Connection;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;
import javax.imageio.ImageIO;
import sibdex.pokedata;
import sibdex.pokedata.pdI;
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
        return st.size()==0?"":concat.substring(0, concat.length()-1);
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
    /**
     * @deprecated use ArrayList instead
     * @param st
     * @param c
     * @param q
     * @return 
     */
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
        //System.out.println(format+":"+S);
        if (S.equals("")) return "NULL";
        switch (format) {
            case "varchar":
                return "'"+S+"'";
            case "int4":
                // if int, keep only digits
                return S.replaceAll("[^\\d.]", "");
            case "bool":
                return Boolean.parseBoolean(S)+"";
            default:
                return S;
        }
    }
    private static BufferedReader csvopen(File f) throws FileNotFoundException {
        return new BufferedReader(new FileReader(f)); 
    }
    /**
     * fill database with csvs
     * @param c
     * @param f
     * @param delim
     * @throws FileNotFoundException
     * @throws IOException
     * @throws SQLException 
     */
    public static void fillDB(Connection c) throws FileNotFoundException, IOException, SQLException {
        String delim = ",";
        String row;
        System.out.println("Pokemon");
        BufferedReader br = csvopen(new File("./src/assets/data/pokemon.csv"));
        ArrayList<String> trow, theader = null, types = null;
        ArrayList<ArrayList<String>> tvals = new ArrayList<>();
        boolean header = true;
        int abilities_col=0, cinsert=0;
        
        while ((row = br.readLine()) != null) {
            System.out.print((++cinsert));
            trow=new ArrayList<>(Arrays.asList(row.split(delim)));
            if (header) {
                theader=new ArrayList<>(trow);
                types=new ArrayList<>(trow);
                abilities_col=theader.indexOf("abilities");
                theader.remove(abilities_col);
                ResultSetMetaData MD = c.createStatement()
                    .executeQuery("SELECT * FROM pokemon").getMetaData();
                for(int i=1; i<=MD.getColumnCount(); i++) {
                    String colname=MD.getColumnName(i);
                    for(String headername:theader) {
                        if (colname.equals(headername)) {
                            types.set(i-1, MD.getColumnTypeName(i));
                            break;
                }}}
                header = false;
            } else {
                trow.remove(abilities_col);
                for(int i=0; i<trow.size(); i++)
                    trow.set(i, f(trow.get(i), types.get(i)));
                tvals.add(trow);
            }
            System.out.write('\r');
        } 
        pdI pokemon = new pdI("pokemon", theader, tvals);
        String sending = pokemon.send();
        //System.out.println(sending);
        pokemon.exec(c, sending);
        
        

        System.out.println("Evolve");
        br = csvopen(new File("./src/assets/data/evolve.csv"));
        theader = null;
        types = null;
        tvals = new ArrayList<>();
        header = true;
        cinsert=0;
        
        while ((row = br.readLine()) != null) {
            System.out.print((++cinsert));
            trow=new ArrayList<>(Arrays.asList(row.split(delim)));
            if (header) {
                theader=new ArrayList<>(trow);
                types=new ArrayList<>(trow);
                
                ResultSetMetaData MD = c.createStatement()
                    .executeQuery("SELECT * FROM evolve").getMetaData();
                for(int i=1; i<=MD.getColumnCount(); i++) {
                    String colname=MD.getColumnName(i);
                    for(String headername:theader) {
                        if (colname.equals(headername)) {
                            types.set(i-1, MD.getColumnTypeName(i));
                            break;
                }}}
                header = false;
            } else {
                for(int i=0; i<trow.size(); i++)
                trow.set(i, f(trow.get(i), types.get(i)));
                tvals.add(trow);
            }
            System.out.write('\r');
        } 
        pdI evolve = new pdI("evolve", theader, tvals);
        sending = evolve.send();
        //System.out.println(sending);
        evolve.exec(c, sending);
        
        System.out.println("Trainer");
        br = csvopen(new File("./src/assets/data/trainer.csv"));
        theader = null;
        types = null;
        tvals = new ArrayList<>();
        header = true;
        cinsert=0;
        
        while ((row = br.readLine()) != null) {
            System.out.print((++cinsert));
            trow=new ArrayList<>(Arrays.asList(row.split(delim)));
            if (header) {
                theader=new ArrayList<>(trow);
                types=new ArrayList<>(trow);
                
                ResultSetMetaData MD = c.createStatement()
                    .executeQuery("SELECT * FROM trainer").getMetaData();
                for(int i=1; i<=MD.getColumnCount(); i++) {
                    String colname=MD.getColumnName(i);
                    for(String headername:theader) {
                        if (colname.equals(headername)) {
                            types.set(i-1, MD.getColumnTypeName(i));
                            break;
                }}}
                header = false;
            } else {
                for(int i=0; i<trow.size(); i++)
                trow.set(i, f(trow.get(i), types.get(i)));
                tvals.add(trow);
            }
            System.out.write('\r');
        } 
        pdI trainer = new pdI("trainer", theader, tvals);
        sending = trainer.send();
        //System.out.println(sending);
        trainer.exec(c, sending);
    }
    /**
     * @deprecated 
     * @param f
     * @param delim
     * @throws FileNotFoundException
     * @throws IOException 
     */
    public static void completeDB(File f, String delim) throws FileNotFoundException, IOException {
        String row;
        ArrayList<String> trow;
        BufferedReader br = csvopen(f);
        ArrayList<ArrayList<String>> tvals = new ArrayList<>();
        br.readLine();
        int indx;
        while ((row = br.readLine()) != null) {  
            trow=new ArrayList<>(Arrays.asList(row.split(delim)));
            while ((indx=Integer.parseInt(trow.get(0)))!=tvals.size()) {
                tvals.add(new ArrayList<>());
                //tvals.get(indx-1).add((indx-1)+"");
            }
            tvals.get(indx-1).add(trow.get(2));
            
            
        }
        trow = new ArrayList<>();
        for(ArrayList<String> als:tvals) {
            trow.add(c(als,";"));
        }
        
        Files.write(Paths.get("src\\assets\\data\\frname.csv"), trow);
    }
    /**
     * @deprecated 
     * @param f
     * @throws FileNotFoundException
     * @throws IOException 
     */
    public static void sandboxCSV(File f) throws FileNotFoundException, IOException {
        BufferedReader br = csvopen(f);
        String row;
        int col=-1;
        ArrayList<String> trow, classif=new ArrayList<>();
        
        while ((row = br.readLine()) != null) {
            trow = new ArrayList<>(Arrays.asList(row.split(",")));
            if (col==-1) {
                col=trow.indexOf("class");
            } else {
                if (classif.indexOf(trow.get(col))==-1) {
                    classif.add(trow.get(col));
                }
            }
        }
        classif.forEach((t) -> {
            System.out.print("('"+t+"'),\n");
        });
    }
    
    /**
     * SSQL is a Special SQL file, in which, you can write field for autocompletion.
     * Use %something% to pass url to ressources.
     * @param abs
     * @param SSQLPath
     * @param stmt
     * @return 
     */
    public static boolean runSSQLFile(String abs, String SSQLPath, Statement stmt) {
        boolean isScriptExecuted = false;
        try {
            BufferedReader in = new BufferedReader(new FileReader(SSQLPath));
            String str;
            StringBuilder sb = new StringBuilder();
            while ((str = in.readLine()) != null) {
                if (str.contains("%")) {
                    str=str.replaceFirst("%", abs.replaceFirst("/", "")).replaceAll("%", "");
                    System.err.println("Passed : "+str);
                }
                sb.append(str);
            }
                
            in.close();
            stmt.executeLargeUpdate(sb.toString());
            isScriptExecuted = true;
        } catch (Exception e) {
            System.err.println("Failed to Execute" + SSQLPath + ". The error is" + e.getMessage());
        }
        return isScriptExecuted;
    }
    
    public static void sprite(String s) throws IOException, URISyntaxException {
        sprite(new File(s));
    }
    
    public static void sprite(File file) throws IOException, URISyntaxException {
       
        int b=85;
        int f=170;

        BufferedImage img = ImageIO.read(new FileInputStream(file));     
        
        int X=img.getWidth();
        int Y=img.getHeight();
        int mx=X,Mx=0,My=0,my=Y;
        
        Color[][] pxls = new Color[X][Y];        
        
        //System.out.println(X+":"+Y);
        for(int y=0;y<Y;y++) {
            for(int x=0;x<X;x++) {
                try {
                    if ((pxls[x][y]=new Color(
                        img.getRGB(x,y), 
                        true
                    )).getTransparency()==1) {
                        //System.out.print("("+x+":"+y+")");
                        if (x>Mx) Mx=x;
                        if (x<mx) mx=x;
                        if (y>My) My=y;
                        if (y<my) my=y;
                    }
                } catch(ArrayIndexOutOfBoundsException aioobe) {
                }
            }
        }
        
        //System.out.println("X:"+mx+"-"+Mx+" Y:"+my+"-"+My);
        
        for(int y=my;y<My;y++) {
            for(int x=mx;x<Mx;x++) {
                Color pxl=pxls[x][y];
                //(pxl>>24)&0xff)
                if (pxl.getTransparency()==2) {
                    System.out.print("\033[38;2;"+f+";"+f+";"+f+"m░░\033[0m");
                } else {
                    System.out.print("\033[38;2;"+(pxl.getRed())+";"+(pxl.getGreen())+";"+(pxl.getBlue())+"m██\033[0m");
                }
                
            }
            System.out.println("");
        }
        //Color clr = pxls[5][5];
        //System.out.println(clr.getTransparency()+":"+clr.getRed()+":"+clr.getGreen()+":"+clr.getBlue());
    }

}
