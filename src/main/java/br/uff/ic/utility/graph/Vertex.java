package br.uff.ic.utility.graph;

import br.uff.ic.utility.GraphAttribute;
import br.uff.ic.utility.Utils;
import java.awt.BasicStroke;
import java.awt.Paint;
import java.awt.Stroke;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Abstract (Generic) vertex type for the provenance graph
 *
 * Time format must be either a Number or DayNumber:DayName (for the weekend display mode)
 * @author Kohwalter
 */
public abstract class Vertex extends GraphObject {

    private String id;                              // prov:id
    private double normalizedTime;
    private String time;                            // prov:startTime
                                                    // Refactor for datetime type
    
    /**
     * Constructor without attributes
     * Using this constructor, attributes must be added later
     * 
     * @param id vertex unique ID
     * @param label HUman readable name
     * @param time Time-related value. Used for temporal layouts
     */
    public Vertex(String id, String label, String time) {
        this.id = id;
        setLabel(label);
        this.time = time;
        this.attributes  = new HashMap<String, GraphAttribute>();
    }
    
    /**
     * Constructor with attributes
     * @param id
     * @param label
     * @param time
     * @param attributes 
     */
    public Vertex(String id, String label, String time, Map<String, GraphAttribute> attributes) {
        this.id = id;
        setLabel(label);
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
     * Set vertex ID
     * @param t is the new ID
     */
    public void setID(String t) {
        id = t;
    }
    
    public void setNormalizedTime(double t) {
        this.normalizedTime = t;
    }
    
    public double getNormalizedTime() {
        return this.normalizedTime;
    }
    
    /**
     * Method for returning the vertex name (not type) from the sub-classes.
     * i.e. Agent Vertex name = Kohwalter
     *
     * @return (String) name
     */
    
    /**
     * Method for returning the vertex day (if any)
     *
     * @return (int) date
     */
    public float getTime() {    
//        String[] day = this.time.split(":");
        if(Utils.tryParseFloat(this.time))
            return (Float.parseFloat(this.time));
        else if(Utils.tryParseDate(this.time))
        {
//            System.out.println("Time Milliseconds: " + (float) Utils.convertStringDateToDouble(this.time));
            double milliseconds =  Utils.convertStringDateToDouble(this.time);
            int weeks = (int) (milliseconds / (1000*60*60*24*7));
            long days = TimeUnit.MILLISECONDS.toDays((long) milliseconds);
            long hours = TimeUnit.MILLISECONDS.toHours((long) milliseconds);
//            System.out.println("Time Weeks: " + weeks);
//            System.out.println("Time Days: " + days);
            return (float) milliseconds;
        }
        else
            return -1;
    }
    
    public String getTimeString() {    
        return this.time;
    }
    
    public void setTime(String t){
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
                + "<b>Label: " + getLabel() + "</b>"
                + " <br>" + printTime()
                + " <br>" + printAttributes();
    }
    
    public String printTime()
    {
        if(this.time.isEmpty())
        {
            return "";
        }
        return "Time: " + this.time;
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
            return getLabel();
        }
        GraphAttribute aux = attributes.get(attribute);
        if(aux != null) {
            return aux.getAverageValue();
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
        if(Utils.tryParseFloat(attributes.get(attribute).getAverageValue())) {
            return Float.parseFloat(attributes.get(attribute).getAverageValue());
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
