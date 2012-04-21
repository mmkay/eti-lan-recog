package etirecognize.ngram;

import java.io.*;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.TreeSet;

/**
 * LanguageProfile
 * 
 * Class to contain a profile of language, basing on ngrams
 * it contains the frequency of all 1- to 5-grams basing on
 * a specific data set
 * 
 * Based on
 * http://citeseerx.ist.psu.edu/viewdoc/summary?doi=10.1.1.53.9367
 * 
 * @author Mateusz Kulewicz
 */
public class LanguageProfile {
    //language name
    private final String name;
    //array of 1 to 5-grams - not sure which collection I should use to get
    //a good comparison
    private TreeSet<NGramEntity>[] ngram;

    public LanguageProfile(String name) {
        this.name = name;
        ngram[0] = new TreeSet<>();
        ngram[1] = new TreeSet<>();
        ngram[2] = new TreeSet<>();
        ngram[3] = new TreeSet<>();
        ngram[4] = new TreeSet<>();
    }
    
    /*
     * add data from file
     */
    public void addData(File file) throws FileNotFoundException, IOException {
        StringBuffer contents = new StringBuffer();
        BufferedReader reader = null;

        reader = new BufferedReader(new FileReader(file));
        String text = null;

        // repeat until all lines is read
        while ((text = reader.readLine()) != null) {
            text = text.toLowerCase();
            contents.append(text);
        }
        
        //divide buffer into words
        String[] words = contents.toString().split("\\s");
        this.splitIntoNGrams(words);
    }

    private void splitIntoNGrams(String[] words) {
        //not implemented yet
    }
    
}
