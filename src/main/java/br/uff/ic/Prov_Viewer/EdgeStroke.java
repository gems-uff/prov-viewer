/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.ic.Prov_Viewer;

import java.awt.BasicStroke;
import java.awt.Stroke;

/**
 * Class for setting the edge's width (labeled as stroke)
 * @author Kohwalter
 */
public class EdgeStroke {
   
    /**
     * Defines neutral edges to have strokes (dash) of 5.0f
     * Dashes might reduce performance
     * @param edge The edge to be analyzed
     * @return Stroke (neutral edges are dashed, others are not)
     */
    public static Stroke EdgeStroke(Edge edge)
    {
        float[] dash = {5.0f};
        
        if(edge.isNeutral()) 
        {
            return new BasicStroke(1.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10.0f, dash, 0.0f);
        }
        return new BasicStroke(1.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10.0f, null, 0.0f);
    }
    
    /**
     * Allows to manually define (any) edge's stroke
     * @param edge Edge to have the stroke changed
     * @param dash The array representing the dashing pattern
     * @return Stroke
     */
    public static Stroke EdgeStroke(Edge edge, float[] dash)
    {
        return new BasicStroke(1.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10.0f, dash, 0.0f);
    }
    
    /**
     * Method to define edge's width according to its value
     * @param value Edge's value
     * @param average Edge's type average value
     * @return (Stroke) Edge's width proportional to all edges of the same type.
     */
    public static BasicStroke defineStroke(float value, float average)
    {
        float size;
        size = (float) ( 5 * (Math.abs(value) / average)) + 1;
        size = Math.min(6, size);
        if(Float.isNaN(size)) {
            size = 1;
        }
        return new BasicStroke(size, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10.0f, null, 0.0f);
    }
    
    /**
     * Method to define edge stroke by type
     * @param edge Edge
     * @param variables Variables type
     * @return Stroke
     */
    public static Stroke StrokeByType(Edge edge, Variables variables) {
        if (edge.getInfluence().contains(GraphFrame.FilterEdgeCreditsButton.getText())) {
            return defineStroke(edge.getValue(), ((Variables) variables).credits);
        } else if (edge.getInfluence().contains(GraphFrame.FilterEdgeQualityButton.getText())) {
            return defineStroke(edge.getValue(), ((Variables) variables).quality);
        } else if (edge.getInfluence().contains(GraphFrame.FilterEdgeAidButton.getText())) {
            return defineStroke(edge.getValue(), ((Variables) variables).aid);
        } else if (edge.getInfluence().contains(GraphFrame.FilterEdgeProgressButton.getText())) {
            return defineStroke(edge.getValue(), ((Variables) variables).progress);
        } else if (edge.getInfluence().contains(GraphFrame.FilterEdgeValButton.getText())) {
            return defineStroke(edge.getValue(), ((Variables) variables).validation);
        } else if (edge.getInfluence().contains(GraphFrame.FilterEdgeDiscoveryButton.getText())) {
            return defineStroke(edge.getValue(), ((Variables) variables).discovery);
        } else if (edge.getInfluence().contains(GraphFrame.FilterEdgeBugsButton.getText())) {
            return defineStroke(edge.getValue(), ((Variables) variables).bugs);
        } else if (edge.getInfluence().contains(GraphFrame.FilterEdgeRepairButton.getText())) {
            return defineStroke(edge.getValue(), ((Variables) variables).repair);
        } else if (edge.getInfluence().contains(GraphFrame.FilterEdgeTCButton.getText())) {
            return defineStroke(edge.getValue(), ((Variables) variables).tc);
        } else if (edge.getInfluence().contains(GraphFrame.FilterEdgeMoraleButton.getText())) {
            return defineStroke(edge.getValue(), ((Variables) variables).morale);
        } else if (edge.getInfluence().contains(GraphFrame.FilterEdgeStaminaButton.getText())) {
            return defineStroke(edge.getValue(), ((Variables) variables).stamina);
        } else if (edge.getInfluence().contains(GraphFrame.FilterEdgePrototypeButton.getText())) {
            return defineStroke(edge.getValue(), ((Variables) variables).prototype);
        } else if (edge.getInfluence().contains(GraphFrame.FilterEdgeNegotiationButton.getText())) {
            return defineStroke(edge.getValue(), ((Variables) variables).negotiation);
        }
        //float[] dash = {5.0f};
        return EdgeStroke(edge);
    }
    
}
