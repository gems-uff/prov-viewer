/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.ic.Prov_Viewer;

import java.awt.Color;
import java.awt.Paint;
import java.awt.Polygon;
import java.awt.Shape;

/**
 * Subclass for Vertex named after PROV nomenclature and type
 * @author Kohwalter
 */
public class AgentVertex extends Vertex {
    
    private String name;

    /**
     * Constructor
     * @param id This param is used by JUNG for collapsed vertices and tooltips.
     */
    public AgentVertex(String id) {
        super(id);
    }

    /**
     * Constructor overload
     * @param array for TSV Reader
     */
    public AgentVertex(String[] array) {
        super("Agent<br> " + "<b>Name: " + array[1] + "</b>"
                + " <br>" + array[2] + " <br><br>");

        this.name = array[1];
    }

    @Override
    public Shape getShape() {
        int[] XArray = {-7, -2, 3, 8, 8, 3, -2, -7};
        int[] YArray = {-2, -7, -7, -2, 3, 8, 8, 3};

        return new Polygon(XArray, YArray, 8);
    }

    @Override
    public Paint getColor() {
        return new Color(119, 136, 153);
    }

    @Override
    public String getNodeType() {
        return "Agent";
    }

    @Override
    public String getDayName() {
        return " ";
    }

    @Override
    public int getDate() {
        return 0;
    }

    @Override
    public String getName() {
        return name;
    }
}
