/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.ic.provviewer.Vertex;

import br.uff.ic.provviewer.Input.Config;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Paint;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.geom.Rectangle2D;

/**
 * Subclass for Vertex named after PROV nomenclature and type
 *
 * @author Kohwalter
 */
public class ActivityVertex extends Vertex {

//    private String name;
//    private String date;

    /**
     * Constructor
     *
     * @param id This param is used by JUNG for collapsed vertices and tooltips.
     */
    public ActivityVertex(String id) {
        super(id, "", "", "");
    }

    /**
     * Constructor overload
     *
     * @param array for TSV Reader
     */
    public ActivityVertex(String[] array) {
        super(array[0], array[1], array[2], array[3]);

//        this.SetName(array[1]);
//        this.SetDate(array[2]);
    }

    @Override
    public Shape getShape() {
        return new Rectangle2D.Float(-7, -7, 17, 17);
    }

    @Override
    public Stroke getStroke(float width) {
        //dash = null returns a continuous line
        float[] dash = {4.0f};
        final Stroke nodeStroke = new BasicStroke(width, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10.0f, dash, 0.0f);
        return nodeStroke;
    }

    @Override
    public Paint getColor() {
        for (int i = 0; i < Config.actVerAtt.size(); i++) {
            if (this.getAttributeValue(Config.actVerAtt.get(i)).contains(Config.actVerValue.get(i))) {
                return Config.actVerColor.get(i);
            }
        }
        return new Color(159, 177, 252);
    }

    @Override
    public String getNodeType() {
        return "Activity";
    }

//    @Override
//    public String getDayName() {
//        String[] day = this.date.split(":");
//        return day[1];
//    }

//    @Override
//    public String getName() {
//        return name;
//    }

//    @Override
//    public int getDate() {
//        String[] day = this.date.split(":");
//        return Integer.parseInt(day[0]);
//    }
}
