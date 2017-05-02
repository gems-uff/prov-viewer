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

package br.uff.ic.utility.graph;

import br.uff.ic.utility.GraphAttribute;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Class to define the graph objects (Vertex)
 * @author Kohwalter
 */
public class GraphObject extends Object{
    private String label;                           // prov:label
    public Map<String, GraphAttribute> attributes;
    
    /**
     * Get method to return one specific attribute
     * @param attribute Desired attribute name
     * @return the attribute, containing name and value
     */
    public GraphAttribute getAttribute(String attribute)
    {
        return attributes.get(attribute);
    }
    
    /**
     * Get method for all attributes
     * @return vertex attributes as collection
     */
    public Collection<GraphAttribute> getAttributes()
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
            for(GraphAttribute att : this.attributes.values())
            {
                if (!att.getAverageValue().isEmpty() && !att.getValue().equalsIgnoreCase("-") && !att.getValue().equalsIgnoreCase(""))
                    attributeList += att.printAttribute();
            }
        }
        return attributeList;
    }
    
    /**
     * Method to add a new attribute in the attribute map
     * @param att New attribute to be added
     */
    public void addAttribute(GraphAttribute att)
    {
        this.attributes.put(att.getName(), att);
    }
    
    /**
     * Method to add a new attribute in the attribute map
     * @param atts
     */
    public void addAllAttributes(Map<String, GraphAttribute> atts)
    {
        this.attributes.putAll(atts);
    }
    
    public Map<String, String> attributeList()
    {
        Map<String, String> attributeList = new HashMap<>();
        if(!this.attributes.values().isEmpty())
        {
            for(GraphAttribute att : this.attributes.values())
            {
                attributeList.put(att.getName(), att.getName());
            }
        }
        return attributeList;
    }
}
