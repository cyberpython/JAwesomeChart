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
package com.gmigdos.jawesomechart.util;

import java.awt.*;
import java.awt.color.ColorSpace;
import java.awt.font.FontRenderContext;
import java.awt.font.GlyphVector;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.awt.image.ConvolveOp;
import java.awt.image.Kernel;
import java.util.ArrayDeque;
import java.util.Deque;

/**
 *
 * @author Georgios Migdos <cyberpython@gmail.com>
 */
public class Context2D {

    public static final int VERTICAL_ALIGN_MIDDLE = 0;
    public static final int VERTICAL_ALIGN_TOP = 1;
    public static final int VERTICAL_ALIGN_BOTTOM = 2;
    public static final int VERTICAL_ALIGN_BASELINE = 3;
    public static final int HORIZONTAL_ALIGN_LEFT = 4;
    public static final int HORIZONTAL_ALIGN_CENTER = 5;
    public static final int HORIZONTAL_ALIGN_RIGHT = 6;
    private Deque<Graphics2D> stack;
    //private Deque<Graphics2D> bufferStack;//TODO: Use stacks
    //private Deque<Graphics2D> shadowStack;
    BufferedImage temporaryImage;
    BufferedImage shadowImage;
    private Graphics2D buffer;
    private Graphics2D shadow;
    private Shape originalClip;
    private AffineTransform originalClipTransform;
    
    private Paint shadowColor;
    private int shadowBlurRadius;
    private double shadowXOffset;
    private double shadowYOffset;
    
    private double width;
    private double height;
    private double originalWidth;
    private double originalHeight;

    public Context2D(Graphics2D g2d, int width, int height) {
        this.stack = new ArrayDeque<Graphics2D>();

        Graphics2D g = (Graphics2D) g2d.create();
        this.width = width;
        this.height = height;
        this.originalWidth = width;
        this.originalHeight = height;
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        stack.push(g);

        temporaryImage = null;
        shadowImage = null;
        buffer = null;
        shadow = null;

        originalClip = null;
        originalClipTransform = null;
        
        shadowColor = new Color(0f, 0f, 0f, 0.5f);
        shadowBlurRadius = 5;
        shadowXOffset = 3;
        shadowYOffset = 0;
        
        clear();

    }

    public Shape getOriginalClip() {
        return originalClip;
    }

    public void setOriginalClip(Shape originalClip) {
        this.originalClip = originalClip;
        Graphics2D g;
        if (buffer != null) {
            g = buffer;
        } else {
            g = stack.peek();
        }
        this.originalClipTransform = g.getTransform();
        setClip(originalClip);
    }

    public int getShadowBlurRadius() {
        return shadowBlurRadius;
    }

    public void setShadowBlurRadius(int shadowBlurRadius) {
        this.shadowBlurRadius = shadowBlurRadius;
    }
    
    /**
     * @return the shadowColor
     */
    public Paint getShadowColor() {
        return shadowColor;
    }

    /**
     * @param shadowColor the shadowColor to set
     */
    public void setShadowColor(Paint shadowColor) {
        this.shadowColor = shadowColor;
    }

    /**
     * @return the shadowXOffset
     */
    public double getShadowXOffset() {
        return shadowXOffset;
    }

    /**
     * @param shadowXOffset the shadowXOffset to set
     */
    public void setShadowXOffset(double shadowXOffset) {
        this.shadowXOffset = shadowXOffset;
    }

    /**
     * @return the shadowYOffset
     */
    public double getShadowYOffset() {
        return shadowYOffset;
    }

    /**
     * @param shadowYOffset the shadowYOffset to set
     */
    public void setShadowYOffset(double shadowYOffset) {
        this.shadowYOffset = shadowYOffset;
    }

    public double getWidth() {
        return width;
    }

    public double getHeight() {
        return height;
    }

    public void setWidth(double width) {
        this.width = width;
    }

    public void setHeight(double height) {
        this.height = height;
    }
    
