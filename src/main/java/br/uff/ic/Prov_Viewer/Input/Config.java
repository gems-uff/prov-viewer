/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.ic.Prov_Viewer.Input;

import br.uff.ic.Prov_Viewer.GraphFrame;
import br.uff.ic.Prov_Viewer.Vertex.VisualizationModes.AttributeMode;
import br.uff.ic.Prov_Viewer.Vertex.VisualizationModes.ConstantEntityAttributeMode;
import br.uff.ic.Prov_Viewer.Vertex.VisualizationModes.DefaultMode;
import br.uff.ic.Prov_Viewer.Vertex.VisualizationModes.InvertedAttributeMode;
import br.uff.ic.Prov_Viewer.Vertex.VisualizationModes.MultiAttributeMode;
import br.uff.ic.Prov_Viewer.Vertex.VisualizationModes.VertexPaintMode;
import br.uff.ic.Prov_Viewer.Vertex.VisualizationModes.WeekendMode;
import java.awt.Color;
import java.awt.Paint;
import java.util.ArrayList;
import java.util.Collection;
import javax.swing.DefaultComboBoxModel;

/**
 *
 * @author Kohwalter
 */
public class Config {
    //Filter List

    public static String[] filterList;
    public static int length;
    //Edge Stroke
    public static boolean[] edgeStroke;
    //Modes
    public static Collection<VertexPaintMode> vertexModes = new ArrayList<VertexPaintMode>();
    //Edge
    public static String[] edgeInfluenceTypeNotAdd;
    //Temporal Layout
    public static String layoutSpecialVertexType;
    //Vertex Stroke variables
    public static String[] vertexStrokevariables;
    //ActivityVertex
    //All 3 arrays must have the same size
    public static String[] actVerAtt;
    public static String[] actVerValue;
    public static Paint[] actVerColor;

    public static void Initialize() {
        //Initialize Interface Filters
        filterList = new String[]{"Neutral", "Credits", "Quality", "Progress", "Aid", "Val", "Discovery", "Repair", "Bug", "TC", "Morale", "Stamina", "Prototype", "Negotiation"};
        length = filterList.length;
        GraphFrame.FilterList.setListData(filterList);
        //Edges
        //Attribute to define if any edge will use a dif collapse function to compute collapsed value
        //(which is: Multiply values instead of adding)
        edgeInfluenceTypeNotAdd = new String[length];
        edgeInfluenceTypeNotAdd[0] = "Aid";
        for (int i = 1; i < length; i++) {
            edgeInfluenceTypeNotAdd[i] = "Not_Used";
        }
        //Edge Stroke
        edgeStroke = new boolean[length];
        //Edge Stroke
        edgeStroke[0] = false;
        for (int i = 1; i < length; i++) {
            edgeStroke[i] = true;
        }
        //Temporal Layout
        layoutSpecialVertexType = "Project";
        //Initialize Vertex Paint Modes
        DefaultMode def = new DefaultMode("Default");
        vertexModes.add(def);
        AttributeMode morale = new AttributeMode("Morale");
        vertexModes.add(morale);
        AttributeMode stamina = new AttributeMode("Stamina");
        vertexModes.add(stamina);
        InvertedAttributeMode hours = new InvertedAttributeMode("Hours", 6, 12);
        vertexModes.add(hours);
        ConstantEntityAttributeMode credits = new ConstantEntityAttributeMode("Credits");
        vertexModes.add(credits);
        String[] roles = new String[]{"Analyst", "Architect", "Manager", "Marketing", "Programmer", "Tester"};
        MultiAttributeMode role = new MultiAttributeMode("Role", roles);
        vertexModes.add(role);
        WeekendMode week = new WeekendMode("Weekend", "Sat", "Sun");
        vertexModes.add(week);

        String[] items = new String[vertexModes.size()];
        int j = 0;
        for (VertexPaintMode mode : vertexModes) {
            items[j] = mode.GetName();
            j++;
        }
        GraphFrame.StatusFilterBox.setModel(new DefaultComboBoxModel(items));
        //Vertex Stroke variables
        //It evaluates an attribute (i = 0) and changes the vertex stroke if the value equals to
        //any of the defined values ( i > 0)
        vertexStrokevariables = new String[]{"Role", "Idle", "Fired", "Promotion", "Training", "Hired"};
        //ActivityVertex
        //These attributes are used to distinguish vertex color. 
        //The specific values used to change the color (for each attribute) are below
        actVerAtt = new String[]{"Task", "Role", "Morale", "Stamina", "Hours", "Credits"};
        actVerValue = new String[]{"Idle", "Training", "Fired", "Promotion", "Hired", "Negotiation"};
        actVerColor = new Paint[]{new Color(238, 180, 180), new Color(102, 0, 102), new Color(139, 69, 19), new Color(0, 153, 0), new Color(139, 136, 120), new Color(193, 205, 193)};

    }
}
