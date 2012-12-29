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

import com.gmigdos.jawesomechart.core.DataSeriesList;
import com.gmigdos.jawesomechart.core.Labels;
import com.gmigdos.jawesomechart.renderers.twodimensional.Basic2DChartRenderer;
import com.gmigdos.jawesomechart.util.Context2D;
import com.gmigdos.jawesomechart.util.Utilities;
import java.awt.Color;
import java.awt.Font;
import java.awt.Paint;
import java.awt.Stroke;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.text.DecimalFormat;
import java.util.List;

/**
 *
 * @author Georgios Migdos <cyberpython@gmail.com>
 */
public class Basic2DHorizontalChartRenderer extends Basic2DChartRenderer {

    public void drawValueAxisCaption(double x, double y, Context2D context, String text) {
        if (text != null) {
            context.setFont(getValueAxisCaptionFont());
            context.setPaint(getValueAxisCaptionColor());

            context.save();
            context.translate(x, y);
            context.rotate(Math.toRadians(-90));
            context.drawText(text, 0, 0, Context2D.HORIZONTAL_ALIGN_CENTER, Context2D.VERTICAL_ALIGN_TOP);
            context.restore();
        }
    }

    public void drawLabelAxisCaption(double x, double y, Context2D context, String text) {
        if (text != null) {
            context.setFont(getLabelAxisCaptionFont());
            context.setPaint(getLabelAxisCaptionColor());

            context.save();
            context.translate(x, y);
            context.drawText(text, 0, 0, Context2D.HORIZONTAL_ALIGN_CENTER, Context2D.VERTICAL_ALIGN_TOP);
            context.restore();
        }
    }

    public double getOffsetForValueAxisCaption(Context2D context) {
        String valueAxisCaption = getValueAxisCaption();
        if (valueAxisCaption != null) {
            return context.getLineHeight(getValueAxisCaptionFont(), valueAxisCaption) + getValueAxisCaptionMargin();
        }
        return 0;
    }

    public double getOffsetForLabelAxisCaption(Context2D context) {
        String labelAxisCaption = getLabelAxisCaption();
        if (labelAxisCaption != null) {
            return context.getLineHeight(getLabelAxisCaptionFont(), labelAxisCaption) + getLabelAxisCaptionMargin();
        }
        return 0;
    }

    public double getOffsetForValueAxis(Context2D context, DataSeriesList series, List<Double> valueAxisMarks) {
        if (isDrawValueAxisOn()) {
            context.setFont(getValueAxisFont());
            return Utilities.calculateWidestValueWidth(context, valueAxisMarks, getDecimalFormat())
                    + getValueAxisMargin()
                    + getAxisMarkerSize();
        }
        return 0;
    }

    public double getOffsetForLabelAxis(Context2D context, Labels labels) {
        if (isDrawLabelAxisOn()) {
            context.setFont(getLabelAxisFont());
            return getLabelAxisMargin() + context.getStandardLineHeight();
        }
        return 0;
    }

    public void drawLabelAxisAndVericallLines(Context2D context, DataSeriesList series, Labels labels, List<Double> valueAxisMarks) {

        boolean drawLabelAxis = isDrawLabelAxisOn();
        boolean drawVerticalLines = isDrawVerticalLinesOn();

        if (drawLabelAxis || drawVerticalLines) {

            double margin = drawLabelAxis ? getLabelAxisMargin() : 0;
            double axisMarkerSize = drawLabelAxis ? getAxisMarkerSize() : 0;
            double paddingLeft = getPaddingLeft();
            double paddingRight = getPaddingRight();
            double offsetX = getOffsetForValueAxis(context, series, valueAxisMarks);
            double width = context.getWidth() - paddingLeft - paddingRight - getOffsetForValueAxis(context, series, valueAxisMarks);
            double offsetY = getOffsetForLabelAxis(context, labels);
            double height = context.getHeight();
            Font font = getLabelAxisFont();
            Paint labelColor = getLabelColor();
            Color axisMarkerColor = getAxisMarkerColor();
            Color verticalLineColor = getVerticalLineColor();
            Stroke axisMarkerStroke = getAxisMarkerStroke();
            Stroke verticalLineStroke = getVerticalLineStroke();
            int numberOfLabels = series.getMaxDataSeriesLength();

            context.save();

            context.setFont(font);
            double step = width / numberOfLabels;

            String longestLabel = Utilities.calculateWidestLabel(context, labels);
            context.adjustFontSizeToFitTextInWidth(longestLabel, step);

            double halfStep = step / 2;
            double x;

            context.translate(offsetX + paddingLeft, height - offsetY);
            x = halfStep;
            for (int i = 0; i < numberOfLabels; i++) {
                String label = labels.get(i);

                if (drawVerticalLines) {
                    context.setPaint(verticalLineColor);
                    context.setStroke(verticalLineStroke);
                    context.draw(new Line2D.Double(x, 0, x, -height + offsetY));
                }

                if (drawLabelAxis) {
                    context.setPaint(axisMarkerColor);
                    context.setStroke(axisMarkerStroke);
                    context.draw(new Line2D.Double(x, 0, x, axisMarkerSize));

                    context.setPaint(labelColor);
                    context.drawText(label == null ? "" : label, x, axisMarkerSize + margin, Context2D.HORIZONTAL_ALIGN_CENTER, Context2D.VERTICAL_ALIGN_TOP, step);
                }

                x += step;
            }

            context.restore();

        }
    }

