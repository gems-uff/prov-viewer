package br.uff.ic.Prov_Viewer.Input;

import br.uff.ic.Prov_Viewer.Vertex.ActivityVertex;
import br.uff.ic.Prov_Viewer.Vertex.EntityVertex;
import br.uff.ic.Prov_Viewer.Vertex.AgentVertex;
import br.uff.ic.Prov_Viewer.Edge.Edge;
import br.uff.ic.Prov_Viewer.Vertex.Vertex;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * TSV Reader. Class to read the Game Flow Log txt file
 * @author Kohwalter
 */
public class TSVReader {
    private Map<String, Vertex> nodes = new HashMap<String, Vertex>();
    private Collection<Edge> edges = new ArrayList<Edge>();
    
    /**
     * Method to read the file and classify the information according to its vertex/edge type
     * @param file
     * @throws URISyntaxException
     * @throws IOException 
     */
    public TSVReader(String file) throws URISyntaxException, IOException  {
//        Path path = Paths.get(ClassLoader.getSystemResource(file).toURI());
//        List<String> lines = Files.readAllLines(path, Charset.forName("UTF-8"));
         
        int i = 0;
        //for (String line : lines) {
        try{
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line;

            while ((line = br.readLine()) != null) {
                i++;

                String[] names = line.split("\t");

                //Agent Action
                if(names[0].equalsIgnoreCase("AgAc"))
                {
//                    Edge edge = new SDM_Edge(getAgent(Arrays.copyOfRange(names, 1, 16)),
//                            getProcess(Arrays.copyOfRange(names, 16, 29)), "", i);
                    Edge edge = new Edge(getProcess(Arrays.copyOfRange(names, 4, 8)),
                            getAgent(Arrays.copyOfRange(names, 1, 4)), "");
                    edges.add(edge);
                }
                //Action Action
                if(names[0].equalsIgnoreCase("IAcAc"))
                {
                   Edge edge = new Edge(getProcess(Arrays.copyOfRange(names, 1, 5)),
                            getProcess(Arrays.copyOfRange(names, 5, 9)), names[9]); 
                   edges.add(edge);
                }
                //Action Artifact
                if(names[0].equalsIgnoreCase("IAcAr"))
                {
                   Edge edge = new Edge(getProcess(Arrays.copyOfRange(names, 1, 5)),
                            getArtifact(Arrays.copyOfRange(names, 5, 9)), names[9]); 
                   edges.add(edge);
                }
                //Artifac Action
                if(names[0].equalsIgnoreCase("IArAc"))
                {
                    //System.out.println(names.length);
                   Edge edge = new Edge(getArtifact(Arrays.copyOfRange(names, 1, 5)),
                            getProcess(Arrays.copyOfRange(names, 5, 9)), names[9]); 
                   edges.add(edge);
                }
                if(names[0].equalsIgnoreCase("PP"))
                {
                    //System.out.println(line);
                    Edge edge = new Edge(getProject(Arrays.copyOfRange(names, 1, 5)),
                            getProject(Arrays.copyOfRange(names, 5, 9)), names[9]);
                    edges.add(edge);
                }
                if(names[0].equalsIgnoreCase("AcP"))
                {
                    Edge edge = new Edge(getProcess(Arrays.copyOfRange(names, 1, 5)),
                            getProject(Arrays.copyOfRange(names, 5, 9)), names[9]);
                    edges.add(edge);
                }
                if(names[0].equalsIgnoreCase("AgP"))
                {
                    //System.out.println(line);
                    Edge edge = new Edge(getAgent(Arrays.copyOfRange(names, 1, 4)),
                            getProject(Arrays.copyOfRange(names, 4, 8)), names[8]);
                    edges.add(edge);
                }
                if(names[0].equalsIgnoreCase("AcAg"))
                {
                    Edge edge = new Edge(getProcess(Arrays.copyOfRange(names, 1, 5)),
                            getAgent(Arrays.copyOfRange(names, 5, 8)), names[8]);
                    edges.add(edge);
                }
            }
        } catch (IOException e) {
                e.printStackTrace();
        }
    }

    private Vertex getProcess(String[] array) {
        Vertex node = nodes.get(array[0]);
        
        if (node == null) 
        {
            node = new ActivityVertex(array);
            nodes.put(array[0], node);
        }
        return node;
    }
    private Vertex getArtifact(String[] id) {
        return getProject(id);
    }
    private Vertex getAgent(String[] array) {
        Vertex node = nodes.get(array[0]);
        
        if (node == null) 
        {
            node = new AgentVertex(array);
            nodes.put(array[0], node);
        }
        return node;
    }
    private Vertex getProject(String[] array) {
        Vertex node = nodes.get(array[0]);
        
        if (node == null) 
        {
            node = new EntityVertex(array);
            nodes.put(array[0], node);
        }
        return node;
    }
//    private Vertex getClient(String id) {
//        Vertex node = nodes.get(id);
//        
//        if (node == null) 
//        {
//            node = new SDM_ClientVertex(id);
//            nodes.put(id, node);
//        }
//        return node;
//    }

    /**
     * Return edges
     * @return 
     */
    public Collection<Edge> getEdges() {
        return edges;
    }

    /**
     * Return vertices
     * @return 
     */
    public Collection<Vertex> getNodes() {
        return nodes.values();
    }
}
