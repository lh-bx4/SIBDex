/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sibdex;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.SQLTimeoutException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Stream;
import static jdk.nashorn.internal.objects.NativeMath.min;
import misc.read2;
import static misc.read2.S;
import misc.util;
import static misc.util.SPIKES;
import org.postgresql.util.PSQLException;
import sibdex.pokedata.pdI;

/**
 *  mettre tout les fonctions de controle ici
 * @author Louis
 */
public class ctrl {
    
    private static Connection CNX=null;
    private static final int GUESTLVL=0, TRAINERLVL=1, SCIENTISTLVL=2;
    private static final AccessType[] AT = new AccessType[] {
        new AccessType(GUESTLVL, "GUEST"),
        new AccessType(TRAINERLVL, "TRAINER"),
        new AccessType(SCIENTISTLVL, "SCIENTIST"),
    };
    private static final CLIAction[] CLIA = new CLIAction[] {
        new CLIAction("insert") {
            @Override
            public void does() {
                String tmp;
                ArrayList<String> tc;
                ArrayList<String> tvr;
                ArrayList<String[]> tv;
                ResultSetMetaData MD;
                System.out.println("Give the table name where to insert data :");
                String tn = read2.S();
                try {
                    MD = CNX.createStatement()
                            .executeQuery("SELECT * FROM "+tn).getMetaData();
                } catch (SQLException ex) {
                    System.err.println(ex.getMessage());
                    return;
                }
                try {
                    int c=MD.getColumnCount();

                    for (int k=1;k<=c;k++) {
                        System.out.print(MD.getColumnName(k)+":"+MD.getColumnTypeName(k)+" ");
                    }
                } catch (SQLException ex) {
                    System.err.println(ex.getMessage());
                    return;
                }
                System.out.println("Give column names to insert. Empty string to stop.");
                tc = new ArrayList<String>();
                // add limit on the column name
                while (!(tmp=read2.S()).equals("")) {
                    tc.add(tmp);
                }
                System.out.println("Give values. Empty string to stop, + to continue.");
                tv = new ArrayList<String[]>();
                // add limit on the column name
                int i=0;
                do {
                    System.out.println(++i);
                    tvr=new ArrayList<String>();
                    for(String s:tc) {
                        System.out.print(s+":");
                        tvr.add(read2.S());
                    }
                    tv.add(tvr.toArray(new String[0]));
                } while (!"".equals(read2.S()));
                add(new pdI(tn, tc.toArray(new String[0]), tv.toArray(new String[0][0])));
            }
        }
    };
    private static final String[] sql_inits = new String[] {
        "./src/assets/sql/create.sql",
        "./src/assets/sql/insert.sql",
    };
    private static AccessType CAX;
    
    /**
     * see defined accestype
     * @return if access is correclty sat up
    */
    public static boolean isAccessible() {return CNX!=null && CAX!=null;}
    /**
     * log in function
     * @param args 
     * type "T <accesslevel> U <username> P <password>"
     */
    public static void access() {
        access(null);
    } 
        
