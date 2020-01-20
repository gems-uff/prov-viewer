/*
 * The MIT License
 *
 * Copyright 2020 Kohwalter.
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
package br.uff.ic.provviewer.markov;

import br.uff.ic.provviewer.VariableNames;
import br.uff.ic.provviewer.Variables;
import br.uff.ic.utility.GraphAttribute;
import br.uff.ic.utility.graph.AgentVertex;
import br.uff.ic.utility.graph.Edge;
import br.uff.ic.utility.graph.Vertex;

/**
 *
 * @author Kohwalter
 */
public class Markov {
    
    public static void computeMarkovChain(Variables variables){
        for(Edge e : variables.graph.getEdges()) {
            if(!(e.getTarget() instanceof AgentVertex)) {
                float eFreq = e.updateFrequency(variables.numberOfGraphs);
                Vertex vsource = (Vertex) e.getSource();
                float sourceFreq = vsource.updateFrequency(variables.numberOfGraphs);
                Vertex vstarget = (Vertex) e.getTarget();
                float targetFreq = vstarget.updateFrequency(variables.numberOfGraphs);
                e.addAttribute(new GraphAttribute(VariableNames.MarkovIn, String.valueOf(eFreq / targetFreq), "ProvViewer"));
                e.addAttribute(new GraphAttribute(VariableNames.MarkovOut, String.valueOf(eFreq / sourceFreq), "ProvViewer"));
            }
        }
        System.out.println("Done!");
    }
}
