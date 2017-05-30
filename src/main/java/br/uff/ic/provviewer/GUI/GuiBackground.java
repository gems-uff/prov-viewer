/*
 * The MIT License
 *
 * Copyright 2017 Kohwalter.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package br.uff.ic.provviewer.GUI;

import br.uff.ic.provviewer.VariableNames;
import br.uff.ic.utility.IO.BasePath;
import br.uff.ic.provviewer.Variables;
import br.uff.ic.utility.GoogleMapsAPIProjection;
import edu.uci.ics.jung.visualization.Layer;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.io.File;
import javax.swing.ImageIcon;
import javax.swing.JComboBox;

/**
 * Class responsible for loading an image in the background
 *
 * @author Kohwalter
 */
public class GuiBackground {

    /**
     * Method to paint the background with the image from the configuration file
     *
     * @param variables
     * @param Layouts
     */
    public void InitBackground(final Variables variables, final JComboBox Layouts) {
        final ImageIcon whiteIcon = new ImageIcon(BasePath.getBasePathForClass(GuiBackground.class) + File.separator + "images" + File.separator + "White.png");

        ImageIcon mapIcon = null;
        if (!variables.config.imageLocation.equals("")) {
            try {
                mapIcon
                        = new ImageIcon(BasePath.getBasePathForClass(GuiBackground.class) + variables.config.imageLocation);
            } catch (Exception ex) {
                System.err.println("Can't load \"" + variables.config.imageLocation + "\"");
            }
        }
        final ImageIcon icon = mapIcon;
        variables.view.addPreRenderPaintable(new VisualizationViewer.Paintable() {
            @Override
            public void paint(Graphics g) {
                if (icon == null) {
                    ResetBackground(g, variables, whiteIcon);
                } else {
                    Background(g, variables, Layouts, icon, whiteIcon);
                }
            }

            @Override
            public boolean useTransform() {
                return false;
            }
        });
    }

    /**
     * Method to bake the image in the background
     *
     * @param g
     * @param variables
     * @param Layouts
     * @param icon is the image
     * @param whiteIcon is the default (white) background
     */
    public void Background(Graphics g, Variables variables, final JComboBox Layouts,
            final ImageIcon icon, final ImageIcon whiteIcon) {
        final double offsetX;
        final double offsetY;
        if (variables.config.orthogonal) {
            offsetX = (int) ((-icon.getIconWidth() * 0.5) - (variables.config.imageOffsetX * variables.config.coordinatesScale));
            offsetY = (int) ((-icon.getIconHeight() * 0.5) + (variables.config.imageOffsetY * variables.config.coordinatesScale));
        } else {
            GoogleMapsAPIProjection googleAPI = new GoogleMapsAPIProjection(variables.config.googleZoomLevel);
            Point2D coord = googleAPI.FromCoordinatesToPixel((float) variables.config.imageOffsetX, (float) variables.config.imageOffsetY);
            offsetX = coord.getX();
            offsetY = coord.getY();
        }
        Graphics2D g2d = (Graphics2D) g.create();
        AffineTransform oldXform = g2d.getTransform();
        SetTransform(g2d, variables);
        if (Layouts.getSelectedItem().equals(VariableNames.layout_spatial)) {
            DrawImage(g2d, variables, icon, (int) offsetX, (int) offsetY);
        } else {
            DrawImage(g2d, variables, whiteIcon, (int) offsetX, (int) offsetY);
        }
        g2d.setTransform(oldXform);
    }

    /**
     * Method to clear the background with the default white image
     *
     * @param g
     * @param variables
     * @param whiteIcon is the default background image
     */
    public void ResetBackground(Graphics g, Variables variables, final ImageIcon whiteIcon) {
        Graphics2D g2d = (Graphics2D) g;
        AffineTransform oldXform = g2d.getTransform();
        SetTransform(g2d, variables);
        g.drawImage(whiteIcon.getImage(), -100000, -100000,
                10000000, 10000000, variables.view);
        g2d.setTransform(oldXform);
    }

    /**
     * Method to set the background image transform
     *
     * @param g2d is the Graphics2D used by the image
     * @param variables
     */
    public void SetTransform(Graphics2D g2d, Variables variables) {
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

    /**
     * Method to draw the image in the background
     *
     * @param g is the Graphics 2D for the image
     * @param variables
     * @param icon is the image
     * @param offsetX image X offset in the frame
     * @param offsetY image Y offset in the frame
     */
    public void DrawImage(Graphics g, Variables variables,
            final ImageIcon icon, final int offsetX, final int offsetY) {
        g.drawImage(icon.getImage(), offsetX, offsetY,
                icon.getIconWidth(), icon.getIconHeight(), variables.view);
    }
}
