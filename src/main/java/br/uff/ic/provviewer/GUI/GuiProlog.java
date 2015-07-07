/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.ic.provviewer.GUI;

import br.uff.ic.XMLConverter.XMLConverter;
import br.uff.ic.provviewer.BasePath;
import br.uff.ic.provviewer.Collapser;
import br.uff.ic.provviewer.Filter.Filters;
import static br.uff.ic.provviewer.GraphFrame.StatusFilterBox;
import br.uff.ic.provviewer.Inference.PrologInference;
import br.uff.ic.provviewer.Variables;
import java.io.File;
import javax.swing.JToggleButton;

/**
 *
 * @author Kohwalter
 */
public class GuiProlog {
    public static void InitializeProlog(PrologInference testProlog, boolean prologIsInitialized, JToggleButton InitPrologButton)
    {
        if(InitPrologButton.isSelected() && !prologIsInitialized)
        {
            prologIsInitialized = true;
            testProlog.Init();
        }
    }
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
    private static void ConvertXML(File file)
    {
        XMLConverter xmlConv = new XMLConverter();
        xmlConv.ConvertXMLtoProlog(file);
    }
    public static void SimilarityInference(boolean InitPrologButton, PrologInference testProlog, Variables variables, String edgeType)
    {
        if(InitPrologButton)
        {
            System.out.println("Starting Prolog Inference: " + edgeType);
            GuiButtons.Reset(variables);
            String list;
            list = testProlog.QueryCollapse((String)StatusFilterBox.getSelectedItem(), edgeType);
            System.out.println("Collapsing...");
            variables.collapser.CollapseIrrelevant(variables, list, edgeType);
            System.out.println("Finished Collapsing");
        }
    }
}
