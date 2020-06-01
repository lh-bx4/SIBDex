/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sibdex;

import java.io.IOException;
import java.net.URISyntaxException;
import static misc.read2.pS;
import misc.util;

/**
 *
 * @author Louis
 */
public class cli {
    
    public static void main(String[] args) throws IOException, URISyntaxException {
        run();
        System.exit(0);
    }
    
    public static void run() {
       
        do {
            ctrl.access(new String[] {"postgres", "su"});
        } while(!ctrl.isAccessible());
        
        String C;
        do {
            // cls escapes
            System.out.println("\f");
            System.out.print("\033[H\033[2J");  
            System.out.flush();
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
}
