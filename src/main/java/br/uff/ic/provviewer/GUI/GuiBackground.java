/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.ic.provviewer.GUI;

import br.uff.ic.provviewer.BasePath;
import br.uff.ic.provviewer.Variables;
import edu.uci.ics.jung.visualization.Layer;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.io.File;
import javax.swing.ImageIcon;
import javax.swing.JComboBox;

/**
 *
 * @author Kohwalter
 */
public class GuiBackground {

    public static void InitBackground(final Variables variables, final JComboBox Layouts) {
        final ImageIcon whiteIcon = new ImageIcon(BasePath.getBasePathForClass(GuiBackground.class) + File.separator + "images" + File.separator + "White.png");

        ImageIcon mapIcon = null;
        try {
            mapIcon
                    = new ImageIcon(BasePath.getBasePathForClass(GuiBackground.class) + variables.config.imageLocation);
        } catch (Exception ex) {
            System.err.println("Can't load \"" + variables.config.imageLocation + "\"");
        }

        final ImageIcon icon = mapIcon;
        if (icon != null) {
            final int offsetX = (int) ((-icon.getIconWidth() * 0.5) - (variables.config.imageOffsetX * variables.config.coordinatesScale));
            final int offsetY = (int) ((-icon.getIconHeight() * 0.5) + (variables.config.imageOffsetY * variables.config.coordinatesScale));
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
                    } else {
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
