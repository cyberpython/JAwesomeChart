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
package com.gmigdos.jawesomechart.renderers;

import com.gmigdos.jawesomechart.JAwesomeChart;
import com.gmigdos.jawesomechart.core.DataSeries;
import com.gmigdos.jawesomechart.core.DataSeriesDataProvider;
import com.gmigdos.jawesomechart.core.DataSeriesList;
import com.gmigdos.jawesomechart.core.Labels;
import com.gmigdos.jawesomechart.core.Legend;
import com.gmigdos.jawesomechart.util.Context2D;
import com.gmigdos.jawesomechart.util.StringsProvider;
import com.gmigdos.jawesomechart.util.Utilities;
import java.awt.*;
import java.awt.geom.Arc2D;
import java.awt.geom.Ellipse2D;
import java.io.File;
import java.text.DecimalFormat;

/**
 *
 * @author Georgios Migdos <cyberpython@gmail.com>
 */
public class PieChartRenderer extends BaseChartRenderer{

    private Stroke pieBorderStroke;
    private Paint pieBorderColor;
    private float pieFillOpacity;
    private double startAngle;
    private double total;
    private boolean overrideTotal;
    private double explosionOffset;
    private boolean isDoughnut;

    public PieChartRenderer() {
        pieBorderColor = Color.WHITE;
        pieBorderStroke = new BasicStroke(2.0f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);
        pieFillOpacity = 1f;
        startAngle = 0;
        explosionOffset = 0;
        total = -1;
        overrideTotal = false;
        this.isDoughnut = false;
        setSeriesNameRenderingOn(false);
        setValueRenderingOn(false);
        setShadowBlurRadius(3);
        setShadowColor(new Color(0f, 0f, 0f, 0.3f));
    }

    public void setIsDoughnut(boolean isDoughnut) {
        this.isDoughnut = isDoughnut;
    }

    public boolean isDoughnut() {
        return isDoughnut;
    }    

    @Override
    public String getHumanReadableName() {
        return StringsProvider.CHART_RENDERER_PIE;
    }

    @Override
    public void draw(Context2D context, DataSeriesList series, Labels labels) {
        
        super.draw(context, series, labels);        
        boolean drawSeriesNamesEnabled = isSeriesNameRenderingOn();
        boolean drawValuesEnabled = isValueRenderingOn();
        double width = context.getWidth();
        double height = context.getHeight();        
        double startAngleBefore = startAngle;
        DecimalFormat format = getDecimalFormat();
        
        if (series.size() > 0) {
            context.save();
            
            boolean shadowsOn = isShadowsOn();
            if (shadowsOn) {
                context.beginShadowedDrawing();
            }
            
            double paddingTop = getPaddingTop();
            double paddingBottom = getPaddingBottom();
            double paddingLeft = getPaddingLeft();
            double paddingRight = getPaddingRight();
            
            context.translate(paddingLeft, paddingTop);
            
            context.setPaint(Color.RED);
            
            double chartAreaWidth = width - paddingLeft - paddingRight;
            double chartAreaHeight = height - paddingTop - paddingBottom;

            double chartAreaCenterX = chartAreaWidth / 2;
            double chartAreaCenterY = chartAreaHeight / 2;

            String namesAndValues[] = new String[series.size()];
            if(drawSeriesNamesEnabled){
                for (int i = 0; i < namesAndValues.length; i++) {
                    namesAndValues[i] = series.get(i).getName() + (drawValuesEnabled?" - ":"");
                }
            }
            if(drawValuesEnabled){
                for (int i = 0; i < namesAndValues.length; i++) {
                    DataSeriesDataProvider dp = series.get(i).get(0);
                    namesAndValues[i] = namesAndValues[i] + dp.toString(format);
                }
            }

            double pieRadius = Math.min(chartAreaCenterX, chartAreaCenterY);
            Font labelFont = getLabelFont();
            Paint labelColor = getLabelColor();
            double labelMargin = getLabelMargin();
            if (drawSeriesNamesEnabled || drawValuesEnabled) {
                double maxLabelWidth = context.calculateStringWidth(context.calculateWidestLine(namesAndValues, labelFont), labelFont);
                pieRadius -= maxLabelWidth + labelMargin;
            }
            
            if (series.size() == 1) {
                Arc2D.Double arc = new Arc2D.Double();
                arc.setArcByCenter(chartAreaCenterX, chartAreaCenterY, pieRadius, 0, 360, Arc2D.OPEN);
                DataSeries s = series.get(0);
                context.setPaint(s.getSeriesColor(), pieFillOpacity);
                context.fill(arc);
                context.setStroke(pieBorderStroke);
                context.setPaint(pieBorderColor);
                context.draw(arc);
                
                if (drawSeriesNamesEnabled || drawValuesEnabled) {
                    context.setFont(labelFont);
                    context.setPaint(labelColor);
                    String text = (drawSeriesNamesEnabled?(s.getName()+(drawValuesEnabled?" - ":"")):"") + (drawValuesEnabled?s.get(0).toString(format):"");
                    context.drawText(text, chartAreaCenterX + pieRadius + labelMargin, chartAreaCenterY, Context2D.HORIZONTAL_ALIGN_LEFT, Context2D.VERTICAL_ALIGN_MIDDLE);
                }

            } else {
                double explOffset = isDoughnut?0:explosionOffset;
                pieRadius -= explOffset;
                double angExt = 0;
                double sum = series.getSumOfPositivesOnFirstColumn();
                if(overrideTotal){
                    sum = total;
                }
                double factor = 360 / sum;
                DataSeriesDataProvider dp;
                double rotateBy;
                double textAngle;
                double value;
                int horzAlign;
                int sign;
                Arc2D.Double arc;
                double totalTextAngle;
                
                context.translate(chartAreaCenterX, chartAreaCenterY);
                
                for (DataSeries dataseries : series) {
                    dp = dataseries.get(0);
                    if (dp != null) {
                        value = dp.getDataValue();
                        if (value > 0) {
                            angExt = value * factor;
                            arc = new Arc2D.Double();
                            
                            context.rotate(Math.toRadians(angExt));
                            context.translate(explOffset, -explOffset);
                            
                            arc.setArcByCenter(0, 0, pieRadius, 0, angExt, Arc2D.PIE);
                            

                            context.setPaint(dataseries.getSeriesColor(), pieFillOpacity);
                            context.fill(arc);
                            context.setStroke(pieBorderStroke);
                            context.setPaint(pieBorderColor);
                            context.draw(arc);
                            
                            

                            
                            if (drawSeriesNamesEnabled || drawValuesEnabled) {
                                textAngle = angExt / 2;
                                
                                context.setFont(labelFont);
                                context.setPaint(dataseries.getSeriesColor());
                                
                                totalTextAngle = startAngle+textAngle;
                                if((totalTextAngle > 90) && (totalTextAngle<270)){
                                    sign = -1;
                                    horzAlign = Context2D.HORIZONTAL_ALIGN_RIGHT;
                                    rotateBy = -Math.toRadians(textAngle-180);
                                    
                                }else{
                                    sign = 1;
                                    horzAlign = Context2D.HORIZONTAL_ALIGN_LEFT;
                                    rotateBy = -Math.toRadians(textAngle);
                                }
                                
                                context.rotate(rotateBy);
                                String text = (drawSeriesNamesEnabled?(dataseries.getName()+(drawValuesEnabled?" - ":"")):"") + (drawValuesEnabled?dataseries.get(0).toString(format):"");
                                context.drawText(text, sign * (pieRadius + labelMargin ), 0, horzAlign, Context2D.VERTICAL_ALIGN_MIDDLE);
                                context.rotate(-rotateBy);

                            }
                            
                            context.translate(-explOffset, explOffset);
                            //context.rotate(-Math.toRadians(startAngle));
                            startAngle += angExt;
                        }
                    }
                }
                
                if(isDoughnut){
                    Composite tmpComposite = context.getComposite();
                    context.setComposite(AlphaComposite.Clear);
                    double d = pieRadius;
                    double r = d/2;
                    Ellipse2D e = new Ellipse2D.Double(-r, -r, d, d);
                    context.fill(e);
                    context.setComposite(tmpComposite);
                    context.setStroke(pieBorderStroke);
                    context.setPaint(pieBorderColor);
                    context.draw(e);
                }
            }

            if (shadowsOn) {
                context.endShadowedDrawing();
            }
            
            context.restore();
        }
        startAngle = startAngleBefore;
    }

