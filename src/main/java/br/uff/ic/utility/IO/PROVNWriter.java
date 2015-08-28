/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.ic.utility.IO;

import br.uff.ic.utility.graph.Edge;
import java.io.FileNotFoundException;
import java.util.Collection;

/**
 *
 * @author Kohwalter
 */
public class PROVNWriter {
    
     public Collection<Object> vertexList;
     public Collection<Edge> edgeList;
     
     public PROVNWriter(Collection<Object> vertices, Collection<Edge> edges) {
        vertexList = vertices;
        edgeList = edges;
    }
     
     public void saveToProvn(String fileName) throws FileNotFoundException {
         
         // TODO
         
     }
    
}
