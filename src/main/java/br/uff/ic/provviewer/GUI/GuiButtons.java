/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.ic.provviewer.GUI;

import br.uff.ic.provviewer.BasePath;
import br.uff.ic.provviewer.Collapser;
import br.uff.ic.provviewer.Variables;
import br.uff.ic.provviewer.Edge.Edge;
import br.uff.ic.provviewer.Filter.Filters;
import static br.uff.ic.provviewer.GraphFrame.StatusFilterBox;
import br.uff.ic.provviewer.Input.Config;
import br.uff.ic.provviewer.Layout.Spatial_Layout;
import br.uff.ic.provviewer.Layout.Temporal_Layout;
import br.uff.ic.provviewer.Vertex.AgentVertex;
import br.uff.ic.provviewer.Vertex.ColorScheme.VertexPainter;
import edu.uci.ics.jung.algorithms.layout.CircleLayout;
import edu.uci.ics.jung.algorithms.layout.FRLayout;
import edu.uci.ics.jung.algorithms.layout.FRLayout2;
import edu.uci.ics.jung.algorithms.layout.ISOMLayout;
import edu.uci.ics.jung.algorithms.layout.KKLayout;
import edu.uci.ics.jung.visualization.Layer;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import edu.uci.ics.jung.visualization.control.DefaultModalGraphMouse;
import edu.uci.ics.jung.visualization.control.ModalGraphMouse;
import edu.uci.ics.jung.visualization.decorators.EdgeShape;
import edu.uci.ics.jung.visualization.decorators.ToStringLabeller;
import edu.uci.ics.jung.visualization.picking.PickedInfo;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.util.Collection;
import java.util.HashSet;
import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import org.apache.commons.collections15.Transformer;

/**
 *
 * @author Kohwalter
 */
public class GuiButtons {
    public static void Exit()
    {
        System.exit(0);
    }
    public static void Expand(Collapser collapser, Variables variables, Filters filter)
    {
        Collection picked = new HashSet(variables.view.getPickedVertexState().getPicked());
        collapser.Expander(variables, filter, picked);
    }
    public static void Collapse(Collapser collapser, Variables variables, Filters filter)
    {
        Collection picked = new HashSet(variables.view.getPickedVertexState().getPicked());
        collapser.Collapse(variables, filter, picked, true);
    }
    public static void Reset(Collapser collapser, Variables variables, Filters filter)
    {
        collapser.ResetGraph(variables, filter);
    }
    public static void MouseModes(DefaultModalGraphMouse mouse, JComboBox MouseModes)
    {
        String mode = (String)MouseModes.getSelectedItem();
        if(mode.equalsIgnoreCase("Picking"))
        {
             mouse.setMode(ModalGraphMouse.Mode.PICKING);
        }
        if(mode.equalsIgnoreCase("Transforming"))
        {
             mouse.setMode(ModalGraphMouse.Mode.TRANSFORMING);
        }
    }
    public static void CollapseAgent(Collapser collapser, Variables variables, Filters filter)
    {
        PickedInfo<Object> picked_state;
        picked_state = variables.view.getPickedVertexState();
        Object node = null;
        //Get the selected node
        for(Object z : variables.layout.getGraph().getVertices())
        {
            if (picked_state.isPicked(z))
            {
                node = z;
            }
        }
        //Select the node and its neighbors to be collapsed
        if(variables.layout.getGraph().getNeighbors(node) != null)
        {
            Collection picked = new HashSet(variables.layout.getGraph().getNeighbors(node));
            picked.add(node);
            if(!(node instanceof AgentVertex)) 
            {
                picked.removeAll(picked);
            }   
            collapser.Collapse(variables, filter, picked, true);
        }
    }
    public static void Filter(Collapser collapser, Variables variables, Filters filter)
    {
        collapser.Filters(variables, filter);
    }
    public static void EdgeLineMode(JComboBox EdgeLineShapeSelection, Variables variables)
    {
        String mode = (String)EdgeLineShapeSelection.getSelectedItem();
        if(mode.equalsIgnoreCase("QuadCurve"))
        {
             variables.view.getRenderContext().setEdgeShapeTransformer(new EdgeShape.QuadCurve<Object,Edge>());
        }
        if(mode.equalsIgnoreCase("Line"))
        {
             variables.view.getRenderContext().setEdgeShapeTransformer(new EdgeShape.Line<Object,Edge>());
        }
        variables.view.repaint();
    }
    public static void StatusFilter(Variables variables)
    {
        VertexPainter.VertexPainter((String)StatusFilterBox.getSelectedItem(), variables.view, variables);
        variables.view.repaint();
    }
    public static void EdgeTextDisplay(Variables variables, Boolean ShowEdgeTextButton)
    {
        if(ShowEdgeTextButton)
        {
           variables.view.getRenderContext().setEdgeLabelTransformer(new ToStringLabeller<Edge>()); 
        }
        else
        {
            //Show nothing
            variables.view.getRenderContext().setEdgeLabelTransformer(new Transformer<Edge, String>() {

                @Override
                public String transform(Edge i) {
                    return "";
                }
            });
        }
        variables.view.repaint();
    }
    public static void LayoutSelection(Variables variables, JComboBox Layouts)
    {
        String layout = (String) Layouts.getSelectedItem();
        if (layout.equalsIgnoreCase("CircleLayout")) {
            variables.layout = new CircleLayout<Object, Edge>(variables.layout.getGraph());
        }
        if (layout.equalsIgnoreCase("FRLayout")) {
            variables.layout = new FRLayout<Object, Edge>(variables.layout.getGraph());
        }
        if (layout.equalsIgnoreCase("FRLayout2")) {
            variables.layout = new FRLayout2<Object, Edge>(variables.layout.getGraph());
        }
        if (layout.equalsIgnoreCase("TemporalLayout")) {
            variables.layout = new Temporal_Layout<Object, Edge>(variables.layout.getGraph());
        }
        if (layout.equalsIgnoreCase("SpatialLayout")) {
            variables.layout = new Spatial_Layout<Object, Edge>(variables.layout.getGraph());
        }
        if (layout.equalsIgnoreCase("ISOMLayout")) {
            variables.layout = new ISOMLayout<Object, Edge>(variables.layout.getGraph());
        }
        if (layout.equalsIgnoreCase("KKLayout")) {
            variables.layout = new KKLayout<Object, Edge>(variables.layout.getGraph());
        }
        InitBackground(variables, Layouts);
        variables.view.setGraphLayout(variables.layout);
        variables.view.repaint();
    }
    
