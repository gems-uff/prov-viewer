package br.uff.ic.utility.graph;

import br.uff.ic.utility.GraphAttribute;
import br.uff.ic.provviewer.EdgeType;
import br.uff.ic.provviewer.Variables;
import br.uff.ic.utility.Utils;
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
    private final String type;           // Edge type (prov edges)
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
     * @param attributes
     * @param target
     * @param source 
     */
    public Edge(String id, String influence, String type, String value, 
            String label, Map<String, GraphAttribute> attributes, Object target, Object source) {
        this.id = id;
        this.source = source;
        this.target = target;
        this.type = type;
        if (influence.equalsIgnoreCase("") || (influence == null) || influence.equalsIgnoreCase("Neutral")) {
            setLabel("Neutral");
            this.value = "0";
        } else {
            setLabel(influence);
            this.value = value;
        }
        setLabel(label);
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
        if (label.equalsIgnoreCase("") || label == null || "-".equals(label) || label.equalsIgnoreCase("Neutral")) {
            setLabel("Neutral");
            this.value = "0";
        } else {
            this.value = value;
        }
        setLabel(label);
        hide = false;
        collapsed = false;
        this.attributes  = new HashMap<>();
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
            setLabel("Neutral");
        } else {
            setLabel(influence);
        }
        this.value = "0";
        this.type = getLabel();
        hide = false;
        collapsed = false;
        this.attributes  = new HashMap<>();
    }

    /**
     * Return Edge id
     * @return 
     */
    public String getID() {
        return id;
    }
    
    public void setID(String t) {
        id = t;
    }
    
    /**
     * Method to get the edge source
     *
     * @return vertex source
     */
    public Object getSource() {
        return source;
    }

    public void setSource(Object t) {
        source = t;
    }
    
    /**
     * Method to get the edge target
     *
     * @return vertex target
     */
    public Object getTarget() {
        return target;
    }
    
    public void setTarget(Object t) {
        target = t;
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
        else if(Utils.tryParseFloat(this.value.split(" ")[0])) {
            return Float.parseFloat(this.value.split(" ")[0]);
        }
        else {
            return 0;
        }
    }
    
    public void setValue(String t)
    {
        this.value = t;
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
     * Method to get the edge influence + value
     *
     * @return (String) influence
     */
    public String getEdgeTooltip() {
        String atts = "";
        if(!this.attributes.values().isEmpty())
        {
            atts = "<br>" + this.printAttributes();
        }
        
        String v = this.value;
        String l = getLabel();
        String t = "(" + this.type + ")";
        
        if("0".equals(this.value))
            v = "";
        if("".equals(getLabel()))
        {
            l = "";
            t = this.type;
        }
        if(getLabel().contentEquals(this.type))
        {
            l = "";
            t = this.type;
        }
        return v + " " + l + " " + t + " " + atts;
        
//        return v + " " + getLabel() + " (" + this.type + ")" + atts;
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
    public void setHide(boolean t) {
        hide = t;
    }

    /**
     * Method to set the collapsed parameter
     *
     * @param t (boolean) collapsed = t
     */
    public void setCollapse(boolean t) {
        collapsed = t;
    }

    /**
     * Method to check if the edge is of the neutral type (Empty influence or
     * value equals zero
     *
     * @return (boolean) is neutral or not
     */
    public boolean isNeutral() {
        return (getLabel().equalsIgnoreCase(""))
                || (getLabel().isEmpty())
                || (getLabel().equalsIgnoreCase("Neutral"));
    }

    /**
     * Method to override JUNG's default toString method
     *
     * @return edge details
     */
    @Override
    public String toString() {
        String font = "<html><font size=\"4\", font color=\"blue\">";
        if(getLabel().isEmpty())
            return font + this.type;
        else if(getLabel().equals(this.type))
            return font + this.type;
        else
            return font + this.type + " (" + getLabel() + ")";
    }

    /**
     * Method to get the edge color (red, black, or green), defined by value
     * @param variables
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
            // TODO add inverted color scheme for increase in value to be red and decrease to be green
            // if (variables.config.edgetype.get(j).isInverted)
            // DO
            // else
            if (v > 0) {
                return compareValueGreen(v, 0, variables.config.edgetype.get(j).max);
            }
            else {
                return compareValueRed(v, variables.config.edgetype.get(j).min, 0);
            }
        }
    }

    public Paint compareValueGreen(float value, double min, double max){
        int proportion = (int) Math.round(510 * Math.abs(value - min) / (float) Math.abs(max - min));
        proportion = Math.max(proportion, 0);
        return new Color(0, Math.min(255, proportion), 0);
    }
    public Paint compareValueRed(float value, double min, double max){
        int proportion = (int) Math.round(510 * Math.abs(value - min) / (float) Math.abs(max - min));
        proportion = Math.min(proportion, 510);
        return new Color(Math.min(255, 510 - proportion), 0, 0);
    }
    
    /**
     * Method used during the collapse of edges. This method defines if the
     * collapsed edge influence value will be the sum of the edges values or
     * the average
     *
     * @param variables
     * @return (boolean) Return true if influence from collapsed edges are
     * added. Return false if average
     */
    public boolean addInfluence(Variables variables) {
        for (EdgeType edgetype : variables.config.edgetype) {
            if (this.getEdgeTooltip().contains(edgetype.type)) {
                if (edgetype.collapse.equalsIgnoreCase("AVERAGE")) {
                    return false;
                }
            }
        }
        return true;
    }
}