    public void resize(double width, double height){
        this.width = width;
        this.height = height;
        //setClip(new Rectangle2D.Double(0, 0, width+1, height+1));
    }

    public Graphics2D save() {
        //TODO: shadowed drawing
        Graphics2D result = (Graphics2D) stack.peek().create();
        stack.push(result);
        return result;
    }

    public Graphics2D restore() {
        //TODO: shadowed drawing
        stack.pop().dispose();
        return stack.peek();
    }

    public Graphics2D reset() {
        Graphics2D g = null;
        while (!stack.isEmpty()) {
            g = stack.pop();
            if (stack.size() > 1) {
                g.dispose();
            }
        }
        stack.push(g);
        if (buffer != null) {
            buffer.dispose();
        }
        if (shadow != null) {
            shadow.dispose();
        }
        buffer = null;
        shadow = null;
        return g;
    }
    
    //TODO: fix (shadows)
    private void clear() {
        Graphics2D g = stack.peek();
        Composite old = g.getComposite();
        Paint p = g.getPaint();
        Composite composite = AlphaComposite.getInstance(AlphaComposite.CLEAR, 0.0f);
        g.setComposite(composite);
        g.setPaint(new Color(0, 0, 0, 0));
        g.fill(new Rectangle2D.Double(0, 0, width, height));
        g.setComposite(old);
        g.setPaint(p);
    }
    
    public void setClip(Shape clipRect) {
        if (buffer != null) {
            buffer.setClip(clipRect);
            if (shadow != null) {
                shadow.setClip(clipRect);
            }
        } else {
            Graphics2D g = stack.peek();
            g.setClip(clipRect);
        }
    }
    
    public void drawCurrentClip(Paint p){
        Paint oldPaint;
        Stroke oldStroke;
        Rectangle2D bounds;
        Stroke s = new BasicStroke(1, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND, 1, new float[]{5}, 0);
        if (buffer != null) {
            oldPaint = buffer.getPaint();
            oldStroke = buffer.getStroke();
            buffer.setPaint(p);
            buffer.setStroke(s);
            bounds = (Rectangle2D) buffer.getClip().getBounds2D().clone();
            bounds.setRect(bounds.getX(), bounds.getY(), bounds.getWidth()-1, bounds.getHeight()-1);
            buffer.draw(bounds);
            buffer.setPaint(oldPaint);
            buffer.setStroke(oldStroke);
        } else {
            Graphics2D g = stack.peek();
            oldPaint = g.getPaint();
            oldStroke = g.getStroke();
            g.setPaint(p);
            g.setStroke(s);
            bounds = (Rectangle2D) g.getClip().getBounds2D().clone();
            bounds.setRect(bounds.getX(), bounds.getY(), bounds.getWidth()-1, bounds.getHeight()-1);
            g.draw(bounds);
            g.setPaint(oldPaint);
            g.setStroke(oldStroke);
        }
    }

    public void resetClip() {
        AffineTransform t;
        if (buffer != null) {
            t = buffer.getTransform();
            buffer.setTransform(originalClipTransform);
            buffer.setClip(originalClip);
            buffer.setTransform(t);
            if (shadow != null) {
                t = shadow.getTransform();
                shadow.setTransform(originalClipTransform);
                shadow.setClip(originalClip);
                shadow.setTransform(t);
            }
        } else {
            Graphics2D g = stack.peek();
            t = g.getTransform();
            g.setTransform(originalClipTransform);
            g.setClip(originalClip);
            g.setTransform(t);
        }
    }

    public void clearClip() {
        if (buffer != null) {
            buffer.setClip(null);
            if (shadow != null) {
                shadow.setClip(null);
            }
        } else {
            Graphics2D g = stack.peek();
            g.setClip(null);
        }
    }

    public void translate(double x, double y) {
        if (buffer != null) {
            buffer.translate(x, y);
            if (shadow != null) {
                shadow.translate(x, y);
            }
        } else {
            Graphics2D g = stack.peek();
            g.translate(x, y);
        }
    }

