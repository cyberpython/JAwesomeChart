/*
 * The MIT License
 *
 * Copyright 2012 Georgios Migdos <cyberpython@gmail.com>.
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
package com.gmigdos.jawesomechart.renderers.twodimensional.horizontal;

import com.gmigdos.jawesomechart.JAwesomeChart;
import com.gmigdos.jawesomechart.core.DataSeriesDataProvider;
import com.gmigdos.jawesomechart.core.DataSeriesList;
import com.gmigdos.jawesomechart.core.Labels;
import com.gmigdos.jawesomechart.util.Context2D;
import com.gmigdos.jawesomechart.util.StringsProvider;
import com.gmigdos.jawesomechart.util.Utilities;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Stroke;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Path2D;
import java.io.File;

/**
 *
 * @author Georgios Migdos <cyberpython@gmail.com>
 */
public class LineChartRenderer extends Basic2DHorizontalChartRenderer{
    
    private Stroke lineStroke;
    private float lineOpacity;
    private float pointOpacity;
    private double pointRadius;
    private boolean drawLines;
    private boolean drawPoints;

    public LineChartRenderer() {
        this.lineStroke = new BasicStroke(4.0f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_MITER);
        this.lineOpacity = 1.0f;
        this.pointOpacity = 1.0f;
        this.pointRadius = 6;
        this.drawLines = true;
        this.drawPoints = false;
    }
    
    /**
     * @return the lineStroke
     */
    public Stroke getLineStroke() {
        return lineStroke;
    }

    /**
     * @param lineStroke the lineStroke to set
     */
    public void setLineStroke(Stroke lineStroke) {
        this.lineStroke = lineStroke;
    }

    /**
     * @return the lineOpacity
     */
    public float getLineOpacity() {
        return lineOpacity;
    }

    /**
     * @param lineOpacity the lineOpacity to set
     */
    public void setLineOpacity(float lineOpacity) {
        this.lineOpacity = lineOpacity;
    }

    /**
     * @return the pointOpacity
     */
    public float getPointOpacity() {
        return pointOpacity;
    }

    /**
     * @param pointOpacity the pointOpacity to set
     */
    public void setPointOpacity(float pointOpacity) {
        this.pointOpacity = pointOpacity;
    }

    /**
     * @return the pointRadius
     */
    public double getPointRadius() {
        return pointRadius;
    }

    /**
     * @param pointRadius the pointRadius to set
     */
    public void setPointRadius(double pointRadius) {
        this.pointRadius = pointRadius;
    }

    /**
     * @return the drawLines
     */
    public boolean isDrawLinesOn() {
        return drawLines;
    }

    /**
     * @param drawLines the drawLines to set
     */
    public void setDrawLines(boolean drawLines) {
        this.drawLines = drawLines;
    }

    /**
     * @return the drawPoints
     */
    public boolean isDrawPointsOn() {
        return drawPoints;
    }

    /**
     * @param drawPoints the drawPoints to set
     */
    public void setDrawPoints(boolean drawPoints) {
        this.drawPoints = drawPoints;
    }
    

    @Override
    public String getHumanReadableName() {
        return StringsProvider.CHART_RENDERER_LINE;
    }

