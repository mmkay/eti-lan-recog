package etirecognize.ngram;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * MainComparator - class that compares language profiles with given samples
 * @author mat
 */
public class MainComparator {
    
    //TODO: comparation of profiles
    
    public static void main(String[] args) {
        LanguageProfile prof1 = new LanguageProfile("eng");
        try {
            prof1.addData(new File("./src/etirecognize/ngram/samples/eng.txt"));
        } catch (FileNotFoundException ex) {
            Logger.getLogger(MainComparator.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(MainComparator.class.getName()).log(Level.SEVERE, null, ex);
        }
        for (int i = 0; i < 5; i++) {
            System.out.println((i+1)+"-grams ("+prof1.order[i].size()+"):");
        }
    }
}
