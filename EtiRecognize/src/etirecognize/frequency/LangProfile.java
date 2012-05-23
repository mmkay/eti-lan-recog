/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package etirecognize.frequency;

import java.io.*;
import java.util.TreeMap;

/**
 *
 * @author Monia
 */
public class LangProfile {
    
    private String name;
    public double[] letterFreq;
    public double[] code;
    
    LangProfile(String name) throws FileNotFoundException, IOException {
        this.name = name;
        letterFreq = new double[26];
        code = new double[MainFreq.langs.length];
        addData();
    }
    
    //wczytanie z pliku częstotliwości występowania liter w języku
    public void addData() throws FileNotFoundException, IOException {
        
        BufferedReader reader = null;
        reader = new BufferedReader(new FileReader("./src/etirecognize/frequency/lang/"+name+".txt"));
        String line = null;
        
        for(int i=0; i<26; i++) {
            line = reader.readLine();
            double wartosc = Double.parseDouble(line);
            letterFreq[i] = wartosc;
        }
        
        for(int i=0; i<4; i++) {
            line = reader.readLine();
            double wartosc = Double.parseDouble(line);
            code[i] = wartosc;
        }
    }
}
