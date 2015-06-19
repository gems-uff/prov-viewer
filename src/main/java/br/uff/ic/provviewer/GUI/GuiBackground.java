/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.ic.provviewer.GUI;

import br.uff.ic.provviewer.BasePath;
import br.uff.ic.provviewer.GraphFrame;
import br.uff.ic.provviewer.Input.Config;
import br.uff.ic.provviewer.Variables;
import edu.uci.ics.jung.visualization.Layer;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import javax.swing.ImageIcon;
import javax.swing.JComboBox;

/**
 *
 * @author Kohwalter
 */
public class GuiBackground {
    public static void InitBackground(final Variables variables, final JComboBox Layouts) {
//        final ImageIcon whiteIcon = new ImageIcon(getClass().getResource("/images/White.png"));
//        System.out.println("ImageIcon: " + BasePath.getBasePathForClass(GraphFrame.class) + "/images/White.png");
        final ImageIcon whiteIcon = new ImageIcon(BasePath.getBasePathForClass(GraphFrame.class) + "/images/White.png");

        ImageIcon mapIcon = null;
        //Config.imageLocation = "/images/AngrybotsMap.png";
        try {
            mapIcon
//                    = new ImageIcon(getClass().getResource(Config.imageLocation));
                    = new ImageIcon(BasePath.getBasePathForClass(GraphFrame.class) + Config.imageLocation);
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
