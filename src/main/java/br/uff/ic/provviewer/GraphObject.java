/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.ic.provviewer;

import java.util.Collection;
import java.util.Map;

/**
 * Class to define the graph objects (Vertex)
 * @author Kohwalter
 */
public class GraphObject extends Object{
    private String label;                           // prov:label
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
    
    public String getLabel(){
        return this.label;
    }
    
    public void setLabel(String t){
        this.label = t;
    }
    
    /**
     * Method that generates a String with all attributes names and values
     * @return String containing attribute names and values
     */
    public String printAttributes()
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
    public void addAttribute(Attribute att)
    {
        this.attributes.put(att.getName(), att);
    }
    
    /**
     * Method to add a new attribute in the attribute map
     * @param atts
     */
    public void addAllAttributes(Map<String, Attribute> atts)
    {
        this.attributes.putAll(atts);
    }
}
