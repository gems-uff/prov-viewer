/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.ic.SDM;

import br.uff.ic.Prov_Viewer.Edge;
import br.uff.ic.Prov_Viewer.EdgeStroke;
import br.uff.ic.Prov_Viewer.Variables;
import java.awt.Stroke;

/**
 * Subclass of EdgeStroke
 * @author Kohwalter
 */
public class SDM_EdgeStroke extends EdgeStroke{

    /**
     * Method to define edge stroke by type
     * @param edge Edge
     * @param variables Variables type
     * @return Stroke
     */
    public static Stroke StrokeByType(Edge edge, Variables variables) {
        if ((edge.getInfluence().contains("credits")) || (edge.getInfluence().contains("Credits"))) {
            return defineStroke(edge.getValue(), ((SDM_Variables) variables).credits);
        } else if ((edge.getInfluence().contains("quality")) || (edge.getInfluence().contains("Quality"))) {
            return defineStroke(edge.getValue(), ((SDM_Variables) variables).quality);
        } else if ((edge.getInfluence().contains("aid")) || (edge.getInfluence().contains("Aid"))) {
            return defineStroke(edge.getValue(), ((SDM_Variables) variables).aid);
        } else if ((edge.getInfluence().contains("progress")) || (edge.getInfluence().contains("Progress"))) {
            return defineStroke(edge.getValue(), ((SDM_Variables) variables).progress);
        } else if ((edge.getInfluence().contains("validation")) || (edge.getInfluence().contains("Validation"))) {
            return defineStroke(edge.getValue(), ((SDM_Variables) variables).validation);
        } else if ((edge.getInfluence().contains("discovery")) || (edge.getInfluence().contains("Discovery"))) {
            return defineStroke(edge.getValue(), ((SDM_Variables) variables).discovery);
        } else if (edge.getInfluence().contains("Bug")) {
            return defineStroke(edge.getValue(), ((SDM_Variables) variables).bugs);
        } else if (edge.getInfluence().contains("Repair")) {
            return defineStroke(edge.getValue(), ((SDM_Variables) variables).repair);
        } else if ((edge.getInfluence().contains("tc")) || (edge.getInfluence().contains("TC"))) {
            return defineStroke(edge.getValue(), ((SDM_Variables) variables).tc);
        } else if (edge.getInfluence().contains("Morale")) {
            return defineStroke(edge.getValue(), ((SDM_Variables) variables).morale);
        } else if (edge.getInfluence().contains("Stamina")) {
            return defineStroke(edge.getValue(), ((SDM_Variables) variables).stamina);
        }
        //float[] dash = {5.0f};
        return EdgeStroke(edge);
    }
}