    @Override
    public void draw(Context2D context, DataSeriesList series, Labels labels) {
        super.draw(context, series, labels);
        
        if(isDrawLinesOn() || isDrawPointsOn()){
        
            context.save();

                if (isShadowsOn()) {
                    context.beginShadowedDrawing();
                }

                    double height = context.getHeight(); //- offsetTopForNameAndValue - offsetBottomForNameAndValue;
                    double width = context.getWidth();
                    int maxNumberOfPoints = series.getMaxDataSeriesLength();

                    double step = width / maxNumberOfPoints;
                    double halfStep = step/2;
                    double minData = series.getMinValue();
                    double maxData = series.getMaxValue();
                    double dataDistance = getDataDistance(maxData, minData);

                    double value;
                    double y;
                    boolean lastValueNull = false;
                    DataSeriesDataProvider dp;

                    Color dataSeriesColor;
                    double diameter = 2 * getPointRadius();

                    context.setStroke(getLineStroke());
                    context.translate(halfStep, 0);

                    for (int i=0; i<series.size(); i++) {
                        
                        dataSeriesColor = series.get(i).getSeriesColor();
                        
                        if(isDrawLinesOn()){
                            context.setPaint(dataSeriesColor, getLineOpacity());

                            Path2D path = new Path2D.Double();
                            int x = 0;
                            do {
                                dp = series.get(i).get(x);
                            } while (dp == null);

                            if (dp != null) {
                                y = dp.getDataValue() * height / dataDistance;
                                path.moveTo(0, -y);

                                while (x < series.get(i).size()) {
                                    dp = series.get(i).get(x);
                                    if (dp != null) {
                                        y = dp.getDataValue() * height / dataDistance;
                                        if (lastValueNull) {
                                            path.moveTo(x * step, -y);
                                        } else {
                                            path.lineTo(x * step, -y);
                                        }
                                        lastValueNull = false;
                                    } else {
                                        lastValueNull = true;
                                    }
                                    x++;
                                }
                            }
                            context.draw(path);
                        }
                        
                        
                        // Draw the point:
                        if (isDrawPointsOn()) {
                            context.setPaint(dataSeriesColor, getPointOpacity());
                            for (int x = 0; x < series.get(i).size(); x++) {
                                dp = series.get(i).get(x);
                                if (dp != null) {
                                    y = dp.getDataValue() * height / dataDistance;
                                    Ellipse2D point = new Ellipse2D.Double(x * step - getPointRadius(), -y - getPointRadius(), diameter, diameter);
                                    context.fill(point);
                                }
                            }
                        }
                        
                        

                    }
                    
                    
                    


                if (isShadowsOn()) {
                    context.endShadowedDrawing();
                }

            context.restore();
        }
        
    }
    
    public static void main(String[] args) {
        
        JAwesomeChart ac = new JAwesomeChart(600, 500);
        LineChartRenderer renderer = new LineChartRenderer();
        ac.setRenderer(renderer);

        ac.setTitle("Dekstop Browser Market Share 2011");
        renderer.setValueAxisCaption("Market share (%)");
        renderer.setLabelAxisCaption("Month");

        ac.setLabels(new String[]{"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"});
        ac.addSeries("IE", new double[]{46, 45.44, 45.11, 44.52, 43.87, 43.58, 42.45, 41.89, 41.66, 40.18, 40.63, 38.65}, Color.decode("0x443769"));
        ac.addSeries("Firefox", new double[]{30.68, 30.37, 29.98, 29.67, 29.29, 28.34, 27.95, 27.49, 26.79, 26.39, 25.23, 25.27}, Color.decode("0xFF2400"));
        ac.addSeries("Chrome", new double[]{15.68, 16.54, 17.37, 18.29, 19.36, 20.65, 22.14, 23.16, 23.61, 25, 25.69, 27.27}, Color.decode("0x4A83BD"));
        ac.addSeries("Safari", new double[]{5.09, 5.08, 5.02, 5.04, 5.01, 5.07, 5.17, 5.19, 5.6, 5.93, 5.92, 6.08}, Color.decode("0xFF8300"));
        ac.addSeries("Opera", new double[]{2, 2, 1.97, 1.91, 1.84, 1.74, 1.66, 1.67, 1.72, 1.81, 1.82, 1.98}, Color.decode("0xFF0096"));
        ac.addSeries("Other", new double[]{0.55, 0.55, 0.54, 0.57, 0.63, 0.61, 0.63, 0.61, 0.62, 0.69, 0.71, 0.75}, Color.decode("0x236A14"));

        JAwesomeChart.drawAndSaveChartAsImageFile(ac, "/home/cyberpython/temp/awesomechart/07-line-chart.png");
        
        renderer.setDrawPoints(true);
        
        JAwesomeChart.drawAndSaveChartAsImageFile(ac, "/home/cyberpython/temp/awesomechart/08-line-chart.png");
        
        renderer.setDrawLines(false);
        
        JAwesomeChart.drawAndSaveChartAsImageFile(ac, "/home/cyberpython/temp/awesomechart/09-line-chart.png");
        
        Utilities.xdgOpenFile(new File("/home/cyberpython/temp/awesomechart/07-line-chart.png"));
        
    }
}
