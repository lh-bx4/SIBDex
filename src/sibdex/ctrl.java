/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sibdex;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Stream;
import static jdk.nashorn.internal.objects.NativeMath.min;
import static misc.read2.S;
import misc.util;
import static misc.util.SPIKES;
import sibdex.pokedata.pdI;

/**
 *  mettre tout les fonctions de controle ici
 * @author Louis
 */
public class ctrl {
    
    private static final Properties prop = new Properties();
    private static final String prop_file = "./src/assets/config.properties";
    private static final String FIRSTLAUNCH="first_launch";
    private static Connection CNX=null;
    /**
     * change following your postgres instalation
     */
    private static final String 
            DEVUSN="postgres",
            DEVPWD="su";
    
    private static final boolean _devMode=true;
    private static final int GUESTLVL=0, TRAINERLVL=1, SCIENTISTLVL=2;
    private static final AccessType[] AT = new AccessType[] {
        new AccessType(GUESTLVL, "GUEST", "guestusn", "guestpwd"),
        new AccessType(TRAINERLVL, "TRAINER"),
        new AccessType(SCIENTISTLVL, "SCIENTIST"),
        new AccessType(3, "DEVMODE", DEVUSN, DEVPWD),
    };
    private static final String url="jdbc:postgresql://localhost/pokemon";
    private static final String[] sql_inits = new String[] {
        "./src/assets/sql/create.sql"
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
    public static void access(String[] args) 
        throws 
            SQLException,
            SQLTimeoutException,
            AssertionError,
            UnsupportedOperationException 
    {
        int _tmp;
        String _an;
        ArrayList<String> _load = new ArrayList<>(Arrays.asList(args));
        String _username,_password;
        
        if (_load.size()==0) {
            String[] id = securePrompt.request(true, true);
            CNX=psql_connect(id);
            CAX=_fa(id[2]);
        } else if ((_tmp=_load.indexOf("T"))!=-1)                               // Seeking type...
        {
            if ((ctrl.CAX=_fa(_an=_load.get(_tmp+1)))!=null)                         /// Finding type...
            {
                System.out.println(CAX.level+CAX.name);
                _username=CAX.usn;
                _password=CAX.pwd;
                if (CAX.un) {if ((_tmp=_load.indexOf("U"))!=-1)                 /// Prompted username required ?... Seeking username...
                {
                    _username=_load.get(_tmp+1);                                //// Register username
                } else                                                          /// No username found
                {
                    throw new AssertionError(
                        "Please specify username.\n SYNTAX:\"U <username>\""
                    );
                }}
                if (CAX.pw) {if ((_tmp=_load.indexOf("P"))!=-1)                 /// Prompted password required ?... Seeking password...
                {
                    _password=_load.get(_tmp+1);                                //// Register password
                } else                                                          /// No password found
                {
                    throw new AssertionError(
                        "Please specify password.\n SYNTAX:\"P <password>\""
                    );
                }}
                CNX=psql_connect(securePrompt.request(CAX.un, CAX.pw));
            } else                                                              /// Unknown type... 
            {
                throw new AssertionError(
                    "Unknown access level : \""+_an+"\". add access names here"   
                );
            }                                     
        } else                                                                  // No type found
        {
            ArrayList<String> atn = new ArrayList<>();
            for(AccessType at:AT) atn.add(at.name);
            throw new AssertionError(
                "Unknown type. "+util.d(atn, '|', SPIKES)
            );
        }
    }

    /**
     * find access
     * @param acnm accesslevel name
     * @return AccessType from name acnm
     */
    private static AccessType _fa(String acnm) {
        for(AccessType r:AT) if (acnm.equals(r.name)) return r;
        return null;
    }
    
    
    private static Connection psql_connect(String[] id) throws SQLException {
        return psql_connect(id[0], id[1]);
    }
    public static Connection psql_connect(String usn, String pwd) throws SQLException {
        return DriverManager.getConnection(url, usn, pwd);
    }
    
    public static void connect() throws SQLException {
        connect(CAX.usn, CAX.pwd);
    }
    public static void connect(String usn, String pwd) throws SQLException,SQLTimeoutException {
        CNX=null;
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(ctrl.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.out.println("Attempt to connect to <"+usn+"> <"+pwd+">");
        try {
            CNX = DriverManager.getConnection(url, usn, pwd);
            System.out.println("Connection successfully established.");
        } catch (SQLException ex) {
            System.err.println("An error has occured, please try again.");
            Logger.getLogger(ctrl.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private static boolean checkAccess(int[] permission) {
        int m = permission[0];
        for(int ab:permission)m=ab<m?ab:m;
        return CAX!=null?CAX.level>=m:false;
    }
    

    
    private static FileOutputStream config() {
        File f;
        FileOutputStream fos;
        f=new File(prop_file);
        try {
            f.createNewFile();
            fos=new FileOutputStream(f);
            prop.setProperty(FIRSTLAUNCH, "true");                              // set the property file
            prop.store(fos, null);
            return fos;
        } catch (IOException ex) {
            System.err.println("Cannot create file. "+ex);
            return null;
        }
    }
    /**
     * Initialize data in sql. See inits[] for request files.
     * @param usn Connection username
     * @param pwd Connection password
     * @throws SQLException : retry if connection failed
     */
    public static void init() throws SQLException {
        File pf = new File(prop_file);
        FileInputStream in;
        FileOutputStream out;
        if (!pf.exists()) {
            try {
                pf.createNewFile();
            } catch (IOException ex) {
                System.err.println("Unknown exception while creating properties file.");
                System.err.println("Be sure to have permission in working directory.");
            }
            try {
                out = new FileOutputStream(pf);
                prop.setProperty(FIRSTLAUNCH,"true");                           // set property file
                prop.store(out, null);
                init();
            } catch (FileNotFoundException ex) {
                System.err.println("Unknow error.");                             // This error shouldn't be possible due to the if statement.
            } catch (IOException ex) {
                System.err.println("Unknow error.");
            }
        } else {
            try {
                in = new FileInputStream(pf);
                prop.load(in);
                if (prop.getProperty(FIRSTLAUNCH).equals("true"))               // if first launch
                {
                    System.out.println("For SIBex to work, you need to provide your Super User PSQL login.");
                    System.out.println("You will need to repeat this operation untill initialisation succeed.");
                    Connection cnx;
                    cnx=psql_connect(securePrompt.request(true, true));
                    //psql_connect(usn, pwd);
                    for (String s:sql_inits)                                    // executing sql scripts
                        cnx.createStatement().executeQuery("\\i "+s);
                    prop.setProperty(FIRSTLAUNCH, "false");
                    cnx.close();
                } else {
                    System.out.println("Initilisation skiped.");
                }
            } catch (FileNotFoundException ex) {
                System.err.println("Unknow error");                             // This error shouldn't be possible due to the if statement.
            } catch (IOException ex) {
                System.err.println("Prop cannot be loaded or else.");
            }
        }
    }
    
    protected static void add(pdI I){
        if (!checkAccess(new int[] 
            {TRAINERLVL, SCIENTISTLVL}
        )) {
            System.err.println("Access denied.");
            return;
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
        System.err.println("Print authorized action here.");
    }
    
    public static boolean read_action(String a) {
        System.err.println("Do action here");
        return !a.toLowerCase().equals("quit");
    }
       
    private static class AccessType {
        protected final int level;
        protected final String name,usn,pwd;        
        protected boolean un=false,pw=false;                                    // is usn/pwd is prompted (true) or auto (false)
        
        /**
         * complete access parametrisation
         * @param lvl
         * @param nm
         * @param USN
         * @param PWD
         * @param hasUsername
         * @param hasPassword 
         */
        protected AccessType(int lvl, String nm, String USN, String PWD, boolean hasUsername, boolean hasPassword) {
            this.level=lvl;
            this.name=nm;
            this.usn=USN;
            this.pwd=PWD;
            this.un=hasUsername;
            this.pw=hasPassword;
        }
        /**
         * auto log in
         * @param lvl
         * @param nm
         * @param USN
         * @param PWD 
         */
        protected AccessType(int lvl, String nm, String USN, String PWD) {
            this(lvl, nm, USN, PWD, false, false);
        }
        
        /**
         * Username and empty password auto
         * @param lvl
         * @param nm
         * @param USN 
         */
        protected AccessType(int lvl, String nm, String USN) {
            this(lvl, nm, USN, "", false, false);
        }
        
        /**
         * fully prompted
         * @param lvl
         * @param nm 
         */
        protected AccessType(int lvl, String nm) {
            this(lvl, nm, "", "", true, true);
        }


    }
    //Récupère seulement les ID et les Noms pour l'affichage général : chaque paire correspond à 1 poké
    //On n'a aucun intérêt à récup toutes les datas car elles ne seront pas toutes display
    protected ArrayList getAllPokemon(Connexion con){
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
    protected Pokemon getPokemon(Connexion con,int id){
        Pokemon pokemon;
        Statement stmt = con.createStatement();
        String psql = "Select * FROM pokemon WHERE id = "+Integer.toString(id);
        ResultSet rs = stmt.executeQuery(psql);
        if(rs.next()){
        pokemon.setId(rs.getInt("id"));
        pokemon.setAtk(rs.getInt("attack"));
        pokemon.setDef(rs.getInt("Defense"));
        
        }
        return(pokemon);
    }

}