    private static void InitBackground(final Variables variables, final JComboBox Layouts) {
        final ImageIcon whiteIcon = new ImageIcon(BasePath.getBasePathForClass(GuiButtons.class) + "/images/White.png");

        ImageIcon mapIcon = null;
        try {
            mapIcon
                    = new ImageIcon(BasePath.getBasePathForClass(GuiButtons.class) + Config.imageLocation);
        } catch (Exception ex) {
            System.err.println("Can't load \"" + Config.imageLocation + "\"");
        }
        
        final ImageIcon icon = mapIcon;
        final int offsetX = (int) ((-icon.getIconWidth() * 0.5) - (Config.imageOffsetX * Config.coordinatesScale));
        final int offsetY = (int) ((-icon.getIconHeight() *  0.5) + (Config.imageOffsetY * Config.coordinatesScale));
        
        if (icon != null) {
                variables.view.addPreRenderPaintable(new VisualizationViewer.Paintable() {
                    public void paint(Graphics g) {
                        Graphics2D g2d = (Graphics2D) g;
                        AffineTransform oldXform = g2d.getTransform();
                        AffineTransform lat
                                = variables.view.getRenderContext().getMultiLayerTransformer().getTransformer(Layer.LAYOUT).getTransform();
                        AffineTransform vat
                                = variables.view.getRenderContext().getMultiLayerTransformer().getTransformer(Layer.VIEW).getTransform();
                        AffineTransform at = new AffineTransform();
                        at.concatenate(g2d.getTransform());
                        at.concatenate(vat);
                        at.concatenate(lat);
                        g2d.setTransform(at);
                        if (Layouts.getSelectedItem().equals("SpatialLayout")) {
                            g.drawImage(icon.getImage(), offsetX, offsetY,
                                icon.getIconWidth(), icon.getIconHeight(), variables.view);
                        }
                        else
                        {
                            g.drawImage(whiteIcon.getImage(), offsetX, offsetY,
                                icon.getIconWidth(), icon.getIconHeight(), variables.view);
                        }
                        g2d.setTransform(oldXform);
                    }

                    public boolean useTransform() {
                        return false;
                    }
                });
        } 
    }
}