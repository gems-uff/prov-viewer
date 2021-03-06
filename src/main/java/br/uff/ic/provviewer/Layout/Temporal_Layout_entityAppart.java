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
import br.uff.ic.utility.Utils;
import br.uff.ic.utility.graph.ActivityVertex;
import br.uff.ic.utility.graph.AgentVertex;
import br.uff.ic.utility.graph.EntityVertex;
import br.uff.ic.utility.graph.Vertex;
import edu.uci.ics.jung.graph.Graph;
import java.awt.geom.Point2D;
import java.util.Collection;
import java.util.ConcurrentModificationException;

/**
 * Temporal Layout for Prov Viewer. Based on Temporal_Layout_Template
 *
 * @author Kohwalter
 * @param <V>
 * @param <E>
 */
public class Temporal_Layout_entityAppart<V, E> extends ProvViewerLayout<V, E> {

    /**
     * Creates an instance for the specified graph.
     *
     * @param g
     */
    public Temporal_Layout_entityAppart(Graph<V, E> g, Variables variables) {
        super(g, variables);
//        initialize();
    }

    @Override
    public void reset() {
        doInit();
    }

    @Override
    public void initialize() {
        doInit();
    }

    private double XDISTANCE = 50.0 * this.variables.config.scale;
    private double YDISTANCE = -100.0;
    private int agentQnt = 0;
    private Graph<V, E> graph;

    private void doInit() {
        graph = getGraph();
        //Starting Y position
        double ypos = 100.0;
        //X offset for Agent-type nodes
        double xOffset = 10.0;
        //Compute Agent-type node position
        agentQnt = 0;
        for (V v : graph.getVertices()) {
            if (v instanceof AgentVertex) {
                agentQnt++;
            }
        }
        ypos = ypos * (int) (agentQnt * 0.5);
        for (V v1 : graph.getVertices()) {
            // If the backbone happens to be an agent, then we need to set it to y = 0 to correctly position all his activities
            if ((v1 instanceof AgentVertex) && ((Vertex) v1).getLabel().contains(this.variables.config.layoutSpecialVertexType)) {
                //I want the Backbone to always be on Y = 0
                calcAgentPositions((V) v1, 0, xOffset);
            } else if (v1 instanceof Graph) {
                for (Object vertex : ((Graph) v1).getVertices()) {
                    if (vertex instanceof AgentVertex) {
                        //Change offset sign so 2 consecutive agents 
                        //dont have the same X position
                        xOffset = xOffset * -2;

                        if (Math.abs(xOffset) > 60) {
                            xOffset = 10;
                        }
                        //Compute position for the agent
                        calcAgentPositions((V) v1, ypos, xOffset);
                        //Update Y position for the next agent
                        ypos += 100.0;
                        //Skip position 0
                        if (ypos == -100.0) {
                            ypos += 400.0;
                        }
                    }
                }
            } else if (v1 instanceof AgentVertex) {
                //Change offset sign so 2 consecutive agents 
                //dont have the same X position
                xOffset = xOffset * -2;

                if (Math.abs(xOffset) > 60) {
                    xOffset = 10;
                }
                //Compute position for the agent
                calcAgentPositions((V) v1, ypos, xOffset);
                //Update Y position for the next agent
                ypos += 100.0;
                //Skip position 0
                if (ypos == -100.0) {
                    ypos += 400.0;
                }
            }
        }
        //Compute position for all node-types (minus Agent)
        for (V v2 : graph.getVertices()) {
            calcPositions(v2);
        }
        //Check if there are nodes at the same place, if so apply repulsion
        for (V v3 : graph.getVertices()) {
            calcRepulsion(v3);
        }
    }

    /**
     *
     * @param v
     * @param ypos
     * @param xOffset
     */
    protected synchronized void calcAgentPositions(V v, double ypos, double xOffset) {
        Point2D xyd = transform(v);
        xyd.setLocation(-XDISTANCE + xOffset, ypos);
    }

    /**
     *
     * @param v
     */
    protected synchronized void calcPositions(V v) {

        Point2D xyd = transform(v);

        double newXPos;
        double newYPos;

        if (v instanceof Vertex) {
            //Node's X position is defined by the day it was created
            double time = ((Vertex) v).getNormalizedTime();
            time = Utils.convertTime(variables.config.timeScale, time, variables.selectedTimeScale);
            
            newXPos = Math.round(time) * XDISTANCE;
            //If node is from the backbone type
            if ((v instanceof Vertex) && ((Vertex) v).getLabel().contains(this.variables.config.layoutSpecialVertexType)) {
                //I want the backbone-type node to always be on Y = 0
                xyd.setLocation(newXPos + XDISTANCE * 0.2, 0);
            } //If node is a ArtifactNode-type
            else if (v instanceof EntityVertex) {
                xyd.setLocation(newXPos, 200);
            } //If node is a ProcessNode-type
            //            else if(v instanceof ActivityVertex)
            else if (v instanceof ActivityVertex){
                //The XY position for this type of node is dependable of the
                //agent who executed the process
                //Get edges from node v
                Collection<E> edges = graph.getOutEdges(v);
                for (E edge : edges) {
                    //if the edge link to an Agent-node
                    if (graph.getDest(edge) instanceof AgentVertex) {
                        //Compute position according to the agent position
                        Point2D agentPos = transform(graph.getDest(edge));
                        //Adding an offset to not be in the same line
                        newYPos = agentPos.getY() + 50;
                        //Compute X from the Agent position, removing the -XDISTANCE
                        //to start at x=0, instead of x= -XDISTANCE position
                        //newXPos = agentPos.getX() + XDISTANCE + newXPos;
                        xyd.setLocation(newXPos, newYPos);
                    }
                }
            }
        } else if (v instanceof Graph) {
            newXPos = xyd.getX();
            newYPos = xyd.getY();
            xyd.setLocation(newXPos, newYPos);
        }
    }

    double variation = 30;

    /**
     * Check if 2 nodes are at the same position, if so add an offset
     *
     * @param v1
     */
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

    private double EPSILON = 0.000001D;

    /**
     *
     * @param a
     * @param b
     * @return
     */
    protected boolean Equals(double a, double b) {
        return Math.abs(a - b) < EPSILON;
    }

//    protected MyLayout.FRVertexData getFRData(V v) {
//        return frVertexData.get(v);
//    }
    /**
     * This one is an incremental visualization.
     *
     * @return
     */
    public boolean isIncremental() {
        return true;
    }

    /**
     * Returns true once the current iteration has passed the maximum count,
     * <tt>MAX_ITERATIONS</tt>.
     *
     * @return
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
//
//    protected static class FRVertexData extends Point2D.Double
//    {
//        protected void offset(double x, double y)
//        {
//            this.x += x;
//            this.y += y;
//        }
//
//        protected double norm()
//        {
//            return Math.sqrt(x*x + y*y);
//        }
//     }
}
