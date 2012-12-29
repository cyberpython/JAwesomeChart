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
import com.gmigdos.jawesomechart.core.*;
import com.gmigdos.jawesomechart.util.Context2D;
import com.gmigdos.jawesomechart.util.StringsProvider;
import com.gmigdos.jawesomechart.util.Utilities;
import java.awt.*;
import java.awt.geom.Path2D;
import java.awt.geom.Rectangle2D;
import java.io.File;
import java.text.DecimalFormat;

/**
 *
 * @author Georgios Migdos <cyberpython@gmail.com>
 */
public class ColumnChartRenderer extends Basic2DHorizontalChartRenderer {

    private double gap;
    private float columnOpacity;
    private Paint columnBorderColor;
    private Stroke columnBorderStroke;
    private DataSeriesList series;
    private Context2D context;

    public ColumnChartRenderer() {
        gap = 20;
        columnOpacity = 1.0f;
        columnBorderColor = Color.WHITE;
        columnBorderStroke = new BasicStroke(2.0f, BasicStroke.CAP_SQUARE, BasicStroke.JOIN_MITER);
        setDrawVerticalLines(false);
        series = null;
        context = null;
    }

    public Paint getColumnBorderColor() {
        return columnBorderColor;
    }

    public void setColumnBorderColor(Paint columnBorderColor) {
        this.columnBorderColor = columnBorderColor;
    }

    public Stroke getColumnBorderStroke() {
        return columnBorderStroke;
    }

    public void setColumnBorderStroke(Stroke columnBorderStroke) {
        this.columnBorderStroke = columnBorderStroke;
    }

    public float getColumnOpacity() {
        return columnOpacity;
    }

    public void setColumnOpacity(float columnOpacity) {
        this.columnOpacity = columnOpacity;
    }

    public double getGap() {
        return gap;
    }

    public void setGap(double gap) {
        this.gap = gap;
    }
    
    public double getGapBetweenColumns(int numberOfColumnGroups){
        if (numberOfColumnGroups == 1) {
            return gap;
        }
        return 0;
    }

    @Override
    public void draw(Context2D context, DataSeriesList series, Labels labels) {
        
        this.series = series;
        this.context = context;
        
        int numberOfColumnGroups = series.getMaxDataSeriesLength();
        if (numberOfColumnGroups == 1) {
            setDrawLabelAxis(false);
        }
        
        super.draw(context, series, labels);

        context.save();

        if (isShadowsOn()) {
            context.beginShadowedDrawing();
        }

        double paddingTop = getPaddingTop();
        double paddingBottom = getPaddingBottom();
        double height = context.getHeight(); //- offsetTopForNameAndValue - offsetBottomForNameAndValue;
        double gapBetweenCols = getGapBetweenColumns(numberOfColumnGroups);
        double width = context.getWidth();
        
        int numberOfColumnsPerGroup = series.size();
        double columnGroupWidth = (width - (numberOfColumnGroups - 1) * gap) / numberOfColumnGroups;
        double columnWidth = (columnGroupWidth - (numberOfColumnsPerGroup - 1) * gapBetweenCols) / numberOfColumnsPerGroup;
        double halfColumnWidth = columnWidth / 2;
        double dataDistance = getDataDistance(series.getMaxValue(), series.getMinValue());

        double posH = getPositiveAreaSize(series, height);
        double negH = getNegativeAreaSize(series, height);

        double lineWidth = getHorizontalLineStrokeForZero().getLineWidth();
        double paddingLeft = getPaddingLeft();
        double paddingRight = getPaddingRight();

        Rectangle2D positiveClip = new Rectangle2D.Double(-paddingLeft, -posH - paddingTop, width + paddingLeft + paddingRight, posH + paddingTop - lineWidth);
        Rectangle2D negativeClip = new Rectangle2D.Double(-paddingLeft, lineWidth, width + paddingLeft + paddingRight, negH + getPaddingBottom() - lineWidth);

        DataSeriesDataProvider dp;
        double y;
        double x;
        double value;
        int columnNo = 0;
        int textValign;
        Path2D column;
        
        //TODO: do all this only if the column width is large enough:
        boolean isValueRenderingOn = isValueRenderingOn();
        boolean isSeriesNameRenderingOn = isSeriesNameRenderingOn();
        double seriesNameMargin = getLabelMargin();
        double valueMargin = getValueMargin();
        DecimalFormat df = getDecimalFormat();
        Font seriesNameFont = getLabelFont();
        Font valueFont = getValueFont();
        Paint valuePaint = getValueColor();
        context.setFont(seriesNameFont);
        context.adjustFontSizeToFitTextInWidth(Utilities.calculateWidestSeriesName(context, series), columnWidth);
        seriesNameFont = context.getFont();
        context.setFont(valueFont);
        context.adjustFontSizeToFitTextInWidth(String.valueOf(Utilities.calculateWidestValue(context, series, df)), columnWidth);
        valueFont = context.getFont();
        double seriesNameLineHeight = context.getStandardLineHeight(seriesNameFont);
        double valueLineHeight = context.getStandardLineHeight(valueFont);

        for (int i = 0; i < numberOfColumnGroups; i++) {

            columnNo = 0;
            for (DataSeries dataSeries : series) {
                dp = dataSeries.get(i);

                if (dp != null) {
                    value = dp.getDataValue();
                    y = -(value * height / dataDistance);
                    x = i * (columnGroupWidth + gap) + (columnWidth + gapBetweenCols) * columnNo;

                    column = new Path2D.Double();
                    column.moveTo(x, 0);
                    column.lineTo(x, y);
                    column.lineTo(x + columnWidth, y);
                    column.lineTo(x + columnWidth, 0);


                    if (value >= 0) {
                        context.setClip(positiveClip);
                    } else {
                        context.setClip(negativeClip);
                    }

                    context.setPaint(dataSeries.getSeriesColor(), columnOpacity);
                    context.fill(column);

                    context.setStroke(columnBorderStroke);
                    context.setPaint(columnBorderColor);
                    context.draw(column);
                    
                    if(isSeriesNameRenderingOn){
                        context.setFont(seriesNameFont);
                        context.setPaint(dataSeries.getSeriesColor());
                        if(y<0){
                            y-=seriesNameMargin;
                            textValign = Context2D.VERTICAL_ALIGN_BOTTOM;
                        }else{
                            y+=seriesNameMargin;
                            textValign = Context2D.VERTICAL_ALIGN_TOP;
                        }                       
                        context.drawText(dataSeries.getName(), x+halfColumnWidth, y, Context2D.HORIZONTAL_ALIGN_CENTER, textValign);
                        if(y<0){
                            y-=seriesNameLineHeight;
                        }else{
                            y+=seriesNameLineHeight;
                        }
                    }
                    
                    if(isValueRenderingOn){
                        context.setFont(valueFont);
                        context.setPaint(valuePaint);
                        if(y<0){
                            y-=valueMargin;
                            textValign = Context2D.VERTICAL_ALIGN_BOTTOM;
                        }else{
                            y+=valueMargin;
                            textValign = Context2D.VERTICAL_ALIGN_TOP;
                        }                       
                        context.drawText(df.format(value), x+halfColumnWidth, y, Context2D.HORIZONTAL_ALIGN_CENTER, textValign);
                    }
                    
                    

                    columnNo++;
                }

            }

        }

        if (isShadowsOn()) {
            context.endShadowedDrawing();
        }

        context.restore();
    }

    
    @Override
    public double getPaddingTop() {
        double offsetTopForNameAndValue = 0;
        if(series!=null && context!=null){  
            Font seriesNameFont = getLabelFont();
            Font valueFont = getValueFont();
            double maxData = series.getMaxValue();
            double seriesNameMargin = getLabelMargin();
            double valueMargin = getValueMargin();
            double spaceForSeriesName = isSeriesNameRenderingOn()?context.getStandardLineHeight(seriesNameFont)+seriesNameMargin:0;
            double spaceForValue = isValueRenderingOn()?context.getStandardLineHeight(valueFont)+valueMargin:0;
            offsetTopForNameAndValue = ((maxData>0)?(spaceForSeriesName+spaceForValue):0);
        }
        return super.getPaddingTop() + offsetTopForNameAndValue;
    }
    