    public void translate(int x, int y) {
        if (buffer != null) {
            buffer.translate(x, y);
            if (shadow != null) {
                shadow.translate(x, y);
            }
        } else {
            Graphics2D g = stack.peek();
            g.translate(x, y);
        }
    }

    public void rotate(double theta) {
        if (buffer != null) {
            buffer.rotate(theta);
            if (shadow != null) {
                shadow.rotate(theta);
            }
        } else {
            Graphics2D g = stack.peek();
            g.rotate(theta);
        }
    }

    public void rotate(double theta, double x, double y) {
        if (buffer != null) {
            buffer.rotate(theta);
            if (shadow != null) {
                shadow.rotate(theta, x, y);
            }
        } else {
            Graphics2D g = stack.peek();
            g.rotate(theta, x, y);
        }
    }

    public void scale(double sx, double sy) {
        if (buffer != null) {
            buffer.scale(sx, sy);
            if (shadow != null) {
                shadow.scale(sx, sy);
            }
        } else {
            Graphics2D g = stack.peek();
            g.scale(sx, sy);
        }
    }

    public void transform(AffineTransform Tx) {
        if (buffer != null) {
            buffer.transform(Tx);
            if (shadow != null) {
                shadow.transform(Tx);
            }
        } else {
            Graphics2D g = stack.peek();
            g.transform(Tx);
        }
    }

    public FontMetrics getFontMetrics() {
        if (buffer != null) {
            return buffer.getFontMetrics();
        } else {
            return stack.peek().getFontMetrics();
        }
    }

    public Font getFont() {
        if (buffer != null) {
            return buffer.getFont();
        } else {
            return stack.peek().getFont();
        }
    }

    @Override
    protected void finalize() throws Throwable {
        while (!stack.isEmpty()) {
            stack.pop().dispose();
        }
        if (buffer != null) {
            buffer.dispose();
        }
        if (shadow != null) {
            shadow.dispose();
        }
        buffer = null;
        shadow = null;
        super.finalize();
    }

    public void setFont(Font f) {
        if (buffer != null) {
            buffer.setFont(f);
            if (shadow != null) {
                shadow.setFont(f);
            }
        } else {
            Graphics2D g = stack.peek();
            g.setFont(f);
        }
    }

    public void setPaint(Paint p) {
        if (buffer != null) {
            buffer.setPaint(p);
        } else {
            Graphics2D g = stack.peek();
            g.setPaint(p);
        }
    }

    public void setPaint(Color c, float opacity) {
        float[] colorComponents = new float[3];
        c.getRGBColorComponents(colorComponents);
        Color newColor = new Color(ColorSpace.getInstance(ColorSpace.CS_sRGB), colorComponents, opacity);

        if (buffer != null) {
            buffer.setPaint(newColor);
        } else {
            Graphics2D g = stack.peek();
            g.setPaint(newColor);
        }
    }

    public void setStroke(Stroke s) {
        if (buffer != null) {
            buffer.setStroke(s);
            if (shadow != null) {
                shadow.setStroke(s);
            }
        } else {
            Graphics2D g = stack.peek();
            g.setStroke(s);
        }
    }

    public void setComposite(Composite comp) {
        if (buffer != null) {
            buffer.setComposite(comp);
            if (shadow != null) {
                shadow.setComposite(comp);
            }
        } else {
            Graphics2D g = stack.peek();
            g.setComposite(comp);
        }
    }
    
    public Composite getComposite() {
        if (buffer != null) {
            return buffer.getComposite();
        } else {
            Graphics2D g = stack.peek();
            return g.getComposite();
        }
    }

    public void draw(Shape s) {
        if (buffer != null) {
            buffer.draw(s);
            if (shadow != null) {
                shadow.draw(s);
            }
        } else {
            Graphics2D g = stack.peek();
            g.draw(s);
        }
    }

