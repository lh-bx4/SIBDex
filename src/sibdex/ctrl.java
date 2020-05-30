/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sibdex;

import java.io.BufferedReader;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import misc.read2;
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
            public void does() throws SQLException {
                if (!checkAccess(new int[] 
                    {TRAINERLVL, SCIENTISTLVL}
                )) {
                    System.err.println("Access denied.");
                    //return;
                };
                
                try {
                    pdI I = new pdI(CNX);
                    String s = I.send();
                    System.out.println(s);
                    I.exec(CNX, s);
                } catch (UnsupportedOperationException uoe) {
                    System.err.println(uoe.getMessage());
                }
            }
        },
        new CLIAction("whateveryouwant") {
            @Override
            public void does() throws SQLException {
                System.out.println("Check ctrl.jav line 34 to add other CLIActions. They are listed automatically.");
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
        boolean t;
        id=(t=uinfo!=null&&uinfo.length==2)?uinfo:securePrompt.request(true, true);
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
                    access();
                }                
            } else if (ex.getMessage().contains("authentification par mot de passe échouée pour l'utilisateur")) {
                System.err.println("Wrong username or password.");
                access();
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
        CAX=t?AT[2]:fa(id[2]);
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
                try {
                    action.does();
                } catch (SQLException ex) {
                    System.err.println(ex.getMessage());
                    Logger.getLogger(ctrl.class.getName()).log(Level.SEVERE, null, ex);
                }
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
        
        public abstract void does() throws SQLException;

    }
    //Récupère seulement les ID et les Noms pour l'affichage général : chaque paire correspond à 1 poké
    //On n'a aucun intérêt à récup toutes les datas car elles ne seront pas toutes display
    protected ArrayList getAllPokemon(Connection con) throws SQLException{
        Statement stmt = con.createStatement();
        String psql = "SELECT id,name FROM pokemon" ;
        ResultSet rs = stmt.executeQuery(psql);
        //On recup les pokémons sous forme d'une ArrayList contenant des paires (id,name)
        ArrayList<ArrayList<String>> pokemons = new ArrayList<ArrayList<String>>();
        while(rs.next()){
            ArrayList<String> pokemon = new ArrayList<String>();
            int id = rs.getInt("id");
            String name = rs.getString("name");
            pokemon.add(Integer.toString(id));
            pokemon.add(name);
            pokemons.add(pokemon);
        }
        rs.close();
        return(pokemons);
    }
    protected Pokemon getPokemon(Connection con,int id) throws SQLException{
        Pokemon pokemon;
        Statement stmt = con.createStatement();
        String psql = "Select * FROM pokemon WHERE id = "+Integer.toString(id);
        ResultSet rs = stmt.executeQuery(psql);
        // pokemon hasn't been initialised
        pokemon = new Pokemon();
        if(rs.next()){
        pokemon.setId(rs.getInt("id"));
        pokemon.setAtk(rs.getInt("attack"));
        pokemon.setDefense(rs.getInt("Defense"));
        
        }
        return(pokemon);
    }

}