    public Paint getPieBorderColor() {
        return pieBorderColor;
    }

    public void setPieBorderColor(Paint pieBorderColor) {
        this.pieBorderColor = pieBorderColor;
    }

    public Stroke getPieBorderStroke() {
        return pieBorderStroke;
    }

    public void setPieBorderStroke(Stroke pieBorderStroke) {
        this.pieBorderStroke = pieBorderStroke;
    }

    public float getPieFillOpacity() {
        return pieFillOpacity;
    }

    public void setPieFillOpacity(float pieFillOpacity) {
        this.pieFillOpacity = pieFillOpacity;
    }

    public double getStartAngle() {
        return startAngle;
    }

    public void setStartAngle(double startAngle) {
        this.startAngle = startAngle;
    }

    public double getTotal() {
        return total;
    }

    public void setOverrideTotal(boolean override, double total) {
        this.total = total;
        this.overrideTotal = override;
    }

    public double getExplosionOffset() {
        return explosionOffset;
    }

    public void setExplosionOffset(double explosionOffset) {
        this.explosionOffset = explosionOffset;
    }
    
    public static void main(String[] args) {

        JAwesomeChart ac = new JAwesomeChart(600, 400);

        ac.addSeries("IE", new double[]{42.93}, Color.decode("0x443769"));
        ac.addSeries("Firefox", new double[]{28.2}, Color.decode("0xFF2400"));
        ac.addSeries("Chrome", new double[]{21.08}, Color.decode("0x4A83BD"));
        ac.addSeries("Safari", new double[]{5.33}, Color.decode("0xFF8300"));
        ac.addSeries("Opera", new double[]{1.84}, Color.decode("0xFF0096"));
        ac.addSeries("Other", new double[]{0.62}, Color.decode("0x236A14"));

        ac.setTitle("Wordwide Dekstop Browser Market Share");
        ac.setSubtitle("January 2011");
        
        PieChartRenderer renderer = new PieChartRenderer();
        ac.setRenderer(renderer);
        
        JAwesomeChart.drawAndSaveChartAsImageFile(ac, "/home/cyberpython/temp/awesomechart/01-pie-chart.png");
        
        renderer.setExplosionOffset(10);
        
        JAwesomeChart.drawAndSaveChartAsImageFile(ac, "/home/cyberpython/temp/awesomechart/02-pie-chart.png");
        
        renderer.setIsDoughnut(true);
        
        JAwesomeChart.drawAndSaveChartAsImageFile(ac, "/home/cyberpython/temp/awesomechart/03-pie-chart.png");
        
        Utilities.xdgOpenFile(new File("/home/cyberpython/temp/awesomechart/01-pie-chart.png"));
    }
    
}
