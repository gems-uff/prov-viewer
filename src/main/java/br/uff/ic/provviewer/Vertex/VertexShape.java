package br.uff.ic.provviewer.Vertex;

import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.visualization.decorators.EllipseVertexShapeTransformer;
import java.awt.Shape;
import java.awt.geom.Ellipse2D;

/**
 * Class that defines each vertex type shape
 *
 * @author Kohwalter
 * @param <V> JUNG's V (Vertex) type
 */
public class VertexShape<V> extends EllipseVertexShapeTransformer<V> {

    public VertexShape() {
        setSizeTransformer(new VertexSize<V>(20));
    }

    /**
     * Create the vertex shape using VertexShapeFactory<V> factory;
     *
     * @param v JUNG's V (Vertex) type
     * @return Shape
     */
    @Override
    public Shape transform(V v) {
        if (v instanceof Graph) {
            Object vertex;
            vertex = (((Graph) v).getVertices()).iterator().next();
            while (vertex instanceof Graph) {
                vertex = (((Graph) vertex).getVertices()).iterator().next();
            }
            v = (V) vertex;
        }
        if (v instanceof EntityVertex) {
            return new Ellipse2D.Float(-7, -7, 20, 20);
        }
        if (v instanceof AgentVertex) {
            return factory.getRegularPolygon(v, 5);
        } else//activity vertex 
        {
            return factory.getRegularPolygon(v, 4);
        }
    }
}
