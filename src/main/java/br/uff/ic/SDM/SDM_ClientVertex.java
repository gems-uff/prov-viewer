package br.uff.ic.SDM;

import br.uff.ic.Prov_Viewer.Vertex;
import java.awt.Color;
import java.awt.Paint;
import java.awt.Polygon;
import java.awt.Shape;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 * Vertex Subclass customized to SDM
 * @author Kohwalter
 */
public class SDM_ClientVertex extends Vertex {

    /**
     * Constructor
     * @param id This param is used by JUNG for collapsed vertices and tooltips.
     */
    public SDM_ClientVertex(String id) {
        super("<b>Client</b>");
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
        return "Client";
    }

    @Override
    public String getDayName() {
        return null;
    }

    @Override
    public int getDate() {
        return 0;
    }

    @Override
    public String getName() {
        return "Client";
    }
}