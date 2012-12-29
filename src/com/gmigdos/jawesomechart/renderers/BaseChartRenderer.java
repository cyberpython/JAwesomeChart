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

import com.gmigdos.jawesomechart.core.DataSeriesList;
import com.gmigdos.jawesomechart.core.Labels;
import com.gmigdos.jawesomechart.util.Context2D;
import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.text.DecimalFormat;

/**
 *
 * @author Georgios Migdos <cyberpython@gmail.com>
 */
public class BaseChartRenderer implements ChartRenderer{
    
    private final static Font DEFAULT_LABEL_FONT = new Font("SansSerif", Font.BOLD, 12);
    private final static Color DEFAULT_LABEL_COLOR = new Color(33, 33, 33);
    private final static double DEFAULT_LABEL_MARGIN = 10;
    
    private final static Font DEFAULT_VALUE_FONT = new Font("SansSerif", Font.BOLD, 12);
    private final static Color DEFAULT_VALUE_COLOR = new Color(51, 51, 51);
    private final static double DEFAULT_VALUE_MARGIN = 10;
    
    private final static Color GRADIENT_COLOR_1 = new Color(253, 253, 253);
    private final static Color GRADIENT_COLOR_2 = new Color(237, 237, 237);
    private final static Color DEFAULT_BORDER_COLOR = new Color(153, 153, 153);
    
    private final static double DEFAULT_PADDING_TOP = 10;
    private final static double DEFAULT_PADDING_BOTTOM = 10;
    private final static double DEFAULT_PADDING_LEFT = 10;
    private final static double DEFAULT_PADDING_RIGHT = 10;
    
    private final static DecimalFormat DEFAULT_DECIMAL_FORMAT = new DecimalFormat("#.##");
    
    private final static Paint DEFAULT_SHADOW_COLOR = new Color(0f, 0f, 0f, 0.5f);
    
    private double labelMargin;
    private Paint labelColor;
    private Font labelFont;
    
    private double valueMargin;
    private Paint valueColor;
    private Font valueFont;
    
    private double paddingTop;
    private double paddingBottom;
    private double paddingLeft;
    private double paddingRight;
    
    private Paint backgroundFill;
    private Paint borderPaint;
    private Stroke borderStroke;
    private boolean bgFillModified;
    private boolean shadowsOn;
    
    private double shadowOffsetX;
    private double shadowOffsetY;
    private int shadowBlurRadius;
    private Paint shadowColor;
    
    private DecimalFormat decimalFormat;
    
    private boolean seriesNameRenderingOn;
    private boolean valueRenderingOn;

    public BaseChartRenderer() {
        
        seriesNameRenderingOn = true;
        valueRenderingOn = true;
        
        labelColor = DEFAULT_LABEL_COLOR;
        labelFont = DEFAULT_LABEL_FONT;
        labelMargin = DEFAULT_LABEL_MARGIN;
        valueColor = DEFAULT_VALUE_COLOR;
        valueFont = DEFAULT_VALUE_FONT;
        valueMargin = DEFAULT_VALUE_MARGIN;
        
        paddingTop = DEFAULT_PADDING_TOP;
        paddingBottom = DEFAULT_PADDING_BOTTOM;
        paddingLeft = DEFAULT_PADDING_LEFT;
        paddingRight = DEFAULT_PADDING_RIGHT;
        bgFillModified = false;
        borderPaint = DEFAULT_BORDER_COLOR;
        borderStroke = new BasicStroke();
        shadowsOn = true;
        decimalFormat = DEFAULT_DECIMAL_FORMAT;
        
        shadowOffsetX = 3;
        shadowOffsetY = 0;
        shadowBlurRadius = 5;
        shadowColor = DEFAULT_SHADOW_COLOR;
    }

    public double getShadowOffsetX() {
        return shadowOffsetX;
    }

    public void setShadowOffsetX(double shadowOffsetX) {
        this.shadowOffsetX = shadowOffsetX;
    }

    public double getShadowOffsetY() {
        return shadowOffsetY;
    }

    public void setShadowOffsetY(double shadowOffsetY) {
        this.shadowOffsetY = shadowOffsetY;
    }

    public int getShadowBlurRadius() {
        return shadowBlurRadius;
    }

    public void setShadowBlurRadius(int shadowBlurRadius) {
        this.shadowBlurRadius = shadowBlurRadius;
    }

    public Paint getShadowColor() {
        return shadowColor;
    }

    public void setShadowColor(Paint shadowColor) {
        this.shadowColor = shadowColor;
    }
    
    public boolean isSeriesNameRenderingOn() {
        return seriesNameRenderingOn;
    }

    public void setSeriesNameRenderingOn(boolean seriesNameRenderingOn) {
        this.seriesNameRenderingOn = seriesNameRenderingOn;
    }

    public boolean isValueRenderingOn() {
        return valueRenderingOn;
    }

    
    
