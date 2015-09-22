/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.ic.utility;

import br.uff.ic.utility.Utils;

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
            this.minValue = Float.parseFloat(this.value);
            this.maxValue = Float.parseFloat(this.value);
        }
        else {
            this.minValue = 0;
            this.maxValue = 0;
        }
    }

    /**
     * Method to update the attribute when computing the collapsed set
     * @param value is the attribute value
     */
    public void updateAttribute(String value) {
        this.quantity++;
        if (Utils.tryParseFloat(value)) {
            this.value = Float.toString(Float.parseFloat(this.value) + Float.parseFloat(value));
            this.minValue = Math.min(this.minValue, Float.parseFloat(value));
            this.maxValue = Math.max(this.maxValue, Float.parseFloat(value));
        } else {
            this.value += " " + value;
        }
    }

    /**
     * Method to return the attribute name
     * @return name
     */
    public String getName() {
        return name;
    }

    /**
     * method to return the attribute value
     * @return value
     */
    public String getValue() {
        return value;
    }

    public String getMin() {
        return Float.toString(minValue);
    }

    /**
     * Method to return the maximum value for this attribute in the verter-graph
     * @return max value
     */
    public String getMax() {
        return Float.toString(maxValue);
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
        if(quantity == 1)
            return this.getName() + ": " + this.getValue() + " <br>";
        else
            return this.getName() + ": " + printValue();
    }

    /**
     * Function to print the attribute parameters
     * @return a string with the attribute characteristics
     */
    public String printValue() {
        if (Utils.tryParseFloat(this.value)) {
            return (Float.parseFloat(this.value) / this.quantity)
                    + " (" + this.getMin() + " ~ "
                    + this.getMax() + ")" + "<br>";
        } else {
            return this.value + "<br>";
        }
    }
    
    public String toNotationString() {
        return this.getName() + "=" + this.getValue();
    }
}
