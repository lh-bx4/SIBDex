/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sibdex;

import java.sql.SQLException;
import java.sql.SQLTimeoutException;
import java.util.logging.Level;
import java.util.logging.Logger;
import static misc.read2.S;
import static misc.read2.pS;
import static sibdex.pokedata.*;

/**
 *
 * @author Louis
 */
public class cli {
    
    static String[] 
            args1 = new String[] {
                "T", "GUEST", 
                "U", "poes", 
                "P", "s"
            },
            dm = new String[] {
                "T", "DEVMODE"
            },
            empty = new String[] {}
    ;
    
    public static void main(String[] args) {
        run(empty);
    }
    
    public static void run(String[] args) {

        try {
            ctrl.init();
        } catch (SQLException ex) {
            System.err.println("Error has occured. Please retry.");
            Logger.getLogger(cli.class.getName()).log(Level.SEVERE, null, ex);
            return;
        }
        
        do {
            try {
                ctrl.access(args);
            } catch (AssertionError ae) {
                Logger.getLogger(cli.class.getName()).log(Level.INFO, null, ae);
            } catch (UnsupportedOperationException uoe) {
                Logger.getLogger(cli.class.getName()).log(Level.SEVERE, null, uoe);
            } catch (SQLTimeoutException te) {
                Logger.getLogger(cli.class.getName()).log(Level.WARNING, null, te); 
            } catch (SQLException e){
                Logger.getLogger(cli.class.getName()).log(Level.SEVERE, null, e);
                System.err.println("Make sure JDBC driver is installed");
            }
        } while(!ctrl.isAccessible());
        
        String C;
        do {
            // cls escapes
            System.out.println("\f");
            System.out.print("\033[H\033[2J");  
            System.out.flush();
            ctrl.pactions();
            
            
        } while(ctrl.read_action(pS()));
        return;

    }
    
    public static void test1() {
        pdI t = new pdI("pokemon", new String[] {"health", "name"}, new String[][] {
            {"20", "zerma"},
            {"30", "zebi"},
        });
        // why my CNx is null pointer ?
        ctrl.add(t);
    }
}
