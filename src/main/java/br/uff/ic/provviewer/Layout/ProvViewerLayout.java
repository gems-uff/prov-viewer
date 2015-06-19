/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.ic.provviewer.Layout;

import br.uff.ic.provviewer.Variables;
import edu.uci.ics.jung.algorithms.layout.AbstractLayout;
import edu.uci.ics.jung.algorithms.util.IterativeContext;
import edu.uci.ics.jung.graph.Graph;

/**
 *
 * @author Kohwalter
 * @param <V>
 * @param <E>
 */
public abstract class ProvViewerLayout<V, E> extends AbstractLayout<V, E> implements IterativeContext {
    public Variables variables;
    
    public ProvViewerLayout(Graph<V, E> g, Variables variables) {
        super(g);
        this.variables = variables;
    }
}
