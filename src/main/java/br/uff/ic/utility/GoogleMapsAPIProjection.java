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

package br.uff.ic.utility;

import java.awt.geom.Point2D;

/**
 *
 * @author Kohwalter
 */
public class GoogleMapsAPIProjection {
    private final double PixelTileSize = 256d;
    private final double DegreesToRadiansRatio = 180d / Math.PI;
    private final double RadiansToDegreesRatio = Math.PI / 180d;
    private final Point2D PixelGlobeCenter;
    private final double XPixelsToDegreesRatio;
    private final double YPixelsToRadiansRatio;

    public GoogleMapsAPIProjection(double zoomLevel)
    {
        double pixelGlobeSize = this.PixelTileSize * Math.pow(2d, zoomLevel);
        this.XPixelsToDegreesRatio = pixelGlobeSize / 360d;
        this.YPixelsToRadiansRatio = pixelGlobeSize / (2d * Math.PI);
        float halfPixelGlobeSize = (float) (pixelGlobeSize / 2d);
        this.PixelGlobeCenter = new Point2D.Float(
            halfPixelGlobeSize, halfPixelGlobeSize);
    }

    public Point2D FromCoordinatesToPixel(float x1, float y1)
    {
        double x = Math.round(this.PixelGlobeCenter.getX()
            + (x1 * this.XPixelsToDegreesRatio));
        double f = Math.min(
            Math.max(
                 Math.sin(y1 * RadiansToDegreesRatio),
                -0.9999d),
            0.9999d);
        double y = Math.round(this.PixelGlobeCenter.getY() + .5d * 
            Math.log((1d + f) / (1d - f)) * -this.YPixelsToRadiansRatio);
        return new Point2D.Double(x, y);
    }

//    public PointF FromPixelToCoordinates(PointF pixel)
//    {
//        var longitude = (pixel.X - this.PixelGlobeCenter.X) /
//            this.XPixelsToDegreesRatio;
//        var latitude = (2 * Math.Atan(Math.Exp(
//            (pixel.Y - this.PixelGlobeCenter.Y) / -this.YPixelsToRadiansRatio))
//            - Math.PI / 2) * DegreesToRadiansRatio;
//        return new PointF(
//            Convert.ToSingle(latitude),
//            Convert.ToSingle(longitude));
//    }
}
