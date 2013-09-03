/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.ic.SDM;

import br.uff.ic.Prov_Viewer.Edge;
import br.uff.ic.Prov_Viewer.Variables;
import edu.uci.ics.jung.graph.DirectedGraph;
import java.util.Collection;

/**
 * Subclass of Variables
 * @author Kohwalter
 */
public class SDM_Variables extends Variables{
   
    
    float credits, quality, aid, progress, validation, discovery, bugs, repair, tc, funds, morale, stamina;
    boolean showMorale = false;
    boolean showStamina = false;
    boolean showHours = false;
    boolean showWeekend = false;
    boolean showCredits = false;
    boolean showRole = false;
    
    //Vertex filters mode
    @Override
    public void SetMode(String mode)
    {
        if(mode.equalsIgnoreCase("Default"))
        {
            SetDefault();
        }
        if(mode.equalsIgnoreCase("Morale"))
        {
             SetMorale();
        }
        if(mode.equalsIgnoreCase("Stamina"))
        {
             SetStamina();
        }
        if(mode.equalsIgnoreCase("Hours"))
        {
            SetHours();
        }
        if(mode.equalsIgnoreCase("Weekend"))
        {
             SetWeekend();
        }
        if(mode.equalsIgnoreCase("Credits"))
        {
             SetCredits();
        }
        if(mode.equalsIgnoreCase("Role"))
        {
             SetRole();
        }
    }
    @Override
    public void SetDefault()
    {
        showMorale = false;
        showStamina = false;
        showHours = false;
        showWeekend = false;
        showCredits = false;
        showRole = false;
    }
    //find max values for each variable. Used for edge width.
    @Override
    public void FindMax(DirectedGraph<Object,Edge> graph)
    {
        credits = quality = aid = progress = validation = discovery = bugs = 
                repair = tc = funds = morale = stamina = 0;
        int cCredits = 0;
        
        Collection<Edge> edges = graph.getEdges();
        for (Edge edge : edges)
        {
            quality = super.CompareMax("Quality", quality, edge);
            aid = super.CompareMax("Aid", aid, edge);
            progress = super.CompareMax("Progress", progress, edge);
            validation = super.CompareMax("Validation", validation, edge);
            discovery = super.CompareMax("Discovery", discovery, edge);
            bugs = super.CompareMax("Bug", bugs, edge);
            repair = super.CompareMax("Repair", repair, edge);
            tc = super.CompareMax("TC", tc, edge);
            morale = super.CompareMax("Morale", morale, edge);
            stamina = super.CompareMax("Stamina", stamina, edge);
            if((edge.getInfluence().contains("credits")) ||(edge.getInfluence().contains("Credits")))
            {
                credits += Math.abs(edge.getValue());
                cCredits ++;
            }
        }
        credits = credits / cCredits;
        Collection<Object> nodes = graph.getVertices();
        for (Object node : nodes)
        {
            if(node instanceof SDM_ProjectVertex)
            {
                funds = Math.max(funds, Math.abs(((SDM_ProjectVertex)node).getCredits()));
            }
        }
    }
    /**
     * Method to set Morale mode
     */
    public void SetMorale()
    {
        SetDefault();
        showMorale = true;
    }
    /**
     * Method to set Stamina mode
     */
    public void SetStamina()
    {
        SetDefault();
        showStamina = true;
    }
    /**
     * Method to set Hours mode
     */
    public void SetHours()
    {
        SetDefault();
        showHours = true;
    }
    /**
     * Method to set Weekend mode
     */
    public void SetWeekend()
    {
        SetDefault();
        showWeekend = true;
    }
    /**
     * Method to set Credits mode
     */
    public void SetCredits()
    {
        SetDefault();
        showCredits = true;
    }
    /**
     * Method to set Role mode
     */
    public void SetRole()
    {
        SetDefault();
        showRole = true;
    }
}
