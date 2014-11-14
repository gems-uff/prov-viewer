/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.ic.provviewer;

import java.util.Collection;
import java.util.Map;

/**
 *
 * @author Kohwalter
 */
public class GraphObject extends Object{
    
    public Map<String, Attribute> attributes;
    
    /**
     * Get method to return one specific attribute
     * @param attribute Desired attribute name
     * @return the attribute, containing name and value
     */
    public Attribute getAttribute(String attribute)
    {
        return attributes.get(attribute);
    }
    
    /**
     * Get method for all attributes
     * @return vertex attributes as collection
     */
    public Collection<Attribute> getAttributes()
    {
        return attributes.values();
    }
    
    /**
     * Method that generates a String with all attributes names and values
     * @return String containing attribute names and values
     */
    public String PrintAttributes()
    {
        String attributeList = "";
        if(!this.attributes.values().isEmpty())
        {
            for(Attribute att : this.attributes.values())
            {
                attributeList += att.printAttribute();
            }
        }
        return attributeList;
    }
    
    /**
     * Method to add a new attribute in the attribute map
     * @param att New attribute to be added
     */
    public void AddAttribute(Attribute att)
    {
        this.attributes.put(att.getName(), att);
    }
    
    /**
     * Method to add a new attribute in the attribute map
     * @param att New attribute to be added
     */
    public void AddAllAttributes(Map<String, Attribute> atts)
    {
        this.attributes.putAll(atts);
    }
    
    
    /**
     * Method to check if it is possible to parse the value to float
     * @param value desired to be parsed to float
     * @return boolean
     */
    public boolean tryParseFloat(String value) {
        try {
            Float.parseFloat(value);
            return true;
        } catch (NumberFormatException nfe) {
            return false;
        }
    }
    
}
