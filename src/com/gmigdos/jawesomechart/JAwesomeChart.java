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
package com.gmigdos.jawesomechart;

import com.gmigdos.jawesomechart.core.DataSeriesList;
import com.gmigdos.jawesomechart.core.Labels;
import com.gmigdos.jawesomechart.core.Legend;
import com.gmigdos.jawesomechart.renderers.ChartRenderer;
import com.gmigdos.jawesomechart.renderers.twodimensional.horizontal.ColumnChartRenderer;
import com.gmigdos.jawesomechart.util.Context2D;
import com.gmigdos.jawesomechart.util.Utilities;
import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import javax.imageio.ImageIO;

/**
 *
 * @author Georgios Migdos <cyberpython@gmail.com>
 */
public class JAwesomeChart {

    private final static double DEFAULT_PADDING_TOP = 10;
    private final static double DEFAULT_PADDING_BOTTOM = 10;
    private final static double DEFAULT_PADDING_LEFT = 10;
    private final static double DEFAULT_PADDING_RIGHT = 10;
    private final static double DEFAULT_TITLE_MARGIN = 18;
    private final static double DEFAULT_SUBTITLE_MARGIN = 2;
    private final static double DEFAULT_LEGEND_MARGIN = 10;
    private final static Font DEFAULT_TITLE_FONT = new Font("SansSerif", Font.BOLD, 16);
    private final static Font DEFAULT_SUBTITLE_FONT = new Font("SansSerif", Font.BOLD, 14);
    private final static Color DEFAULT_TITLE_COLOR = new Color(51, 51, 51);
    private final static Color DEFAULT_SUBTITLE_COLOR = new Color(88, 88, 88);
    public static final Color TRANSPARENT = new Color(1, 1, 1, 0.0f);
    private String title;
    private String subtitle;
    private DataSeriesList dataseries;
    private Labels labels;
    private ChartRenderer renderer;
    private Legend legend;
    private int width;
    private int height;
    private double paddingTop;
    private double paddingBottom;
    private double paddingLeft;
    private double paddingRight;
    private double titleMargin;
    private double subtitleMargin;
    private double legendMargin;
    private Font titleFont;
    private Font subtitleFont;
    private Color titleColor;
    private Color subtitleColor;
    private Paint backgroundPaint;
    private Paint borderPaint;
    private BasicStroke borderStroke;
    private boolean drawLegend;

    public JAwesomeChart(int width, int height) {

        title = null;
        subtitle = null;
        
        paddingTop = DEFAULT_PADDING_TOP;
        paddingBottom = DEFAULT_PADDING_BOTTOM;
        paddingLeft = DEFAULT_PADDING_LEFT;
        paddingRight = DEFAULT_PADDING_RIGHT;

        titleMargin = DEFAULT_TITLE_MARGIN;
        subtitleMargin = DEFAULT_SUBTITLE_MARGIN;
        legendMargin = DEFAULT_LEGEND_MARGIN;

        titleFont = DEFAULT_TITLE_FONT;
        subtitleFont = DEFAULT_SUBTITLE_FONT;

        titleColor = DEFAULT_TITLE_COLOR;
        subtitleColor = DEFAULT_SUBTITLE_COLOR;

        backgroundPaint = TRANSPARENT;
        borderPaint = TRANSPARENT;

        dataseries = new DataSeriesList();
        labels = new Labels();

        legend = new Legend(width/4, width-paddingLeft-paddingRight, dataseries);
        renderer = null;

        this.width = width;
        this.height = height;

        
        borderStroke = new BasicStroke();

        drawLegend = true;
    }

    public void clearSeries() {
        this.dataseries.clear();
    }

    public void addSeries(String name, double[] values, Color seriesColor) {
        dataseries.addSeries(name, values, seriesColor);
    }

    public void addSeries(String name, double[] values) {
        dataseries.addSeries(name, values);
    }

