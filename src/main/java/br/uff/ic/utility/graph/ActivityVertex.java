/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.ic.utility.graph;

import br.uff.ic.provviewer.Variables;
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
        super(id, "", "");
    }
    
    public ActivityVertex(String id, String label, String date) {
        super(id, label, date);
    }

    /**
     * Constructor overload
     *
     * @param array for TSV Reader
     */
    public ActivityVertex(String[] array) {
        super(array[0], array[1], array[2]);
    }


    @Override
    public Paint getColor() {
        return new Color(159, 177, 252);
    }
    
    /**
     * Color function used by Default Color Scheme
     * (Activity vertex only)
     * @return 
     */
    public Paint getDefaultColor(Variables variables) {
        for (int i = 0; i < variables.config.actVerAtt.size(); i++) {
            if (this.getAttributeValue(variables.config.actVerAtt.get(i)).equalsIgnoreCase(variables.config.actVerValue.get(i))) {
                return variables.config.actVerColor.get(i);
            }
        }
        return new Color(159, 177, 252);
    }

    @Override
    public String getNodeType() {
        return "Activity";
    }
}
