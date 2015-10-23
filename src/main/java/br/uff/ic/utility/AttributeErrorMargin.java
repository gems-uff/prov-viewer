/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.ic.utility;

/**
 * Class used for creating a list of acceptable error margins for specific attributes
 * Allows to define the weight for the attribute to be used during similarity evaluation
 * @author Kohwalter
 */
public class AttributeErrorMargin {
    private String name;
    private String value;
    private float weight;

    public AttributeErrorMargin(String n, String v) {
        this.name = n;
        this.value = v;
        this.weight = 1;
    }
    
    public AttributeErrorMargin(String n, String v, float w) {
        this.name = n;
        this.value = v;
        this.weight = w;
    }
    
    /**
     * Method to return the attribute name
     * @return name
     */
    public String getName() {
        return this.name;
    }
    
    /**
     * Method to return the attribute value
     * @return name
     */
    public String getValue() {
        return this.value;
    }
    
    /**
     * Method to return the attribute weight
     * @return name
     */
    public float getWeight() {
        return this.weight;
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
     * Method to set the attribute weight
     * @param t is the new weight
     */
    public void setWeight(float t) {
        this.weight = t;
    }
}
