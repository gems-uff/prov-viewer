package br.uff.ic.provviewer.Layout;

import br.uff.ic.provviewer.Variables;
import br.uff.ic.utility.graph.ActivityVertex;
import br.uff.ic.utility.graph.AgentVertex;
import br.uff.ic.utility.graph.EntityVertex;
import br.uff.ic.utility.graph.Vertex;
import edu.uci.ics.jung.graph.Graph;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Collection;
import java.util.ConcurrentModificationException;
import java.util.List;

/**
 * Temporal Layout for Prov Viewer. Based on Temporal_Layout_Template
 * @author Kohwalter
 * @param <V>
 * @param <E> 
 */
public class Temporal_Layout<V, E> extends ProvViewerLayout<V, E> {

    /**
     * Creates an instance for the specified graph.
     * @param g 
     */
    public Temporal_Layout(Graph<V, E> g, Variables variables) {
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

    private double XDISTANCE = 5.0 * this.variables.config.scale;
    private double YDISTANCE = -100.0;
    private int agentQnt = 0;
    private Graph<V,E> graph;
    
    private void doInit() {
    	graph = getGraph();
        //Starting Y position
        double ypos = -100.0;
        //X offset for Agent-type nodes
        double xOffset = 10.0;
        //Compute Agent-type node position
        
        //Sort Agent vertices to avoid changing position during graph visualization
        List sorted = new ArrayList(graph.getVertices());
            //AgentID comparator
//            Comparator comparator = new Comparator<Object>() {
//                @Override
//                public int compare(Object c1, Object c2) {
//                    if(!(c1 instanceof Graph) && !(c2 instanceof Graph))
//                        return ((Vertex)c1).getID().compareTo(((Vertex)c2).getID());
//                    else
//                        return 0;
//                }
//            };
//            //Sort nodes by ID
//            Collections.sort(sorted, comparator);
            
        agentQnt = 0;
        for(int i = 0; i < sorted.size(); i++) 
        {
            if(sorted.get(i) instanceof AgentVertex)
            {
                agentQnt ++;
            }
        }
        ypos = ypos * (int)(agentQnt * 0.5);    
        for(int i = 0; i < sorted.size(); i++) 
        {
            // If the backbone happens to be an agent, then we need to set it to y = 0 to correctly position all his activities
            if((sorted.get(i) instanceof AgentVertex) && ((Vertex)sorted.get(i)).getLabel().contains(this.variables.config.layoutSpecialVertexType))
            {
                    //I want the Backbone to always be on Y = 0
                    calcAgentPositions((V)sorted.get(i), 0, xOffset);
            }
            else if(sorted.get(i) instanceof Graph) {
                for(Object vertex : ((Graph)sorted.get(i)).getVertices())
                {
                    if(vertex instanceof AgentVertex)
                    {
                        //Change offset sign so 2 consecutive agents 
                        //dont have the same X position
                        xOffset = xOffset * -2;

                        if(Math.abs(xOffset) > 60) {
                            xOffset = 10;
                        }  
                        //Compute position for the agent
                        calcAgentPositions((V)sorted.get(i), ypos, xOffset);
                        //Update Y position for the next agent
                        ypos += 100.0;
                        //Skip position 0
                        if(ypos == -100.0) 
                        {
                            ypos += 400.0;
                        }
                    }
                } 
            }
            else if(sorted.get(i) instanceof AgentVertex)
            {
                //Change offset sign so 2 consecutive agents 
                //dont have the same X position
                xOffset = xOffset * -2;

                if(Math.abs(xOffset) > 60) {
                    xOffset = 10;
                }  
                //Compute position for the agent
                calcAgentPositions((V)sorted.get(i), ypos, xOffset);
                //Update Y position for the next agent
                ypos += 100.0;
                //Skip position 0
                if(ypos == -100.0) 
                {
                    ypos += 400.0;
                }
            }
        }
        //Compute position for all node-types (minus Agent)
        for(V v2 : graph.getVertices()) 
        {
            calcPositions(v2);
        }
        //Check if there are nodes at the same place, if so apply repulsion
        for(V v3 : graph.getVertices()) 
        {
            calcRepulsion(v3);
        }
    }
    /**
     * 
     * @param v
     * @param ypos
     * @param xOffset 
     */
    protected synchronized void calcAgentPositions(V v, double ypos, double xOffset)
    {
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
        
        if(v instanceof Vertex)
        {
            //Node's X position is defined by the day it was created
            newXPos = Math.round(((Vertex)v).getTime()) * XDISTANCE;
            //If node is from the backbone type
            if((v instanceof Vertex) && ((Vertex)v).getLabel().contains(this.variables.config.layoutSpecialVertexType))
            {
                    //I want the backbone-type node to always be on Y = 0
                    xyd.setLocation(newXPos + XDISTANCE * 0.2, 0);
            }

            //If node is a ArtifactNode-type
            else if(v instanceof EntityVertex)
            {
                xyd.setLocation(newXPos, 200);
            }
            //If node is a ProcessNode-type
//            else if(v instanceof ActivityVertex)
            else
            {
                //The XY position for this type of node is dependable of the
                //agent who executed the process
                //Get edges from node v
                Collection<E> edges = graph.getOutEdges(v);
                for (E edge : edges)
                {
                    //if the edge link to an Agent-node
                    if(graph.getDest(edge) instanceof AgentVertex)
                    {
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
        }
        else if (v instanceof Graph)
        {
            newXPos = xyd.getX();
            newYPos = xyd.getY();
//            List v_list = new ArrayList(((Graph) v).getVertices());
//            for (Object vnext : v_list) {
//                if (!(v instanceof Graph))
//                    newXPos += Math.round(((Vertex)vnext).getDate()) * XDISTANCE;
////                newYPos += transform((V)vnext).getY();
//            }
//            newXPos = newXPos / v_list.size();
//            newYPos = transform((V)v_list.get(0)).getY();
//            Collection<E> edges = graph.getOutEdges(v);
//            for (E edge : edges)
//            {
//                //if the edge link to an Agent-node
//                if(graph.getDest(edge) instanceof AgentVertex)
//                {
//                    //Compute position according to the agent position
//                    Point2D agentPos = transform(graph.getDest(edge));
//                    //Adding an offset to not be in the same line
//                    newYPos = agentPos.getY() + 50;
//                }
//            }
            xyd.setLocation(newXPos, newYPos);
        }
    }
    
    double variation = 30;
    
    /**
     * Check if 2 nodes are at the same position, if so add an offset
     * @param v1 
     */
    protected synchronized void calcRepulsion(V v1) {
        //Only Process and Artifact types can have the same position, so lets check
        if((v1 instanceof ActivityVertex) || ((v1 instanceof EntityVertex) && !((Vertex)v1).getLabel().contains(this.variables.config.layoutSpecialVertexType)))
        {
            try {
                for(V v2 : graph.getVertices()) 
                {
                    if((v2 instanceof ActivityVertex)||((v2 instanceof EntityVertex) && !((Vertex)v2).getLabel().contains(this.variables.config.layoutSpecialVertexType)))
                    {
                        //A check to see if we are not comparing him with himself
                        if(v1 != v2)
                        {
                            Point2D p1 = transform(v1);
                            Point2D p2 = transform(v2);
                            if(p1 == null || p2 == null) {
                                continue;
                            }
                            //Need to check both X and Y positions, so it is from the same employee
                            if(Equals(p1.getX(), p2.getX()) && Equals(p1.getY(), p2.getY()))
                            {
                                p1.setLocation(p1.getX(), p1.getY() - variation);
                                p2.setLocation(p2.getX(), p2.getY() + variation);
                                //Need to check again in case another node is at the same new position
                                calcRepulsion(v1);
                            }
                        }
                    }
                }
            } catch(ConcurrentModificationException cme) {
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
    protected boolean Equals(double a, double b)
    {
        return Math.abs(a - b) < EPSILON;
    }

//    protected MyLayout.FRVertexData getFRData(V v) {
//        return frVertexData.get(v);
//    }

    /**
     * This one is an incremental visualization.
     * @return 
     */
    public boolean isIncremental() {
        return true;
    }

    /**
     * Returns true once the current iteration has passed the maximum count,
     * <tt>MAX_ITERATIONS</tt>.
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
