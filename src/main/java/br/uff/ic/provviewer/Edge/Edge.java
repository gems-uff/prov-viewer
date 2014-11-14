package br.uff.ic.provviewer.Edge;

import br.uff.ic.provviewer.Attribute;
import br.uff.ic.provviewer.GraphObject;
import br.uff.ic.provviewer.Input.Config;
import java.awt.Color;
import java.awt.Paint;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Edge Class
 *
 * @author Kohwalter
 */
public class Edge extends GraphObject{

    private String id;
    private Object source;
    private Object target;
    private String influence;      // Influence type (i.e. Damage)
    private String value;          // Influence Value (i.e. 5.0)
    private String type;           // Edge type (prov edges)
    private String label;          // Human readable name
    //used to hide this edge when collapsing a group of edges
    private boolean hide;
    //used to say this edge is a temporary one
    private boolean collapsed;
//    private String location;
//    private String type;
//    private String role;
    

    /**
     * Constructor
     * @param id
     * @param influence
     * @param type
     * @param value
     * @param label
     * @param target
     * @param source 
     */
    public Edge(String id, String influence, String type, String value, String label, Map<String, Attribute> attributes, Object target, Object source) {
        this.id = id;
        this.source = source;
        this.target = target;
        this.type = type;
        if (influence.equalsIgnoreCase("") || (influence == null) || influence.equalsIgnoreCase("Neutral")) {
            this.influence = "Neutral";
            this.value = "0";
        } else {
            this.influence = influence;
            this.value = value;
        }
        this.label = label;
        hide = false;
        collapsed = false;
        this.attributes.putAll(attributes);
    }
    
    /**
     * Constructor without extra attributes
     * @param id
     * @param influence
     * @param type
     * @param value
     * @param target
     * @param source 
     */
    public Edge(String id, String influence, String type, String value, String label, Object target, Object source) {
        this.id = id;
        this.source = source;
        this.target = target;
        this.type = type;
        if (influence.equalsIgnoreCase("") || (influence == null) || influence.equalsIgnoreCase("Neutral")) {
            this.influence = "Neutral";
            this.value = "0";
        } else {
            this.influence = influence;
            this.value = value;
        }
        this.label = label;
        hide = false;
        collapsed = false;
        this.attributes  = new HashMap<String, Attribute>();
    }
    
    /**
     * Constructor without influence value, label, type (type=influence) and no extra attribute
     * @deprecated Used only on outdated TSVReader
     * @param target Vertex target
     * @param source Vertex source
     * @param influence Influence value and name (i.e. "+9 damage")
     */
    public Edge(String id, Object target, Object source, String influence) {
        this.id = id;
        this.source = source;
        this.target = target;
        if (influence.equalsIgnoreCase("")) {
            this.influence = "Neutral";
        } else {
            this.influence = influence;
        }
        this.value = "0";
        this.type = this.influence;
        this.label = "";
        hide = false;
        collapsed = false;
        this.attributes  = new HashMap<String, Attribute>();
    }

    /**
     * Return Edge id
     * @return 
     */
    public String getID() {
        return id;
    }
    /**
     * Method to get the edge source
     *
     * @return vertex source
     */
    public Object getSource() {
        return source;
    }

    /**
     * Method to get the edge target
     *
     * @return vertex target
     */
    public Object getTarget() {
        return target;
    }
    
    /**
     * Method for returning the edge value 
     *
     * @return (float) edge influence value
     */
    public float getValue() {
        if(tryParseFloat(this.value)) {
            return Float.parseFloat(this.value);
        }
        else {
            return 0;
        }
    }

    /**
     * Method for returning the edge influence
     *
     * @return
     */
    public String getInfluence() {
        return influence;
    }
    
    /**
     * Method for returning the edge type
     *
     * @return
     */
    public String getType() {
        return this.type;
    }
    
    /**
     * Method for returning the edge label
     *
     * @return
     */
    public String getLabel() {
        return this.label;
    }

