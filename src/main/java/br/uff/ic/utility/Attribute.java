/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package br.uff.ic.utility;

/**
 * Class for the attribute tuples that contain the attribute name and the attribute value
 * @author Kohwalter
 */
public class Attribute {

    private String name;
    private String value;
    
    /**
     * Default constructor
     * @param name is the attribute name
     * @param value is the attribute value
     */
    public Attribute(String name, String value) {
        this.name = name;
        this.value = value;
    }  
    
    /**
     * Deprecated Constructor
     * @param value 
     */
    public @Deprecated Attribute(String value) {
        this.name = "Unknown";
        this.value = value;
    }  
    
    /**
     * Deprecated Constructor
     * @deprecated
     */
    public @Deprecated Attribute() {
        this.name = "Unknown";
        this.value = "0";
    }  
    
    /**
     * Function to return the attribute name
     * @return attribute.name
     */
    public String getName()
    {
        return name;
    }
    
      /**
     * Function to return the attribute value
     * @return attribute.value
     */
    public String getValue()
    {
        return value;
    }
    
    /**
     * Method to set the attribute name
     * @param t is the new name for the attribute
     */
    public void setName(String t)
    {
        this.name = t;
    }
    
    /**
     * Method to set the attribute value
     * @param t is the new value for the attribute
     */
    public void setValue(String t)
    {
        this.value = t;
    }
    
    /**
     * Method to print the attribute in the format of Name: Value
     * @return the string with the output
     */
    public String printAttribute()
    {
        return this.getName() + ": " + this.getValue() + " <br>";
    }

    public String toNotationString() {
        return this.getName() + "= " + "\"" + this.getValue() + "\"";
    }
}
