
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.postgresql.util.PSQLException;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Louis
 */
public class test {
    public static void main(String[] args) {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(test.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            Connection c = DriverManager.getConnection("jdbc:postgresql://localhost/whatthe", "postgres", "su");
        } catch (PSQLException ex) {
            System.out.println(ex.getErrorCode());
            if (ex.getMessage().contains("Connection to localhost") && ex.getMessage().contains("refused.")) {
                System.err.println("Connection erro. Make sure psql service is running.");
            } else if (ex.getMessage().contains("la base de données") && ex.getMessage().contains("n'existe pas")) {
                try {
                    Connection c = DriverManager.getConnection("jdbc:postgresql://localhost:5432/pokedex?currentSchema=pokedex", "postgres", "su");
                    
                } catch (SQLException ex1) {
                    System.out.println(ex1.getErrorCode());
                }
                
            }
            Logger.getLogger(test.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            
        }
        
    }
}
