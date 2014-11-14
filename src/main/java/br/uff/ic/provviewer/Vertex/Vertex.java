package br.uff.ic.provviewer.Vertex;

import br.uff.ic.provviewer.Attribute;
import br.uff.ic.provviewer.GraphObject;
import java.awt.BasicStroke;
import java.awt.Paint;
import java.awt.Stroke;
import java.util.Collection;
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
    private String details;                         // Other text information
    
    // Refactor to be inside attributes
//    private String location;                        // prov:location
//    private String type;                            // prov:type
//    private String endTime;                         // prov:endTime
    
//    private Map<String, Attribute> attributes;      // prov:value

    /**
     * Constructor without attributes
     * Using this constructor, attributes must be added later
     * 
     * @param id vertex unique ID
     * @param label HUman readable name
     * @param time Time-related value. Used for temporal layouts
     * @param details Other textual information for the vertex
     */
    public Vertex(String id, String label, String time, String details) {
        this.id = id;
        this.label = label;
        this.time = time;
        this.details = details;
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
    public Vertex(String id, String label, String time, Map<String, Attribute> attributes, String details) {
        this.id = id;
        this.label = label;
        this.time = time;
        this.details = details;
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
    public float getDate() {    
        String[] day = this.time.split(":");
        return (Float.parseFloat(day[0]));
    }
    
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

    /**
     * Method to return the attribute value (not necessarily a number)
     * If the attribute does not exist, returns "Unknown"
     * @param attribute
     * @return 
     */
    public String getAttributeValue(String attribute) {
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
        if(tryParseFloat(attributes.get(attribute).getValue())) {
            return Float.parseFloat(attributes.get(attribute).getValue());
        }
        else {
            return 0;
        }
    }
    
//    /**
//     * Get method to return one specific attribute
//     * @param attribute Desired attribute name
//     * @return the attribute, containing name and value
//     */
//    public Attribute getAttribute(String attribute)
//    {
//        return attributes.get(attribute);
//    }
//    
//    /**
//     * Get method for all attributes
//     * @return vertex attributes as collection
//     */
//    public Collection<Attribute> getAttributes()
//    {
//        return attributes.values();
//    }
//    
//    /**
//     * Method that generates a String with all attributes names and values
//     * @return String containing attribute names and values
//     */
//    public String PrintAttributes()
//    {
//        String attributeList = "";
//        for(Attribute att : getAttributes())
//        {
//            attributeList += att.printAttribute();
//        }
//        return attributeList;
//    }
//    
//    /**
//     * Method to add a new vertex attribute in the attribute map
//     * @param att New attribute to be added
//     */
//    public void AddAttribute(Attribute att)
//    {
//        attributes.put(att.getName(), att);
//    }
//
//    /**
//     * Method to check if it is possible to parse the value to float
//     * @param value desired to be parsed to float
//     * @return boolean
//     */
//    private boolean tryParseFloat(String value) {
//        try {
//            Float.parseFloat(value);
//            return true;
//        } catch (NumberFormatException nfe) {
//            return false;
//        }
//    }
    
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
