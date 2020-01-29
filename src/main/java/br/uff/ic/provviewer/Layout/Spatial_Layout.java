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
import br.uff.ic.utility.GoogleMapsAPIProjection;
import br.uff.ic.utility.graph.ActivityVertex;
import br.uff.ic.utility.graph.EntityVertex;
import br.uff.ic.utility.graph.Vertex;
import edu.uci.ics.jung.graph.Graph;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.ConcurrentModificationException;
import java.util.List;

/**
 * Template for a temporal graph layout. Lines represent each agent and his
 * activities. Columns represent passage of time
 *
 * @author Kohwalter
 * @param <V> JUNG's V (Vertex) type
 * @param <E> JUNG's E (Edge) type
 */
public class Spatial_Layout<V, E> extends ProvViewerLayout<V, E> {

    public Spatial_Layout(Graph<V, E> g, Variables variables) {
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

//    private Graph<V, E> layout_graph;

    /**
     * Initialize layout
     */
    private void doInit() {
//        layout_graph = getGraph();
        //Compute position for all node-types (minus Agent)
        for (V v2 : getGraph().getVertices()) {
            calcPositions(v2);
        }
        //Check if there are nodes at the same place, if so apply repulsion
        /*
         for(V v3 : layout_graph.getVertices()) 
         {
         calcRepulsion(v3);
         }
         */
    }

    /**
     * Calculate each entity and activity vertex position in the layout
     *
     * @param v Activity or entity vertex
     */
    protected synchronized void calcPositions(V v) {
        Point2D xyd = transform(v);
        double newXPos = 0;
        double newYPos = 0;

        // Use the middle vertex atribute for position
        if (v instanceof Graph) {
            int i = ((Graph) v).getVertexCount();
           
            //Sort vertices by ID
            List sorted = new ArrayList(((Graph) v).getVertices());
            Comparator comparator = new Comparator<Object>() {
                @Override
                public int compare(Object c1, Object c2) {
                    if(!(c1 instanceof Graph) && !(c2 instanceof Graph))
                        return ((Vertex)c1).getID().compareTo(((Vertex)c2).getID());
                    else
                        return 0;
                }
            };
            Collections.sort(sorted, comparator);
//             End sorting;
            Vertex middle;
            Object middleVertex = sorted.toArray()[(int) (i * 0.5)];
            while (middleVertex instanceof Graph) {
                middleVertex = ((Graph)middleVertex).getVertices().toArray()[0];
            }
            middle = (Vertex) middleVertex;
            if(variables.config.orthogonal)
                calcPositionsPixel(xyd, (Vertex) middle);
            else
                calcPositionsLatLon(xyd, (Vertex) middle);
        }

        // Use vertex atribute for position
        if (v instanceof Vertex) {
            if(variables.config.orthogonal)
                calcPositionsPixel(xyd, (Vertex) v);
            else
                calcPositionsLatLon(xyd, (Vertex) v);
        }
    }
    
    protected synchronized void calcPositionsPixel(Point2D xyd, Vertex v) {
        double newXPos = -((Vertex) v).getAttributeValueDouble(variables.config.layoutAxis_X) * variables.config.coordinatesScale;
        double newYPos = ((Vertex) v).getAttributeValueDouble(variables.config.layoutAxis_Y) * variables.config.coordinatesScale;
        xyd.setLocation(newXPos, newYPos);
    }
    
    protected synchronized void calcPositionsLatLon(Point2D xyd, Vertex v) {
        double latitude    = v.getAttributeValueDouble(variables.config.layoutAxis_Y); // (φ)
        double longitude   = v.getAttributeValueDouble(variables.config.layoutAxis_X);   // (λ)

        double mapWidth    = variables.config.width;
        double mapHeight   = variables.config.height;

        // get x value
        double x = (longitude + 180) * (mapWidth / 360);

        // convert from degrees to radians
        double latRad = latitude * Math.PI / 180;

        // get y value
        double mercN = Math.log(Math.tan((Math.PI / 4) + (latRad / 2)));
        double y     = (mapHeight / 2) - (mapWidth * mercN / (2 * Math.PI));
        
        GoogleMapsAPIProjection googleAPI = new GoogleMapsAPIProjection(variables.config.googleZoomLevel);
        Point2D coord = googleAPI.FromCoordinatesToPixel((float)v.getAttributeValueDouble(variables.config.layoutAxis_X), (float)v.getAttributeValueDouble(variables.config.layoutAxis_Y));
        xyd.setLocation(coord.getX(), coord.getY());
    }

    double variation = 1.0;

    //Check if 2 nodes are at the same position, if so add an offset
    /**
     * Method to check if there is any other vertex at the same position of this
     * one (x,y)
     *
     * @param v1 Vertex used to check if there is any other vertex at the same
     * position
     */
    protected synchronized void calcRepulsion(V v1) {
        //Only Process and Artifact types can have the same position, so lets check
        if ((v1 instanceof ActivityVertex) || (v1 instanceof EntityVertex)) {
            try {
                for (V v2 : getGraph().getVertices()) {
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
                            //p2.setLocation(p2.getX(), p2.getY() + variation);
                            //Need to check again in case another node is at the same new position
                            calcRepulsion(v1);
                        }
                    }
                }
            } catch (ConcurrentModificationException cme) {
            }
        }
    }

    private double EPSILON = 1.0;

    /**
     * Check if a and b are equals
     *
     * @param a double
     * @param b double
     * @return if both values are equal
     */
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
