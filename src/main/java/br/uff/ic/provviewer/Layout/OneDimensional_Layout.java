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

import br.uff.ic.provviewer.VariableNames;
import br.uff.ic.provviewer.Variables;
import br.uff.ic.utility.Utils;
import br.uff.ic.utility.graph.ActivityVertex;
import br.uff.ic.utility.graph.AgentVertex;
import br.uff.ic.utility.graph.EntityVertex;
import br.uff.ic.utility.graph.Vertex;
import edu.uci.ics.jung.graph.Graph;
import java.awt.geom.Point2D;
import java.util.ConcurrentModificationException;

/**
 * Layout to display vertices in a timeline fashion
 * Each agent will have its own line
 * The X displacement is based on an attribute value
 * Vertices are ordered by their Attribute's value
 *
 * @author Kohwalter
 * @param <V> JUNG's V (Vertex) type
 * @param <E> JUNG's E (Edge) type
 */
public class OneDimensional_Layout<V, E> extends ProvViewerTimelineLayout<V, E> {
    double variation = scale * 0.5;
    private double EPSILON = 0.000001D;

    public OneDimensional_Layout(Graph<V, E> g, Variables variables) {
        super(g, variables);
    }

    @Override
    public void reset() {
        doInit();
    }

    @Override
    public void initialize() {
        doInit();
    }

    /**
     * Initialize layout
     */
    private void doInit() {

        x_att = Utils.removeMinusSign(x_att);
        y_att = Utils.removeMinusSign(y_att);
        boolean isReverse_X = Utils.getMinusSign(x_att);

        setAllVertexOrder(Utils.getVertexAttributeComparator(x_att), Utils.getVertexAttributeComparator(y_att));
        V previous = vertex_ordered_list.get(0);
        int i = 0;
        int agentY = 0;
        double yPos = 0;
        double xPos = 0;
        int yGraphOffset = 0;
        scale = (int) (9 * variables.config.vertexSize);
        for (V v : vertex_ordered_list) {
            yPos = 0;
            int attValue;
            Point2D coord = transform(v);
            yGraphOffset = getYGraphOffSet(v);
            
            if(Utils.isItTime(x_att)) {
                attValue = (int) ((Vertex) v).getNormalizedTime();
                attValue = (int) Utils.convertTime(variables.config.timeScale, attValue, variables.selectedTimeScale) * scale;
            } else {
                attValue = (int) ((Vertex) v).getAttributeValueFloat(x_att);
            }

            if (v instanceof AgentVertex || ((Vertex)v).hasAttribute(VariableNames.CollapsedVertexAgentAttribute)) {
                yPos = agentY;
                agentY = agentY + scale;
                i = attValue + scale;
                xPos = i;
            } else if (v instanceof ActivityVertex) {
                if (layout_graph.getOutEdges(v) != null) {
                    yPos = findAgent(v, yPos);
                    i = attValue + scale;
                    xPos = i;
                }
            } else if (v instanceof EntityVertex) {
                if (layout_graph.getOutEdges(v) != null) {
                    for (E neighbor : layout_graph.getOutEdges(v)) {
                        if (layout_graph.getDest(neighbor) instanceof ActivityVertex || ((Vertex)layout_graph.getDest(neighbor)).hasAttribute(VariableNames.CollapsedVertexActivityAttribute)) {
                            Point2D activityPos = transform(layout_graph.getDest(neighbor));
                            yPos = activityPos.getY();
                        }
                        if((previous instanceof ActivityVertex) || (previous instanceof AgentVertex)) {
                            i = i + scale;
                            xPos = i;
                        }
                    }
                }
            } else {
                System.out.println("Not found");
                yPos = 0;
                i = attValue + scale;
                xPos = i;
            }
            yPos = yPos + yGraphOffset;
            if (isReverse_X) {
                xPos *= -1;
            }
            coord.setLocation(xPos, yPos);
            previous = v;
        }
        for (V v3 : graph.getVertices()) {
            calcRepulsion(v3);
        }
    }

    protected synchronized void calcRepulsion(V v1) {
        //Only Process and Artifact types can have the same position, so lets check
        if ((v1 instanceof ActivityVertex) || ((v1 instanceof EntityVertex) && !((Vertex) v1).getLabel().contains(this.variables.config.layoutSpecialVertexType))) {
            try {
                for (V v2 : graph.getVertices()) {
                    if ((v2 instanceof ActivityVertex) || ((v2 instanceof EntityVertex) && !((Vertex) v2).getLabel().contains(this.variables.config.layoutSpecialVertexType))) {
                        //A check to see if we are not comparing him with himself
                        if (v1 != v2) {
                            Point2D p1 = transform(v1);
                            Point2D p2 = transform(v2);
                            if (p1 == null || p2 == null) {
                                continue;
                            }
                            //Need to check both X and Y positions, so it is from the same employee
                            if (Equals(p1.getX(), p2.getX()) && Equals(p1.getY(), p2.getY())) {
                                p1.setLocation(p1.getX(), p1.getY() - variation);
                                p2.setLocation(p2.getX(), p2.getY() + variation);
                                //Need to check again in case another node is at the same new position
                                calcRepulsion(v1);
                            }
                        }
                    }
                }
            } catch (ConcurrentModificationException cme) {
//                calcRepulsion(v1);
            }
        }
    }
    
    private double findAgent(V v, double y) {
        double yPos = -1;
        if (layout_graph.getOutEdges(v) != null) {
            for (E neighbor : layout_graph.getOutEdges(v)) {
                //if the edge link to an Agent-node
                if (layout_graph.getDest(neighbor) instanceof AgentVertex || ((Vertex)layout_graph.getDest(neighbor)).hasAttribute(VariableNames.CollapsedVertexAgentAttribute)) {
                    Point2D agentPos = transform(layout_graph.getDest(neighbor));
                    return agentPos.getY();
                }
                else {
                    return findAgent(layout_graph.getDest(neighbor), yPos);
                }
            }
        }
        return y;
    }
    private double findActivity(V v, double y) {
        double yPos = -1;
        if (layout_graph.getOutEdges(v) != null) {
            for (E neighbor : layout_graph.getOutEdges(v)) {
                //if the edge link to an Agent-node
                if (layout_graph.getDest(neighbor) instanceof ActivityVertex || ((Vertex)layout_graph.getDest(neighbor)).hasAttribute(VariableNames.CollapsedVertexActivityAttribute)) {
                    Point2D actPos = transform(layout_graph.getDest(neighbor));
                    return actPos.getY();
                }
                else {
                    return findActivity(layout_graph.getDest(neighbor), yPos);
                }
            }
        }
        return y;
    }
    protected boolean Equals(double a, double b) {
        return Math.abs(a - b) < EPSILON;
    }
    /**
     * This one is an incremental visualization.
     *
     * @return true
     */
    public boolean isIncremental() {
        return true;
    }

    /**
     * Returns true once the current iteration has passed the maximum count,
     * <tt>MAX_ITERATIONS</tt>.
     *
     * @return true
     */
    @Override
    public boolean done() {
//        if (currentIteration > mMaxIterations || temperature < 1.0/max_dimension)
//        {
//            return true;
//        }
//        return false;
        return true;
    }

    @Override
    public void step() {
//        throw new UnsupportedOperationException("Not supported yet.");
    }
}
