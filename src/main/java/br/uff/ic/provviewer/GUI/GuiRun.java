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
package br.uff.ic.provviewer.GUI;

import br.uff.ic.utility.IO.BasePath;
import br.uff.ic.utility.graph.Edge;
import br.uff.ic.provviewer.GraphFrame;
import br.uff.ic.provviewer.Variables;
import edu.uci.ics.jung.graph.DirectedGraph;
import java.io.File;

/**
 * Class to run the application
 * @author Kohwalter
 */
public class GuiRun {
    
    /**
     * Main loop of the program
     */
    public static void Run()
    {
        System.out.println("Graph: " + BasePath.getBasePathForClass(GuiRun.class) + Variables.demo);
        File graphFile = new File(BasePath.getBasePathForClass(GuiRun.class) + Variables.demo);
        final DirectedGraph<Object, Edge> graph = GuiReadFile.getGraph(graphFile);
        
        java.awt.EventQueue.invokeLater(new Runnable() {
                
            @Override
                public void run() {
                    new GraphFrame(graph).setVisible(true);
                }
            });
    }
    
}
