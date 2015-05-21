package br.uff.ic.provviewer.Layout;

import br.uff.ic.provviewer.Input.Config;
import br.uff.ic.provviewer.Vertex.ActivityVertex;
import br.uff.ic.provviewer.Vertex.EntityVertex;
import br.uff.ic.provviewer.Vertex.Vertex;
import edu.uci.ics.jung.algorithms.layout.AbstractLayout;
import edu.uci.ics.jung.algorithms.util.IterativeContext;
import edu.uci.ics.jung.graph.Graph;
import java.awt.geom.Point2D;
import java.util.ConcurrentModificationException;

/**
 * Template for a temporal graph layout. Lines represent each agent and his
 * activities. Columns represent passage of time
 *
 * @author Kohwalter
 * @param <V> JUNG's V (Vertex) type
 * @param <E> JUNG's E (Edge) type
 */
public class Spatial_Layout<V, E> extends AbstractLayout<V, E> implements IterativeContext {

    public Spatial_Layout(Graph<V, E> g) {
        super(g);
    }

    @Override
    public void reset() {
        doInit();
    }

    @Override
    public void initialize() {
        doInit();
    }

    private Graph<V, E> layout_graph;

    /**
     * Initialize layout
     */
    private void doInit() {
        layout_graph = getGraph();
        //Compute position for all node-types (minus Agent)
        for (V v2 : layout_graph.getVertices()) {
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
            Vertex middle = (Vertex) (((Graph) v).getVertices().toArray())[(int) (i * 0.5)];
            newXPos = -middle.getAttributeValueFloat(Config.layoutAxis_X) * Config.coordinatesScale;
            newYPos = middle.getAttributeValueFloat(Config.layoutAxis_Y) * Config.coordinatesScale;
            xyd.setLocation(newXPos, newYPos);
        }

        // Use vertex atribute for position
        if (v instanceof Vertex) {
            newXPos = -((Vertex) v).getAttributeValueFloat(Config.layoutAxis_X) * Config.coordinatesScale;
            newYPos = ((Vertex) v).getAttributeValueFloat(Config.layoutAxis_Y) * Config.coordinatesScale;
            xyd.setLocation(newXPos, newYPos);
        }

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
                for (V v2 : layout_graph.getVertices()) {
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
