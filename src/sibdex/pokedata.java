/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sibdex;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import misc.read2;
import misc.util;
import static misc.util.*;
import static sibdex.ctrl.add;

/**
 *
 * @author Louis
 */
public abstract class pokedata {
    public static final int INSERT = 1, UPDATE = 2, DELETE = 3, PLAYER = 4;
            
    private final int type;
    
    protected pokedata(int type) {
        this.type=type;
    }
    
    public String send(String[] s) {
        return util.c(s, " ")+";";
    }
    
    public void exec(Connection c, String q) {
        try {
            Statement stmt = c.createStatement();
            stmt.executeUpdate(q);
        } catch (SQLException ex) {
            Logger.getLogger(pokedata.class.getName()).log(Level.INFO, null, ex);
        }
    }
    
    /**
     * Insert to database
     */
    public static class pdI extends pokedata {
        private final String tName;
        private final ArrayList<String> tCols;
        private final ArrayList<ArrayList<String>> tVals;
        
        public pdI(String tn, ArrayList<String> tc, ArrayList<ArrayList<String>> tv) {
            super(INSERT);
            this.tName=tn;
            this.tCols=tc;
            this.tVals=tv;
        }
        public pdI(Connection c) throws SQLException {
            super(INSERT);
            String tmp;
            ArrayList<String> tc;                                               // columns
            ArrayList<String> tvr;                                              // table value rows
            ArrayList<ArrayList<String>> tv;                                    // table values
            ResultSetMetaData MD;
            ResultSet Rs;
            ArrayList<Boolean> b;
            String tn ="";
            System.out.println("Choose the table name where to insert data :");
            Rs = c.createStatement()
                .executeQuery("SELECT table_name FROM information_schema.tables WHERE table_schema='chen'");
            ArrayList<String> tns = new ArrayList<String>();
            while(Rs.next()) {
                tns.add(Rs.getString("table_name"));
            }
            b = read2.ArrowSelector(tns, true, false);
            for(int k=0;k<tns.size();k++) if (b.get(k)) tn=tns.get(k);
            if (tn.equals("")) throw new UnsupportedOperationException("You must provide a table name in order to insert.");

            MD = c.createStatement()
                .executeQuery("SELECT * FROM "+tn).getMetaData();
                        
            int n=MD.getColumnCount();
            ArrayList<String> 
                cols = new ArrayList<String>(), 
                colstype=new ArrayList<String>()
            ;
            for (int k=1;k<=n;k++) {
                cols.add(MD.getColumnName(k));
                //colstype.add(MD.getColumnTypeName(k));
            }
            
            // taking choosed columns
            System.out.println("Give column names to insert. Empty . to stop.");
            tc = new ArrayList<String>();
            b = read2.ArrowSelector(cols,false,true);
            for(int k=0;k<cols.size();k++) {
                if (b.get(k)) {
                    tc.add(cols.get(k));
                    colstype.add(MD.getColumnTypeName(k+1));
                }
            }
            
            System.out.println("Give values. Empty string to stop, + to continue.");
            tv = new ArrayList<ArrayList<String>>();
            // add limit on the column name
            int i=0;
            do {
                System.out.println(++i);
                tvr=new ArrayList<String>();
                for(int k=0;k<tc.size();k++) {
                    System.out.print(colstype.get(k)+":"+tc.get(k)+":");
                    tvr.add(util.f(read2.S(), colstype.get(k)));
                }
                System.out.print(util.d(tvr, ",", BRACKETS)+",?");
                tv.add(tvr);
            } while (!"".equals(read2.S()));
            tName=tn;
            tCols=tc;
            tVals=tv;
        }
        
        public String send() throws UnsupportedOperationException {
            // TODO see if all lines have the right format
            boolean t1;
            if (t1=tCols.size()!=0) // check if there are more than 0 column selected
            {
                if (tCols.size()!=tVals.get(0).size()) 
                    throw new UnsupportedOperationException("Different values length while inserting.");
            } else {
                //throw new UnsupportedOperationException("Empty tCols is deprecated.");
            }
            
            ArrayList<String> t = new ArrayList<>();
            for(ArrayList<String> r:tVals)
                t.add(util.d(r,",", BRACKETS));
            
            return super.send(new String[] 
                {
                    "INSERT",
                    "INTO",
                    tName,
                    t1?util.e(util.c(tCols,","), BRACKETS):"",
                    "VALUES",
                    util.c(t,",")
                }
            );
        }   
    }
    
