/*
 * The MIT License
 *
 * Copyright 2017 Kohwalter.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
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
