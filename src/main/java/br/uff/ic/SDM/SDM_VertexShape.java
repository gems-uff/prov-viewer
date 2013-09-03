package br.uff.ic.SDM;

import br.uff.ic.Prov_Viewer.VertexSize;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.visualization.decorators.EllipseVertexShapeTransformer;
import java.awt.Shape;
import java.awt.geom.Ellipse2D;

/**
 * Overload of VertexShape, customized to SDM
 * @author Kohwalter
 * @param <V> 
 */
public class SDM_VertexShape<V> extends EllipseVertexShapeTransformer<V> {

        SDM_VertexShape() {
            setSizeTransformer(new VertexSize<V>(20));
        }
        /**
         * 
         * @param v
         * @return 
         */
        @Override
        public Shape transform(V v) 
        {
            //Shape priority depending on the types of vertices in it
            //project > agent > client > process
            if(v instanceof Graph) 
            {
                //int size = ((Graph)v).getVertexCount();
                String line = ((Graph)v).toString();
                if(line.contains("Project")) {
                    return new Ellipse2D.Float(-7, -7, 30, 30);
                }
                else if((line.contains("Agent"))||(line.contains("Client"))) {
                    return factory.getRegularPolygon(v, 8);
                }
                else if(line.contains("Activity")) {
                    return factory.getRegularPolygon(v, 4);
                }
                else {
                    return new Ellipse2D.Float(-7, -7, 30, 30);
                }
            }
            if(v instanceof SDM_ArtifactVertex) 
            {
                return new Ellipse2D.Float(-7, -7, 20, 20);
            }
            if(v instanceof SDM_ProjectVertex) 
            {
                return new Ellipse2D.Float(-7, -7, 20, 20);
            }
            if((v instanceof SDM_AgentVertex) || (v instanceof SDM_ClientVertex) )
            {
                return factory.getRegularPolygon(v, 8);
            }
            else//process node 
            {
                return factory.getRegularPolygon(v, 4);
            }
        }
    }
