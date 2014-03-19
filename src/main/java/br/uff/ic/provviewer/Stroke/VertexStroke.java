package br.uff.ic.provviewer.Stroke;

import br.uff.ic.provviewer.Input.Config;
import br.uff.ic.provviewer.Edge.Edge;
import br.uff.ic.provviewer.Vertex.ActivityVertex;
import br.uff.ic.provviewer.Vertex.Vertex;
import edu.uci.ics.jung.algorithms.layout.Layout;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import edu.uci.ics.jung.visualization.picking.PickedState;
import java.awt.BasicStroke;
import java.awt.Stroke;

/**
 * Class to define vertex strokes/borders/dashed
 *
 * @author Kohwalter
 */
public class VertexStroke {

    /**
     * Method to compute Vertex Stroke
     *
     * @param v JUNG's V (Vertex) type
     * @param dash The array representing the dashing pattern
     * @param view VisualizationViewer<Object, Edge>
     * @param layout Layout<Object, Edge>
     * @return Stroke
     */
    public static Stroke VertexStroke(Object v, float[] dash, VisualizationViewer<Object, Edge> view, Layout<Object, Edge> layout) {
        PickedState<Object> picked_state = view.getPickedVertexState();
        if (picked_state.isPicked(v.toString())) {
            return new BasicStroke(7.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10.0f, dash, 0.0f);
        } else {
            for (Object w : layout.getGraph().getNeighbors(v)) {
                if (picked_state.isPicked(w)) {
                    return new BasicStroke(4.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10.0f, dash, 0.0f);
                }
            }
            return new BasicStroke(1.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10.0f, dash, 0.0f);
        }
    }

    /**
     * Compute Vertex Stroke. Uses SDM terms (idle, promoted, fired, training,
     * hired)
     *
     * @param v
     * @param view
     * @param layout
     * @return
     */
    public static Stroke VertexStroke(Object v, VisualizationViewer<Object, Edge> view, Layout<Object, Edge> layout) {
        float[] dash = null;

        if (v instanceof Vertex) {
            if (!Config.vertexStrokevariables.isEmpty()) {
                for (int i = 0; i < Config.vertexStrokevariables.size(); i++) {
                    String[] list = Config.vertexStrokevariables.get(i).split(" ");
                    String att = ((Vertex) v).getAttributeValue(list[0]);
                    if (!"".equals(att)) {
                        for (int j = 1; j < list.length; j++) {
                            System.out.println("Attvalue: " + att);
                            System.out.println("Listvalue: " + list[j]);
                            if (att.equalsIgnoreCase(list[j])) {
                                dash = new float[1];
                                dash[0] = 4.0f;
                            }
                        }
                    }
                }
            }
        }
        return VertexStroke(v, dash, view, layout);
    }
}
