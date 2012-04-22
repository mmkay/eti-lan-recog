package etirecognize.ngram;

import java.io.*;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Set;
import java.util.TreeSet;

/**
 * LanguageProfile
 * 
 * Class to contain a profile of language or a document, basing on ngrams
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
    private HashMap<String, Integer>[] ngram;
    private TreeSet<NGramEntity>[] sortedNGrams;
    public LinkedList<String>[] order;

    public LanguageProfile(String name) {
        this.name = name;
        ngram = new HashMap[5];
        sortedNGrams = new TreeSet[5];
        order = new LinkedList[5];
        for (int i = 0; i < 5; i++) {
            ngram[i] = new HashMap<>();
            sortedNGrams[i] = new TreeSet<>();
            order[i] = new LinkedList<>();
        }
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
            text = text.replaceAll("(\\W)", " ");
            text = text.replaceAll("(\\d)", "");
            //System.out.println(text);
            contents.append(text);
        }
        
        //divide buffer into words
        String buffer = contents.toString();
        String[] words = buffer.split("\\s");
        this.splitIntoNGrams(words);
    }

    private void splitIntoNGrams(String[] words) {
        for (String word : words) {
            for (int i = 1; i <= 5; i++) {
                // i-gram
                for (int j = 0; j <= word.length() - i; j++) {
                    String key = word.substring(j, j+i);
                    if (ngram[i-1].containsKey(key)) {
                        ngram[i-1].put(key, ngram[i-1].get(key) + 1);
                    }
                    else {
                        ngram[i-1].put(key, 1);
                    }
                }
            }
        }
        //at this point, we've got a HashMap of keys and their values
        
        for (int i = 0; i < 5; i++) {
            Set<String> keys;
            keys = ngram[i].keySet();
            for (String key : keys) {
                NGramEntity entity = new NGramEntity(key);
                entity.setOccurences(ngram[i].get(key));
                boolean check = sortedNGrams[i].add(entity);
            }
        }
        
        
        //NGramEntities are now sorted into the TreeSets, so we've got a profile
        //it might be useful to simply put names into the LinkedList
        
        for (int i = 0; i < 5; i++) {
            NGramEntity element = sortedNGrams[i].last();
            do {
                String name = element.getSequence();
                order[i].add(name);
            } while ((element = sortedNGrams[i].lower(element)) != null );
        }
    }
    
}
