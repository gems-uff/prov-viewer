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

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Class to define a vertex-graph attribute (collapsed vertices)
 * @author Kohwalter
 */
public class GraphAttribute {

    private String name;
    private String value; // Need to change to only use this variable for speedup purposes during queries.
    private double minValue;
    private double maxValue;
//    private int quantity;
    private Map<String, String> originalValues;  // Need to change to a Map<String, String> to represent (OriginGraph, Value)
  
    /**
     * Default constructor
     * @param name is the attribute name
     * @param value is the attribute value
     * @param origin is the name of the graph that has this attribute
     */
    public GraphAttribute(String name, String value, String origin) {
        this.name = name;
        this.value = value;
//        this.quantity = 1;
        if (Utils.tryParseFloat(value)){
            this.minValue = Utils.convertDouble(value.trim());
            this.maxValue = Utils.convertDouble(value.trim());
            double v = Utils.convertDouble(value.trim());
//            double v = ((int) (Utils.convertDouble(value.trim()) * 10000)) * 0.0001f;
//            this.value = String.valueOf(v);
        }
        else {
            this.minValue = 0;
            this.maxValue = 0;
        }
        this.originalValues = new HashMap<>();
        this.originalValues.put(origin, value);
    }
    
    public GraphAttribute(String name, Map<String, String> values) {
        this.name = name;
        this.originalValues = new HashMap<>();
        this.originalValues.putAll(values);
        
        updateAttribute(values);
    }
    
    /** 
     * Constructor with all variables (used when quantity >=3)
     * @param name
     * @param value
     * @param min
     * @param max
     * @param quantity
     * @param values 
     */
    public GraphAttribute(String name, String value, String min, String max, Map<String, String> values) {
        this.name = name;
        this.value = value;
//        this.quantity = Integer.valueOf(quantity);
        this.minValue = Utils.convertFloat(min);
        this.maxValue = Utils.convertFloat(max);
        this.originalValues = new HashMap<>();
        this.originalValues.putAll(values);
    }
    
    /**
     * Method to update the attribute when computing the collapsed set
     * @param values is the Map that contains all original values
     */
    public void updateAttribute(Map<String, String> values) {
        this.minValue = Float.POSITIVE_INFINITY;
        this.maxValue = Float.NEGATIVE_INFINITY;
        String testFirstValue = (String) values.values().toArray()[0];
        if (Utils.tryParseFloat(testFirstValue) && Utils.tryParseFloat(testFirstValue)) {
            originalValues.putAll(values);
            float v = 0;
            for(String s : originalValues.values()) {
                v += Utils.convertFloat(s);
                this.minValue = Math.min(this.minValue, Utils.convertFloat(s));
                this.maxValue = Math.max(this.maxValue, Utils.convertFloat(s));
            }
            this.value = Float.toString(v);
            
        } else { // This value is a String
            originalValues.putAll(values);
            this.value = "";
            for(String s : originalValues.values()) {
                if(!this.value.contains(s))
                    this.value += ", " + s;
            }
            this.value = this.value.replaceFirst(", ", "");
        }
//        this.quantity = originalValues.size();
    }
    
    /**
     * Method to return the attribute name
     * @return name
     */
    public String getName() {
        return this.name;
    }

    /**
     * method to return the attribute value
     * @return value
     */
    public String getAverageValue() {
        // Return the average number
        if ((this.originalValues.size() > 1) && Utils.tryParseFloat(this.value))
            return Double.toString(Utils.convertDouble(this.value) / this.originalValues.size()); 
        else
            return this.value;
    }
    
    public String getValue() {
        return this.value;
    }

    /**
     * Method to return the minimum value for this attribute in the vertex-graph
     * @return 
     */
    public String getMin() {
        return Double.toString(this.minValue);
    }

    /**
     * Method to return the maximum value for this attribute in the vertex-graph
     * @return max value
     */
    public String getMax() {
        return Double.toString(this.maxValue);
    }

    /**
     * Method to return the quantity of vertices that has this attribute in the vertex-graph
     * @return 
     */
    public String getQuantity() {
        return Integer.toString(this.originalValues.size());
    }
    
    public Map<String, String> getOriginalValues() {
        return this.originalValues;
    }
    public Collection<String> getValues() {
        // Need to refactor to return the Map
        return this.originalValues.values();
    }
    /**
     * Method to set the attribute name
     * @param t is the new attribute name
     */
    public void setName(String t) {
        this.name = t;
    }

    /**
     * Method to set the attribute value
     * @param t is the new value
     */
    public void setValue(String t) {
        this.value = t;
    }

    /**
     * Function to print the attribute
     * @return string with the attribute
     */
    public String printAttribute() {
        if(this.originalValues.size() == 1)
            return this.getName() + ": " + this.getAverageValue() + " <br>";
        else
            return this.getName() + ": " + printValue();
    }

    /**
     * Function to print the attribute parameters
     * @return a string with the attribute characteristics
     */
    public String printValue() {
        if (Utils.tryParseFloat(this.value)) {
            if(this.originalValues.size() > 2) {
                return (Utils.convertFloat(this.value) / this.originalValues.size())
                        + " (" + this.getMin() + " ~ "
                        + this.get1stQuartile() + " ~"
                        + this.getMedian() + " ~"
                        + this.get3rdQuartile() + " ~"
                        + this.getMax() + ")" + "<br>";
            } else if(this.originalValues.size() > 1){
                return (Utils.convertFloat(this.value) / this.originalValues.size())
                        + " (" + this.getMin() + " ~ "
                        + this.getMax() + ")" + "<br>";
            }
            else
                return this.value + "<br>";
        }
        else {
            return this.value + "<br>";
        }
    }
    
    public String toNotationString() {
        return this.getName() + "=" + this.getAverageValue();
    }
    
     // This method is only used for tests cases
//    public void incrementQuantity() {
//        quantity++;
//    }
     // This method is only used for tests cases
    public void setMax(float t) {
        this.maxValue = t;
    }
     // This method is only used for tests cases
    public void setMin(float t) {
        this.minValue = t;
    }
    
    /**
     * Method to retrieve the median
     * @return 
     */
    public String getMedian() {
        return Utils.median(originalValues.values().toArray(), 0, originalValues.size());
    }
    
    /**
     * Method to return the 1st quartile
     * @return 
     */
    public String get1stQuartile() {
        return getQuartile(1);
    }
    
    /**
     * Method to return the 3rd quartile
     * @return 
     */
    public String get3rdQuartile() {
        return getQuartile(3);
    }
    
    /**
     * Method to retrieve the quartile value from an array
     * @param quartile The quartile desires (25 for 1st and 75 for 3rd)
     * @return 
     */
    public String getQuartile(int quartile) {
        return Utils.quartile(originalValues.values().toArray(), quartile);
    }
    
    /**
     * Method created to rename the "origin" key from the originalValues Map to
     * be "Graph Origin + vertex ID".
     * @param ID 
     */
    public void updateOriginalValuesWithID(String ID){
        Map<String, String> temporaryValues; 
        temporaryValues = new HashMap<>();
        for(String key : originalValues.keySet())
            temporaryValues.put(key + "_VERTEXID_" + ID, originalValues.get(key));
        originalValues.clear();
        originalValues.putAll(temporaryValues);
    }
}
