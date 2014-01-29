/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.ic.Prov_Viewer.Stroke;

import br.uff.ic.Prov_Viewer.Input.Config;
import br.uff.ic.Prov_Viewer.Edge.Edge;
import br.uff.ic.Prov_Viewer.Variables;
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
        for(int i = 0; i < Config.length; i++)
        {
            if (edge.getInfluence().contains(Config.filterList[i])) {
                return defineStroke(edge.getValue(), ((Variables) variables).edgeArray[i].value[0]);
            }
        }
        
//        if (edge.getInfluence().contains(GraphFrame.FilterEdge01.getText())) {
//            return defineStroke(edge.getValue(), ((Variables) variables).edge01[0]);
//        } else if (edge.getInfluence().contains(GraphFrame.FilterEdge02.getText())) {
//            return defineStroke(edge.getValue(), ((Variables) variables).edge02[0]);
//        } else if (edge.getInfluence().contains(GraphFrame.FilterEdge04.getText())) {
//            return defineStroke(edge.getValue(), ((Variables) variables).edge03[0]);
//        } else if (edge.getInfluence().contains(GraphFrame.FilterEdge03.getText())) {
//            return defineStroke(edge.getValue(), ((Variables) variables).edge04[0]);
//        } else if (edge.getInfluence().contains(GraphFrame.FilterEdge05.getText())) {
//            return defineStroke(edge.getValue(), ((Variables) variables).edge05[0]);
//        } else if (edge.getInfluence().contains(GraphFrame.FilterEdge06.getText())) {
//            return defineStroke(edge.getValue(), ((Variables) variables).edge06[0]);
//        } else if (edge.getInfluence().contains(GraphFrame.FilterEdge08.getText())) {
//            return defineStroke(edge.getValue(), ((Variables) variables).edge08[0]);
//        } else if (edge.getInfluence().contains(GraphFrame.FilterEdge07.getText())) {
//            return defineStroke(edge.getValue(), ((Variables) variables).edge07[0]);
//        } else if (edge.getInfluence().contains(GraphFrame.FilterEdge09.getText())) {
//            return defineStroke(edge.getValue(), ((Variables) variables).edge09[0]);
//        } else if (edge.getInfluence().contains(GraphFrame.FilterEdge10.getText())) {
//            return defineStroke(edge.getValue(), ((Variables) variables).edge10[0]);
//        } else if (edge.getInfluence().contains(GraphFrame.FilterEdge11.getText())) {
//            return defineStroke(edge.getValue(), ((Variables) variables).edge11[0]);
//        } else if (edge.getInfluence().contains(GraphFrame.FilterEdge12.getText())) {
//            return defineStroke(edge.getValue(), ((Variables) variables).edge12[0]);
//        } else if (edge.getInfluence().contains(GraphFrame.FilterEdge13.getText())) {
//            return defineStroke(edge.getValue(), ((Variables) variables).edge13[0]);
//        }
        //float[] dash = {5.0f};
        return EdgeStroke(edge);        
    }
    
}
