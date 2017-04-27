/*
 * Copyright (C) 2017 Kohwalter
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */
package br.uff.ic.provviewer.Input;

import br.uff.ic.utility.AttributeErrorMargin;
import br.uff.ic.utility.Vocabulary;
import java.io.File;
import java.util.HashMap;
import java.util.Map;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 *
 * @author Kohwalter
 */
public class SimilarityConfig {
    
    private Map<String, AttributeErrorMargin> restrictionList; // List of errors for each specified attribute, including their weights. Otherwise the default weight is 1
    private String defaultError; // Default value error if not specified anywhere
    private float defaultWeight; // Default weight if not specified anywhere
    private float similarityThreshold; // Used to define if two vertices are similar. The similarity result will need to be highter than the similarityThreshold
    private AttributeErrorMargin epsilon; // Auxiliary variable used in this class
    private Vocabulary vocabulary; // The vocabulary that contains string synonyms
    
    public Map<String, AttributeErrorMargin> getRestrictionList() {
        return restrictionList;
    }
    
    public float getSimilarityThreshold() {
        return similarityThreshold;
    }
    
    public String getDefaultError() {
        return defaultError;
    }
    
    public float getDefaultWeight() {
        return defaultWeight;
    }
    
    public Map<String, String> getVocabulary() {
        return vocabulary.getVocabulary();
    }
    
    public Vocabulary returnVocabulary() {
        return vocabulary;
    }
    
    public void readFile(File fXmlFile) {
        try {
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(fXmlFile);
            doc.getDocumentElement().normalize();
            NodeList nList;
            
            restrictionList = new HashMap<>();
            vocabulary = new Vocabulary();
            defaultError = "0";
            
            nList = doc.getElementsByTagName("default_error");
            defaultError = nList.item(0).getTextContent();
            nList = doc.getElementsByTagName("default_weight");
            defaultWeight = Float.parseFloat(nList.item(0).getTextContent());
            nList = doc.getElementsByTagName("similarityThreshold");
            similarityThreshold = Float.parseFloat(nList.item(0).getTextContent());
            
            nList = doc.getElementsByTagName("attributeErrorList");
            for (int temp = 0; temp < nList.getLength(); temp++) {
                Node nNode = nList.item(temp);
                if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                    String name;
                    String value;
                    float weight;
                    Element eElement = (Element) nNode;
                    name = eElement.getElementsByTagName("name").item(0).getTextContent();
                    value = eElement.getElementsByTagName("value").item(0).getTextContent();
                    weight = Float.parseFloat(eElement.getElementsByTagName("weight").item(0).getTextContent());
                    epsilon = new AttributeErrorMargin(name, value, weight);
                    restrictionList.put(name, epsilon);
                    
                    // Populate the Vocabulary
                    // Detection: Check if there is a comma since it separates the words
                    if(value.contains(",")) {
                        // So we have a list of strings for the vocabulary
                        vocabulary.addVocabulary(value);
                    }
                }
            }
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
}
