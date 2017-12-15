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
package br.uff.ic.provviewer;

import java.awt.Dimension;
import java.awt.RenderingHints;
import java.awt.Stroke;
import java.awt.geom.Point2D;
import org.apache.commons.collections15.Predicate;
import edu.uci.ics.jung.algorithms.layout.Layout;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.util.Context;
import edu.uci.ics.jung.graph.util.Pair;
import edu.uci.ics.jung.visualization.RenderContext;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import edu.uci.ics.jung.visualization.renderers.BasicEdgeRenderer;
import edu.uci.ics.jung.visualization.transform.shape.GraphicsDecorator;
import org.apache.commons.collections15.functors.TruePredicate;

/**
 *
 * @author Kohwalter
 */
public class ImproveJUNGPerformance<V, E> {

    Object antialiasing = null;

    /**
     * Method to disable the antialising for better performance

     * @param vv VisualizationViewer
     */
    public void DisableAntialiasing(VisualizationViewer<V, E> vv) {
        vv.getRenderingHints().remove(RenderingHints.KEY_ANTIALIASING);
    }
    
    /**
     * Enable anti-aliasing
     * @param vv VisualizationViewer
     */
    public void EnableAntialiasing(VisualizationViewer<V, E> vv) {
        vv.getRenderingHints().put(RenderingHints.KEY_ANTIALIASING, antialiasing);
    }
    
    /**
     * Init
     * @param vv VisualizationViewer
     */
    public void init(VisualizationViewer<V, E> vv) {
        antialiasing = vv.getRenderingHints().get(RenderingHints.KEY_ANTIALIASING);
    }
    
    /**
     * Method to omit the arrowheads from the edges
     * @param vv VisualizationViewer
     */
    public void OmitArrowHeads(VisualizationViewer<V, E> vv) {
        Predicate<Context<Graph<V, E>, E>> edgeArrowPredicate = 
            new Predicate<Context<Graph<V,E>,E>>()
        {
            @Override
            public boolean evaluate(Context<Graph<V, E>, E> arg0)
            {
                return false;
            }
        };
        vv.getRenderContext().setEdgeArrowPredicate(edgeArrowPredicate);
    }
    
    /**
     * Enable edge arrowheads
     * @param vv VisualizationViewer
     */
    public void EnableArrowHeads(VisualizationViewer<V, E> vv) {
        Predicate<Context<Graph<V, E>, E>> edgeArrowPredicate = 
            new Predicate<Context<Graph<V,E>,E>>()
        {
            @Override
            public boolean evaluate(Context<Graph<V, E>, E> arg0)
            {
                return true;
            }
        };
        vv.getRenderContext().setEdgeArrowPredicate(edgeArrowPredicate);
    }

    /**
     * Method to skip all vertices that are not in the visible area
     * @param vv VisualizationViewer
     */
    public void doNotPaintInvisibleVertices(
        final VisualizationViewer<V, E> vv)
    {
        Predicate<Context<Graph<V, E>, V>> vertexIncludePredicate = 
            new Predicate<Context<Graph<V,E>,V>>()
        {
            Dimension size = new Dimension();

            @Override
            public boolean evaluate(Context<Graph<V, E>, V> c)
            {
                vv.getSize(size);
                Point2D point = vv.getGraphLayout().transform(c.element);
                Point2D transformed = 
                    vv.getRenderContext().getMultiLayerTransformer()
                        .transform(point);
                if (transformed.getX() < 0 || transformed.getX() > size.width)
                {
                    return false;
                }
                if (transformed.getY() < 0 || transformed.getY() > size.height)
                {
                    return false;
                }
                return true;
            }
        };
        vv.getRenderContext().setVertexIncludePredicate(vertexIncludePredicate);
        doPaintEdgesAtLeastOneVertexIsVisible(vv);
    }
    
    /**
     * Paint invisible vertices
     * @param vv VisualizationViewer
     */
    public void PaintInvisibleVertices(VisualizationViewer<V, E> vv) {
        Predicate<Context<Graph<V,E>,V>> vertexIncludePredicate = TruePredicate.getInstance();
        vv.getRenderContext().setVertexIncludePredicate(vertexIncludePredicate);
    }

    /**
     * Method to include edges that have at least one vertex in the current visualization window
     * @param vv VisualizationViewer
     */
    private void doPaintEdgesAtLeastOneVertexIsVisible(VisualizationViewer<V, E> vv)
    {
        vv.getRenderer().setEdgeRenderer(new BasicEdgeRenderer<V, E>()
        {
            @Override
            public void paintEdge(RenderContext<V,E> rc, Layout<V, E> layout, E e) 
            {
                GraphicsDecorator g2d = rc.getGraphicsContext();
                Graph<V,E> graph = layout.getGraph();
                if (!rc.getEdgeIncludePredicate().evaluate(
                        Context.<Graph<V,E>,E>getInstance(graph,e)))
                    return;

                Pair<V> endpoints = graph.getEndpoints(e);
                V v1 = endpoints.getFirst();
                V v2 = endpoints.getSecond();
                if (!rc.getVertexIncludePredicate().evaluate(
                        Context.<Graph<V,E>,V>getInstance(graph,v1)) && 
                    !rc.getVertexIncludePredicate().evaluate(
                        Context.<Graph<V,E>,V>getInstance(graph,v2)))
                    return;

                Stroke new_stroke = rc.getEdgeStrokeTransformer().transform(e);
                Stroke old_stroke = g2d.getStroke();
                if (new_stroke != null)
                    g2d.setStroke(new_stroke);

                drawSimpleEdge(rc, layout, e);

                // restore paint and stroke
                if (new_stroke != null)
                    g2d.setStroke(old_stroke);
            }
        });
    }
}
