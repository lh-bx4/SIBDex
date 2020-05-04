/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package misc;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Reader;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import static java.util.Arrays.stream;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.*;
import static java.util.stream.StreamSupport.stream;
/**
 *
 * @author Louis
 */
public class CsvWriter {

    private File file;
    private String separator;
    private List<String[]> db;

    public String format(String[] line) {
        return Stream.of(line).collect(Collectors.joining(this.separator));
    }
    
    public static void fill() throws IOException {
        BufferedReader table = new BufferedReader(
            new FileReader(new File("src\\assets\\data\\table.html"))
        );
        
        ArrayList<String> sprites = new ArrayList<>();
        int l = "./Liste des Pokémon par statistiques de base — Poképédia_files/".length();
        table.lines().forEach(
            (line) -> {
                if (line.contains("img")) {
                    int a = line.indexOf("./Liste des Pokémon par statistiques de base — Poképédia_files/")+l;
                    int b = line.indexOf("\"",a+1);
                    String img = line.subSequence(a, b).toString();
                    System.out.println(img);
                    sprites.add(img);
                }
            }
        );
        Files.write(Paths.get("src\\assets\\data\\table.csv"), sprites);
    }
    
    public static void main(String[] args) throws FileNotFoundException {
        try {
            CsvWriter.fill();
        } catch (IOException ex) {
            Logger.getLogger(CsvWriter.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
   
}