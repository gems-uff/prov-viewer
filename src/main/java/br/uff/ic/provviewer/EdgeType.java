/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.ic.provviewer;

/**
 *
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
    
    public EdgeType(){
        this.type = "";
        this.stroke = "";
        this.collapse = "";
        this.max = 0;
        this.min = 0;
        this.count = 0;
        this.total = 0;
    }
    public EdgeType(String type, String stroke, String collapse){
        this.type = type;
        this.stroke = stroke;
        this.collapse = collapse;
    }
    
}
