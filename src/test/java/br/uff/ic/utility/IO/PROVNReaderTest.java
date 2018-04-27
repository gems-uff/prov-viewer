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

import br.uff.ic.utility.graph.Edge;
import br.uff.ic.provviewer.GUI.GuiRun;
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
public class PROVNReaderTest {
    
    public PROVNReaderTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }

    /**
     * Test of readFile method, of class PROVNReader.
     */
    @Test
    public void testReadFile() throws Exception {
//        System.out.println("readFile");
        testFiles("bundles1.provn");
        testFiles("bundles2.provn");
        testFiles("container0.provn");
        testFiles("container1.provn");
        testFiles("container2.provn");
        testFiles("example-blog1.provn");
        testFiles("example-blog2.provn");
        testFiles("prov-family.provn");
        testFiles("prov-family-graphics.provn");
        testFiles("file-example1.asn");
        testFiles("file-example2.asn");
        testFiles("file-example3.asn");
        testFiles("compound-1HHB.pn");
        testFiles("paolo-test.pn");
        testFiles("primer.pn");
        testFiles("container3.prov");
        testFiles("prov-dm-example1.prov-asn");
        testFiles("sculpture.prov-asn");
        testFiles("w3c-publication1.prov-asn");
        testFiles("w3c-publication2.prov-asn");
        testFiles("w3c-publication3.prov-asn");
        testFiles("ProvViewer.provn");
    }
    
    public void testFiles(String file) throws Exception {
        String path = File.separator + "Graph" + File.separator + "Test" + File.separator + file;
        File f = new File(BasePath.getBasePathForClass(GuiRun.class) + path);
        PROVNReader instance = new PROVNReader(f);
        instance.readFile();
        DirectedGraph<Object, Edge> g = new DirectedSparseMultigraph<Object, Edge>();
        for (Edge edge : instance.getEdges()) {
                g.addEdge(edge, edge.getSource(), edge.getTarget());
            }
    }

    /**
     * Test of Read method, of class PROVNReader.
     */
    @Test
    public void testRead() throws Exception {
//        System.out.println("Read");
        String line = "wasGeneratedBy(e2, a1, -, [ex:fct=\\\"save\\\"])";
        File f = File.createTempFile("empty", ".provn");
        PROVNReader instance = new PROVNReader(f);
        instance.Read(line);
    }
}
