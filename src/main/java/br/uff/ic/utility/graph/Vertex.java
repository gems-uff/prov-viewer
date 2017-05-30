/*
 * The MIT License
 *
 * Copyright 2017 Kohwalter.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package br.uff.ic.utility.graph;

import br.uff.ic.provviewer.VariableNames;
import br.uff.ic.utility.GraphAttribute;
import br.uff.ic.utility.Utils;
import static br.uff.ic.utility.Utils.isItTime;
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
    private double normalizedTime;
//    private String time;                            // prov:startTime
                                                    // Refactor for datetime type
    private String timeFormat;
    private String timeScale;
    private String timeLabel = "Timestamp";
    
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
//        this.time = time;
        GraphAttribute t = new GraphAttribute(timeLabel, time);
        this.attributes = new HashMap<>();
        this.attributes.put(t.getName(), t);
        setLabel(label);
        timeFormat = "nanoseconds";
        timeScale = "nanoseconds";
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
        this.attributes = new HashMap<>();
        this.attributes.putAll(attributes);
        GraphAttribute t = new GraphAttribute(timeLabel, time);
        this.attributes.put(t.getName(), t);
        setLabel(label);
        timeFormat = "nanoseconds";
        timeScale = "nanoseconds";
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
    public double getTime() {    
//        String[] day = this.time.split(":");
        String time = this.attributes.get(timeLabel).getAverageValue();
        if(Utils.tryParseFloat(time))
            return (Double.parseDouble(time));
        else if(Utils.tryParseDate(time))
        {
            double milliseconds =  Utils.convertStringDateToFloat(time);
            return milliseconds;
        }
        else
            return -1;
    }
    
    /**
     * Method to get the value of the variable time 
     * @return time
     */
    public String getTimeString() {    
        return this.attributes.get(timeLabel).getAverageValue();
    }
    
    /**
     * Method to set the value of the variable time
     * @param t is the new value
     */
    public void setTime(String t){
        GraphAttribute time = new GraphAttribute(timeLabel, t);
        this.attributes.put(timeLabel, time);
    }

    
    /**
     * (Optional) Method for returning the day of the week instead of the day's
     * number.
     *
     * @return (String) the day of the week (mon, tue, wed, ...)
     */
    public String getDayName() {
        String[] day = this.attributes.get(timeLabel).getAverageValue().split(":");
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
        double nt = this.getTime();
        return "Timestamp: " + Utils.convertTime(timeFormat, nt, timeScale) + " (" + timeScale + ")";
    }
    
    public void setTimeScalePrint(String timeFormat, String timeScale) {
        this.timeFormat = timeFormat;
        this.timeScale = timeScale;
    }

    /**
     * Method to return the attribute value (not necessarily a number)
     * If the attribute does not exist, returns "Unknown"
     * @param attribute
     * @return 
     */
    @Override
    public String getAttributeValue(String attribute) {
        if(attribute.equalsIgnoreCase("Label")) {
            return getLabel();
        }
        GraphAttribute aux = attributes.get(attribute);
        if(aux != null) {
            return aux.getAverageValue();
        }
        else {
            return deltaAttributeValue(attribute);
//            return "Unknown";
        }
    }
    
    /**
     * Method to return the values in an attribute that were separated by a comma
     * @param attribute is the attribute that we want to get the values
     * @return an array with the values
     */
    @Override
    public String[] getAttributeValues(String attribute) {
        String values = this.getAttributeValue(attribute);
        return values.split(", ");
    }
    
    /**
     * Method to return the attribute value as float
     * @param attribute
     * @return 
     */
    public float getAttributeValueFloat(String attribute) {
        if(isItTime(attribute)) {
            return (float)getNormalizedTime();
        }
        if(attributes.get(attribute) == null) {
            return deltaAttributeFloatValue(attribute);
        }
        return getAttFloatValue(attribute);
    }
    
    
    
    /**
     * Method that gets both attributes in the string and subtracts them. I.e.: First_Attribute - Second_Attribute
     * @param attribute must be a string with both atributes separared by " - ". Example: "First_Attribute - Second_Attribute"
     * @return the delta
     */
    private float deltaAttributeFloatValue(String attribute) {
        String[] atts = attribute.split(" - ");
        if(atts.length == 2)
            return getAttFloatValue(atts[0]) - getAttFloatValue(atts[1]);
        else
            return Float.NaN;
    }
    
    /**
     * Method that returns the delta from the two attributes or Unknown if any attribute is invalid
     * @param attribute must be a string with both atributes separared by " - ". Example: "First_Attribute - Second_Attribute"
     * @return the delta as a String
     */
    private String deltaAttributeValue(String attribute) {
        if(attribute.equalsIgnoreCase("Label")) {
            return getLabel();
        }
        if(isItTime(attribute)) {
            return String.valueOf(getTime());
        }
        
        String[] atts = attribute.split(" - ");
        if(atts.length == 2) {
            if(VariableNames.UnknownValue.equals(getAttributeValue(atts[0])) || VariableNames.UnknownValue.equals(getAttributeValue(atts[1]))) return VariableNames.UnknownValue;
            else {
                String delta = Float.toString(getAttFloatValue(atts[0]) - getAttFloatValue(atts[1]));
                return delta;
            }
        }
        else
            return VariableNames.UnknownValue;
    }
    
    /**
     * Method that returns the float value of the attribute. If it is not convertable, then it returns Float.NaN
     * @param attribute the attribute that we want to get the value from
     * @return the float value or Float.NaN if it is not possible to convert to float
     */
    private float getAttFloatValue(String attribute) {
        if(isItTime(attribute)) {
            return (float)getNormalizedTime();
        }
        if(attributes.get(attribute) != null) {
            if(Utils.tryParseFloat(attributes.get(attribute).getAverageValue())) {
                return Utils.convertFloat(attributes.get(attribute).getAverageValue());
            }
            else {
                
                return Float.NaN;
            }
        }
        else {
            return Float.NaN;
        }
    }
    
    /**
     * Method that returns TRUE if the vertex has the attribute and FALSE if it does not
     * @param att is the name of the attribute we want to query
     * @return if the vertex has the attribute
     */
    public boolean hasAttribute(String att) {
        if(this.attributes.containsKey(att))
            return true;
        else
            return false;
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