    /**
     * Method to get the edge influence + value
     *
     * @return (String) influence
     */
    public String getEdgeInfluence() {
        return value + " " + influence;
    }

    /**
     * Method to check if the edge is hidden (due to collapses)
     *
     * @return (boolean) if the edge is hidden or not
     */
    public boolean isHidden() {
        return hide;
    }

    /**
     * Method to check if the edge is a collapsed edge (new edge that contains
     * the information of the collapsed ones)
     *
     * @return (boolean) if the edge is collapsed or not
     */
    public boolean isCollapased() {
        return collapsed;
    }

    /**
     * Method to set the hide parameter
     *
     * @param t (boolean) Hide = t
     */
    public void SetHide(boolean t) {
        hide = t;
    }

    /**
     * Method to set the collapsed parameter
     *
     * @param t (boolean) collapsed = t
     */
    public void SetCollapse(boolean t) {
        collapsed = t;
    }

    /**
     * Method to check if the edge is of the neutral type (Empty influence or
     * value equals zero
     *
     * @return (boolean) is neutral or not
     */
    public boolean isNeutral() {
        if ((this.influence.equalsIgnoreCase(""))
                || (this.influence.isEmpty())
                || (this.influence.equalsIgnoreCase("Neutral"))) {
            return true;
        }
        return false;
    }

    /**
     * Method to override JUNG's default toString method
     *
     * @return edge details
     */
    @Override
    public String toString() {
        return this.type + "<br>" + this.label + "<br>" + PrintAttributes();
    }

    /**
     * Method to get the edge color (red, black, or green), defined by value
     * @return
     */
    public Paint getColor() {
        float v = getValue();
        //Green
        if (v > 0) {
            return new Color(34, 139, 34);
        } //Red
        else if (v < 0) {
            return new Color(255, 0, 0);
        } //Black
        else {
            return new Color(0, 0, 0);
        }
    }

    /**
     * Method used during the collapse of edges. This method defines if the
     * collapsed edge influence value will be the sum of the edges values or
     * the average
     *
     * @return (boolean) Return true if influence from collapsed edges are
     * added. Return false if average
     */
    public boolean AddInfluence() {
        for (int i = 0; i < Config.edgetype.size(); i++) {
            if (this.getEdgeInfluence().contains(Config.edgetype.get(i).type)) {
                if(Config.edgetype.get(i).collapse.equalsIgnoreCase("AVERAGE")) {
                    return false;
                }
            }
        }
        return true;
    }
    
//     /**
//     * Get method to return one specific attribute
//     * @param attribute Desired attribute name
//     * @return the attribute, containing name and value
//     */
//    public Attribute getAttribute(String attribute)
//    {
//        return attributes.get(attribute);
//    }
//    
//    /**
//     * Get method for all attributes
//     * @return vertex attributes as collection
//     */
//    public Collection<Attribute> getAttributes()
//    {
//        return attributes.values();
//    }
//    
//    /**
//     * Method that generates a String with all attributes names and values
//     * @return String containing attribute names and values
//     */
//    public String PrintAttributes()
//    {
//        String attributeList = "";
//        if(!attributes.values().isEmpty())
//        {
//            for(Attribute att : attributes.values())
//            {
//                attributeList += att.printAttribute();
//            }
//        }
//        return attributeList;
//    }
//    
//    /**
//     * Method to add a new vertex attribute in the attribute map
//     * @param att New attribute to be added
//     */
//    public void AddAttribute(Attribute att)
//    {
//        attributes.put(att.getName(), att);
//    }
//    
//    /**
//     * Method to check if it is possible to parse the value to float
//     * @param value desired to be parsed to float
//     * @return boolean
//     */
//    public boolean tryParseFloat(String value) {
//        try {
//            Float.parseFloat(value);
//            return true;
//        } catch (NumberFormatException nfe) {
//            return false;
//        }
//    }
}
