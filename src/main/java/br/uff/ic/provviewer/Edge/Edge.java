package br.uff.ic.provviewer.Edge;

import br.uff.ic.provviewer.Attribute;
import br.uff.ic.provviewer.EdgeType;
import br.uff.ic.provviewer.GraphObject;
import br.uff.ic.provviewer.Utils;
import br.uff.ic.provviewer.Variables;
import java.awt.Color;
import java.awt.Paint;
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
    //private String influence;      // Influence type (i.e. Damage)
    private String value;          // Influence Value (i.e. 5.0)
    private String type;           // Edge type (prov edges)
    private String label;          // Human readable name
    //used to hide this edge when collapsing a group of edges
    private boolean hide;
    //used to say this edge is a temporary one
    private boolean collapsed;
    
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
    public Edge(String id, String influence, String type, String value, 
            String label, Map<String, Attribute> attributes, Object target, Object source) {
        this.id = id;
        this.source = source;
        this.target = target;
        this.type = type;
        if (influence.equalsIgnoreCase("") || (influence == null) || influence.equalsIgnoreCase("Neutral")) {
            this.label = "Neutral";
            this.value = "0";
        } else {
            this.label = influence;
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
     * @param type
     * @param value
     * @param label
     * @param target
     * @param source 
     */
    public Edge(String id, String type, String label, String value, Object target, Object source) {
        this.id = id;
        this.source = source;
        this.target = target;
        this.type = type;
        if (label.equalsIgnoreCase("") || (label == null) || label.equalsIgnoreCase("Neutral")) {
            this.label = "Neutral";
            this.value = "0";
        } else {
            this.label = label;
            this.value = value;
        }
        this.label = label;
        hide = false;
        collapsed = false;
        this.attributes  = new HashMap<String, Attribute>();
    }
    
    /**
     * Constructor without influence value, label, type (type=influence) and no extra attribute
     * @param id Edge's ID
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
            this.label = "Neutral";
        } else {
            this.label = influence;
        }
        this.value = "0";
        this.type = this.label;
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
        if(Utils.tryParseFloat(this.value)) {
            return Float.parseFloat(this.value);
        }
        else {
            return 0;
        }
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
        return this.value + " " + this.label;
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
        return (this.label.equalsIgnoreCase(""))
                || (this.label.isEmpty())
                || (this.label.equalsIgnoreCase("Neutral"));
    }

    /**
     * Method to override JUNG's default toString method
     *
     * @return edge details
     */
    @Override
    public String toString() {
        if(this.label.isEmpty())
            return this.type;
        else
            return this.type + " (" + this.label + ")";
    }

    /**
     * Method to get the edge color (red, black, or green), defined by value
     * @return
     */
    public Paint getColor(Variables variables) {
        float v = getValue();
        
//        if(this.label.equalsIgnoreCase("Neutral"))
//        {
//            if(((Vertex)this.source).getDate() < 85)
//                return new Color(255, 0, 0);
//            if(((Vertex)this.source).getDate() < 150)
//                return new Color(0, 255, 0);
//            if(((Vertex)this.source).getDate() < 300)
//                return new Color(0, 0, 255);
//        }     
        // Return blue if neutral edge (value = 0)
        if (v == 0) {
            return new Color(0, 255, 255);
        }
        else
        {
            int j = 0;
            for (int i = 0; i < variables.config.edgetype.size(); i++) {
                if (this.getLabel().contains(variables.config.edgetype.get(i).type)) {
                    j = i;
                }
            }
            
            if (v > 0) {
                return CompareValueGreen(v, 0, variables.config.edgetype.get(j).max);
            }
            else {
                return CompareValueRed(v, variables.config.edgetype.get(j).min, 0);
            }
        }
    }

    public Paint CompareValueGreen(float value, double min, double max){
        int proportion = (int) Math.round(510 * Math.abs(value - min) / (float) Math.abs(max - min));
        proportion = Math.max(proportion, 0);
        return new Color(0, Math.min(255, proportion), 0);
    }
    public Paint CompareValueRed(float value, double min, double max){
        int proportion = (int) Math.round(510 * Math.abs(value - min) / (float) Math.abs(max - min));
        proportion = Math.min(proportion, 510);
        return new Color(Math.min(255, 510 - proportion), 0, 0);
    }
    
    /**
     * Method used during the collapse of edges. This method defines if the
     * collapsed edge influence value will be the sum of the edges values or
     * the average
     *
     * @return (boolean) Return true if influence from collapsed edges are
     * added. Return false if average
     */
    public boolean AddInfluence(Variables variables) {
        for (EdgeType edgetype : variables.config.edgetype) {
            if (this.getEdgeInfluence().contains(edgetype.type)) {
                if (edgetype.collapse.equalsIgnoreCase("AVERAGE")) {
                    return false;
                }
            }
        }
        return true;
    }
}