    public static void access(String[] uinfo) {
        String[] id;
        id=(uinfo!=null&&uinfo.length!=2)?uinfo:securePrompt.request(true, true);
        String usn = id[0];
        String pw = id[1];
        
        Connection c=null;
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException ex) {
        }
        try {
            // connect to bdd
            c = DriverManager.getConnection("jdbc:postgresql://localhost/pokemon?currentSchema=chen", usn, pw);
        } catch (PSQLException ex) {
            // server off
            if (ex.getMessage().contains("Connection to localhost") && ex.getMessage().contains("refused.")) {
                System.err.println("Connection error. Make sure psql service is running.");
            }
            // bdd doesn't exists
            else if (ex.getMessage().contains("la base de données") && ex.getMessage().contains("n'existe pas")) {
                // init
                try {
                    System.err.println("Please provide super user access to postgres for initialisation.");
                    String[] idinit = securePrompt.request(true, true);
                    c = DriverManager.getConnection("jdbc:postgresql://localhost/", idinit[0], idinit[1]);
                    c.createStatement().executeUpdate("CREATE DATABASE pokemon;");
                    c.close();
                    c = DriverManager.getConnection("jdbc:postgresql://localhost/pokemon", idinit[0], idinit[1]);
                    System.out.println("Database pokemon has been created.");
                    for (String s:sql_inits)                                    // executing sql scripts
                        runSQLFile(s, c.createStatement());
                    System.out.println("Database pokemon has been filled. Try manually test.sql to check.");
                    c.close();
                    System.out.println("Setup achieved successfully. You can now connect as user.");
                } catch (SQLException ex1) {
                    System.out.println(ex1.getMessage());
                    System.err.println("Error occured while initializing. Retrying...");
                    access(id);
                }                
            } else if (ex.getMessage().contains("authentification par mot de passe échouée pour l'utilisateur")) {
                System.err.println("Wrong username or password.");
                access(id);
            } else {
                System.out.println(ex.getMessage());
            }
        } catch (SQLException ex) {
            System.out.println(":"+ex.getMessage());
        }    
        
        if (c!=null) {
            CNX=c;
            System.out.println("Connection established.");
        } else {
            System.err.println("Access couldn't be established for unknown reason. Please try again.");
            return;
        }
        CAX=fa(id[2]);
    }

    /**
     * find access
     * @param acnm accesslevel name
     * @return AccessType from name acnm
     */
    private static AccessType fa(String acnm) {
        for(AccessType r:AT) if (acnm.equals(r.name)) return r;
        return null;
    }
    
    private static boolean checkAccess(int[] permission) {
        int m = permission[0];
        for(int ab:permission)m=ab<m?ab:m;
        return CAX!=null?CAX.level>=m:false;
    }
    
    private static boolean isDB(Connection c) throws SQLException {
        ResultSet resultSet = c.getMetaData().getCatalogs();
        while (resultSet.next()) {
          if (resultSet.getString(1).equals("pokemon")) {
                return true;
            }
        }
        return false;
    }
    public static boolean runSQLFile(String aSQLScriptFilePath, Statement stmt) {
        boolean isScriptExecuted = false;
        try {
            BufferedReader in = new BufferedReader(new FileReader(aSQLScriptFilePath));
            String str;
            StringBuffer sb = new StringBuffer();
            while ((str = in.readLine()) != null) {
                sb.append(str + "\n ");
            }
            in.close();
            stmt.executeUpdate(sb.toString());
            isScriptExecuted = true;
        } catch (Exception e) {
            System.err.println("Failed to Execute" + aSQLScriptFilePath +". The error is"+ e.getMessage());
        }
        return isScriptExecuted;
    }
    
    protected static void add(pdI I){
        if (!checkAccess(new int[] 
            {TRAINERLVL, SCIENTISTLVL}
        )) {
            System.err.println("Access denied.");
            //return;
        };
        System.out.println(I.send());
        I.exec(CNX, I.send());
    }
    
    protected void update(){
        
    }
    
    protected void remove(){
       
    }
    
    public static void pactions() {
        System.out.println("LOG TYPE : "+CAX.name);
        System.out.print("Actions:{ ");
        for(CLIAction action:CLIA) {
            System.out.print(action.name+" ");
        }
        System.out.println("quit }");
    }
    
    public static boolean read_action(String a) {
        for(CLIAction action:CLIA) {
            if (a.toLowerCase().equals(action.name)) {
                action.does();
                return true;
            }
        }
        return !a.toLowerCase().equals("quit");
    }
       
    private static class AccessType {
        protected final int level;
        protected final String name;
        
        /**
         * complete access parametrisation
         * @param lvl
         * @param nm
         * @param USN
         * @param PWD
         * @param hasUsername
         * @param hasPassword 
         */
        protected AccessType(int lvl, String nm) {
            this.level=lvl;
            this.name=nm;
        }
    }
    
    private static abstract class CLIAction {

        public final String name;
        
        public CLIAction(String nm) {
            name=nm;
        }
        
        public abstract void does();
        
    }

}
