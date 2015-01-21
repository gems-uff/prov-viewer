/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.ic.provviewer.Vertex;

import br.uff.ic.provviewer.Input.Config;
import java.awt.Color;
import java.awt.Paint;

/**
 * Subclass for Vertex named after PROV nomenclature and type
 *
 * @author Kohwalter
 */
public class ActivityVertex extends Vertex {


    /**
     * Constructor
     *
     * @param id This param is used by JUNG for collapsed vertices and tooltips.
     */
    public ActivityVertex(String id) {
        super(id, "", "", "");
    }
    
    public ActivityVertex(String id, String label, String date, String details) {
        super(id, label, date, details);
    }

    /**
     * Constructor overload
     *
     * @param array for TSV Reader
     */
    public ActivityVertex(String[] array) {
        super(array[0], array[1], array[2], array[3]);
    }


    @Override
    public Paint getColor() {
        for (int i = 0; i < Config.actVerAtt.size(); i++) {
            //if (this.getAttributeValue(Config.actVerAtt.get(i)).contains(Config.actVerValue.get(i))) {
            if (this.getAttributeValue(Config.actVerAtt.get(i)).equalsIgnoreCase(Config.actVerValue.get(i))) {
                return Config.actVerColor.get(i);
            }
        }
        return new Color(159, 177, 252);
    }

    @Override
    public String getNodeType() {
        return "Activity";
    }
}
