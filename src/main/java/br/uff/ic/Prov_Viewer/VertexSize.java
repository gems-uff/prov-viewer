
package br.uff.ic.Prov_Viewer;

import edu.uci.ics.jung.graph.Graph;
import org.apache.commons.collections15.Transformer;

/**
 * Class to define vertex size
 * @author Kohwalter
 * @param <V> JUNG's V (Vertex) type
 */
public class VertexSize<V> implements Transformer<V,Integer> {
    	int size;
        /**
         * Method for defining the vertex size
         * @param size define the size of the vertex
         */
        public VertexSize(Integer size) {
            this.size = size;
        }

        /**
         *
         * @param v the vertex being analyzed
         * @return the vertex size. If the vertex is a graph (collapsed vertices) then it will be twice bigger.
         */
        public Integer transform(V v) {
            if(v instanceof Graph) {
                return (size * 2);
            }
            return size;
        }
    }
