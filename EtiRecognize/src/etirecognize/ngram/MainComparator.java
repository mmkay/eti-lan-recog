package etirecognize.ngram;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.Vector;

/**
 * MainComparator - class that compares language profiles with given samples
 *
 * @author mat
 */
public class MainComparator {

    static String[] langs = {"eng", "pol", "ger", "rus", "fra", "cze", "hrv",
        "danish", "norwegian"};
    static String[] checked = {"dan4", "dan5", "nor4", "nor5"};

    public static void main(String[] args) throws FileNotFoundException,
            IOException {

        Vector<LanguageProfile> languages = new Vector<LanguageProfile>();
        Vector<LanguageProfile> tocheck = new Vector<LanguageProfile>();

        //create profiles of languages and samples to check
        for (String lang : langs) {
            LanguageProfile prof = new LanguageProfile(lang);
            prof.addData(new File("./src/etirecognize/ngram/languages/" + lang
                    + ".txt"));
            languages.add(prof);
        }
        for (String lang : checked) {
            LanguageProfile prof = new LanguageProfile(lang);
            prof.addData(new File("./src/etirecognize/ngram/samples/" + lang
                    + ".txt"));
            tocheck.add(prof);
        }

        //sample check
        for (int i = 0; i < tocheck.size(); i++) {
            System.out.println("Checking sample " + tocheck.get(i).getName());
            int dist = 100000;
            int dist2 = 100000;
            String langname = "";
            String lang2 = "";
            LanguageProfile first = null;
            LanguageProfile second = null;
            for (int j = 0; j < languages.size(); j++) {
                int temp = calculateDistance(languages.get(j), tocheck.get(i));
                if (temp < dist) {
                    dist2 = dist;
                    dist = temp;
                    lang2 = langname;
                    langname = languages.get(j).getName();
                    second = first;
                    first = languages.get(j);
                } else {
                    if (temp < dist2) {
                        dist2 = temp;
                        lang2 = languages.get(j).getName();
                        second = languages.get(j);
                    }
                }
                double percent = (90000 - temp) * 100.0 / 90000.0;
                DecimalFormat df = new DecimalFormat("###.##");
                //System.out.println("Language " + languages.get(j).getName() + ": "
                //        + temp + ", " + df.format(percent) + "%");
            }
            System.out.println("Detected language: " + langname + ", distance: "
                    + dist);
            System.out.println("Second language: " + lang2 + ", distance: " + dist2);

            /*
             * Now, we try to obtain language-specific characteristics for two
             * closest matches to distinguish right language.
             */

            long closer = calculateMinorDifferences(tocheck.get(i), first,
                    second);
            if (closer < 0) {
                System.out.println("Specific N-grams: " + langname + ", result ="
                        + closer);
            } else if (closer > 0) {
                System.out.println("Specific N-grams: " + lang2 + ", result ="
                        + closer);
            }
        }
    }

    /*
     * Method that calculates the distance between a language "lang" and a
     * sample we are trying to identify the language of.
     */
    public static int calculateDistance(LanguageProfile lang,
            LanguageProfile sample) {
        int result = 0;

        for (String key : sample.order) {
            if (lang.order.contains(key)) {
                result += java.lang.Math.abs(lang.order.indexOf(key)
                        - sample.order.indexOf(key));
            } else {
                result += lang.order.size();
            }
        }

        return result;
    }

    /*
     * Calculate minor differences between profiles. get - profile we're looking
     * for the differences first, second - similar language profiles method
     * compares 'get' profile with first and second and returns negative value
     * if first profile is closer, and positive if second is closer.
     */
    private static long calculateMinorDifferences(LanguageProfile get,
            LanguageProfile first, LanguageProfile second) {
        long distance = 0;
        int pos1, pos2;
        for (String key : get.order) {
            if (first.order.contains(key)) {
                pos1 = first.order.size() - first.order.indexOf(key);
            } else {
                pos1 = 0;
            }
            if (second.order.contains(key)) {
                pos2 = second.order.size() - second.order.indexOf(key);
            } else {
                pos2 = 0;
            }
            distance += (get.order.size() - get.order.indexOf(key))*(pos2 - pos1);
        }
        return distance;
    }
}
