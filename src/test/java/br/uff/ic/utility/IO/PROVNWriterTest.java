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
import edu.uci.ics.jung.graph.DirectedGraph;
import edu.uci.ics.jung.graph.DirectedSparseMultigraph;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author Kohwalter
 */
public class PROVNWriterTest {
    
    public PROVNWriterTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }

    /**
     * Test of saveToProvn method, of class PROVNWriter.
     */
    @Test
    public void testSaveToProvn() throws Exception {
//        System.out.println("saveToProvn");
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
        testFiles("sculpture.prov-asn");
        testFiles("w3c-publication1.prov-asn");
        testFiles("w3c-publication2.prov-asn");
        testFiles("w3c-publication3.prov-asn");
        testFiles("ProvViewer.provn");
        testFiles("prov-dm-example1.prov-asn");

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
        PROVNWriter provnWriter = new PROVNWriter(g.getVertices(), g.getEdges());
        provnWriter.saveToProvn("target" + File.separator + "PROVN_Writer_Test");
        readFile("target" + File.separator + "PROVN_Writer_Test.provn");
    }
    public void readFile(String path) throws URISyntaxException, IOException {
        File f = new File(path);
        PROVNReader instance = new PROVNReader(f);
        instance.readFile();
        DirectedGraph<Object, Edge> g = new DirectedSparseMultigraph<Object, Edge>();
        for (Edge edge : instance.getEdges()) {
                g.addEdge(edge, edge.getSource(), edge.getTarget());
            }
    }

//    /**
//     * Test of attOrMarker method, of class PROVNWriter.
//     */
//    @Test
//    public void testTimeOrMarker() {
//        System.out.println("attOrMarker");
//        String time = "";
//        PROVNWriter instance = null;
//        String expResult = "";
//        String result = instance.attOrMarker(time);
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of keyword method, of class PROVNWriter.
//     */
//    @Test
//    public void testKeyword() {
//        System.out.println("keyword");
//        String s = "";
//        PROVNWriter instance = null;
//        String expResult = "";
//        String result = instance.keyword(s);
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of breakline method, of class PROVNWriter.
//     */
//    @Test
//    public void testBreakline() {
//        System.out.println("breakline");
//        PROVNWriter instance = null;
//        String expResult = "";
//        String result = instance.breakline();
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of startDocument method, of class PROVNWriter.
//     */
//    @Test
//    public void testStartDocument() throws Exception {
//        System.out.println("startDocument");
//        String namespaces = "";
//        PROVNWriter instance = null;
//        instance.startDocument(namespaces);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of close method, of class PROVNWriter.
//     */
//    @Test
//    public void testClose() throws Exception {
//        System.out.println("close");
//        PROVNWriter instance = null;
//        instance.close();
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of symbol method, of class PROVNWriter.
//     */
//    @Test
//    public void testSymbol() {
//        System.out.println("symbol");
//        String s = "";
//        PROVNWriter instance = null;
//        String expResult = "";
//        String result = instance.symbol(s);
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of optionalAttributes method, of class PROVNWriter.
//     */
//    @Test
//    public void testOptionalAttributes() {
//        System.out.println("optionalAttributes");
//        Collection<Attribute> attrs = null;
//        PROVNWriter instance = null;
//        String expResult = "";
//        String result = instance.optionalAttributes(attrs);
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of write method, of class PROVNWriter.
//     */
//    @Test
//    public void testWrite() throws Exception {
//        System.out.println("write");
//        String s = "";
//        PROVNWriter instance = null;
//        instance.write(s);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of writeln method, of class PROVNWriter.
//     */
//    @Test
//    public void testWriteln() throws Exception {
//        System.out.println("writeln");
//        String s = "";
//        PROVNWriter instance = null;
//        instance.writeln(s);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of optionalId method, of class PROVNWriter.
//     */
//    @Test
//    public void testOptionalId() {
//        System.out.println("optionalId");
//        String id = "";
//        PROVNWriter instance = null;
//        String expResult = "";
//        String result = instance.optionalId(id);
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of getID method, of class PROVNWriter.
//     */
//    @Test
//    public void testGetID() {
//        System.out.println("getID");
//        Object id = null;
//        PROVNWriter instance = null;
//        String expResult = "";
//        String result = instance.getID(id);
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
    
}
