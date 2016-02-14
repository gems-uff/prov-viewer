/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.ic.utility;

import java.awt.geom.Point2D;

/**
 *
 * @author Kohwalter
 */
public class GoogleMapsAPIProjection {
    private double PixelTileSize = 256d;
    private double DegreesToRadiansRatio = 180d / Math.PI;
    private double RadiansToDegreesRatio = Math.PI / 180d;
    private Point2D PixelGlobeCenter;
    private double XPixelsToDegreesRatio;
    private double YPixelsToRadiansRatio;

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
