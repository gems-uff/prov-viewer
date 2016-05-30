/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.ic.provviewer;

/**
 * Class to define the parameters for each edge type
 * @author Kohwalter
 */
public class EdgeType {
    public String type;
    public String stroke;
    public String collapse;
    public float max;
    public float min;
    public int count;
    public float total;
    public boolean isInverted;
    
    /**
     * Empty constructor
     */
    public EdgeType(){
        this.type = "";
        this.stroke = "";
        this.collapse = "";
        this.max = (float) Double.NEGATIVE_INFINITY;
        this.min = (float) Double.POSITIVE_INFINITY;
        this.count = 0;
        this.total = 0;
        this.isInverted = false;
    }
    
    /**
     * Default constructor
     * @param type is the edge type
     * @param stroke is the stroke
     * @param collapse is the collapse
     */
    public EdgeType(String type, String stroke, String collapse){
        this.type = type;
        this.stroke = stroke;
        this.collapse = collapse;
        this.isInverted = false;
    }
    
    public EdgeType(String type, String stroke, String collapse, boolean isInverted){
        this.type = type;
        this.stroke = stroke;
        this.collapse = collapse;
        this.isInverted = isInverted;
    }
    
}
