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
    
    static TreeMap<String, double[]> codes = new TreeMap<>();
    static String[] langs = {"eng", "pol", "ger", "fra"};
    static String tekst = "./src/etirecognize/ngram/samples/fra-test.txt";
    
    //te mapy w sumie powodują zbędne przepisywanie danych w tę i nazad
    //później to wszystko przebuduję
    static TreeMap<String, LangProfile> languages = new TreeMap<>();
    static TreeMap<Character, Integer> letterCount = new TreeMap<>();
    static TreeMap<String, Double> letterOccur = new TreeMap<>();
    static double[] letOc = new double[26];
    
    //funkcja opracowująca średnie występowanie liter w tekście
    //czasem się wywala, głównie na polskich tekstach
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
        
        for (char znak : letterCount.keySet()) {
            double occur = (double)(letterCount.get(znak))/textLen;
            letterOccur.put(Character.toString(znak), occur);
        }
        
        for(int i=0; i<26; i++)
            letOc[i] = (double)letterOccur.values().toArray()[i];
        
    }
    
    public static void main(String[] args) throws FileNotFoundException, IOException {
        
        scanText();
        for(String lang : langs) {
            languages.put(lang, new LangProfile(lang));
        }
        
        //rozwiązanie doraźne - neurony wymagają na wyjściu tablicy doubli
        double[]  kod1 = {0,0,0,1};
        codes.put("eng", kod1);
        double[] kod2 = {0,0,1,0};
        codes.put("pol", kod2);
        double[] kod3 = {0,1,0,0};
        codes.put("ger",kod3);
        double[] kod4 = {1,0,0,0};
        codes.put("fra",kod4);
        
        NeuralNetwork neuralNetwork = new MultiLayerPerceptron(26,13,4);
        TrainingSet<SupervisedTrainingElement> trainingSet = new TrainingSet<>(26,4);
        
        //właściwe uczenie sieci
        //na razie dostaje tylko po jednym zestawie na język, ale o dziwo to wystarcza
        //(przynajmniej dla Twoich tekstów testowych)
        for(String lang : langs) {
            LangProfile lp = languages.get(lang);
            double[] values = new double[26];
            for(int i=0; i<26; i++)
                values[i] = (double)lp.letterFreq.values().toArray()[i];
            
            trainingSet.addElement(new SupervisedTrainingElement(values, codes.get(lang)));
        }
        neuralNetwork.learn(trainingSet);
        neuralNetwork.setInput(letOc);
        neuralNetwork.calculate();
        for(int i=0; i<4; i++)
            System.out.println(neuralNetwork.getOutput()[i]);
    }
}
