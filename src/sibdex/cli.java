/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sibdex;

import java.awt.Toolkit;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import static misc.read2.pS;
import misc.util;

/**
 *
 * @author Louis
 */
public class cli {
    
    public static void main(String[] args) throws IOException, URISyntaxException {
        (new cli()).run();
        System.exit(0);
    }
    
    public void run() throws URISyntaxException {
       
        do {
            ctrl.access(new String[] {"postgres", "su"});
        } while(!ctrl.isAccessible());
        String uri = cli.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath();
        System.out.println("Running in "+uri);
        try {
            File[] i = (new File(uri+"/assets/img/sprites/")).listFiles();
            File opening = i[
                    (new Random()).nextInt(
                            i.length)];
            System.out.println(opening.getName().substring(0, 3));
            util.sprite(opening);
        } catch (IOException ex) {
            Logger.getLogger(cli.class.getName()).log(Level.SEVERE, null, ex);
        } catch (URISyntaxException ex) {
            Logger.getLogger(cli.class.getName()).log(Level.SEVERE, null, ex);
        }
        String C;
        do {
            // cls escapes
            //System.out.println("\f");
            //System.out.print("\033[H\033[2J");  
            //System.out.flush();
            ctrl.pactions();
            
            
        } while(ctrl.read_action(pS()));
    }
    
    
    /*
    public static void test1() {
        pdI t = new pdI("pokemon", new String[] {"health", "name"}, new String[][] {
            {"20", "zerma"},
            {"30", "zebi"},
        });
        // why my CNx is null pointer ?
        ctrl.add(t);
    }
*/

    public cli() {
    }
}
