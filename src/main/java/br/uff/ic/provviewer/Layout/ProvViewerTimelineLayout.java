/*
 * The MIT License
 *
 * Copyright 2017 Kohwalter.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package br.uff.ic.provviewer.Layout;

import br.uff.ic.provviewer.Variables;
import br.uff.ic.utility.graph.Vertex;
import edu.uci.ics.jung.graph.Graph;
import java.awt.geom.Point2D;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Kohwalter
 */
public abstract class ProvViewerTimelineLayout<V, E> extends ProvViewerLayout<V, E> {
    public String x_att;
    public String y_att;
    public int scale;
    
    public ProvViewerTimelineLayout(Graph<V, E> g, Variables variables) {
        super(g, variables);
        x_att = variables.layout_attribute_X;
        y_att = variables.layout_attribute_Y;
        scale = 4 * variables.config.vertexSize;
    }
    
       
    
    public void positionEntitiesTimeline(int entityXPos) {
        double yPos = 0;
        double xPos = 0;
        for(V v : entity_ordered_list) {
            Point2D coord = transform(v);
            // Position then in the middle
            xPos = entityXPos;
            entityXPos = entityXPos + scale;
            yPos = -10 * scale;
            coord.setLocation(xPos, yPos);
        }
    }
    
    public void positionEntitiesTimelineGraphs(int entityXPos) {
        int yGraphOffset = 0;
        double yPos = 0;
        double xPos = 0;
        int previous_Value = 0;
        int previous_yOffset = 0;
        for (V v : entity_ordered_list) {
            Point2D coord = transform(v);
            int k = 0;
            for (String g : variables.graphNames) {
                if (((Vertex) v).getAttributeValue("GraphFile").contains(g)) {
                    yGraphOffset = (int) (variables.config.vertexSize * k);
                    break;
                }
                k++;
            }
            if (previous_Value != (int) ((Vertex) v).getAttributeValueDouble(x_att)) {
                entityXPos = entityXPos + scale;
            } else if (previous_yOffset == yGraphOffset) {
                entityXPos = (int) (entityXPos + (scale * 0.25));
            }
            xPos = entityXPos;
                        
            yPos = -10 * scale + yGraphOffset;
            coord.setLocation(xPos, yPos);
        }
    }
    
    public int getYGraphOffSet(V v) {
        int k = 0;
        int yGraphOffset = 0;
        for (String g : variables.graphNames) {
            if (((Vertex) v).getAttributeValue("GraphFile").contains(g)) {
                yGraphOffset = (int) (variables.config.vertexSize * k);
                break;
            }
            k++;
        }
        
        return yGraphOffset;
    }
    
    public double findNeighbor(V v) {
        double y = 0;
        Map<String, V> neighbors = new HashMap<>();
        if (layout_graph.getOutEdges(v).size() > 0) {
            for (E neighbor : layout_graph.getOutEdges(v)) {
                Point2D vNeighbor = transform(layout_graph.getDest(neighbor));
                if(!neighbors.containsKey(((Vertex)layout_graph.getDest(neighbor)).getID())) {
                    y += vNeighbor.getY();
                    neighbors.put(((Vertex)layout_graph.getDest(neighbor)).getID(), layout_graph.getDest(neighbor));
                }
            }
            y /= neighbors.size();
        }
        
        return y;
    }
    
}
