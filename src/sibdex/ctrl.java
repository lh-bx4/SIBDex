/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sibdex;

import java.sql.Connection;
import java.sql.DriverManager;

/**
 *  mettre tout les fonctions de controle ici
 * @author Louis
 */
public class ctrl {
 
    public static void connect(String url, String username, String pwd){
       try{
        Class.forName("org.postgresql.Driver");
        Connection c = DriverManager.getConnection(
        url,username,pwd
        );
      }catch(Exception e){
        e.printStackTrace();
      }
    }
    public static void init(){
    
    }
    protected void add(){
        
    }
    
    protected void update(){
        
    }
    
    protected void remove(){
       
    }
}
