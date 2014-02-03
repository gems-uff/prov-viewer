/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.ic.Prov_Viewer.Input;

import br.uff.ic.Prov_Viewer.GraphFrame;
import br.uff.ic.Prov_Viewer.Vertex.VisualizationModes.AttributeMode;
import br.uff.ic.Prov_Viewer.Vertex.VisualizationModes.DefaultMode;
import br.uff.ic.Prov_Viewer.Vertex.VisualizationModes.EntityAttributeMode;
import br.uff.ic.Prov_Viewer.Vertex.VisualizationModes.InvertedAttributeMode;
import br.uff.ic.Prov_Viewer.Vertex.VisualizationModes.MultiAttributeMode;
import br.uff.ic.Prov_Viewer.Vertex.VisualizationModes.VertexPaintMode;
import br.uff.ic.Prov_Viewer.Vertex.VisualizationModes.WeekendMode;
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
    public static boolean[] EdgeStroke;
    //Modes
    public static Collection<VertexPaintMode> vertexModes = new ArrayList<VertexPaintMode>();
    //Edge
    public static String[] EinfTypeNotAdd;
    //Temporal Layout
    public static String TLspecialVertexType;
    
    //Vertex Stroke variables
    //TODO: Change to array for scalability
    public static String VSattribute;
    public static String VSvar1;
    public static String VSvar2;
    public static String VSvar3;
    public static String VSvar4;
    public static String VSvar5;
    //ActivityVertex
    //TODO: Change to array for scalability
    public static String AVatt1;
    public static String AVatt2;
    public static String AVatt3;
    public static String AVatt4;
    public static String AVatt5;
    public static String AVatt6;
    public static String AVval1;
    public static String AVval2;
    public static String AVval3;
    public static String AVval4;
    public static String AVval5;
    public static String AVval6;

    public static void Initialize() {
        //Initialize Interface Filters
        filterList = new String[]{"Neutral", "Credits", "Quality", "Progress", "Aid", "Val", "Discovery", "Repair", "Bug", "TC", "Morale", "Stamina", "Prototype", "Negotiation"};
        length = filterList.length;
        GraphFrame.FilterList.setListData(filterList);
        //Edges
        //Attribute to define if any edge will use a dif collapse function to compute collapsed value
        //(which is: Multiply values instead of adding)
        EinfTypeNotAdd = new String[length];
        EinfTypeNotAdd[0] = "Aid";
        for (int i = 1; i < length; i++) {
            EinfTypeNotAdd[i] = "Not_Used";
        }
        //Edge Stroke
        EdgeStroke = new boolean[length];
        //Edge Stroke
        EdgeStroke[0] = false;
        for (int i = 1; i < length; i++) {
            EdgeStroke[i] = true;
        }
        //Temporal Layout
        TLspecialVertexType = "Project";
        //Initialize Vertex Paint Modes
        DefaultMode def = new DefaultMode("Default");
        vertexModes.add(def);
        AttributeMode morale = new AttributeMode("Morale");
        vertexModes.add(morale);
        AttributeMode stamina = new AttributeMode("Stamina");
        vertexModes.add(stamina);
        InvertedAttributeMode hours = new InvertedAttributeMode("Hours", 6, 12);
        vertexModes.add(hours);
        EntityAttributeMode credits = new EntityAttributeMode("Credits");
        vertexModes.add(credits);
        String[] roles = new String[]{"Analyst", "Architect", "Manager", "Marketing", "Programmer", "Tester"};
        MultiAttributeMode role = new MultiAttributeMode("Role", roles);
        vertexModes.add(role);
        WeekendMode week = new WeekendMode("Weekend", "Sat", "Sun");
        vertexModes.add(week);

        String[] items = new String[10];
        int j = 0;
        for (VertexPaintMode mode : vertexModes) {
            items[j] = mode.GetName();
            j++;
        }
        GraphFrame.StatusFilterBox.setModel(new DefaultComboBoxModel(items));

        //TODO: See at variables declarations
        //Vertex Stroke variables
        //It evaluates an attribute (VSattribute) and changes the vertex stroke if the value equals to
        //any of the defined values (VSvar1 to VSvar5)
        VSattribute = "Role";
        VSvar1 = "Idle";
        VSvar2 = "Fired";
        VSvar3 = "Promotion";
        VSvar4 = "Training";
        VSvar5 = "Hired";

        //ActivityVertex
        //These attributes are used to distinguish vertex color. 
        //The specific values used to change the color (for each attribute)
        //are below

        //TODO: Make arrays instead for scalability
        AVatt1 = "Task";
        AVatt2 = "Role";
        AVatt3 = "Morale";
        AVatt4 = "Stamina";
        AVatt5 = "Hours";
        AVatt6 = "Credits";
        //These are the corresponding values for each attribute
        AVval1 = "Idle";
        AVval2 = "Training";
        AVval3 = "Fired";
        AVval4 = "Promotion";
        AVval5 = "Hired";
        AVval6 = "Negotiation";
    }
}
