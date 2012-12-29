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

import com.gmigdos.jawesomechart.core.DataSeries;
import com.gmigdos.jawesomechart.core.DataSeriesDataProvider;
import com.gmigdos.jawesomechart.core.DataSeriesList;
import com.gmigdos.jawesomechart.core.Labels;
import java.awt.Color;
import java.io.File;
import java.text.DecimalFormat;
import java.util.List;

/**
 *
 * @author Georgios Migdos <cyberpython@gmail.com>
 */
public class Utilities {

    public static Color generateRandomColor() {
        int r = (int) Math.ceil(Math.random() * 150 + 50);
        int g = (int) Math.ceil(Math.random() * 150 + 50);
        int b = (int) Math.ceil(Math.random() * 150 + 50);

        return new Color(r, g, b);
    }
    
    public static String calculateWidestSeriesName(Context2D context, DataSeriesList series){
        double maxWidth = 0;
        double current;
        String name;
        String longest = "";
        for (DataSeries dataSeries : series) {
            name = dataSeries.getName();
            if(name!=null){
                current = context.calculateStringWidth(name);
                if (current>maxWidth) {
                    maxWidth = current;
                    longest = name;
                }
            }
        }
        return longest;
    }
    
    public static String calculateWidestLabel(Context2D context, Labels labels){
        double maxWidth = 0;
        double current;
        String longest = "";
        for (String label:labels) {
            if(label!=null){
                current = context.calculateStringWidth(label);
                if (current>maxWidth||longest==null) {
                    maxWidth = current;
                    longest = label;
                }
            }
        }
        return longest;
    }
    
    public static double calculateWidestLabelWidth(Context2D context, Labels labels){
        double maxWidth = 0;
        double current;
        for (String label:labels) {
            if(label!=null){
                current = context.calculateStringWidth(label);
                if (current>maxWidth) {
                    maxWidth = current;
                }
            }
        }
        return maxWidth;
    }
    
    public static double calculateWidestValue(Context2D context, DataSeriesList series, DecimalFormat df){
        double maxWidth = 0;
        double current;
        double maxValue = Double.NaN;
        for (DataSeries data:series) {
            for (DataSeriesDataProvider dp : data) {
                Double value = dp.getDataValue();
                if(value!=null){
                    current = context.calculateStringWidth(df.format(value.doubleValue()));
                    if (current>maxWidth) {
                        maxWidth = current;
                        maxValue = value;
                    }
                }
            }
            
        }
        return maxValue;
    }
    
    public static double calculateWidestValueWidth(Context2D context, List<Double> values, DecimalFormat df){
        double maxWidth = 0;
        double current;
        for (Double d:values) {
            current = context.calculateStringWidth(df.format(d.doubleValue()));
            if (current>maxWidth) {
                maxWidth = current;
            }
        }
        return maxWidth;
    }

    public static void xdgOpenFile(File f) {
        String CROSS_DESKTOP_OPEN_FILE_COMMAND = "/usr/bin/xdg-open";
        try {
            File parentDir = f.getParentFile();
            String fname = f.getName();
            String cmd = "/bin/sh " + CROSS_DESKTOP_OPEN_FILE_COMMAND + " " + fname;
            Runtime rt = Runtime.getRuntime();
            Process p = rt.exec(cmd, null, parentDir);
            p.waitFor();
        } catch (Exception e) {
        }
    }
}
