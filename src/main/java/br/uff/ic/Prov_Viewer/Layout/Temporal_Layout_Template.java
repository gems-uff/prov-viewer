package br.uff.ic.Prov_Viewer.Layout;

import br.uff.ic.Prov_Viewer.Vertex.ActivityVertex;
import br.uff.ic.Prov_Viewer.Vertex.AgentVertex;
import br.uff.ic.Prov_Viewer.Vertex.EntityVertex;
import br.uff.ic.Prov_Viewer.Vertex.Vertex;
import edu.uci.ics.jung.algorithms.layout.AbstractLayout;
import edu.uci.ics.jung.algorithms.util.IterativeContext;
import edu.uci.ics.jung.graph.Graph;
import java.awt.geom.Point2D;
import java.util.Collection;
import java.util.ConcurrentModificationException;

/**
 * Template for a temporal graph layout. Lines represent each agent and his activities. Columns represent passage of time
 * @author Kohwalter
 * @param <V> JUNG's V (Vertex) type
 * @param <E> JUNG's E (Edge) type
 */
public class Temporal_Layout_Template<V, E> extends AbstractLayout<V, E> implements IterativeContext {

//    private Map<V, MyLayout.FRVertexData> frVertexData =
//    	LazyMap.decorate(new HashMap<V,MyLayout.FRVertexData>(), new Factory<MyLayout.FRVertexData>() {
//            @Override
//    		public MyLayout.FRVertexData create() {
//    			return new MyLayout.FRVertexData();
//    		}});


//    private double max_dimension;

    /**
     * Creates an instance for the specified graph.
     * @param g Graph
     */
    public Temporal_Layout_Template(Graph<V, E> g) {
        super(g);
//        initialize();
    }

    /**
     * Creates an instance of size {@code d} for the specified graph.
     */
//    public MyLayout(Graph<V, E> g, Dimension d) {
//        super(g, new RandomLocationTransformer<V>(d), d);
////        initialize();
//        max_dimension = Math.max(d.height, d.width);
////    }
//
//	@Override
//	public void setSize(Dimension size) {
//		if(initialized == false) {
//			setInitializer(new RandomLocationTransformer<V>(size));
//		}
//		super.setSize(size);
//        max_dimension = Math.max(size.height, size.width);
//	}

        @Override
	public void reset() {
		doInit();
	}
    @Override
    public void initialize() {
    	doInit();
    }

    private double XDISTANCE = 200.0;
    private double YDISTANCE = 50.0;
    private Graph<V,E> layout_graph;
    
    /**
     * Initialize layout
     */
    private void doInit() {
    	layout_graph = getGraph();
        //Starting Y position
        double ypos = -250.0;
        //X offset for Agent-type nodes
        double xOffset = 10.0;
        //Compute Agent-type node position
        for(V v : layout_graph.getVertices()) 
        {
            if(v instanceof Graph) {
                for(Object vertex : ((Graph)v).getVertices())
                {
                    if(vertex instanceof AgentVertex)
                    {
                        //Change offset sign so 2 consecutive agents 
                        //dont have the same X position
                        xOffset = xOffset * -2;

                        if(Math.abs(xOffset) > 60) 
                        {
                            xOffset = 10;
                        }  
                        //Compute position for the agent
                        calcAgentPositions(v, ypos, xOffset);
                        //Update Y position for the next agent
                        ypos += 100.0;
                        //Skip position 0
                        if(ypos == -50.0) 
                        {
                            ypos += 150.0;
                        }
                    }
                } 
            }
            else if(v instanceof AgentVertex)
            {
                //Change offset sign so 2 consecutive agents 
                //dont have the same X position
                xOffset = xOffset * -2;

                if(Math.abs(xOffset) > 60) 
                {
                    xOffset = 10;
                }  
                //Compute position for the agent
                calcAgentPositions(v, ypos, xOffset);
                //Update Y position for the next agent
                ypos += 100.0;
                //Skip position 0
                if(ypos == -50.0) 
                {
                    ypos += 150.0;
                }
            }
        }
        //Compute position for all node-types (minus Agent)
        for(V v2 : layout_graph.getVertices()) 
        {
            calcPositions(v2);
        }
        //Check if there are nodes at the same place, if so apply repulsion
        for(V v3 : layout_graph.getVertices()) 
        {
            calcRepulsion(v3);
        }
    }
    /**
     * Calculate agent positions (first column of the graph)
     * @param v An agent vertex
     * @param ypos Y position (current line)
     * @param xOffset X offset (to act as labels of each line)
     */
    protected synchronized void calcAgentPositions(V v, double ypos, double xOffset)
    {
        Point2D xyd = transform(v);
        xyd.setLocation(-XDISTANCE + xOffset, ypos);
    }
    
    /**
     * Calculate each entity and activity vertex position in the layout
     * @param v Activity or entity vertex
     */
    protected synchronized void calcPositions(V v) {
//        MyLayout.FRVertexData fvd = getFRData(v);
//        if(fvd == null) {
//            return;
//        }
        Point2D xyd = transform(v);

        double newXPos;// = xyd.getX();
        double newYPos;// = xyd.getY();
        
        if(v instanceof Vertex)
        {
            //Node's X position is defined by the day it was created
            newXPos = ((Vertex)v).getDate() * XDISTANCE;
            //If node is a ArtifactNode-type
            if(v instanceof EntityVertex)
            {
                newYPos = -YDISTANCE * 6;
                xyd.setLocation(newXPos, newYPos);
            }
            //If node is a ProcessNode-type
            else if(v instanceof ActivityVertex)
            {
                //The XY position for this type of node is dependable of the
                //agent who executed the process
                
                //Get edges from node v
//                Collection<E> edges = graph.getOutEdges(v);
                Collection<E> edges = layout_graph.getInEdges(v);
                for (E edge : edges)
                {
                    //if the edge link to an Agent-node
                    if(layout_graph.getSource(edge) instanceof AgentVertex)
                    {
                        //Compute position according to the agent position
//                        Point2D agentPos = transform(graph.getDest(edge));
                        Point2D agentPos = transform(layout_graph.getSource(edge));
                        //Adding an offset to not be in the same line
                        newYPos = agentPos.getY() + 50;
                        //Compute X from the Agent position, removing the -XDISTANCE
                        //to start at x=0, instead of x= -XDISTANCE position
                        newXPos = agentPos.getX() + XDISTANCE + newXPos;
                        xyd.setLocation(newXPos, newYPos);
                    }
                }
            }
        }
    }
    
    double variation = 30;
    
    //Check if 2 nodes are at the same position, if so add an offset
    /**
     * Method to check if there is any other vertex at the same position of this one (x,y)
     * @param v1 Vertex used to check if there is any other vertex at the same position
     */
    protected synchronized void calcRepulsion(V v1) {
        //Only Process and Artifact types can have the same position, so lets check
        if((v1 instanceof ActivityVertex) || (v1 instanceof EntityVertex))
        {
            try {
                for(V v2 : layout_graph.getVertices()) 
                {
                    if((v2 instanceof ActivityVertex)||(v2 instanceof EntityVertex))
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
                                p1.setLocation(p1.getX(), p1.getY() - variation * 0.5);
                                p2.setLocation(p2.getX(), p2.getY() + variation * 0.5);
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
     * Check if a and b are equals
     * @param a double
     * @param b double
     * @return if both values are equal
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
     * @return true
     */
    public boolean isIncremental() {
        return true;
    }

    /**
     * Returns true once the current iteration has passed the maximum count,
     * <tt>MAX_ITERATIONS</tt>.
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