    public void fill(Shape s) {
        if (buffer != null) {
            buffer.fill(s);
            if (shadow != null) {
                shadow.fill(s);
            }
        } else {
            Graphics2D g = stack.peek();
            g.fill(s);
        }
    }

    public void beginShadowedDrawing() {

        if (buffer != null) {
            buffer.dispose();
        }
        if (shadow != null) {
            shadow.dispose();
        }

        
        Graphics2D g = stack.peek();
        
        AffineTransform tranform = g.getTransform();

        temporaryImage = new BufferedImage((int)Math.ceil(originalWidth), (int)Math.ceil(originalHeight), BufferedImage.TYPE_INT_ARGB);
        shadowImage = new BufferedImage((int)Math.ceil(originalWidth), (int)Math.ceil(originalHeight), BufferedImage.TYPE_INT_ARGB);
        
        buffer = (Graphics2D) temporaryImage.getGraphics();
        shadow = (Graphics2D) shadowImage.getGraphics();

        buffer.setRenderingHints(g.getRenderingHints());
        buffer.setComposite(g.getComposite());
        buffer.setPaint(g.getPaint());
        buffer.setStroke(g.getStroke());
        shadow.setRenderingHints(g.getRenderingHints());
        shadow.setComposite(g.getComposite());
        shadow.setPaint(shadowColor);
        shadow.setStroke(g.getStroke());
        
        buffer.transform(tranform);
        shadow.transform(tranform);
        
        shadow.translate(shadowXOffset, shadowYOffset);
    }

    public void endShadowedDrawing() {
        if (buffer != null && shadow != null) {
            Graphics2D g = stack.peek();
            
            AffineTransform tranform = g.getTransform();

            shadowImage = blur(shadowImage, shadowBlurRadius);
            g.drawImage(shadowImage, (int) -tranform.getTranslateX(), (int) -tranform.getTranslateY(), null);
            g.drawImage(temporaryImage, (int) -tranform.getTranslateX(), (int) -tranform.getTranslateY(), null);
        }

        if (buffer != null) {
            buffer.dispose();
        }
        if (shadow != null) {
            shadow.dispose();
        }
        buffer = null;
        shadow = null;
        temporaryImage = null;
        shadowImage = null;
    }
    
    private BufferedImage blur(BufferedImage src, int blurRadius) {

        int size = blurRadius * 2 + 1;
        int kernelSize = size * size;
        float weight = 1.0f / (kernelSize);
        float[] blur = new float[kernelSize];

        for (int i = 0; i < kernelSize; i++) {
            blur[i] = weight;
        }

        Kernel kernel = new Kernel(size, size, blur);
        ConvolveOp op = new ConvolveOp(kernel);

        return op.filter(src, null);
    }

    public void drawText(String text, double x, double y, int horizontalAlignment, int verticalAlignment) {
        drawText(text, x, y, horizontalAlignment, verticalAlignment, -1);
    }

    public void drawText(String text, double x, double y, int horizontalAlignment, int verticalAlignment, double maxWidth) {
        Graphics2D g2d;
        if (buffer != null) {
            g2d = buffer;
        } else {
            g2d = stack.peek();
        }

        Font oldFont = g2d.getFont();
        if (maxWidth > 0) {
            adjustFontSizeToFitTextInWidth(text, maxWidth);
        }
        FontMetrics fontMetrics = g2d.getFontMetrics();
        Rectangle stringBounds = fontMetrics.getStringBounds(text, g2d).getBounds();

        Font font = g2d.getFont();
        FontRenderContext renderContext = g2d.getFontRenderContext();
        GlyphVector glyphVector = font.createGlyphVector(renderContext, text);
        Rectangle visualBounds = glyphVector.getVisualBounds().getBounds();
        
        Double textX;
        Double textY;
        if (horizontalAlignment == HORIZONTAL_ALIGN_CENTER) {
            textX = x - stringBounds.width / 2;
        } else if (horizontalAlignment == HORIZONTAL_ALIGN_RIGHT) {
            textX = x - stringBounds.width;
        } else {
            textX = x;
        }
        if (verticalAlignment == VERTICAL_ALIGN_BOTTOM) {
            textY = y - visualBounds.y - visualBounds.height;
        } else if (verticalAlignment == VERTICAL_ALIGN_MIDDLE) {
            textY = y - visualBounds.height / 2 - visualBounds.y;
        } else if (verticalAlignment == VERTICAL_ALIGN_TOP) {
            textY = y - visualBounds.y;
        } else {
            textY = y;
        }

        g2d.drawString(text, textX.floatValue(), textY.floatValue());
        if (shadow != null) {
            shadow.drawString(text, textX.floatValue(), textY.floatValue());
            shadow.setFont(oldFont);
        }
        g2d.setFont(oldFont);
    }

