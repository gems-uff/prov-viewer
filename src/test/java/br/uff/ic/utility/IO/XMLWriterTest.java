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

package br.uff.ic.utility.IO;

import br.uff.ic.provviewer.GUI.GuiRun;
import br.uff.ic.utility.graph.Edge;
import br.uff.ic.utility.graph.Vertex;
import edu.uci.ics.jung.graph.DirectedGraph;
import edu.uci.ics.jung.graph.DirectedSparseMultigraph;
import java.io.File;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author Kohwalter
 */
public class XMLWriterTest {
    
    public XMLWriterTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }

    /**
     * Test of saveToXML method, of class XMLWriter.
     */
    @Test
    public void testSaveToXML() throws Exception {
//        System.out.println("saveToXML");
//        System.out.println("File: " + Variables.demo);
        File f = new File(BasePath.getBasePathForClass(GuiRun.class) + File.separator + "Graph" + File.separator + "Car_Tutorial.xml");
        UnityReader file = new UnityReader(f);
        file.readFile();
        DirectedGraph<Object, Edge> g = new DirectedSparseMultigraph<Object, Edge>();
        
        for (Edge edge : file.getEdges()) {
                g.addEdge(edge, (Vertex) edge.getSource(), (Vertex) edge.getTarget());
            }
            
        XMLWriter xmlWriter = new XMLWriter(g.getVertices(), g.getEdges());
        xmlWriter.saveToXML("target" + File.separator + "XML_Writer_Test");
    }
    
//    @Test
//    public void separateBusLinesTest() throws Exception {
//        System.out.println("saveToXML");
//        System.out.println("File: " + Variables.demo);
//        File f = new File(BasePath.getBasePathForClass(GuiRun.class) + Variables.demo);
//        UnityReader file = new UnityReader(f);
//        file.readFile();
//        DirectedGraph<Object, Edge> g = new DirectedSparseMultigraph<Object, Edge>();
//        DirectedGraph<Object, Edge> g2 = new DirectedSparseMultigraph<Object, Edge>();
//        Map<String, Edge> processedEdges = new HashMap<String, Edge>();
//        for (Edge edge : file.getEdges()) {
//            if (edge.getLabel().equalsIgnoreCase("Neutral")) {
//                processedEdges.put(((Vertex) edge.getSource()).getID() + ((Vertex) edge.getTarget()).getID(), edge);
//            }
//        }
//        for (Edge edge : processedEdges.values()) {
//            if (((Vertex) edge.getSource()).getAttributeValue("Linha").equalsIgnoreCase("5") && (((Vertex) edge.getTarget()).getAttributeValue("Linha").equalsIgnoreCase("5"))) {
//                g.addEdge(edge, (Vertex) edge.getSource(), (Vertex) edge.getTarget());
//            } else if (((Vertex) edge.getSource()).getAttributeValue("Linha").equalsIgnoreCase("4") && (((Vertex) edge.getTarget()).getAttributeValue("Linha").equalsIgnoreCase("4"))) {
//                g2.addEdge(edge, (Vertex) edge.getSource(), (Vertex) edge.getTarget());
//            }
//        }
//
//        XMLWriter xmlWriter = new XMLWriter(g.getVertices(), g.getEdges());
//        xmlWriter.saveToXML("bus_linha5");
//        xmlWriter = new XMLWriter(g2.getVertices(), g.getEdges());
//        xmlWriter.saveToXML("bus_linha4");
//    }
}