     /**
     * Update database with the id of the object to update
     */
    public static final class pdU extends pokedata {

        private final String tName;
        private final ArrayList<String> tCols;
        private final ArrayList<ArrayList<String>> tVals;
        private final int id;

        public pdU(Connection con) throws SQLException {
            super(UPDATE);
            ArrayList<String> tc;                                               // columns
            ArrayList<String> tvr;                                              // table value rows
            ArrayList<ArrayList<String>> tv;                                    // table values
            ResultSetMetaData MD;
            ResultSet Rs;
            ArrayList<Boolean> b;
            String tn = "";
            System.out.println("Choose the table name where to edit data :");
            Rs = con.createStatement()
                    .executeQuery("SELECT table_name FROM information_schema.tables WHERE table_schema='chen'");
            ArrayList<String> tns = new ArrayList<String>();
            while (Rs.next()) {
                tns.add(Rs.getString("table_name"));
            }
            b = read2.ArrowSelector(tns, true, false);
            for (int k = 0; k < tns.size(); k++) {
                if (b.get(k)) {
                    tn = tns.get(k);
                }
            }
            if (tn.equals("")) {
                throw new UnsupportedOperationException("You must provide a table name in order to edit.");
            }

            MD = con.createStatement()
                    .executeQuery("SELECT * FROM " + tn).getMetaData();

            int n = MD.getColumnCount();
            ArrayList<String> cols = new ArrayList<String>(),
                    colstype = new ArrayList<String>();
            for (int k = 1; k <= n; k++) {
                cols.add(MD.getColumnName(k));
            }
            System.out.println("Choose the id of the object you want to edit");
            this.id = read2.pI();
            // taking choosed columns
            System.out.println("Give column names to edit. Empty . to stop.");
            tc = new ArrayList<String>();
            b = read2.ArrowSelector(cols, false, true);
            for (int k = 0; k < cols.size(); k++) {
                if (b.get(k)) {
                    tc.add(cols.get(k));
                    colstype.add(MD.getColumnTypeName(k + 1));
                }
            }
            System.out.println("Give values. Empty string to stop, + to continue.");
            tv = new ArrayList<ArrayList<String>>();
            // add limit on the column name
            int i = 0;
            do {
                System.out.println(++i);
                tvr = new ArrayList<String>();
                for (int k = 0; k < tc.size(); k++) {
                    System.out.print(colstype.get(k) + ":" + tc.get(k) + ":");
                    tvr.add(util.f(read2.S(), colstype.get(k)));
                }
                System.out.print(util.d(tvr, ",", BRACKETS) + ",?");
                tv.add(tvr);
            } while (!"".equals(read2.S()));
            this.tName = tn;
            this.tCols = tc;
            this.tVals = tv;
        }

        public String send() throws UnsupportedOperationException {
            // TODO see if all lines have the right format
            boolean t1;
            if (t1 = tCols.size() != 0) // check if there are more than 0 column selected
            {
                if (tCols.size() != tVals.get(0).size()) {
                    throw new UnsupportedOperationException("Different values length while inserting.");
                }
            ArrayList<String> t = new ArrayList<>();
            for (ArrayList<String> r : tVals) {
                t.add(util.d(r, ",", BRACKETS));
            }
            return(super.send(new String[]{
                "UPDATE",
                tName,
                "SET",
                t1 ? util.e(util.c(tCols, ","), BRACKETS) : "",
                "=",
                util.c(t, ","),
                "WHERE id = ",
                Integer.toString(id)
            }
            ));
        }   
    }

    /**
     * Delete from database
     */
    public static final class pdD extends pokedata {    
        
        public pdD(int type) {
            super(DELETE);
        }
        
    }
    
    /**
     * Add an access to database
     */
    public static final class pdP extends pokedata {    
        
        public pdP(int type) {
            super(PLAYER);
        }
        
    }
}
