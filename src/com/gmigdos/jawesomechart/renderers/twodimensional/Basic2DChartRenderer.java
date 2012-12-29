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
package com.gmigdos.jawesomechart.renderers.twodimensional;

import com.gmigdos.jawesomechart.core.DataSeriesList;
import com.gmigdos.jawesomechart.exceptions.IllegalValueException;
import com.gmigdos.jawesomechart.renderers.BaseChartRenderer;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Stroke;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 *
 * @author Georgios Migdos <cyberpython@gmail.com>
 */
public abstract class Basic2DChartRenderer extends BaseChartRenderer{
    
    private final static Font DEFAULT_VALUE_AXIS_CAPTION_FONT = new Font("SansSerif", Font.BOLD, 12);
    private final static Font DEFAULT_LABEL_AXIS_CAPTION_FONT = new Font("SansSerif", Font.BOLD, 12);
    
    private final static Font DEFAULT_VALUE_AXIS_FONT = new Font("SansSerif", Font.PLAIN, 10);
    private final static Font DEFAULT_LABEL_AXIS_FONT = new Font("SansSerif", Font.BOLD, 10);
    
    private final static Color DEFAULT_VALUE_AXIS_CAPTION_COLOR = new Color(200, 200, 200);
    private final static Color DEFAULT_LABEL_AXIS_CAPTION_COLOR = new Color(200, 200, 200);
    
    private final static Color DEFAULT_VALUE_AXIS_TEXT_COLOR = new Color(51, 51, 51);
    private final static Color DEFAULT_LABEL_AXIS_TEXT_COLOR = new Color(51, 51, 51);
    
    private final static double DEFAULT_VALUE_AXIS_CAPTION_MARGIN = 5;
    private final static double DEFAULT_LABEL_AXIS_CAPTION_MARGIN = 5;
    private final static double DEFAULT_VALUE_AXIS_MARGIN = 5;
    private final static double DEFAULT_LABEL_AXIS_MARGIN = 5;
    
    private final static Stroke DEFAULT_AXIS_MARKER_STROKE = new BasicStroke(1.0f);
    private final static Stroke DEFAULT_HORIZONTAL_LINE_STROKE = new BasicStroke(1.0f,
                        BasicStroke.CAP_BUTT,
                        BasicStroke.JOIN_MITER,
                        10.0f, new float[]{2}, 0);
    private final static Stroke DEFAULT_VERTICAL_LINE_STROKE = new BasicStroke(1.0f,
                        BasicStroke.CAP_BUTT,
                        BasicStroke.JOIN_MITER,
                        10.0f, new float[]{2}, 0);
    private final static Stroke DEFAULT_AXIS_MARKER_STROKE_FOR_ZERO = new BasicStroke();
    private final static BasicStroke DEFAULT_HORIZONTAL_LINE_STROKE_FOR_ZERO = new BasicStroke();

    
    private final static Color DEFAULT_AXIS_MARKER_COLOR = new Color(153, 153, 153);
    private final static Color DEFAULT_HORIZONTAL_LINE_COLOR = new Color(153, 153, 153);
    private final static Color DEFAULT_VERTICAL_LINE_COLOR = new Color(153, 153, 153);
    private final static Color DEFAULT_AXIS_MARKER_FOR_ZERO_COLOR = new Color(153, 153, 153);
    private final static Color DEFAULT_HORIZONTAL_LINE_FOR_ZERO_COLOR = new Color(153, 153, 153);
        
    private Font valueAxisCaptionFont;
    private Font labelAxisCaptionFont;
    private Font valueAxisFont;
    private Font labelAxisFont;
    
    private Color valueAxisCaptionColor;
    private Color labelAxisCaptionColor;
    private Color valueAxisTextColor;
    private Color labelAxisTextColor;
    
    private double valueAxisCaptionMargin;
    private double labelAxisCaptionMargin;
    private double valueAxisMargin;
    private double labelAxisMargin;
    
    private double axisMarkerSize;
    
    private String valueAxisCaption;
    private String labelAxisCaption;
    
    private int valueAxisSegments;
    
    private boolean drawValueAxis;
    private boolean drawLabelAxis;
    private boolean drawHorizontalLines;
    private boolean drawVerticalLines;
    
    private Stroke axisMarkerStroke;
    private Stroke axisMarkerStrokeForZero;
    private Stroke horizontalLineStroke;
    private BasicStroke horizontalLineStrokeForZero;
    private Stroke verticalLineStroke;
    
    private Color axisMarkerColor;
    private Color axisMarkerColorForZero;
    private Color horizontalLineColor;
    private Color horizontalLineColorForZero;
    private Color verticalLineColor;

