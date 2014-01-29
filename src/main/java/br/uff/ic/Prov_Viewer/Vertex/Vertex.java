package br.uff.ic.Prov_Viewer.Vertex;

import java.awt.BasicStroke;
import java.awt.Paint;
import java.awt.Shape;
import java.awt.Stroke;

/**
 * Abstract (Generic) vertex type for the provenance graph
 * @author Kohwalter
 */
public abstract class Vertex extends Object{
    private String id;
  
    /**
     * Constructor
     * @param id This param is used by JUNG for collapsed vertices and tooltips.
     */
    public Vertex(String id) {
        this.id = id;
    }
    /**
     * Return the vertex ID
     * @return (String) id
     */
    public String getID() {
        return id;
    }
    
    /**
     *  This overrides the default JUNG method for displaying information
     * @return (String) id 
     */
    @Override
    public String toString() {
        return id;
    }
    
    public String getAttributeValue(String attribute)
    {
        String[] line = id.split(attribute);
        if (line.length > 1) {
            String[] line2 = line[1].split(" ");

            return line2[1];
        }
        return "0";
    }
    
    public int getAttributeValueInteger(String attribute)
    {
        return Integer.parseInt(this.getAttributeValue(attribute));
    }
    
    /**
     * Method to return the vertex shape
     * @return the Shape of the vertex
     */
    public abstract Shape getShape();
    /**
     * Method for getting the vertex border size
     * @deprecated use VertexStroke class instead
     * @param width Define the border width
     * @return (Stroke) returns the new vertex border width
     */
    public Stroke getStroke(float width) {
        float dash[] = null;
        final Stroke nodeStroke = new BasicStroke(width, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10.0f, dash, 0.0f);
        return nodeStroke;
    }
    /**
     * Method for defining the vertex color
     * @return (Paint) vertex color
     */
    public abstract Paint getColor();
    /**
     * Method used to identify the vertex type
     * @deprecated Use the vertex sub-class to check the type
     * @return (String) vertex type
     */
    public abstract String getNodeType();
    /**
     * (Optional) Method for returning the day of the week instead of the day's number.
     * @return (String) the day of the week (mon, tue, wed, ...)
     */
    public abstract String getDayName();
    /**
     * Method for returning the vertex name (not type) from the sub-classes.
     * i.e. Agent Vertex name = Kohwalter
     * @return (String) name
     */
    public abstract String getName();
    /**
     * Method for returning the vertex day (if any)
     * @return (int) date
     */
    public abstract int getDate();
}
