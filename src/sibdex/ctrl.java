/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sibdex;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Paths;
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
import misc.util;
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
        },

    };
    /*
    private interface quickQuery {
        String quickdo(String[] args);
    }
    */
    
    
    private static final String[] sql_inits = new String[] {
        "./src/assets/sql/create.ssql",
        "./src/assets/sql/insert.ssql",
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
        
    public static void access(String[] uinfo)  {
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
                    String[] idinit = new String[] {"postgres", "su"};
                    //securePrompt.request(true, true);
                    c = DriverManager.getConnection("jdbc:postgresql://localhost/", idinit[0], idinit[1]);
                    c.createStatement().executeUpdate("CREATE DATABASE pokemon;");
                    //c.close();
                    c = DriverManager.getConnection("jdbc:postgresql://localhost/pokemon", idinit[0], idinit[1]);
                    c.createStatement().executeUpdate("CREATE SCHEMA chen");
                    c.createStatement().executeUpdate("SET search_path TO chen");
                    //c.close();
                    //c = DriverManager.getConnection("jdbc:postgresql://localhost/pokemon?currentSchema=chen", idinit[0], idinit[1]);
                    System.out.println("Database pokemon has been created.");
                    
                    for (String s:sql_inits)                                    // executing sql scripts
                        util.runSSQLFile(
                                ctrl.class.getResource("../").toURI().toString().replaceAll("%20", " ").replaceAll("file:", ""),
                                s, 
                                c.createStatement());
                    util.runSSQLFile("", "./src/assets/sql/create.ssql", c.createStatement());
                    util.fillDB(c, new File("./src/assets/sql/pokemon_bck_utf8.csv"), ",");
                    
                    System.out.println("Database pokemon has been filled. Try manually test.sql to check.");
                    c.close();
                    System.out.println("Setup achieved successfully. You can now connect as user.");
                } catch (SQLException ex1) {
                    System.out.println(ex1.getMessage());
                    System.err.println("Error occured while initializing. Retrying...");
                    access();
                } catch (IOException ex1) {
                    System.err.println("Can't find csv data.");
                } catch (URISyntaxException ex1) {
                    Logger.getLogger(ctrl.class.getName()).log(Level.SEVERE, null, ex1);
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
     protected ArrayList getPokemonStmt(Connection con, String psql) throws SQLException{
        Statement stmt = con.createStatement();
        ResultSet rs = stmt.executeQuery(psql);
        //On recup les pokÃ©mons sous forme d'une ArrayList contenant des pokÃ©mons
        ArrayList<Pokemon> pokemons = new ArrayList<Pokemon>();
        while(rs.next()){
            Pokemon pokemon = new Pokemon();
            pokemon.setId(rs.getInt("id"));
            pokemon.setName(rs.getString("name"));
            pokemon.setType1(rs.getString("type1"));
            pokemon.setType2(rs.getString("type2"));
            pokemon.setGen(rs.getInt("gen"));
            pokemons.add(pokemon);
        }
        rs.close();
        return(pokemons);
    }
    protected ArrayList getAllPokemon(Connection con) throws SQLException{
        return(getPokemonStmt(con, "SELECT * FROM pokemon"));
    }
    protected ArrayList getPokemonByType(Connection con, String type) throws SQLException{
        return(getPokemonStmt(con, "SELECT * FROM pokemon WHERE type1 ='"+type+"' OR type2= '"+type+"'"));            
    }
    protected ArrayList getPokemonByName(Connection con, String name) throws SQLException{
        return(getPokemonStmt(con, "SELECT * FROM pokemon WHERE name LIKE '"+name+"%'"));
    }
    //rÃ©cupÃ©ration d'un pokÃ©mon via son ID 
    protected Pokemon getPokemonByID(Connection con,int id) throws SQLException{
        Pokemon pokemon;
        Statement stmt = con.createStatement();
        String psql = "Select * FROM pokemon WHERE id = "+Integer.toString(id);
        ResultSet rs = stmt.executeQuery(psql);
        // pokemon hasn't been initialised
        pokemon = new Pokemon();
        if(rs.next()){
        pokemon.setName(rs.getString("name"));
        pokemon.setType1(rs.getString("type1"));
        pokemon.setType2(rs.getString("type2"));
        pokemon.setId(rs.getInt("id"));
        pokemon.setAttack(rs.getInt("attack"));
        pokemon.setSpeattack(rs.getInt("spe_attack"));
        pokemon.setSpedefense(rs.getInt("spe_defense"));
        pokemon.setDefense(rs.getInt("defense"));
        pokemon.setSpeed(rs.getInt("speed"));
        pokemon.setHealth(rs.getInt("health"));
        pokemon.setExperience(rs.getInt("experience"));
        pokemon.setHeight(rs.getInt("height"));
        pokemon.setWeight(rs.getInt("weight"));
        pokemon.setGen(rs.getInt("gen"));
        pokemon.setAgainst_bug(rs.getInt("against_bug"));
        pokemon.setAgainst_dark(rs.getInt("against_dark"));
        pokemon.setAgainst_dragon(rs.getInt("against_dragon"));
        pokemon.setAgainst_electric(rs.getInt("against_electric"));
        pokemon.setAgainst_fairy(rs.getInt("against_fairy"));
        pokemon.setAgainst_fight(rs.getInt("against_fight"));
        pokemon.setAgainst_flying(rs.getInt("against_flying"));
        pokemon.setAgainst_fire(rs.getInt("against_fire"));
        pokemon.setAgainst_ghost(rs.getInt("against_ghost"));
        pokemon.setAgainst_grass(rs.getInt("against_grass"));
        pokemon.setAgainst_ground(rs.getInt("against_ground"));
        pokemon.setAgainst_ice(rs.getInt("against_ice"));
        pokemon.setAgainst_normal(rs.getInt("against_normal"));
        pokemon.setAgainst_poison(rs.getInt("against_poison"));
        pokemon.setAgainst_psychic(rs.getInt("against_psychic"));
        pokemon.setAgainst_rock(rs.getInt("against_rock"));
        pokemon.setAgainst_steel(rs.getInt("against_steel"));
        pokemon.setAgainst_water(rs.getInt("against_water"));
        }
        rs.close();
        return(pokemon);
    }
    protected void PrintPkmnList(ArrayList<Pokemon> pokemons){
        for(int i=0; i<= pokemons.size();i++){
            System.out.println(
                    pokemons.get(i).id
                    + " | " + pokemons.get(i).name
                    + " | TYPE1 : "+ pokemons.get(i).type1
                    + " | TYPE2 : "+ pokemons.get(i).type2
                    + " | GEN : " + pokemons.get(i).gen);
        }
    }
    
    //Cherche une sous Ã©volution s'il n'y en a aucune, modifie le boolean dÃ©finit dans la classe Pokemon
    protected Pokemon getDownEvolution(Connection con, Pokemon pokemon) throws SQLException {
        String psql = "SELECT pd.id FROM evolve e JOIN pokemon pd ON e.down=pd.id JOIN pokemon pu ON e.up=pu.id WHERE pu.id ="+pokemon.id;
        Statement stmt = con.createStatement();
        ResultSet rs = stmt.executeQuery(psql);
        int id = 0 ;
        Pokemon pokemonR = new Pokemon();
        if(rs.next()){
            id = rs.getInt("id");
            pokemonR = getPokemonByID(con,id);
            pokemon.setHasDownEv(true);
        } else {
            pokemon.setHasDownEv(false);
        }
        return(pokemonR);
    }
    //Cherche une surÃ©volution s'il n'y en a aucune renvoie un "pokemon" ayant un ID de 0
    protected Pokemon getUpEvolution(Connection con, Pokemon pokemon) throws SQLException{
        String psql = "SELECT pu.id FROM evolve e JOIN pokemon pd ON e.down = pd.id JOIN pokemon pu ON e.up = pu.id WHERE pd.id ="+pokemon.id;
        Statement stmt = con.createStatement();
        ResultSet rs = stmt.executeQuery(psql);
        int id = 0 ;
        Pokemon pokemonR = new Pokemon();
        if(rs.next()){
            id = rs.getInt("id");
            pokemonR = getPokemonByID(con,id);
            pokemon.setHasUpEv(true);
        } else {
            pokemon.setHasUpEv(false);
        }
        return(pokemonR);
    }
    
    //retourne une arraylist contenant la famille d'Ã©volution du pokÃ©mon
    protected ArrayList getPokemonEvolutions(Connection con, Pokemon pokemon) throws SQLException{
        ArrayList<Pokemon> evolutions = new ArrayList();
        Pokemon Up = new Pokemon();
        Up = getUpEvolution(con, pokemon);
        Pokemon Down = new Pokemon();
        Down = getDownEvolution(con,pokemon);
        evolutions.add(Down);
        evolutions.add(Up);
        //si pas d'Ã©volutions ou dÃ©jÃ  1 Ã©volution dessus / 1 Ã©volution dessous : STOP et renvoie
        if(((pokemon.hasDownEv == false) && (pokemon.hasUpEv == false)) ||((pokemon.hasDownEv == true)&&(pokemon.hasUpEv == true))){
          return(evolutions);
        } else {
            //sinon si il n'y a pas dessous, teste si il y a 2 dessus et si oui l'ajoute
            if(pokemon.hasDownEv == false){
                Pokemon UpUp = getUpEvolution(con,Up);
                if(Up.hasUpEv == true){
                    evolutions.add(Up);
                }
            //si il y a dessous teste si il y a 2 dessous et si oui l'ajoute
            } else {
                Pokemon DownDown = getDownEvolution(con,Down);
                if(Down.hasDownEv == true){
                    evolutions.add(DownDown);
                }
            }
        }
        return(evolutions);
    }
    protected void PokemonDisplay(Connection con, int id) throws SQLException{
        System.out.println(getPokemonByID(con,id));
        System.out.println("----- E V O L U T I O N S -----");
        
    }
}
