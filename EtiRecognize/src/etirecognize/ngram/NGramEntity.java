/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package etirecognize.ngram;

import java.util.Objects;

/**
 * NGramEntity - an entity for n-gram 
 * @author mat
 */
public class NGramEntity implements Comparable { 
    
    //sequence of chars in an n-gram
    private String sequence;
    private int occurences;

    public NGramEntity(String sequence) {
        this.sequence = sequence;
        this.occurences = 1;
    }

    public String getSequence() {
        return sequence;
    }

    public void setSequence(String sequence) {
        this.sequence = sequence;
    }
    

    public int getOccurences() {
        return occurences;
    }

    public void setOccurences(int occurences) {
        this.occurences = occurences;
    }
    
    public void incrementOccurences() {
        this.occurences++;
    }
    
    public int getN() {
        return this.sequence.length();
    }

    @Override
    public int compareTo(Object t) {
        NGramEntity compared = (NGramEntity)t;
        if (occurences > compared.getOccurences()) {
            return 1;
        }
        else if (occurences == compared.getOccurences()) {
            return 0;
        }
        else {
            return -1;
        }
    }
    
    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final NGramEntity other = (NGramEntity) obj;
        if (!Objects.equals(this.sequence, other.sequence)) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 47 * hash + Objects.hashCode(this.sequence);
        return hash;
    }
}