    public Basic2DChartRenderer() {
        valueAxisCaptionFont = DEFAULT_VALUE_AXIS_CAPTION_FONT;
        valueAxisCaptionColor = DEFAULT_VALUE_AXIS_CAPTION_COLOR;
        valueAxisCaptionMargin = DEFAULT_VALUE_AXIS_CAPTION_MARGIN;
        valueAxisCaption = null;
        
        valueAxisFont = DEFAULT_VALUE_AXIS_FONT;
        valueAxisTextColor = DEFAULT_VALUE_AXIS_TEXT_COLOR;
        
        labelAxisCaptionFont = DEFAULT_LABEL_AXIS_CAPTION_FONT;
        labelAxisCaptionColor = DEFAULT_LABEL_AXIS_CAPTION_COLOR;
        labelAxisCaptionMargin = DEFAULT_LABEL_AXIS_CAPTION_MARGIN;
        labelAxisCaption = null;
        
        labelAxisFont = DEFAULT_LABEL_AXIS_FONT;
        labelAxisTextColor = DEFAULT_LABEL_AXIS_TEXT_COLOR;
        
        valueAxisMargin = DEFAULT_VALUE_AXIS_MARGIN;
        labelAxisMargin = DEFAULT_LABEL_AXIS_MARGIN;
        
        axisMarkerSize = 5;
        
        valueAxisSegments = 10;
        
        drawValueAxis = true;
        drawLabelAxis = true;
        drawHorizontalLines = true;
        drawVerticalLines = true;
        
        axisMarkerStroke = DEFAULT_AXIS_MARKER_STROKE;
        horizontalLineStroke = DEFAULT_HORIZONTAL_LINE_STROKE;
        verticalLineStroke = DEFAULT_VERTICAL_LINE_STROKE;
        horizontalLineStrokeForZero = DEFAULT_HORIZONTAL_LINE_STROKE_FOR_ZERO;
        axisMarkerStrokeForZero = DEFAULT_AXIS_MARKER_STROKE_FOR_ZERO;
        
        axisMarkerColor = DEFAULT_AXIS_MARKER_COLOR;
        horizontalLineColor = DEFAULT_HORIZONTAL_LINE_COLOR;
        verticalLineColor = DEFAULT_VERTICAL_LINE_COLOR;
        horizontalLineColorForZero = DEFAULT_HORIZONTAL_LINE_FOR_ZERO_COLOR;
        axisMarkerColorForZero = DEFAULT_AXIS_MARKER_FOR_ZERO_COLOR;
    }

    public Font getLabelAxisFont() {
        return labelAxisFont;
    }

    public void setLabelAxisFont(Font labelAxisFont) {
        this.labelAxisFont = labelAxisFont;
    }

    public Font getValueAxisFont() {
        return valueAxisFont;
    }

    public void setValueAxisFont(Font valueAxisFont) {
        this.valueAxisFont = valueAxisFont;
    }

    public Color getLabelAxisTextColor() {
        return labelAxisTextColor;
    }

    public void setLabelAxisTextColor(Color labelAxisTextColor) {
        this.labelAxisTextColor = labelAxisTextColor;
    }

    public Color getValueAxisTextColor() {
        return valueAxisTextColor;
    }

    public void setValueAxisTextColor(Color valueAxisTextColor) {
        this.valueAxisTextColor = valueAxisTextColor;
    }

    public double getLabelAxisMargin() {
        return labelAxisMargin;
    }

    public void setLabelAxisMargin(double labelAxisMargin) {
        this.labelAxisMargin = labelAxisMargin;
    }

    public double getValueAxisMargin() {
        return valueAxisMargin;
    }

    public void setValueAxisMargin(double valueAxisMargin) {
        this.valueAxisMargin = valueAxisMargin;
    }

    public boolean isDrawLabelAxisOn() {
        return drawLabelAxis;
    }

    public void setDrawLabelAxis(boolean drawLabelAxisOn) {
        this.drawLabelAxis = drawLabelAxisOn;
    }

    public boolean isDrawValueAxisOn() {
        return drawValueAxis;
    }

    public void setDrawValueAxis(boolean drawValueAxisOn) {
        this.drawValueAxis = drawValueAxisOn;
    }

    public boolean isDrawHorizontalLinesOn() {
        return drawHorizontalLines;
    }

    public void setDrawHorizontalLines(boolean drawHorizontalLines) {
        this.drawHorizontalLines = drawHorizontalLines;
    }

    public boolean isDrawVerticalLinesOn() {
        return drawVerticalLines;
    }

    public void setDrawVerticalLines(boolean drawVerticalLines) {
        this.drawVerticalLines = drawVerticalLines;
    }

    public double getAxisMarkerSize() {
        return axisMarkerSize;
    }

    public void setAxisMarkerSize(double axisMarkerSize) {
        this.axisMarkerSize = axisMarkerSize;
    }
    
