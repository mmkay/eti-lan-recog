/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package etirecognize.frequency;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.TreeMap;
import org.neuroph.core.NeuralNetwork;
import org.neuroph.core.learning.SupervisedTrainingElement;
import org.neuroph.core.learning.TrainingSet;
import org.neuroph.nnet.MultiLayerPerceptron;
/**
 *
 * @author Monia
 */

//rozpoznawanie języka na podstawie średniej częstotliwości występowania liter
//z użyciem sieci neuronowych z biblioteki http://neuroph.sourceforge.net/
public class MainFreq {

    static String[] langs = {"eng", "pol", "ger", "fra", "cze", "dan", "nor"};
    static String tekst = "./src/etirecognize/ngram/samples/pol-wiki.txt";
    
    static TreeMap<String, LangProfile> languages = new TreeMap<>();
    static TreeMap<Character, Integer> letterCount = new TreeMap<>();
    static double[] letterOccur = new double[26];
    
    //funkcja opracowująca średnie występowanie liter w tekście
    //już nie wyrzuca błędu przy wczytywaniu polskich tekstów
    public static void scanText() throws FileNotFoundException, IOException {
        
        BufferedReader reader = null;
        reader = new BufferedReader(new FileReader(tekst));
        
        String line = null;
        int textLen = 0;

        while ((line = reader.readLine()) != null) {
            line.toLowerCase();
            for (char znak : line.toCharArray()) {
                if (znak >='a' && znak <='z') {
                    if (letterCount.containsKey(znak)) {
                        letterCount.put(znak, letterCount.get(znak) + 1);
                    } else {
                        letterCount.put(znak, 1);
                    }
                    textLen++;
                }
            }
        }
        
        int i = 0;
        for (char znak : letterCount.keySet()) {
            double occur = (double)(letterCount.get(znak))/textLen;
            letterOccur[i] = occur;
            i++;
        }        
    }
    
    public static void main(String[] args) throws FileNotFoundException, IOException {
        
        scanText();
        for(String lang : langs) {
            languages.put(lang, new LangProfile(lang));
        }
        
        NeuralNetwork neuralNetwork = new MultiLayerPerceptron(26,13,langs.length);
        TrainingSet<SupervisedTrainingElement> trainingSet = new TrainingSet<>(26,langs.length);
        
        //właściwe uczenie sieci
        for(String lang : langs) {
            LangProfile lp = languages.get(lang);            
            trainingSet.addElement(new SupervisedTrainingElement(lp.letterFreq, lp.code));
        }
        neuralNetwork.learn(trainingSet);
        neuralNetwork.setInput(letterOccur);
        neuralNetwork.calculate();
        
        double[] results = neuralNetwork.getOutput();
        double currentMax = results[0];
        int indexMax = 0;
        
        for(int i=0; i<langs.length; i++) {
            System.out.println(results[i]);
            if(results[i] > currentMax)
                indexMax = i;
        }
        System.out.println("Wykryto język: "+langs[indexMax]);
    }
}
