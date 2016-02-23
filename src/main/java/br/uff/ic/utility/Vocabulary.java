/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.ic.utility;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Kohwalter
 */
public class Vocabulary {
    private Map<String, String> vocabulary; 
    
    /**
     * Empty Constructor
     */
    public Vocabulary() {
        vocabulary = new HashMap<>();
    }
    
    public Vocabulary(String values) {
        vocabulary = new HashMap<>();
        addVocabulary(values);
    }
    
    /**
     * Insert synonymous in the vocabulary
     * The list of words must be separated by a comma in the following format:
     * "Word1,Word2,Word3"
     * @param values is the list of synonymous words
     */
    public void addVocabulary(String values) {
        String[] words = values.toLowerCase().split(",");
        for(String word : words) {
            if(!vocabulary.containsKey(word))
                vocabulary.put(word, values.toLowerCase().replace(",", " ") + " ");
            else {
                // Need to update the synonymous words for the current word
                String synonymous = vocabulary.get(word);
                synonymous = synonymous + " " + values.toLowerCase().replace(",", " ") + " ";
                vocabulary.put(word, synonymous);
            }
        }
    }
    
    public Map<String, String> getVocabulary() {
        return vocabulary;
    }
}