    public void draw(Graphics g) {
        //BufferedImage bi = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        //Graphics2D g2d = (Graphics2D) bi.createGraphics();
        
        Graphics2D g2d = (Graphics2D) g;
        Context2D context = new Context2D(g2d, width, height);

        fillBackground(context, backgroundPaint, width, height);
        drawOuterBorder(context, borderPaint, borderStroke, width, height);

        drawTitleAndSubtitle(context);
        
        drawLegend(context);

        drawChart(context); //NOTE: should always be last to draw as renderers 
                            //may not restore the context transformations correctly
        //g.drawImage(bi, 0, 0, null);
    }

    private void fillBackground(Context2D context, Paint backgroundPaint, double width, double height) {
        context.setPaint(backgroundPaint);
        context.fill(new Rectangle2D.Double(0, 0, width, height));
    }

    private void drawOuterBorder(Context2D context, Paint borderPaint, BasicStroke borderStroke, double width, double height) {
        float borderWidth = borderStroke.getLineWidth();
        context.setStroke(borderStroke);
        context.setPaint(borderPaint);
        context.draw(new Rectangle2D.Double(borderWidth / 2, borderWidth / 2, width - borderWidth, height - borderWidth));
    }

    private void drawCenteredText(Context2D context, String text, double width, Font textFont, Color textColor, double maxWidth) {
        context.setFont(textFont);
        context.setPaint(textColor);
        context.drawText(text, width / 2, 0, Context2D.HORIZONTAL_ALIGN_CENTER, Context2D.VERTICAL_ALIGN_TOP, maxWidth);
    }

    private void drawTitleAndSubtitle(Context2D context) {
        double widthWithoutPadding = width - paddingLeft - paddingRight;
        context.save();

        context.translate(0, paddingTop);

        //Draw title:
        if (title != null) {
            drawCenteredText(context, title, width, titleFont, titleColor, widthWithoutPadding);
            context.translate(0, context.getLineHeight(titleFont, title));
        }

        //Draw subtitle:
        if (subtitle != null) {
            context.translate(0, subtitleMargin);
            drawCenteredText(context, subtitle, width, subtitleFont, subtitleColor, widthWithoutPadding);
            context.translate(0, context.getLineHeight(subtitleFont, subtitle));
        }
        
        if(title!=null || subtitle!=null){
           context.translate(0, titleMargin);
        }

        context.restore();
    }

    private void drawChart(Context2D context) {
        if (renderer != null) {
            double offsetTop = calculateOffsetCausedByTitleAndSubtitle(context);
            double chartWidth = width - paddingLeft - paddingRight;
            double chartHeight = height - offsetTop - paddingBottom;
            
            context.save();
            context.translate(paddingLeft, offsetTop);

            if (drawLegend && legend != null) {
                Legend.LegendPosition position = legend.getPosition();
                if (position.equals(Legend.LegendPosition.LEGEND_POSITION_LEFT)
                        || position.equals(Legend.LegendPosition.LEGEND_POSITION_RIGHT)) {

                    chartWidth -= legend.getWidth() + legendMargin;

                    if (position.equals(Legend.LegendPosition.LEGEND_POSITION_LEFT)) {
                        context.translate(legend.getWidth()+legendMargin, 0);
                    }
                    
                } else {
                    chartHeight -= legend.getHeight() + legendMargin;
                    if (position.equals(Legend.LegendPosition.LEGEND_POSITION_TOP)) {
                        context.translate(0, legend.getHeight()+legendMargin);
                    }
                }
            }
            
            context.resize(chartWidth, chartHeight);
            renderer.draw(context, dataseries, labels);
            context.restore();
        }
    }