    public void drawValueAxisAndHorizontalLines(Context2D context, DataSeriesList series, Labels labels, List<Double> valueAxisMarks) {
        boolean drawValueAxis = isDrawValueAxisOn();
        boolean drawHorizontalLines = isDrawHorizontalLinesOn();
        if (drawValueAxis || drawHorizontalLines) {
            DecimalFormat df = getDecimalFormat();
            double minData = series.getMinValue();
            double maxData = series.getMaxValue();
                    
            double margin = drawValueAxis ? getValueAxisMargin() : 0;
            double axisMarkerSize = drawValueAxis ? getAxisMarkerSize() : 0;
            double dataDistance = getDataDistance(maxData, minData);
            double paddingTop = getPaddingTop();
            double paddingBottom = getPaddingBottom();
            double height = context.getHeight() - paddingTop - paddingBottom - getOffsetForLabelAxis(context, labels);
            double width = context.getWidth() - getOffsetForValueAxis(context, series, valueAxisMarks);
            double posH = getPositiveAreaSize(series, height);
            double x;
            double y;
            double minValue;
            double maxValue;
            Font font = getValueAxisFont();
            Color valueAxisTextColor = getValueAxisTextColor();
            Color axisMarkerColor = getAxisMarkerColor();
            Color axisMarkerColorForZero = getAxisMarkerColorForZero();
            Color horizontalLineColor = getHorizontalLineColor();
            Color horizontalLineColorForZero = getHorizontalLineColorForZero();
            Stroke axisMarkerStroke = getAxisMarkerStroke();
            Stroke axisMarkerStrokeForZero = getAxisMarkerStrokeForZero();
            Stroke horizontalLineStroke = getHorizontalLineStroke();
            Stroke horizontalLineStrokeForZero = getHorizontalLineStrokeForZero();

            context.setFont(font);
            double maxValueWidth = drawValueAxis ? Utilities.calculateWidestValueWidth(context, valueAxisMarks, df) : 0;


            if (maxData < 0) {
                maxValue = 0;
                minValue = minData;
            } else if (minData > 0) {
                maxValue = maxData;
                minValue = 0;
            } else {
                maxValue = maxData;
                minValue = minData;
            }

            context.save();
            context.translate(maxValueWidth, paddingTop + posH);

            for (Double value : valueAxisMarks) {

                if (value >= minValue && value <= maxValue) {
                    y = -(value * height / dataDistance);

                    if (value != 0) {
                        if (drawHorizontalLines) {
                            context.setPaint(horizontalLineColor);
                            context.setStroke(horizontalLineStroke);
                            x = margin + axisMarkerSize;
                            context.draw(new Line2D.Double(x, y, x + width, y));
                        }

                        if (drawValueAxis) {
                            context.setPaint(axisMarkerColor);
                            context.setStroke(axisMarkerStroke);
                            context.draw(new Line2D.Double(margin, y, margin + axisMarkerSize, y));
                        }
                    } else {
                        if (drawHorizontalLines) {
                            context.setPaint(horizontalLineColorForZero);
                            context.setStroke(horizontalLineStrokeForZero);
                            x = margin + axisMarkerSize;
                            context.draw(new Line2D.Double(x, y, x + width, y));
                        }

                        if (drawValueAxis) {
                            context.setPaint(axisMarkerColorForZero);
                            context.setStroke(axisMarkerStrokeForZero);
                            context.draw(new Line2D.Double(margin, y, margin + axisMarkerSize, y));
                        }
                    }

                    if (drawValueAxis) {
                        context.setPaint(valueAxisTextColor);
                        context.drawText(df.format(value), 0, y, Context2D.HORIZONTAL_ALIGN_RIGHT, Context2D.VERTICAL_ALIGN_MIDDLE);
                    }

                }
            }

            context.restore();
        }
    }

