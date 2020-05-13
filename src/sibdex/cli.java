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
import static sibdex.pokedata.*;

/**
 *
 * @author Louis
 */
public class cli {
    public static void main(String[] args) {
        ctrl.setUrl("jdbc:postgresql://localhost/pokemon");
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
        pdI t = new pdI("pokemon", new String[] {"health", "name"}, new String[][] {
            {"20", "zerma"},
            {"30", "zebi"},
        });
        // why my CNx is null pointer ?
        ctrl.add(t);

    }
}