    private void drawLegend(Context2D context) {
        if (legend != null && drawLegend) {
            legend.setContext(context);
            double tX = 0;
            double tY = 0;
            double offsetTop = calculateOffsetCausedByTitleAndSubtitle(context);
            Legend.LegendPosition position = legend.getPosition();

            if (position.equals(Legend.LegendPosition.LEGEND_POSITION_LEFT)) {
                tX = paddingLeft;
                tY = offsetTop;
            } else if (position.equals(Legend.LegendPosition.LEGEND_POSITION_RIGHT)) {
                tX = width - paddingRight - legend.getWidth();
                tY = offsetTop;
            } else if (position.equals(Legend.LegendPosition.LEGEND_POSITION_TOP)) {
                tX = paddingLeft;
                tY = offsetTop;
            } else if (position.equals(Legend.LegendPosition.LEGEND_POSITION_BOTTOM)) {
                tX = paddingLeft;
                tY = height - paddingBottom - legend.getHeight();
            }
            context.save();
            context.translate(tX, tY);
            legend.draw();
            context.restore();
        }
    }

    private double calculateOffsetCausedByTitleAndSubtitle(Context2D context) {
        double offsetTop = paddingTop;
        if (title != null) {
            offsetTop += context.getLineHeight(titleFont, title);
        }
        if (subtitle != null) {
            offsetTop += context.getLineHeight(subtitleFont, subtitle) + subtitleMargin;
        }
        
        if(title!=null || subtitle!=null){
            offsetTop += titleMargin;
        }
        return offsetTop;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public DataSeriesList getDataseries() {
        return dataseries;
    }

    public void setDataseries(DataSeriesList dataseries) {
        this.dataseries = dataseries;

    }

    public Labels getLabels() {
        return labels;
    }

    public void setLabels(Labels labels) {
        this.labels = labels;

    }

    public void setLabels(String[] labels) {
        this.labels.clear();
        this.labels.addAll(Arrays.asList(labels));

    }

    public ChartRenderer getRenderer() {
        return renderer;
    }

    public void setRenderer(ChartRenderer renderer) {
        this.renderer = renderer;
    }

    public Legend getLegend() {
        return legend;
    }

    public void setLegend(Legend legend) {
        this.legend = legend;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;

    }

    public String getSubtitle() {
        return subtitle;
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;

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
     * @return the titleMargin
     */
    public double getTitleMargin() {
        return titleMargin;
    }

    /**
     * @param titleMargin the titleMargin to set
     */
    public void setTitleMargin(double titleMargin) {
        this.titleMargin = titleMargin;
    }

    /**
     * @return the subtitleMargin
     */
    public double getSubtitleMargin() {
        return subtitleMargin;
    }

    /**
     * @param subtitleMargin the subtitleMargin to set
     */
    public void setSubtitleMargin(double subtitleMargin) {
        this.subtitleMargin = subtitleMargin;
    }

    /**
     * @return
     */
    public double getLegendMargin() {
        return legendMargin;
    }

    /**
     * @param legendMargin
     */
    public void setLegendMargin(double legendMargin) {
        this.legendMargin = legendMargin;
    }

    /**
     * @return the titleFont
     */
    public Font getTitleFont() {
        return titleFont;
    }

    /**
     * @param titleFont the titleFont to set
     */
    public void setTitleFont(Font titleFont) {
        this.titleFont = titleFont;
    }

    /**
     * @return the subtitleFont
     */
    public Font getSubtitleFont() {
        return subtitleFont;
    }

    /**
     * @param subtitleFont the subtitleFont to set
     */
    public void setSubtitleFont(Font subtitleFont) {
        this.subtitleFont = subtitleFont;
    }

    /**
     * @return the titleColor
     */
    public Color getTitleColor() {
        return titleColor;
    }

    /**
     * @param titleColor the titleColor to set
     */
    public void setTitleColor(Color titleColor) {
        this.titleColor = titleColor;
    }

    /**
     * @return the subtitleColor
     */
    public Color getSubtitleColor() {
        return subtitleColor;
    }

    /**
     * @param subtitleColor the subtitleColor to set
     */
    public void setSubtitleColor(Color subtitleColor) {
        this.subtitleColor = subtitleColor;
    }

    /**
     * @return the backgroundPaint
     */
    public Paint getBackgroundPaint() {
        return backgroundPaint;
    }

    /**
     * @param backgroundPaint the backgroundPaint to set
     */
    public void setBackgroundPaint(Paint backgroundPaint) {
        this.backgroundPaint = backgroundPaint;
    }

    /**
     * @return the borderPaint
     */
    public Paint getBorderPaint() {
        return borderPaint;
    }

    /**
     * @param borderPaint the borderPaint to set
     */
    public void setBorderPaint(Paint borderPaint) {
        this.borderPaint = borderPaint;
    }

    /**
     * @return the borderStroke
     */
    public BasicStroke getBorderStroke() {
        return borderStroke;
    }

    /**
     * @param borderStroke the borderStroke to set
     */
    public void setBorderStroke(BasicStroke borderStroke) {
        this.borderStroke = borderStroke;
    }

    public boolean isDrawLegendOn() {
        return drawLegend;
    }

    public void setDrawLegend(boolean drawLegend) {
        this.drawLegend = drawLegend;
    }

    public static void drawAndSaveChartAsImageFile(JAwesomeChart ac, String filePath) {
        BufferedImage bi = new BufferedImage(ac.getWidth(), ac.getHeight(), BufferedImage.TYPE_INT_ARGB);
        long t1 = System.currentTimeMillis();
        double i;
        int iterations = 1;
        for (i = 0; i < iterations; i += 1) {
            ac.draw(bi.getGraphics());
        }
        long t2 = System.currentTimeMillis();
        System.out.println(ac.getRenderer().getHumanReadableName() + " chart - mean time: " + ((t2 - t1) / i) + " ms");
        try {
            File f = new File(filePath);
            if (f.exists()) {
                f.delete();
            }
            f.mkdirs();
            ImageIO.write(bi, "png", f);
        } catch (IOException ioe) {
            System.err.println(ioe.getMessage());
        }
    }

    public static void main(String[] args) {
        
        JAwesomeChart ac = new JAwesomeChart(960, 450);
        ac.setTitle("Αποτελέσματα Βουλευτικών Εκλογών 17/06/2012");
        ac.addSeries("ΝΔ", new double[]{29.66}, Color.decode("0x003A65"));
        ac.addSeries("ΣΥ.ΡΙΖ.Α", new double[]{26.89}, Color.decode("0xBC1C93"));
        ac.addSeries("ΠΑ.ΣΟ.Κ", new double[]{12.28}, Color.decode("0x146D36"));
        ac.addSeries("ΑΝΕΞ.ΕΛΛ", new double[]{7.51}, Color.decode("0x8FB7D5"));
        ac.addSeries("ΧΡ.ΑΥΓΗ", new double[]{6.92}, Color.decode("0x333333"));
        ac.addSeries("ΔΗΜ.ΑΡ", new double[]{6.26}, Color.decode("0xFF7E00"));
        ac.addSeries("Κ.Κ.Ε", new double[]{4.5}, Color.decode("0xFF0012"));
        ac.addSeries("ΔΗ.ΞΑ", new double[]{1.59}, Color.decode("0xBE71FF"));
        ac.addSeries("ΛΑ.Ο.Σ", new double[]{1.58}, Color.decode("0x70A2C9"));
        ac.addSeries("ΟΙΚ.ΠΡΑ", new double[]{0.88}, Color.decode("0x819862"));
        ac.addSeries("ΛΟΙΠΑ", new double[]{1.93}, Color.decode("0xAAAAAA"));
        
        ChartRenderer renderer2 = new ColumnChartRenderer();
        ac.setRenderer(renderer2);
        drawAndSaveChartAsImageFile(ac, "/home/cyberpython/Desktop/greek-legislative-elections-2012-06-17-bars.png");
        
        Utilities.xdgOpenFile(new File("/home/cyberpython/Desktop/greek-legislative-elections-2012-06-17-bars.png"));
    }
}
