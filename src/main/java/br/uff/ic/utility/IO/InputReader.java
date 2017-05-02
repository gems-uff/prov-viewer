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
import br.uff.ic.utility.graph.Vertex;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Kohwalter
 */
public abstract class InputReader {
    Map<String, Vertex> nodes;
    Collection<Edge> edges;
    File file;
    /**
     * Return edges
     *
     * @return
     */
    public Collection<Edge> getEdges() {
        return edges;
    }

    /**
     * Return vertices
     *
     * @return
     */
    public Collection<Vertex> getNodes() {
        return nodes.values();
    }

    public InputReader(File f){
        file = f;
        nodes = new HashMap<>();
        edges = new ArrayList<>();
    }
    
//    public void addEdge(String id, String influence, String type, String value, String label, Map<String, Attribute> attributes, String target, String source)
//    {
//        Edge edge = new Edge(id, influence, type, value, label, attributes, nodes.get(target), nodes.get(source));
//        edges.add(edge);
//    }
    
    /**
     *  Method to create and add an edge
     * @param id
     * @param type
     * @param label
     * @param value
     * @param target
     * @param source 
     */
    public void addEdge(String id, String type, String label, String value, String target, String source)
    {
        //AddEdge(id, type, label, value, target, source);
        Edge edge = new Edge(id, type, label, value, nodes.get(target), nodes.get(source));
        edges.add(edge);
    }
    /**
     * Method to add an edge
     * @param edge: the edge to be added
     */
    public void addEdge(Edge edge)
    {
        edges.add(edge);
    }
    
    /**
     * Method to add Vertex to the Map
     * @param node 
     */
    public void addNode(Vertex node)
    {
        nodes.put(node.getID(), node);
    }
    
    /**
     * Abstract method to read a XML file depending on the XML-Schema
     */
    public abstract void readFile()throws URISyntaxException, IOException;

}