    @Override
    public void draw(Context2D context, DataSeriesList series, Labels labels) {

        //TODO: adjust label font size depending on label count

        String valueAxisCaption = getValueAxisCaption();
        String labelAxisCaption = getLabelAxisCaption();
        double width = context.getWidth();
        double height = context.getHeight();
        List<Double> valueAxisMarks = generateValueAxisMarkPositions(series.getMinValue(), series.getMaxValue(), getValueAxisSegments());


        double offsetForValueAxisCaption = getOffsetForValueAxisCaption(context);
        double offsetForLabelAxisCaption = getOffsetForLabelAxisCaption(context);
        double offsetForValueAxis = getOffsetForValueAxis(context, series, valueAxisMarks);
        double offsetForLabelAxis = getOffsetForLabelAxis(context, labels);

        double offsetX = offsetForValueAxisCaption;
        double offsetY = offsetForLabelAxisCaption;

        context.save();

        drawValueAxisCaption(0, offsetForValueAxisCaption + offsetForLabelAxis + (height - offsetForLabelAxisCaption - offsetForLabelAxis) / 2, context, valueAxisCaption);
        drawLabelAxisCaption(offsetForValueAxisCaption + offsetForValueAxis + (width - offsetForValueAxisCaption - offsetForValueAxis) / 2, height - offsetForLabelAxisCaption + getLabelAxisCaptionMargin(), context, labelAxisCaption);

        context.restore();

        width -= offsetX;
        height -= offsetY;
        context.translate(offsetX, 0);
        context.resize(width, height);



        offsetX = offsetForValueAxis;
        offsetY = getOffsetForLabelAxis(context, labels);

        width -= offsetX;
        height -= offsetY;
        context.translate(offsetX, 0);
        context.resize(width, height);

        super.draw(context, series, labels);
        
        
        width += offsetX;
        height += offsetY;
        context.translate(-offsetX, 0);
        context.resize(width, height);

        context.setClip(new Rectangle2D.Double(0, 0, width + 1, height + 1));

        drawValueAxisAndHorizontalLines(context, series, labels, valueAxisMarks);
        drawLabelAxisAndVericallLines(context, series, labels, valueAxisMarks);

        context.setClip(new Rectangle2D.Double(offsetX, 0, width - offsetX + 1, height - offsetY + 1));

        width -= offsetX + getPaddingLeft() + getPaddingRight();
        height -= offsetY + getPaddingTop() + getPaddingBottom();
        double posH = getPositiveAreaSize(series, height);
        context.translate(offsetX + getPaddingLeft(), getPaddingTop());

        context.translate(0, posH);
        context.resize(width, height);
//        context.setPaint(Color.red);
//        context.fill(new Ellipse2D.Double(-5, -5, 10, 10));
    }

    @Override
    public String getHumanReadableName() {
        return "Basic 2D Horizontal";
    }
//    public static void main(String[] args) {
//
//        AwesomeChart ac = new AwesomeChart(600, 400);
//
//        ac.setLabels(new String[]{"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"});
//        ac.addSeries("IE", new double[]{46, 45.44, 45.11, 44.52, 43.87, 43.58, 42.45, 41.89, 41.66, 40.18, 40.63, 38.65}, Color.decode("0x443769"));
//        ac.addSeries("Firefox", new double[]{30.68, 30.37, 29.98, 29.67, 29.29, 28.34, 27.95, 27.49, 26.79, 26.39, 25.23, 25.27}, Color.decode("0xFF2400"));
//        ac.addSeries("Chrome", new double[]{15.68, 16.54, 17.37, 18.29, 19.36, 20.65, 22.14, 23.16, 23.61, 25, 25.69, 27.27}, Color.decode("0x4A83BD"));
//        ac.addSeries("Safari", new double[]{5.09, 5.08, 5.02, 5.04, 5.01, 5.07, 5.17, 5.19, 5.6, 5.93, 5.92, 6.08}, Color.decode("0xFF8300"));
//        ac.addSeries("Opera", new double[]{2, 2, 1.97, 1.91, 1.84, 1.74, 1.66, 1.67, 1.72, 1.81, 1.82, 1.98}, Color.decode("0xFF0096"));
//        ac.addSeries("Other", new double[]{0.55, 0.55, 0.54, 0.57, 0.63, 0.61, 0.63, 0.61, 0.62, 0.69, 0.71, 0.75}, Color.decode("0x236A14"));
//        
//        ac.setTitle("Dekstop Browser Market Share 2011");
//
//        Basic2DChartRenderer renderer = new Basic2DHorizontalChartRenderer();
//        ac.setRenderer(renderer);
//        AwesomeChart.drawAndSaveChartAsImageFile(ac, "/home/cyberpython/temp/awesomechart/02-basic-chart.png");
//
//        ac.getLegend().setPosition(Legend.LegendPosition.LEGEND_POSITION_RIGHT);
//        renderer.setValueAxisCaption("Market share (%)");
//        renderer.setLabelAxisCaption("Browsers");
//        renderer.setDrawValueAxis(true);
//        AwesomeChart.drawAndSaveChartAsImageFile(ac, "/home/cyberpython/temp/awesomechart/03-basic-chart.png");
//
//        Utilities.xdgOpenFile(new File("/home/cyberpython/temp/awesomechart/02-basic-chart.png"));
//    }
}
