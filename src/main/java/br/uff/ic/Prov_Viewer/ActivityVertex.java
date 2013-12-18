/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.ic.Prov_Viewer;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Paint;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.geom.Rectangle2D;

/**
 * Subclass for Vertex named after PROV nomenclature and type
 * @author Kohwalter
 */
public class ActivityVertex extends Vertex {
    
    private String name;
    private String date;
    
    /**
     * Constructor
     * @param id This param is used by JUNG for collapsed vertices and tooltips.
     */
    public ActivityVertex(String id) {
        super(id); 
    }
    
    /**
     * Constructor overload
     * @param array for TSV Reader
     */
    public ActivityVertex(String[] array) {
        super("Activity<br> " + "<b>Name: " + array[1] + "</b>"
                + " <br>" + "Date: " + array[2]
                + " <br>" + array[3] + " <br><br>");

        this.name = array[1];
        this.date = array[2];
    }
      
    @Override
    public Shape getShape() {
        return new Rectangle2D.Float(-7, -7, 17, 17);
    }    

    @Override
    public Stroke getStroke(float width) {
        //dash = null returns a continuous line
        float[] dash = {4.0f};
//        if(!this.task.equalsIgnoreCase("Idle")) {
//            dash = null;
//        }
        final Stroke nodeStroke = new BasicStroke(width, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10.0f, dash, 0.0f);
        return nodeStroke;
    }

    @Override
    public Paint getColor() {
        if(this.getAttributeValue(Config.AVatt1).contains(Config.AVval1)) {
            return new Color(238,180,180);
        }
        else if(this.getAttributeValue(Config.AVatt2).contains(Config.AVval2)) {
            return new Color(102,0,102);
        }
        else if(this.getAttributeValue(Config.AVatt3).contains(Config.AVval3)) {
            return new Color(139,69,19);
        }
        else if(this.getAttributeValue(Config.AVatt4).contains(Config.AVval4)) {
            return new Color(0,153,0);
        }
        else if(this.getAttributeValue(Config.AVatt5).contains(Config.AVval5)) {
            return new Color(139,136,120);
        }
        else if(this.getAttributeValue(Config.AVatt6).contains(Config.AVval6)) {
            return new Color(193,205,193);
        }
        return new Color(190,190,190);
    }
    
    @Override
    public String getNodeType() {
        return "Activity";
    }
    
    @Override
    public String getDayName() {
        String[] day = this.date.split(":");
        return day[1];
    }
    
    @Override
    public String getName() {
        return name;
    }

    @Override
    public int getDate() {
        String[] day = this.date.split(":");
        return Integer.parseInt(day[0]);
    }
}
