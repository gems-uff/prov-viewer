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
    
    /**
     * Empty constructor
     */
    public EdgeType(){
        this.type = "";
        this.stroke = "";
        this.collapse = "";
        this.max = 0;
        this.min = 0;
        this.count = 0;
        this.total = 0;
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
    }
    
}
