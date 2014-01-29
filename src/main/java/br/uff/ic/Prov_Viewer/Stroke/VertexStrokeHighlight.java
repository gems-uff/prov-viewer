/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.ic.Prov_Viewer.Stroke;

import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.visualization.picking.PickedInfo;
import java.awt.BasicStroke;
import java.awt.Stroke;
import org.apache.commons.collections15.Transformer;

/**
 * Class to highlight selected vertex's neighbors
 * @author Kohwalter
 * @deprecated This function is also done at VertexStroke
 * @param <V> JUNG's V (Vertex) type
 * @param <E> JUNG's E (Edge) type
 */
public class VertexStrokeHighlight<V,E> implements
    Transformer<V,Stroke>
    {
        /**
         * Variable
         */
        protected boolean highlight = false;
        /**
         * Variable
         */
        protected Stroke heavy = new BasicStroke(5);
        /**
         * Variable
         */
        protected Stroke medium = new BasicStroke(3);
        /**
         * Variable
         */
        protected Stroke light = new BasicStroke(1);
        /**
         * Variable
         */
        protected PickedInfo<V> pi;
        /**
         * Variable
         */
        protected Graph<V,E> graph;
        
        /**
         * Constructor
         * @param graph 
         * @param pi 
         */
        public VertexStrokeHighlight(Graph<V,E> graph, PickedInfo<V> pi)
        {
            this.graph = graph;
            this.pi = pi;
        }
        
        /**
         * Set highlight
         * @param highlight 
         */
        public void setHighlight(boolean highlight)
        {
            this.highlight = highlight;
        }
        
        /**
         * Method to mark the vertex neighbors by changing their border width
         * @param v Vertex
         * @return Stroke
         */
        public Stroke transform(V v)
        {
            if (highlight)
            {
                if (pi.isPicked(v)) {
                    return heavy;
                }
                else
                {
                	for(V w : graph.getNeighbors(v)) {
//                    for (Iterator iter = graph.getNeighbors(v)v.getNeighbors().iterator(); iter.hasNext(); )
//                    {
//                        Vertex w = (Vertex)iter.next();
                        if (pi.isPicked(w)) {
                                return medium;
                            }
                    }
                    return light;
                }
            }
            else {
                return light;
            } 
        }

    }