    @Override
    public double getPaddingBottom() {
        double offsetBottomForNameAndValue = 0;
        if(series!=null && context!=null){
            Font seriesNameFont = getLabelFont();
            Font valueFont = getValueFont();
            double minData = series.getMinValue();
            double seriesNameMargin = getLabelMargin();
            double valueMargin = getValueMargin();
            double spaceForSeriesName = isSeriesNameRenderingOn()?context.getStandardLineHeight(seriesNameFont)+seriesNameMargin:0;
            double spaceForValue = isValueRenderingOn()?context.getStandardLineHeight(valueFont)+valueMargin:0;
            offsetBottomForNameAndValue = ((minData<0)?(spaceForSeriesName+spaceForValue):0);
        }
        return super.getPaddingBottom() + offsetBottomForNameAndValue;
    }

    @Override
    public String getHumanReadableName() {
        return StringsProvider.CHART_RENDERER_COLUMN;
    }

    public static void main(String[] args) {

        JAwesomeChart ac = new JAwesomeChart(3000, 500);
        ColumnChartRenderer renderer = new ColumnChartRenderer();
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

        JAwesomeChart.drawAndSaveChartAsImageFile(ac, "/home/cyberpython/temp/awesomechart/04-column-chart.png");

        ac = new JAwesomeChart(600, 600);
        ac.setRenderer(renderer);
        renderer.setLabelAxisCaption(null);

        ac.setTitle("Dekstop Browser Market Share");
        ac.setSubtitle("January 2011");

        ac.addSeries("IE", new double[]{46}, Color.decode("0x443769"));
        ac.addSeries("Firefox", new double[]{30.68}, Color.decode("0xFF2400"));
        ac.addSeries("Chrome", new double[]{15.68}, Color.decode("0x4A83BD"));
        ac.addSeries("Safari", new double[]{5.09}, Color.decode("0xFF8300"));
        ac.addSeries("Opera", new double[]{2}, Color.decode("0xFF0096"));
        ac.addSeries("Other", new double[]{0.55}, Color.decode("0x236A14"));

        JAwesomeChart.drawAndSaveChartAsImageFile(ac, "/home/cyberpython/temp/awesomechart/05-column-chart.png");
        
        ac.clearSeries();
        ac.addSeries("IE", new double[]{46}, Color.decode("0x443769"));
        ac.addSeries("Firefox", new double[]{30.68}, Color.decode("0xFF2400"));
        ac.addSeries("Chrome", new double[]{15.68}, Color.decode("0x4A83BD"));
        ac.addSeries("Safari", new double[]{5.09}, Color.decode("0xFF8300"));
        ac.addSeries("Opera", new double[]{2}, Color.decode("0xFF0096"));
        ac.addSeries("Other", new double[]{0.55}, Color.decode("0x236A14"));
        
        ac.getLegend().setPosition(Legend.LegendPosition.LEGEND_POSITION_RIGHT);
        
        JAwesomeChart.drawAndSaveChartAsImageFile(ac, "/home/cyberpython/temp/awesomechart/06-column-chart.png");

        Utilities.xdgOpenFile(new File("/home/cyberpython/temp/awesomechart/05-column-chart.png"));
    }
}