    public void setValueRenderingOn(boolean valueRenderingOn) {
        this.valueRenderingOn = valueRenderingOn;
    }

    public DecimalFormat getDecimalFormat() {
        return decimalFormat;
    }

    public void setDecimalFormat(DecimalFormat decimalFormat) {
        this.decimalFormat = decimalFormat;
    }

    public boolean isShadowsOn() {
        return shadowsOn;
    }

    public void setShadowsOn(boolean shadowsOn) {
        this.shadowsOn = shadowsOn;
    }

    public void setBackgroundFill(Paint backgroundFill) {
        this.backgroundFill = backgroundFill;
        bgFillModified = true;
    }

    public Paint getBackgroundFill() {
        return backgroundFill;
    }

    public Paint getBorderPaint() {
        return borderPaint;
    }

    public void setBorderPaint(Paint borderPaint) {
        this.borderPaint = borderPaint;
    }

    public Stroke getBorderStroke() {
        return borderStroke;
    }

    public void setBorderStroke(Stroke borderStroke) {
        this.borderStroke = borderStroke;
    }
    
    public void setDefaultGradientPaintForBackground(float height){
        backgroundFill = new GradientPaint(0, ((float)height)*0.2f, GRADIENT_COLOR_1, 0, ((float)height)*0.8f, GRADIENT_COLOR_2);
    }

    public boolean isBgFillModified() {
        return bgFillModified;
    }
    
    /**
     * @return the paddingTop
     */
    public double getPaddingTop() {
        return paddingTop;
    }

    /**
     * @param paddingTop the paddingTop to set
     */
    public void setPaddingTop(double paddingTop) {
        this.paddingTop = paddingTop;
    }

    /**
     * @return the paddingBottom
     */
    public double getPaddingBottom() {
        return paddingBottom;
    }

    /**
     * @param paddingBottom the paddingBottom to set
     */
    public void setPaddingBottom(double paddingBottom) {
        this.paddingBottom = paddingBottom;
    }

    /**
     * @return the paddingLeft
     */
    public double getPaddingLeft() {
        return paddingLeft;
    }

    /**
     * @param paddingLeft the paddingLeft to set
     */
    public void setPaddingLeft(double paddingLeft) {
        this.paddingLeft = paddingLeft;
    }

    /**
     * @return the paddingRight
     */
    public double getPaddingRight() {
        return paddingRight;
    }

    /**
     * @param paddingRight the paddingRight to set
     */
    public void setPaddingRight(double paddingRight) {
        this.paddingRight = paddingRight;
    }
    
    /**
     * @return the labelMargin
     */
    public double getLabelMargin() {
        return labelMargin;
    }

    /**
     * @param labelMargin the labelMargin to set
     */
    public void setLabelMargin(double labelMargin) {
        this.labelMargin = labelMargin;
    }

    /**
     * @return the labelColor
     */
    public Paint getLabelColor() {
        return labelColor;
    }

    /**
     * @param labelColor the labelColor to set
     */
    public void setLabelColor(Paint labelColor) {
        this.labelColor = labelColor;
    }

    /**
     * @return the labelFont
     */
    public Font getLabelFont() {
        return labelFont;
    }

    /**
     * @param labelFont the labelFont to set
     */
    public void setLabelFont(Font labelFont) {
        this.labelFont = labelFont;
    }

    /**
     * @return the valueMargin
     */
    public double getValueMargin() {
        return valueMargin;
    }

    /**
     * @param valueMargin the valueMargin to set
     */
    public void setValueMargin(double valueMargin) {
        this.valueMargin = valueMargin;
    }

    /**
     * @return the valueColor
     */
    public Paint getValueColor() {
        return valueColor;
    }

    /**
     * @param valueColor the valueColor to set
     */
    public void setValueColor(Paint valueColor) {
        this.valueColor = valueColor;
    }

    /**
     * @return the valueFont
     */
    public Font getValueFont() {
        return valueFont;
    }

    /**
     * @param valueFont the valueFont to set
     */
    public void setValueFont(Font valueFont) {
        this.valueFont = valueFont;
    }
    
    
    @Override
    public String getHumanReadableName() {
        return "Base";
    }

    @Override
    public void draw(Context2D context, DataSeriesList series, Labels labels) {
        
        context.setShadowBlurRadius(shadowBlurRadius);
        context.setShadowXOffset(shadowOffsetX);
        context.setShadowYOffset(shadowOffsetY);
        context.setShadowColor(shadowColor);
        
        double width = context.getWidth();
        double height = context.getHeight();
        
        if(!isBgFillModified()){
            setDefaultGradientPaintForBackground((float)height);
        }
        
        Rectangle2D.Double r = new Rectangle2D.Double(0, 0, width, height);
        context.setPaint(backgroundFill);
        context.fill(r);
        context.setPaint(borderPaint);
        context.setStroke(borderStroke);
        context.draw(r);
        
    }

    

    
    
}
