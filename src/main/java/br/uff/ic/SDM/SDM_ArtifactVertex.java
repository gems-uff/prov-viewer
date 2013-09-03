package br.uff.ic.SDM;

import br.uff.ic.Prov_Viewer.Vertex;
import java.awt.Color;
import java.awt.Paint;
import java.awt.Shape;
import java.awt.geom.Ellipse2D;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 * Vertex Subclass customized to SDM
 *
 * @author Kohwalter
 */
public class SDM_ArtifactVertex extends Vertex {

    private String date;
    private String name;

    /**
     * Constructor
     *
     * @param id This param is used by JUNG for collapsed vertices and tooltips.
     */
    public SDM_ArtifactVertex(String id) {
        super("Artifact<br> " + id
                + " <br><br>");
    }

    /**
     * Constructor overload
     *
     * @param type i.e. Prototype, test cases
     * @param date date this artifact was created
     */
    public SDM_ArtifactVertex(String type, String date) {
        super("Artifact<br> " + "<b>Type: " + type + "</b>"
                + " <br>" + "Date: " + date
                + " <br><br>");
        this.date = date;
        this.name = type;
    }

    @Override
    public Shape getShape() {
        return new Ellipse2D.Float(-7, -7, 17, 17);
    }

    @Override
    public Paint getColor() {
        if (name.contains("Test")) {
            return new Color(100, 100, 100);
        }
        return new Color(150, 150, 150);
    }

    @Override
    public String getNodeType() {
        return "Artifact";
    }

    @Override
    public String getDayName() {
        return null;
    }

    @Override
    public int getDate() {
//        String id = getID();
//        String[] line = date.split(" ");
//        String[] day = line[1].split(":");
        String[] day = date.split(":");
        return Integer.parseInt(day[0]);
    }

    @Override
    public String getName() {
        return name;
    }
}