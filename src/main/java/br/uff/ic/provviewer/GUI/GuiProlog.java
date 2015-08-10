/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.ic.provviewer.GUI;

import br.uff.ic.XMLConverter.XMLConverter;
import br.uff.ic.provviewer.BasePath;
import static br.uff.ic.provviewer.GraphFrame.StatusFilterBox;
import br.uff.ic.provviewer.Inference.PrologInference;
import br.uff.ic.provviewer.Variables;
import java.io.File;
import javax.swing.JToggleButton;

/**
 * Class responsible for the PROLOG implementation
 * @author Kohwalter
 */
public class GuiProlog {
    
    /**
     * Method to enable the prolog functions
     * @param prolog is the prolog implementation for the automatic inference
     * @param prologIsInitialized is the boolean to check if the prolog can be activated
     * @param InitPrologButton is the boolean to check if the prolog can be activated
     */
    public static void InitializeProlog(PrologInference prolog, boolean prologIsInitialized, JToggleButton InitPrologButton)
    {
        if(InitPrologButton.isSelected() && !prologIsInitialized)
        {
            prologIsInitialized = true;
            prolog.Init();
        }
    }
    
    /**
     * Method to generate the prolog facts using the XML-Inference
     * @param initialGraph
     * @param file
     * @param demo 
     */
    public static void GeneratePrologFacts(boolean initialGraph, File file, String demo)
    {
        if(initialGraph)
        {
            file = new File(BasePath.getBasePathForClass(GuiProlog.class) + demo);
            initialGraph = false;
        }
        ConvertXML(file);
        System.out.println("Finished Converting to Prolog");
    }
    
    /**
     * Method to convert the XML to prolog facts
     * @param file the XML file for conversion
     */
    private static void ConvertXML(File file)
    {
        XMLConverter xmlConv = new XMLConverter();
        xmlConv.ConvertXMLtoProlog(file);
    }
    
    /**
     * Method to apply the similarity collapse in the graph
     * @param InitPrologButton is the boolean to check if the prolog is active
     * @param prolog is the prolog implementation
     * @param variables
     * @param edgeType is the type of edge used for the collapse
     */
    public static void SimilarityCollapse(boolean InitPrologButton, PrologInference prolog, Variables variables, String edgeType)
    {
        if(InitPrologButton)
        {
            System.out.println("Starting Prolog Inference: " + edgeType);
            GuiButtons.Reset(variables);
            String list;
            list = prolog.QueryCollapse((String)StatusFilterBox.getSelectedItem(), edgeType);
            System.out.println("Collapsing...");
            variables.collapser.CollapseIrrelevant(variables, list, edgeType);
            System.out.println("Finished Collapsing");
        }
    }
}
