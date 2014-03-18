package br.uff.ic.provviewer.Edge;

import br.uff.ic.provviewer.Input.Config;
import java.awt.Color;
import java.awt.Paint;

/**
 * Edge. Edge's influence (String) is composed of a value + type (i.e. +9
 * damage)
 *
 * @author Kohwalter
 */
public class Edge {

    private String id;
    private Object source;
    private Object target;
    private String type;
    private float value;
    private String label;
    //used to hide this edge when collapsing a group of edges
    private boolean hide;
    //used to say this edge is a temporary one
    private boolean collapsed;
//    private int id;

    /**
     * Constructor
     *
     * @param target Vertex target
     * @param source Vertex source
     * @param influence Influence value and type (i.e. "+9 damage")
     */
    public Edge(String id, Object target, Object source, String influence) {
        this.id = id;
        this.source = source;
        this.target = target;
        this.label = "";
        if (influence.equalsIgnoreCase("")) {
            this.type = "Neutral";
            this.value = 0;
        } else {
            this.type = getTypeFromInfluence(influence);
            this.value = getValueFromInfluence(influence);
        }
        hide = false;
        collapsed = false;
    }
    
    public Edge(String id, String type, String label, float value, Object target, Object source) {
        this.id = id;
        this.label = label;
        this.source = source;
        this.target = target;
        if (type.equalsIgnoreCase("")) {
            this.type = "Neutral";
            this.value = 0;
        } else {
            this.type = type;
            this.value = value;
        }
        hide = false;
        collapsed = false;
    }

    /**
     * Constructor for neutral edges (influence have no value/neutral)
     *
     * @param target Vertex target
     * @param source Vertex Source
     */
    public Edge(String id, Object target, Object source) {
        this.id = id;
        this.source = source;
        this.target = target;
        this.type = "Neutral";
        this.label = "";
        this.value = 0;
        hide = false;
        collapsed = false;
    }

    /**
     * Method to get the edge's source
     *
     * @return vertex source
     */
    public Object getSource() {
        return source;
    }

    /**
     * Method to get the edge's target
     *
     * @return vertex target
     */
    public Object getTarget() {
        return target;
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
     * Method for returning the edge's influence value (edge.influence = value +
     * type)
     *
     * @return (float) edge's influence value
     */
    public float getValueFromInfluence(String influence) {
        String[] line = influence.toString().split(" ");

        if (this.isNeutral()) {
            return 0;
        } else {
            return Float.parseFloat(line[0]);
        }
    }
    /**
     * Method for returning the edge's influence type (edge.influence = value +
     * type)
     *
     * @return (float) edge's influence value
     */
    public String getTypeFromInfluence(String influence) {
        String[] line = influence.toString().split(" ");

        if (line.length > 1) {
            return line[1];
        } else {
            return "Neutral";
        }
    }
    
    public float getValue() {
        return this.value;
    }

    /**
     * Method for returning the edge's type
     *
     * @return (String) influence type
     */
    public String getType() {
        return type;
    }
    
    public String getLabel() {
        return label;
    }
    
    public String getInfluence() {
        return this.value + " " + this.type;
    }

    /**
     * Method to check if the edge is of the neutral type (Empty influence or
     * value equals zero
     *
     * @return (boolean) is neutral or not
     */
    public boolean isNeutral() {
        if(this.type.equalsIgnoreCase("Neutral")) {
            return true;
        }
        return false;  
    }

    /**
     * Method to override JUNG's default toString method
     *
     * @return edge's influence (value + type)
     */
    @Override
    public String toString() {
        return this.label;
    }

    /**
     * Method to get the edge's color (red, black, or green), definied by the
     * influence's value
     *
     * @return
     */
    public Paint getColor() {
        //Green
        if (this.value > 0) {
            return new Color(34, 139, 34);
        } //Red
        else if (this.value < 0) {
            return new Color(255, 0, 0);
        } //Black
        else {
            return new Color(0, 0, 0);
        }
    }

    /**
     * Method used during the collapse of edges. This method defines if the
     * collapsed edge's influence value will be the sum of the edges values or
     * the average
     *
     * @return (boolean) Return true if influence from collapsed edges are
     * added. Return false if average
     */
    public boolean AddInfluence() {
        for (int i = 0; i < Config.edgecollapse.size(); i++) {
            if (this.type.contains(Config.edgetype.get(i))) {
                if(Config.edgecollapse.get(i)) {
                    return false;
                }
            }
        }
        return true;
    }
}
