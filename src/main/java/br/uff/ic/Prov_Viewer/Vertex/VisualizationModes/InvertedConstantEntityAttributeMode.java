/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.ic.Prov_Viewer.Vertex.VisualizationModes;

import br.uff.ic.Prov_Viewer.Edge.Edge;
import br.uff.ic.Prov_Viewer.Variables;
import br.uff.ic.Prov_Viewer.Vertex.EntityVertex;
import br.uff.ic.Prov_Viewer.Vertex.Vertex;
import edu.uci.ics.jung.graph.DirectedGraph;
import java.awt.Color;
import java.awt.Paint;
import java.util.Collection;

/**
 *
 * @author Kohwalter
 */
public class InvertedConstantEntityAttributeMode extends VertexPaintMode {

    float[] entityValue = new float[]{0, 0};

    public InvertedConstantEntityAttributeMode(String attribute) {
        super(attribute, 0.7, 0.4);
    }

    public InvertedConstantEntityAttributeMode(String attribute, double g, double y) {
        super(attribute, g, y);
    }
    
    public InvertedConstantEntityAttributeMode(String attribute, String empty, double g, double y) {
        super(attribute, g, y);
    }

    @Override
    public Paint Execute(Object v, final Variables variables) {

        ComputeValue(variables.graph);
        if (v instanceof EntityVertex) {
            return this.CompareValue(((EntityVertex) v).getAttributeValueInteger(this.attribute), this.entityValue[0]);
        }
        return ((Vertex) v).getColor();
    }

    @Override
    public Paint CompareValue(int value, float constant) {
        if (value < (constant * this.valueGreenThreshold)) {
            return new Color(0, 255, 0);
        } else {
            if (value < (constant * this.valueYellowThreshold)) {
                return new Color(255, 255, 0);
            } else if (value < (constant * this.valueYellowThreshold * 0.5)) {
                return new Color(140, 23, 23);
            } else {
                return new Color(255, 0, 0);
            }
        }
    }

    public void ComputeValue(DirectedGraph<Object, Edge> graph) {
        Collection<Object> nodes = graph.getVertices();
        for (Object node : nodes) {
            if (node instanceof EntityVertex) {
                entityValue[0] = Math.max(entityValue[0], Math.abs(((EntityVertex) node).getAttributeValueInteger(this.attribute)));
            }
        }
    }
}
