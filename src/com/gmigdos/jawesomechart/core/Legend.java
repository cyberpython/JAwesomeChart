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
package com.gmigdos.jawesomechart.core;

import com.gmigdos.jawesomechart.util.Context2D;
import com.gmigdos.jawesomechart.util.Utilities;
import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.util.Iterator;

/**
 *
 * @author Georgios Migdos <cyberpython@gmail.com>
 */
public class Legend {

    public enum LegendPosition {

        LEGEND_POSITION_TOP, LEGEND_POSITION_BOTTOM, LEGEND_POSITION_LEFT, LEGEND_POSITION_RIGHT
    };
    private static final double DEFAULT_HORIZONTAL_GAP = 5.0;
    private static final double DEFAULT_VERTICAL_GAP = 5.0;
    private final static double DEFAULT_PADDING_VERTICAL = 10.0;
    private final static double DEFAULT_PADDING_HORIZONTAL = 10.0;
    private LegendPosition position;
    private double height;
    private double leftOrRightWidth;
    private double topOrBottomWidth;
    private DataSeriesList series;
    private double horizontalGap;
    private double verticalGap;
    private double paddingVertical;
    private double paddingHorizontal;
    private Font font;
    private Stroke legendBorder;
    private Stroke markerBorder;
    private Color legendBorderColor;
    private Paint legendBackground;
    private Color markerBorderColor;
    private Color textColor;
    private double markerDimension;
    private Context2D context;

    public Legend(double leftOrRightWidth, double topOrBottomWidth, DataSeriesList series) {
        this(LegendPosition.LEGEND_POSITION_BOTTOM, leftOrRightWidth, topOrBottomWidth, series);
    }

    public Legend(Legend.LegendPosition position, double leftOrRightWidth, double topOrBottomWidth, DataSeriesList series) {
        this.position = position;
        this.series = series;
        this.verticalGap = DEFAULT_VERTICAL_GAP;
        this.horizontalGap = DEFAULT_HORIZONTAL_GAP;
        this.paddingVertical = DEFAULT_PADDING_VERTICAL;
        this.paddingHorizontal = DEFAULT_PADDING_HORIZONTAL;
        this.font = new Font("Sans Serif", Font.BOLD, 10);
        this.legendBorder = new BasicStroke();
        this.markerBorder = new BasicStroke();
        this.context = null;
        this.legendBorderColor = new Color(153, 153, 153);
        this.legendBackground = Color.WHITE;
        this.markerBorderColor = Color.BLACK;
        this.textColor = Color.BLACK;
        this.leftOrRightWidth = leftOrRightWidth;
        this.topOrBottomWidth = topOrBottomWidth;
        this.markerDimension = 0;
        updateSize();
    }

    public Context2D getContext() {
        return context;
    }

    public void setContext(Context2D context) {
        this.context = context;
        updateSize();
    }

    public LegendPosition getPosition() {
        return position;
    }

    public void setPosition(LegendPosition position) {
        this.position = position;
    }
    
    public boolean isPlacedOnLeftOrRight(){
        return (position.equals(LegendPosition.LEGEND_POSITION_LEFT) || position.equals(LegendPosition.LEGEND_POSITION_RIGHT));
    }

    private void updateSize() {
        double width;
        
        if (isPlacedOnLeftOrRight()) {
            width = this.leftOrRightWidth;
            if (context != null) {

                context.setFont(font);
                String longestName = Utilities.calculateWidestSeriesName(context, series);
                context.adjustFontSizeToFitTextInWidth(longestName, width - 2 * paddingHorizontal - horizontalGap);
                markerDimension = context.getStandardLineHeight();
                context.adjustFontSizeToFitTextInWidth(longestName, width - 2 * paddingHorizontal - horizontalGap - markerDimension);
                markerDimension = context.getStandardLineHeight();
                height = series.size() * (markerDimension + verticalGap) + 2 * paddingVertical;
                font = context.getFont();
            }
        } else {
            width = topOrBottomWidth;
            if (context != null) {
                context.setFont(font);
                String longestName = Utilities.calculateWidestSeriesName(context, series);

                double maxLineWidth = width - 2 * paddingHorizontal;
                double totalHeight = paddingVertical;
                markerDimension = context.getStandardLineHeight();
                double currentWidthSum = 0;
                double currentLineMaxHeight = 0;

                double entryWidth = markerDimension + horizontalGap + context.calculateStringWidth(longestName);
                if (entryWidth > maxLineWidth) {
                    context.adjustFontSizeToFitTextInWidth("   " + longestName, width - 2 * paddingHorizontal);
                    markerDimension = context.getStandardLineHeight();
                }
                String name;
                boolean first = true;
                for (Iterator<DataSeries> it = series.iterator(); it.hasNext();) {
                    DataSeries dataSeries = it.next();
                    name = dataSeries.getName();
                    if (name != null) {
                        if(first){
                            first = false;
                            totalHeight += context.getLineHeight(font, name)+verticalGap;
                        }
                        entryWidth = markerDimension + horizontalGap + context.calculateStringWidth(name);
                        currentWidthSum += entryWidth;
                        currentLineMaxHeight = Math.max(context.getLineHeight(font, name), currentLineMaxHeight);
                        if (currentWidthSum > maxLineWidth) {
                            totalHeight += currentLineMaxHeight;
                            if(it.hasNext()){
                                totalHeight += verticalGap;
                            }
                            currentWidthSum = entryWidth;
                        }
                        currentWidthSum += 4*horizontalGap;
                        
                    }
                }
                font = context.getFont();
                totalHeight += paddingVertical;
                height = totalHeight;
            }
        }
    }

