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
package br.uff.ic.provviewer.Input;

import br.uff.ic.provviewer.VariableNames;
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
            
            epsilon = new AttributeErrorMargin(VariableNames.CollapsedVertexActivityAttribute, "", 0);
            restrictionList.put(VariableNames.CollapsedVertexActivityAttribute, epsilon);
            epsilon = new AttributeErrorMargin(VariableNames.CollapsedVertexAgentAttribute, "", 0);
            restrictionList.put(VariableNames.CollapsedVertexAgentAttribute, epsilon);
            epsilon = new AttributeErrorMargin(VariableNames.CollapsedVertexEntityAttribute, "", 0);
            restrictionList.put(VariableNames.CollapsedVertexEntityAttribute, epsilon);
            epsilon = new AttributeErrorMargin(VariableNames.similarityAttribute, "", 0);
            restrictionList.put(VariableNames.similarityAttribute, epsilon);
            epsilon = new AttributeErrorMargin(VariableNames.GraphFile, "", 0);
            restrictionList.put(VariableNames.GraphFile, epsilon);
                    
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