    public String getValueAxisCaption() {
        return valueAxisCaption;
    }

    public void setValueAxisCaption(String valueAxisCaption) {
        this.valueAxisCaption = valueAxisCaption;
    }

    public String getLabelAxisCaption() {
        return labelAxisCaption;
    }

    public void setLabelAxisCaption(String labelAxisCaption) {
        this.labelAxisCaption = labelAxisCaption;
    }
    
    public Font getValueAxisCaptionFont() {
        return valueAxisCaptionFont;
    }

    public void setValueAxisCaptionFont(Font valueAxisCaptionFont) {
        this.valueAxisCaptionFont = valueAxisCaptionFont;
    }

    public Font getLabelAxisCaptionFont() {
        return labelAxisCaptionFont;
    }

    public void setLabelAxisCaptionFont(Font labelAxisCaptionFont) {
        this.labelAxisCaptionFont = labelAxisCaptionFont;
    }

    public Color getValueAxisCaptionColor() {
        return valueAxisCaptionColor;
    }

    public void setValueAxisCaptionColor(Color valueAxisCaptionColor) {
        this.valueAxisCaptionColor = valueAxisCaptionColor;
    }

    public Color getLabelAxisCaptionColor() {
        return labelAxisCaptionColor;
    }

    public void setLabelAxisCaptionColor(Color labelAxisCaptionColor) {
        this.labelAxisCaptionColor = labelAxisCaptionColor;
    }

    public double getLabelAxisCaptionMargin() {
        return labelAxisCaptionMargin;
    }

    public void setLabelAxisCaptionMargin(double labelAxisCaptionMargin) {
        this.labelAxisCaptionMargin = labelAxisCaptionMargin;
    }

    public double getValueAxisCaptionMargin() {
        return valueAxisCaptionMargin;
    }

    public void setValueAxisCaptionMargin(double valueAxisCaptionMargin) {
        this.valueAxisCaptionMargin = valueAxisCaptionMargin;
    }
    
    public int getValueAxisSegments() {
        return valueAxisSegments;
    }
    
    /**
     * @return the axisMarkerStroke
     */
    public Stroke getAxisMarkerStroke() {
        return axisMarkerStroke;
    }

    /**
     * @param axisMarkerStroke the axisMarkerStroke to set
     */
    public void setAxisMarkerStroke(Stroke axisMarkerStroke) {
        this.axisMarkerStroke = axisMarkerStroke;
    }

    /**
     * @return the horizontalLineStroke
     */
    public Stroke getHorizontalLineStroke() {
        return horizontalLineStroke;
    }

    /**
     * @param horizontalLineStroke the horizontalLineStroke to set
     */
    public void setHorizontalLineStroke(Stroke horizontalLineStroke) {
        this.horizontalLineStroke = horizontalLineStroke;
    }
    
    /**
     * @return the horizontalLineStroke
     */
    public BasicStroke getHorizontalLineStrokeForZero() {
        return horizontalLineStrokeForZero;
    }

    /**
     * @param horizontalLineStroke the horizontalLineStroke to set
     */
    public void setHorizontalLineStrokeForZero(BasicStroke horizontalLineStrokeForZero) {
        this.horizontalLineStrokeForZero = horizontalLineStrokeForZero;
    }

    /**
     * @return the verticalLineStroke
     */
    public Stroke getVerticalLineStroke() {
        return verticalLineStroke;
    }

    /**
     * @param verticalLineStroke the verticalLineStroke to set
     */
    public void setVerticalLineStroke(Stroke verticalLineStroke) {
        this.verticalLineStroke = verticalLineStroke;
    }

    public Stroke getAxisMarkerStrokeForZero() {
        return axisMarkerStrokeForZero;
    }

    public void setAxisMarkerStrokeForZero(Stroke axisMarkerStrokeForZero) {
        this.axisMarkerStrokeForZero = axisMarkerStrokeForZero;
    }

    /**
     * @return the axisMarkerColor
     */
    public Color getAxisMarkerColor() {
        return axisMarkerColor;
    }

    /**
     * @param axisMarkerColor the axisMarkerColor to set
     */
    public void setAxisMarkerColor(Color axisMarkerColor) {
        this.axisMarkerColor = axisMarkerColor;
    }

    public Color getAxisMarkerColorForZero() {
        return axisMarkerColorForZero;
    }

    public void setAxisMarkerColorForZero(Color axisMarkerColorForZero) {
        this.axisMarkerColorForZero = axisMarkerColorForZero;
    }

    /**
     * @return the horizontalLineColor
     */
    public Color getHorizontalLineColor() {
        return horizontalLineColor;
    }

    /**
     * @param horizontalLineColor the horizontalLineColor to set
     */
    public void setHorizontalLineColor(Color horizontalLineColor) {
        this.horizontalLineColor = horizontalLineColor;
    }
    
