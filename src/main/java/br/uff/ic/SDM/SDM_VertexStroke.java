
package br.uff.ic.SDM;

import br.uff.ic.Prov_Viewer.Edge;
import br.uff.ic.Prov_Viewer.VertexStroke;
import edu.uci.ics.jung.algorithms.layout.Layout;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import java.awt.Stroke;

/**
 * Subclass of VertexStroke
 * @author Kohwalter
 */
public class SDM_VertexStroke extends VertexStroke{  

    /**
     * Compute Vertex Stroke. Uses SDM terms (idle, promoted, fired, training, hired)
     * @param v
     * @param view
     * @param layout
     * @return 
     */
    public static Stroke VertexStroke(Object v, VisualizationViewer<Object, Edge> view, Layout<Object, Edge> layout)
    {
        float[] dash = null;
        
        if(v instanceof SDM_ProcessVertex)
        {
            String role = ((SDM_ProcessVertex)v).getRole();
            if(!"".equals(role))
            {
                if(role.equalsIgnoreCase("Idle") || role.equalsIgnoreCase("Fired") 
                        || role.equalsIgnoreCase("Promotion") || role.equalsIgnoreCase("Training") ||
                        role.equalsIgnoreCase("Hired")) {
                    dash = new float[1];
                    dash[0] = 4.0f;
                }
            }
        }
        return VertexStroke(v, dash, view, layout);
    }
}
