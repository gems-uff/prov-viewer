/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.ic.utility;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Class to define a vertex-graph attribute (collapsed vertices)
 * @author Kohwalter
 */
public class GraphAttribute {

    private String name;
    private String value;
    private float minValue;
    private float maxValue;
    private int quantity;
    private Collection<String> originalValues;
  
    /**
     * Default constructor
     * @param name is the attribute name
     * @param value is the attribute value
     */
    public GraphAttribute(String name, String value) {
        this.name = name;
        this.value = value;
        this.quantity = 1;
        if (Utils.tryParseFloat(value)){
            this.minValue = Utils.convertFloat(value.trim());
            this.maxValue = Utils.convertFloat(value.trim());
            float v = ((int) (Utils.convertFloat(value.trim()) * 10000)) * 0.0001f;
            this.value = String.valueOf(v);
        }
        else {
            this.minValue = 0;
            this.maxValue = 0;
        }
        this.originalValues = new ArrayList<>();
        this.originalValues.add(value);
    }
    
    /**
     * Constructor with min and max values (used when quantity == 2)
     * @param name
     * @param value
     * @param min
     * @param max
     * @param quantity 
     */
    public GraphAttribute(String name, String value, String min, String max, String quantity) {
        this.name = name;
        this.value = value;
        this.quantity = Integer.valueOf(quantity);
        this.minValue = Utils.convertFloat(min);
        this.maxValue = Utils.convertFloat(max);
        this.originalValues = new ArrayList<>();
        this.originalValues.add(min);
        this.originalValues.add(max);

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
    public GraphAttribute(String name, String value, String min, String max, String quantity, Collection<String> values) {
        this.name = name;
        this.value = value;
        this.quantity = Integer.valueOf(quantity);
        this.minValue = Utils.convertFloat(min);
        this.maxValue = Utils.convertFloat(max);
        this.originalValues = new ArrayList<>();
        this.originalValues.addAll(values);
    }
    
    /**
     * Method to update the attribute when computing the collapsed set
     * @param value is the attribute value
     */
    public void updateAttribute(String value) {
        this.quantity++;
        if (Utils.tryParseFloat(value)) {
            this.value = Float.toString(Utils.convertFloat(this.value) + Utils.convertFloat(value));
            this.minValue = Math.min(this.minValue, Utils.convertFloat(value));
            this.maxValue = Math.max(this.maxValue, Utils.convertFloat(value));
                
        } else {
            if(!this.value.equalsIgnoreCase(value))
                this.value += ", " + value;
        }
        originalValues.add(value);
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
        if ((this.quantity > 1) && Utils.tryParseFloat(this.value))
            return Float.toString(Utils.convertFloat(this.value) / this.quantity); 
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
        return Float.toString(this.minValue);
    }

    /**
     * Method to return the maximum value for this attribute in the vertex-graph
     * @return max value
     */
    public String getMax() {
        return Float.toString(this.maxValue);
    }

    /**
     * Method to return the quantity of vertices that has this attribute in the vertex-graph
     * @return 
     */
    public String getQuantity() {
        return Integer.toString(this.quantity);
    }
    
    public Collection<String> getValues() {
        return this.originalValues;
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
        if(this.quantity == 1)
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
            if(this.quantity > 2) {
                return (Utils.convertFloat(this.value) / this.quantity)
                        + " (" + this.getMin() + " ~ "
                        + this.get1stQuartile() + " ~"
                        + this.getMedian() + " ~"
                        + this.get3rdQuartile() + " ~"
                        + this.getMax() + ")" + "<br>";
            } else if(this.quantity > 1){
                return (Utils.convertFloat(this.value) / this.quantity)
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
    public void incrementQuantity() {
        quantity++;
    }
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
        return Utils.median(originalValues.toArray(), 0, originalValues.size());
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
        return Utils.quartile(originalValues.toArray(), quartile);
    }
}