    public void adjustFontSizeToFitTextInWidth(String text, double widthLimit) {

        Graphics2D g2d;
        if (buffer != null) {
            g2d = buffer;
        } else {
            g2d = stack.peek();
        }

        if (widthLimit < 0) {
            return;
        }
        Graphics2D g = (Graphics2D) g2d.create();
        Font f = g2d.getFont();
        float fontSize = f.getSize2D();
        FontMetrics fontMetrics = g2d.getFontMetrics();
        double textWidth = fontMetrics.stringWidth(text);

        while (textWidth > widthLimit) {
            fontSize -= 0.5;
            f = f.deriveFont(fontSize);
            g2d.setFont(f);
            fontMetrics = g2d.getFontMetrics();
            textWidth = fontMetrics.stringWidth(text);
        }

        g.dispose();
        g2d.setFont(f);
        if (shadow != null) {
            shadow.setFont(f);
        }
    }

    public String calculateWidestLine(String[] textLines) {
        double maxWidth = 0.0;
        double textWidth;
        String widestLine = "";
        FontMetrics fontMetrics = getFontMetrics();
        for (String line : textLines) {
            textWidth = fontMetrics.stringWidth(line);
            if (textWidth > maxWidth) {
                maxWidth = textWidth;
                widestLine = line;
            }
        }
        return widestLine;
    }
    
    public String calculateWidestLine(String[] textLines, Font f) {
        Font tmp = getFont();
        setFont(f);
        String result = calculateWidestLine(textLines);
        setFont(tmp);
        return result;
    }

    public int calculateStringWidth(String text) {
        FontMetrics fontMetrics = getFontMetrics();
        return fontMetrics.stringWidth(text);
    }

    public int calculateStringWidth(String text, Font f) {
        Font tmp = getFont();
        setFont(f);
        FontMetrics fontMetrics = getFontMetrics();
        int result = fontMetrics.stringWidth(text);
        setFont(tmp);
        return result;
    }
    
    public int getStandardLineHeight(){
        FontMetrics fontMetrics = getFontMetrics();
        int result = fontMetrics.getHeight();
        return result;
    }
    
    public int getStandardLineHeight(Font f){
        Font tmp = getFont();
        setFont(f);
        FontMetrics fontMetrics = getFontMetrics();
        int result = fontMetrics.getHeight();
        setFont(tmp);
        return result;
    }
    
    public float getLineHeight(String line){
        if(line==null){return 0;}
        
        FontMetrics fontMetrics = getFontMetrics();
        Graphics2D g2d;
        
        if (buffer != null) {
            g2d = buffer;
        } else {
            g2d = stack.peek();
        }
        float result = fontMetrics.getLineMetrics(line, g2d).getHeight();
        return result;
    }
    
    public float getLineHeight(Font f, String line){
        if(line==null){return 0;}
        Font tmp = getFont();
        setFont(f);
        FontMetrics fontMetrics = getFontMetrics();
        Graphics2D g2d;
        
        if (buffer != null) {
            g2d = buffer;
        } else {
            g2d = stack.peek();
        }
        float result = fontMetrics.getLineMetrics(line, g2d).getHeight();
        setFont(tmp);
        return result;
    }
}
