/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sibdex;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.SQLTimeoutException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Stream;
import static jdk.nashorn.internal.objects.NativeMath.min;
import sibdex.pokedata.pdI;

/**
 *  mettre tout les fonctions de controle ici
 * @author Louis
 */
public class ctrl {
    
    private static Connection CNX;
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
    private static String url="";
    public static void setUrl(String u) {url=u;}
    public static boolean isUrl() {return url.length()>0;}
    
    private static AccessType CAX;
    /**
     * see defined accestype
     * @deprecated
     */
    private static int AXLVL=-1;
    public static boolean isAccessible() {return AXLVL!=-1;}
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
        
        if ((_tmp=_load.indexOf("T"))!=-1)                               // Seeking type...
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
            } else                                                              /// Unknown type... 
            {
                throw new AssertionError(
                    "Unknown access level : \""+_an+"\". add access names here"   
               );
            }                                     
        } else                                                                  // No type found
        {
            throw new AssertionError(
                "Please specify access level.\n SYNTAX:\"T <GUEST|TRAINER|SCIENTIST>\"" //programmatically get the list of defined accesses
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
    
    
    public static void connect() throws SQLException {
        connect(CAX.usn, CAX.pwd);
    }
    public static void connect(String username, String pwd) throws SQLException,SQLTimeoutException {
        if (isUrl()) {
            try {
                Class.forName("org.postgresql.Driver");
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(ctrl.class.getName()).log(Level.SEVERE, null, ex);
            }
            CNX = DriverManager.getConnection(
                url,username,pwd
            );
            //System.out.println("Connction is valid ? "+c.isValid(2000));
            System.out.println("Bienvenue "+username+".");
            System.out.println("Vous êtes entrés dans le SIBDex !");
        } else {
            System.err.println("No url");
        }
    }  
    
    private static boolean checkAccess(int[] permission) {
        int m = permission[0];
        for(int ab:permission)m=ab<m?ab:m;
        return CAX.level>=m;
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

}
