package etirecognize.ngram;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Vector;

/**
 * MainComparator - class that compares language profiles with given samples
 * @author mat
 */
public class MainComparator {

    static String[] langs = {"eng", "pol"};
    static String[] checked = {"eng-nasa", "eng-hero", "pol-wiki",
        "eng-oneword", "eng-wiki"};
    
    public static void main(String[] args) throws FileNotFoundException, IOException {
        /*
         * Use example:
        LanguageProfile prof1 = new LanguageProfile("eng");
        try {
            prof1.addData(new File("./src/etirecognize/ngram/samples/eng.txt"));
        } catch (FileNotFoundException ex) {
            Logger.getLogger(MainComparator.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(MainComparator.class.getName()).log(Level.SEVERE, null, ex);
        }
        */
        Vector<LanguageProfile> languages = new Vector<LanguageProfile>();
        Vector<LanguageProfile> tocheck = new Vector<LanguageProfile>();
        
        //create profiles of languages and samples to check
        for (String lang : langs) {
            LanguageProfile prof = new LanguageProfile(lang);
            prof.addData(new File("./src/etirecognize/ngram/samples/"+lang+".txt"));
            languages.add(prof);
        }
        for (String lang : checked) {
            LanguageProfile prof = new LanguageProfile(lang);
            prof.addData(new File("./src/etirecognize/ngram/samples/"+lang+".txt"));
            tocheck.add(prof);
        }
        
        //sample check
        for (int i = 0; i < tocheck.size(); i++) {
            System.out.println("Checking sample "+tocheck.get(i).getName());
            int dist = 100000;
            String langname = "";
            for (int j = 0; j < languages.size(); j++) {
                int temp = calculateDistance(languages.get(j), tocheck.get(i));
                if (temp < dist) {
                    dist = temp;
                    langname = languages.get(j).getName();
                }
                System.out.println("Language "+languages.get(j).getName()+": "+temp);
            }
            System.out.println("Detected language: "+langname+", distance: "+dist);
        }
    }
    
    /* Method that calculates the distance between a language "lang" and
     * a sample we are trying to identify the language of.
     */
    public static int calculateDistance(LanguageProfile lang, LanguageProfile sample) {
        int result = 0;
        
        for (String key : sample.order) {
            if (lang.order.contains(key)) {
                result += java.lang.Math.abs(lang.order.indexOf(key) - sample.order.indexOf(key));
            }
            else {
                result += lang.order.size();
            }
        }
        
        return result;
    }
}
