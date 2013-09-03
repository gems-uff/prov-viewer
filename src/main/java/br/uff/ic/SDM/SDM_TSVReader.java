package br.uff.ic.SDM;

import br.uff.ic.Prov_Viewer.Edge;
import br.uff.ic.Prov_Viewer.Vertex;
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
public class SDM_TSVReader {
    private Map<String, Vertex> nodes = new HashMap<String, Vertex>();
    private Collection<Edge> edges = new ArrayList<Edge>();
    
    /**
     * Method to read the file and classify the information according to its vertex/edge type
     * @param file
     * @throws URISyntaxException
     * @throws IOException 
     */
    public SDM_TSVReader(String file) throws URISyntaxException, IOException  {
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
                    Edge edge = new SDM_Edge(getProcess(Arrays.copyOfRange(names, 16, 29)),
                            getAgent(Arrays.copyOfRange(names, 1, 16)), "");
                    edges.add(edge);
                }
                //Action Action
                if(names[0].equalsIgnoreCase("IAcAc"))
                {
                   Edge edge = new SDM_Edge(getProcess(Arrays.copyOfRange(names, 1, 13)),
                            getProcess(Arrays.copyOfRange(names, 13, 25)), names[25]); 
                   edges.add(edge);
                }
                //Action Artifact
                if(names[0].equalsIgnoreCase("IAcAr"))
                {
                   Edge edge = new SDM_Edge(getProcess(Arrays.copyOfRange(names, 1, 13)),
                            getArtifact(names[13]), names[14]); 
                   edges.add(edge);
                }
                //Artifac Action
                if(names[0].equalsIgnoreCase("IArAc"))
                {
                    //System.out.println(names.length);
                   Edge edge = new SDM_Edge(getArtifact(names[1]),
                            getProcess(Arrays.copyOfRange(names, 2, 14)), names[14]); 
                   edges.add(edge);
                }
                if(names[0].equalsIgnoreCase("PP"))
                {
                    //System.out.println(line);
                    Edge edge = new SDM_Edge(getProject(Arrays.copyOfRange(names, 1, 21)),
                            getProject(Arrays.copyOfRange(names, 21, 41)), names[41]);
                    edges.add(edge);
                }
                if(names[0].equalsIgnoreCase("AcP"))
                {
                    Edge edge = new SDM_Edge(getProcess(Arrays.copyOfRange(names, 1, 13)),
                            getProject(Arrays.copyOfRange(names, 13, 33)), names[33]);
                    edges.add(edge);
                }
                if(names[0].equalsIgnoreCase("CP"))
                {
                    //System.out.println(line);
                    Edge edge = new SDM_Edge(getClient(names[1]),
                            getProject(Arrays.copyOfRange(names, 2, 22)), names[22]);
                    edges.add(edge);
                }
                if(names[0].equalsIgnoreCase("AcAg"))
                {
                    Edge edge = new SDM_Edge(getProcess(Arrays.copyOfRange(names, 1, 13)),
                            getAgent(Arrays.copyOfRange(names, 13, 28)), names[28]);
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
            node = new SDM_ProcessVertex(array);
            nodes.put(array[0], node);
        }
        return node;
    }
    private Vertex getArtifact(String id) {
        Vertex node = nodes.get(id);
        
        if (node == null) 
        {
            String[] line = id.split(" ");
            String date = line[0];
            String type = line[1]; 
            if(id.contains("Cases"))
            {
                type = line[1] + " " +line[2] + " " + line[3];
            }
//            System.out.println("ID: " + id);
//            System.out.println("Date: " + date + " / Type:" + type);
//            node = new ArtifactNode(id);
            node = new SDM_ArtifactVertex(type, date);
            nodes.put(id, node);
        }
        return node;
    }
    private Vertex getAgent(String[] array) {
        Vertex node = nodes.get(array[0]);
        
        if (node == null) 
        {
            node = new SDM_AgentVertex(array);
            nodes.put(array[0], node);
        }
        return node;
    }
    private Vertex getProject(String[] array) {
        Vertex node = nodes.get(array[0]);
        
        if (node == null) 
        {
            node = new SDM_ProjectVertex(array);
            nodes.put(array[0], node);
        }
        return node;
    }
    private Vertex getClient(String id) {
        Vertex node = nodes.get(id);
        
        if (node == null) 
        {
            node = new SDM_ClientVertex(id);
            nodes.put(id, node);
        }
        return node;
    }

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
