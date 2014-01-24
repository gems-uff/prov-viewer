/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.ic.Prov_Viewer;

import javax.swing.DefaultComboBoxModel;

/**
 *
 * @author Kohwalter
 */
public class Config {
    public static String[] filterList;
    //Vertex Stroke variables
    public static String VSattribute;
    public static String VSvar1;
    public static String VSvar2;
    public static String VSvar3;
    public static String VSvar4;
    public static String VSvar5;
    
    //Edge Stroke
    public static boolean[] EdgeStroke;
//    public static boolean ES01;
//    public static boolean ES02;
//    public static boolean ES03;
//    public static boolean ES04;
//    public static boolean ES05;
//    public static boolean ES06;
//    public static boolean ES07;
//    public static boolean ES08;
//    public static boolean ES09;
//    public static boolean ES10;
//    public static boolean ES11;
//    public static boolean ES12;
//    public static boolean ES13;
    //ActivityVertex
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
    //Weekend
    public static String VPMsaturday;
    public static String VPMsunday;
    //Modes
    public static String VPMattMode1;
    public static String VPMattMode2;
    public static String VPMattMode3;
    public static String VPMattMode4;
    public static String VPMattMode5;
    //Mode 4 variables
    public static String VPMattType;
    public static boolean VPMattTypeMax;
    //Mode 5 variables
    public static String VPMattValue1;
    public static String VPMattValue2;
    public static String VPMattValue3;
    public static String VPMattValue4;
    public static String VPMattValue5;
    public static String VPMattValue6;
    //Edge
    public static String[] EinfTypeNotAdd;
//    public static String EinfTypeNotAdd01;
//    public static String EinfTypeNotAdd02;
//    public static String EinfTypeNotAdd03;
//    public static String EinfTypeNotAdd04;
//    public static String EinfTypeNotAdd05;
//    public static String EinfTypeNotAdd06;
//    public static String EinfTypeNotAdd07;
//    public static String EinfTypeNotAdd08;
//    public static String EinfTypeNotAdd09;
//    public static String EinfTypeNotAdd10;
//    public static String EinfTypeNotAdd11;
//    public static String EinfTypeNotAdd12;
//    public static String EinfTypeNotAdd13;
    
    //Temporal Layout
    public static String TLspecialVertexType;
    public static int length;
    

    public static void Initialize() {
        //Initialize Interface Filters
        filterList = new String[]{ "Neutral", "Credits", "Quality", "Progress", "Aid", "Val", "Discovery", "Repair", "Bug", "TC", "Morale", "Stamina", "Prototype", "Negotiation" };
        length = filterList.length;
        GraphFrame.FilterList.setListData(filterList);
        
//        GraphFrame.FilterEdgeNeutral.setText("Neutral");
//        GraphFrame.FilterEdge01.setText("Credits");
//        GraphFrame.FilterEdge02.setText("Quality");
//        GraphFrame.FilterEdge03.setText("Progress");
//        GraphFrame.FilterEdge04.setText("Aid");
//        GraphFrame.FilterEdge05.setText("Val");
//        GraphFrame.FilterEdge06.setText("Discovery");
//        GraphFrame.FilterEdge07.setText("Repair");
//        GraphFrame.FilterEdge08.setText("Bug");
//        GraphFrame.FilterEdge09.setText("TC");
//        GraphFrame.FilterEdge10.setText("Morale");
//        GraphFrame.FilterEdge11.setText("Stamina");
//        GraphFrame.FilterEdge12.setText("Prototype");
//        GraphFrame.FilterEdge13.setText("Negotiation");
        //Initialize Interface Status Filter Box (Display Mode labels)
        String[] items = {"Default", "Morale", "Stamina", "Hours", "Credits", "Role", "Weekend"};
        GraphFrame.StatusFilterBox.setModel(new DefaultComboBoxModel(items));

        //Vertex Stroke variables
        //It evaluates an attribute (VSattribute) and changes the vertex stroke if the value equals to
        //any of the defined values (VSvar1 to VSvar5)
        VSattribute = "Role";
        VSvar1 = "Idle";
        VSvar2 = "Fired";
        VSvar3 = "Promotion";
        VSvar4 = "Training";
        VSvar5 = "Hired";
        
        EdgeStroke = new boolean[length];
        //Edge Stroke
        EdgeStroke[0] = false;
        for (int i = 1; i < length; i++)
        {
            EdgeStroke[i] = true;
        }
//        ES01 = false;
//        ES02 = true;
//        ES03 = true;
//        ES04 = true;
//        ES05 = true;
//        ES06 = true;
//        ES07 = true;
//        ES08 = true;
//        ES09 = true;
//        ES10 = true;
//        ES11 = true;
//        ES12 = true;
//        ES13 = true;
        //ActivityVertex
        //These attributes are used to distinguish vertex color. 
        //The specific values used to change the color (for each attribute)
        //are below
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
        
        //Weekend Days (Saturday/Sunday)
        //How saturday and sunday are spelled for the display mode #4
        VPMsaturday = "Sat";
        VPMsunday = "Sun";

        //Display Mode attributes used
        VPMattMode1 = "Morale";
        VPMattMode2 = "Stamina";
        VPMattMode3 = "Hours";
        VPMattMode4 = "Credits";
        VPMattMode5 = "Role";
        
        //Mode 4
        VPMattType = "Credits";
        VPMattTypeMax = true;
        
        //Mode5
        //7 dif types of colors depending on the value
        VPMattValue1 = "Analyst";
        VPMattValue2 = "Architect";
        VPMattValue3 = "Manager";
        VPMattValue4 = "Marketing";
        VPMattValue5 = "Programmer";
        VPMattValue6 = "Tester";

        //Edge
        //Attribute to define if any edge  
        // will use a dif collapse function to compute collapsed value
        //(Multiply values instead of adding)
        EinfTypeNotAdd = new String[length];
        EinfTypeNotAdd[0] = "Aid";
        for (int i = 1; i < length; i++)
        {
            EinfTypeNotAdd[i] = "Not_Used";
        }
//        EinfTypeNotAdd01 = "Aid";
//        EinfTypeNotAdd02 = "Not_Used";
//        EinfTypeNotAdd03 = "Not_Used";
//        EinfTypeNotAdd04 = "Not_Used";
//        EinfTypeNotAdd05 = "Not_Used";
//        EinfTypeNotAdd06 = "Not_Used";
//        EinfTypeNotAdd07 = "Not_Used";
//        EinfTypeNotAdd08 = "Not_Used";
//        EinfTypeNotAdd09 = "Not_Used";
//        EinfTypeNotAdd10 = "Not_Used";
//        EinfTypeNotAdd11 = "Not_Used";
//        EinfTypeNotAdd12 = "Not_Used";
//        EinfTypeNotAdd13 = "Not_Used";

        //Temporal Layout
        TLspecialVertexType = "Project";
    }
}
