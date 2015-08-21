package br.uff.ic.provviewer.Vertex;

import br.uff.ic.provviewer.Attribute;
import br.uff.ic.provviewer.GraphObject;
import br.uff.ic.provviewer.Utils;
import java.awt.BasicStroke;
import java.awt.Paint;
import java.awt.Stroke;
import java.util.HashMap;
import java.util.Map;

/**
 * Abstract (Generic) vertex type for the provenance graph
 *
 * Time format must be either a Number or DayNumber:DayName (for the weekend display mode)
 * @author Kohwalter
 */
public abstract class Vertex extends GraphObject {

    private String id;                              // prov:id
    private String label;                           // prov:label
    private String time;                            // prov:startTime
                                                    // Refactor for datetime type
    
    /**
     * Constructor without attributes
     * Using this constructor, attributes must be added later
     * 
     * @param id vertex unique ID
     * @param label HUman readable name
     * @param time Time-related value. Used for temporal layouts
     * @param details Other textual information for the vertex
     */
    public Vertex(String id, String label, String time) {
        this.id = id;
        this.label = label;
        this.time = time;
        this.attributes  = new HashMap<String, Attribute>();
    }
    
    /**
     * Constructor with attributes
     * @param id
     * @param label
     * @param time
     * @param attributes
     * @param details 
     */
    public Vertex(String id, String label, String time, Map<String, Attribute> attributes) {
        this.id = id;
        this.label = label;
        this.time = time;
        this.attributes.putAll(attributes);
    }

    /**
     * Return the vertex ID
     *
     * @return (String) id
     */
    public String getID() {
        return id;
    }
    
    /**
     * Method for returning the vertex name (not type) from the sub-classes.
     * i.e. Agent Vertex name = Kohwalter
     *
     * @return (String) name
     */
    public String getLabel(){
        return this.label;
    }
    
    /**
     * Method for returning the vertex day (if any)
     *
     * @return (int) date
     */
    public float getTime() {    
        String[] day = this.time.split(":");
        if(Utils.tryParseFloat(day[0]))
            return (Float.parseFloat(day[0]));
        else
            return 0;
    }
    
    public String getTimeString() {    
        return this.time;
    }
    
    public void SetLabel(String t){
        this.label = t;
    }
    
    public void SetTime(String t){
        this.time = t;
    }


    /**
     * (Optional) Method for returning the day of the week instead of the day's
     * number.
     *
     * @return (String) the day of the week (mon, tue, wed, ...)
     */
    public String getDayName() {
        String[] day = this.time.split(":");
        return day[1];
    }
    
    /**
     * This overrides the default JUNG method for displaying information
     *
     * @return (String) id
     */
    @Override
    public String toString() {
        return this.getNodeType() + "<br> "
                + "<br>ID: " + this.id + "<br>"
                + "<b>Label: " + this.label + "</b>"
                + " <br>" + "Time: " + this.time
                + " <br>" + PrintAttributes();
    }

    /**
     * Method to return the attribute value (not necessarily a number)
     * If the attribute does not exist, returns "Unknown"
     * @param attribute
     * @return 
     */
    public String getAttributeValue(String attribute) {
        if(attribute.equalsIgnoreCase("Label"))
        {
            return label;
        }
        Attribute aux = attributes.get(attribute);
        if(aux != null) {
            return aux.getValue();
        }
        else {
            return "Unknown";
        }
    }
    
    /**
     * Method to return the attribute value as float
     * @param attribute
     * @return 
     */
    public float getAttributeValueFloat(String attribute) {
        if(attributes.get(attribute) == null)
            return 0;
        if(Utils.tryParseFloat(attributes.get(attribute).getValue())) {
            return Float.parseFloat(attributes.get(attribute).getValue());
        }
        else {
            return 0;
        }
    }
    
    /**
     * Method for getting the vertex border size
     *
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
     *
     * @return (Paint) vertex color
     */
    public abstract Paint getColor();

    /**
     * Method used to identify the vertex type
     *
     * 
     * @return (String) vertex type
     */
    public abstract String getNodeType();

}