    /**
     * @return the horizontalLineColor
     */
    public Color getHorizontalLineColorForZero() {
        return horizontalLineColorForZero;
    }

    /**
     * @param horizontalLineColorForZero the horizontalLineColor to set
     */
    public void setHorizontalLineColorForZero(Color horizontalLineColorForZero) {
        this.horizontalLineColorForZero = horizontalLineColorForZero;
    }

    /**
     * @return the verticalLineColor
     */
    public Color getVerticalLineColor() {
        return verticalLineColor;
    }

    /**
     * @param verticalLineColor the verticalLineColor to set
     */
    public void setVerticalLineColor(Color verticalLineColor) {
        this.verticalLineColor = verticalLineColor;
    }

    public void setValueAxisSegments(int valueAxisSegments) throws IllegalValueException{
        if(valueAxisSegments<1){
            throw new IllegalValueException("The value-axis segments number must be > 0.");
        }
        this.valueAxisSegments = valueAxisSegments;
    }

    protected Double getDataDistance(double maxData, double minData) {
        if (maxData < 0) {
            return -minData;
        } else if (minData > 0) {
            return maxData;
        } else {
            return maxData - minData;
        }
    }

    protected double getPositiveAreaSize(DataSeriesList series, double totalMinusMargins) {
        double maxData = series.getMaxValue();
        double minData = series.getMinValue();
        if (maxData < 0) {
            return 0;
        } else if (minData < 0 && maxData > 0) {
            return totalMinusMargins - ((-minData) * totalMinusMargins / getDataDistance(maxData, minData));
        } else {
            return totalMinusMargins;
        }
    }
    
    protected double getNegativeAreaSize(DataSeriesList series, double totalMinusMargins) {
        double maxData = series.getMaxValue();
        double minData = series.getMinValue();
        if (maxData < 0) {
            return  totalMinusMargins;
        } else if (minData < 0 && maxData > 0) {
            return  (-minData) * totalMinusMargins / getDataDistance(maxData, minData);
        } else {
            return  0;
        }
    }
    
    protected Double getCentralValue(double maxData, double minData) {
        if (maxData < 0) {
            return minData / 2;
        } else if (minData > 0) {
            return maxData / 2;
        } else {
            return (maxData / 2 + minData / 2);
        }
    }
    
    protected double calculateValueAxisStepInDataScale(double minData, double maxData) {
        int segN = getValueAxisSegments() + 2;
        double maxMinDataDistance = getDataDistance(maxData, minData);
        if (maxData <= 0 || minData >= 0) {
            return maxMinDataDistance / segN;
        } else {
            double posRel = maxData / maxMinDataDistance;
            int posSegN = (int) (segN * posRel);
            return maxData / posSegN;
        }
    }
    
    private double calculateValueAxisStepInPixels(double valueAxisStepInDataScale, double sizeWithoutMargins, double dataDistance) {
        return valueAxisStepInDataScale * sizeWithoutMargins / dataDistance;
    }
    
    public final List<Double> generateValueAxisMarkPositions(double minData, double maxData, int segmentsNumber) {
        List<Double> result = new ArrayList<Double>();
        double step = calculateValueAxisStepInDataScale(minData, maxData);
        double dataDistance = getDataDistance(maxData, minData);
        double yAxisVal;

        if (maxData < 0) {
            yAxisVal = minData;
            while (yAxisVal < 0) {
                result.add(yAxisVal);
                yAxisVal += step;
            }
            result.add(0.0);
        } else if (minData > 0) {
            yAxisVal = 0.0;
            while (yAxisVal < maxData) {
                result.add(yAxisVal);
                yAxisVal += step;
            }
            result.add(maxData);
        } else {
            int segN = segmentsNumber;
            double posRel = maxData / dataDistance;
            int posSegN = (int) (segN * posRel);
            step = maxData / posSegN;
            double threshold = Math.abs(step)/2;
            yAxisVal = -step;
            while (yAxisVal > minData) {
                result.add(yAxisVal);
                yAxisVal -= step;
            }
            if (Math.abs(minData - yAxisVal) <= threshold) {
                result.add(minData);
            }
            /*if(result.get(result.size()-1).doubleValue()!=minData){
                result.add(minData);
            }*/
            Collections.reverse(result);
            yAxisVal = 0.0;
            while (yAxisVal < maxData) {
                result.add(yAxisVal);
                yAxisVal += step;
            }
            if (Math.abs(yAxisVal - maxData) <= threshold) {
                result.add(maxData);
            }
            /*if(result.get(result.size()-1).doubleValue()!=maxData){
                result.add(maxData);
            }*/

        }
        return result;
    }
    
    @Override
    public String getHumanReadableName() {
        return "Basic 2D";
    }

    
}
