package br.uff.ic.Prov_Viewer.Vertex;

import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.visualization.decorators.EllipseVertexShapeTransformer;
import java.awt.Shape;
import java.awt.geom.Ellipse2D;

/**
 * Class that defines each vertex type shape
 * @author Kohwalter
 * @param <V> JUNG's V (Vertex) type
 */
public class VertexShape<V> extends EllipseVertexShapeTransformer<V> {

        public VertexShape() {
            setSizeTransformer(new VertexSize<V>(20));
        }
        
        /**
         * Create the vertex shape using VertexShapeFactory<V> factory;
         * @param v JUNG's V (Vertex) type
         * @return Shape
         */
        @Override
        public Shape transform(V v) 
        {
            if (v instanceof Graph) {
//                int size = ((Graph)v).getVertexCount();
//                if (size < 8) {   
//                    int sides = Math.max(size, 3);
//                    return factory.getRegularPolygon(v, sides);
//                }
//                else {
//                    return factory.getRegularStar(v, size);
//                }
                String line = ((Graph) v).toString();
                if (line.contains("Entity")) {
                    return new Ellipse2D.Float(-7, -7, 30, 30);
                } else if ((line.contains("Agent"))) {
                    return factory.getRegularPolygon(v, 8);
                } else if (line.contains("Activity")) {
                    return factory.getRegularPolygon(v, 4);
                } else {
                    int size = ((Graph) v).getVertexCount();
                    if (size < 8) {
                        int sides = Math.max(size, 3);
                        return factory.getRegularPolygon(v, sides);
                    } else {
                        return factory.getRegularStar(v, size);
                    }
                }
            }
            if(v instanceof EntityVertex) 
            {
                return new Ellipse2D.Float(-7, -7, 20, 20);
            }
            if(v instanceof AgentVertex)
            {
                return factory.getRegularPolygon(v, 8);
            }
            else//activity vertex 
            {
                return factory.getRegularPolygon(v, 4);
            }
            //return new Rectangle2D.Float(-7, -7, 20, 20);
            //return super.transform(v);
        }
    }
