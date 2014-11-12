package br.uff.ic.provviewer.Vertex;

import br.uff.ic.provviewer.Attribute;
import java.awt.BasicStroke;
import java.awt.Paint;
import java.awt.Shape;
import java.awt.Stroke;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Abstract (Generic) vertex type for the provenance graph
 *
 * @author Kohwalter
 */
public abstract class Vertex extends Object {

    private String id;                              // prov:id
    private String label;                           // prov:label
    private String time;                            // prov:startTime
    private String details;
    
    private String location;                        // prov:location
    private String type;                            // prov:type
//    private String endTime;
    
    private Map<String, Attribute> attributes;      // prov:value

    /**
     * Constructor
     *
     * @param id This param is used by JUNG for collapsed vertices and tooltips.
     * @param name
     * @param date
     * @param details
     */
    
    // Old
    public Vertex(String id, String label, String time, String details) {
        this.id = id;
        this.label = label;
        this.time = time;
        this.type = "";
        this.location = "";
        this.details = details;
        this.attributes  = new HashMap<String, Attribute>();
    }
    
    public Vertex(String id, String name, String label, 
            String location, String type, String startTime, String endTime, String time, String details) {
        this.id = id;
        this.label = name;
        this.time = time;
        this.details = details;
        this.attributes  = new HashMap<String, Attribute>();
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
    public float getDate() {    
        String[] day = this.time.split(":");
        //return Integer.parseInt(day[0]);
        //return Math.round(Float.parseFloat(day[0]));
        return (Float.parseFloat(day[0]));
    }
    
//    public String getFullDate() {
//        return time;
//    }
    
    public void SetLabel(String t){
        this.label = t;
    }
    
    public void SetTime(String t){
        this.time = t;
    }
    
    public void SetDetail(String t){
        this.details = t;
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
                + "<b>Name: " + this.label + "</b>"
                + " <br>" + "Date: " + this.time
                + " <br>" + PrintAttributes()
                + " <br>" + this.details;
    }

    // TODO: Refactor
    public String getAttributeValue(String attribute) {
//        String[] line = this.toString().split(attribute);
//        if (line.length > 1) {
//            String[] line2 = line[1].split(" ");
//            return line2[1];
//        }
//        return "0";
        Attribute aux = attributes.get(attribute);
        if(aux != null)
            return aux.getValue();
        else
            return "0";
    }
    

    public int getAttributeValueInteger(String attribute) {
        if(tryParseInt(attributes.get(attribute).getValue()))
            return Integer.parseInt(attributes.get(attribute).getValue());
        else
            return 0;
    }
    
    public Attribute getAttribute(String attribute)
    {
        return attributes.get(attribute);
    }
    
    public Collection<Attribute> getAttributes()
    {
        return attributes.values();
    }
    
    public String PrintAttributes()
    {
        String attributeList = "";
        for(Attribute att : getAttributes())
        {
            attributeList += att.getName() + ": " + att.getValue() + " <br>";
        }
        return attributeList;
    }
    
    public void AddAttribute(Attribute att)
    {
        attributes.put(att.getName(), att);
    }

    boolean tryParseInt(String value) {
        try {
            Integer.parseInt(value);
            return true;
        } catch (NumberFormatException nfe) {
            return false;
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
