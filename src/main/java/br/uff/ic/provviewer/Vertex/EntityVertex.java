/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.ic.provviewer.Vertex;

import java.awt.Color;
import java.awt.Paint;

/**
 * Subclass for Vertex named after PROV nomenclature and type
 *
 * @author Kohwalter
 */
public class EntityVertex extends Vertex {

    /**
     * Constructor
     *
     * @param id This param is used by JUNG for collapsed vertices and tooltips.
     */
    public EntityVertex(String id) {
        super(id,"","","");
    }
    
    public EntityVertex(String id, String label, String date, String details) {
        super(id, label, date, details);
    }

    public EntityVertex(String[] array) {
        super(array[0], array[1], array[2], array[3]);
    }

    @Override
    public Paint getColor() {
        return new Color(255, 252, 135);
    }

    @Override
    public String getNodeType() {
        return "Entity";
    }
}