    public double getHeight() {
        return height;
    }

    public double getWidth() {
        if(isPlacedOnLeftOrRight()){
            return leftOrRightWidth;
        }
        return topOrBottomWidth;
    }

    public void setLeftOrRightWidth(double leftOrRightWidth) {
        this.leftOrRightWidth = leftOrRightWidth;
        updateSize();
    }

    public void setTopOrBottomWidth(double topOrBottomWidth) {
        this.topOrBottomWidth = topOrBottomWidth;
        updateSize();
    }

    public DataSeriesList getSeries() {
        return series;
    }

    public void setSeries(DataSeriesList series) {
        this.series = series;
    }

    public double getVerticalGap() {
        return verticalGap;
    }

    public void setVerticalGap(double verticalGap) {
        this.verticalGap = verticalGap;
    }

    public double getHorizontalGap() {
        return horizontalGap;
    }

    public void setHorizontalGap(double horizontalGap) {
        this.horizontalGap = horizontalGap;
    }

    public Font getFont() {
        return font;
    }

    public void setFont(Font font) {
        this.font = font;
    }

    public void draw() {
        if (context != null) {
            double width;
            
            if(isPlacedOnLeftOrRight()){
                width = this.leftOrRightWidth;
            }else{
                width = this.topOrBottomWidth;
            }
            
            Rectangle2D legendArea = new Rectangle2D.Double(0, 0, getWidth(), getHeight());
            context.setStroke(legendBorder);
            context.setPaint(legendBackground);
            context.fill(legendArea);
            context.setPaint(legendBorderColor);
            context.draw(legendArea);
            
            updateSize();
            context.setFont(font);
            double moveTextVerticallyBy = context.getFontMetrics().getDescent();
            Rectangle2D marker;
            if (isPlacedOnLeftOrRight()) {                
                int i = 0;
                for (DataSeries dataSeries : series) {

                    marker = new Rectangle2D.Double(paddingHorizontal, paddingVertical + i * (markerDimension + verticalGap), markerDimension, markerDimension);

                    context.setStroke(markerBorder);

                    context.setPaint(dataSeries.getSeriesColor());
                    context.fill(marker);

                    context.setPaint(markerBorderColor);
                    context.draw(marker);

                    context.setPaint(textColor);
                    if (dataSeries.getName() != null) {
                        context.drawText(dataSeries.getName(), paddingHorizontal + markerDimension + horizontalGap, paddingVertical + i * (markerDimension + verticalGap) + markerDimension / 2, Context2D.HORIZONTAL_ALIGN_LEFT, Context2D.VERTICAL_ALIGN_MIDDLE);
                    }

                    i++;
                }
            } else {
                
                double maxLineWidth = width - 2 * paddingHorizontal;
                double currentY = paddingVertical;
                double currentWidthSum = 0;
                double currentX = paddingHorizontal;
                double currentLineMaxHeight = 0;
                double entryWidth;
                String name;
                for (DataSeries dataSeries : series) {
                    name = dataSeries.getName();
                    if (name != null) {
                        entryWidth = markerDimension + horizontalGap + context.calculateStringWidth(name);
                        currentWidthSum += entryWidth;
                        if (currentWidthSum > maxLineWidth) {
                            currentY += currentLineMaxHeight+verticalGap;
                            currentWidthSum = entryWidth;
                            currentX = paddingHorizontal;
                        }
                        
                        marker = new Rectangle2D.Double(currentX, currentY, markerDimension, markerDimension);
                        context.setStroke(markerBorder);

                        context.setPaint(dataSeries.getSeriesColor());
                        context.fill(marker);

                        context.setPaint(markerBorderColor);
                        context.draw(marker);
                        
                        context.setPaint(textColor);
                        context.drawText(name, currentX + markerDimension + horizontalGap, currentY + markerDimension - moveTextVerticallyBy, Context2D.HORIZONTAL_ALIGN_LEFT, Context2D.VERTICAL_ALIGN_BASELINE);
                        
                        currentLineMaxHeight = Math.max(context.getLineHeight(font, name), currentLineMaxHeight);
                        currentX += entryWidth+4*horizontalGap;
                        currentWidthSum += 4*horizontalGap;
                    }
                    
                }
                
            }
            
        }
    }
}
