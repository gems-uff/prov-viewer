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
                    ResetBackground(g, variables, whiteIcon);
                    Background(g, variables, Layouts, icon, offsetX, offsetY, whiteIcon);
                }

                public boolean useTransform() {
                    return false;
                }
            });
        }
    }

    public static void Background(Graphics g, Variables variables, final JComboBox Layouts,
            final ImageIcon icon, final int offsetX, final int offsetY, final ImageIcon whiteIcon) {
        Graphics2D g2d = (Graphics2D) g;
        AffineTransform oldXform = g2d.getTransform();
        SetTransform(g2d, variables);
        if (Layouts.getSelectedItem().equals("SpatialLayout")) {
            DrawImage(g, variables, icon, offsetX, offsetY);
        } else {
            DrawImage(g, variables, whiteIcon, offsetX, offsetY);
        }
        g2d.setTransform(oldXform);
    }

    public static void ResetBackground(Graphics g, Variables variables, final ImageIcon whiteIcon) {
        Graphics2D g2d = (Graphics2D) g;
        AffineTransform oldXform = g2d.getTransform();
        SetTransform(g2d, variables);
        g.drawImage(whiteIcon.getImage(), -100000, -100000,
                10000000, 10000000, variables.view);
        g2d.setTransform(oldXform);
    }
    
    public static void SetTransform(Graphics2D g2d, Variables variables) {
        AffineTransform lat
                = variables.view.getRenderContext().getMultiLayerTransformer().getTransformer(Layer.LAYOUT).getTransform();
        AffineTransform vat
                = variables.view.getRenderContext().getMultiLayerTransformer().getTransformer(Layer.VIEW).getTransform();
        AffineTransform at = new AffineTransform();
        at.concatenate(g2d.getTransform());
        at.concatenate(vat);
        at.concatenate(lat);
        g2d.setTransform(at);
    }

    public static void DrawImage(Graphics g, Variables variables,
            final ImageIcon icon, final int offsetX, final int offsetY) {
        g.drawImage(icon.getImage(), offsetX, offsetY,
                icon.getIconWidth(), icon.getIconHeight(), variables.view);
    }
}
