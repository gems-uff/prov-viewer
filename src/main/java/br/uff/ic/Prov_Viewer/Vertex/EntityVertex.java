/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.ic.Prov_Viewer.Vertex;

import java.awt.Color;
import java.awt.Paint;
import java.awt.Shape;
import java.awt.geom.Ellipse2D;

/**
 * Subclass for Vertex named after PROV nomenclature and type
 *
 * @author Kohwalter
 */
public class EntityVertex extends Vertex {

//    private String name;
//    private String date;

    /**
     * Constructor
     *
     * @param id This param is used by JUNG for collapsed vertices and tooltips.
     */
    public EntityVertex(String id) {
        super(id);
    }

    public EntityVertex(String[] array) {
        super("Entity<br> " + "<b>Name: " + array[1] + "</b>"
                + " <br>" + "Date: " + array[2]
                + " <br>" + array[3] + " <br><br>");

        this.SetName(array[1]);
        this.SetDate(array[2]);
    }

    @Override
    public Shape getShape() {
        return new Ellipse2D.Float(-7, -7, 17, 17);
    }

    @Override
    public Paint getColor() {
        return new Color(255, 252, 135);
    }

    @Override
    public String getNodeType() {
        return "Entity";
    }

//    @Override
//    public String getDayName() {
//        String[] day = date.split(":");
//        return day[1];
//    }
//
//    @Override
//    public int getDate() {
//        String[] day = date.split(":");
//        return Integer.parseInt(day[0]);
//    }

//    @Override
//    public String getName() {
//        return name;
//    }
}
