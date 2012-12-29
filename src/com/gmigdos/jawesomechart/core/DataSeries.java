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

import com.gmigdos.jawesomechart.util.StringsProvider;
import java.awt.Color;
import java.util.ArrayList;
import java.util.Collections;


/**
 * DataSeries represents a data series as a List of {@link DataSeriesDataProvider}'s.
 * 
 * @author Georgios Migdos <cyberpython@gmail.com>
 */
public class DataSeries extends ArrayList<DataSeriesDataProvider> {
    
    private String name;
    private Color seriesColor;
    
    private final static Color DEFAULT_COLOR = new Color(220, 36, 0);
    private static int count = 1;

    /*
     * Creates a new DataSeries object with the default name and color.
     */
    public DataSeries() {
        this(null, null);
    }

    /*
     * Creates a new DataSeries object with the given name and color.
     * 
     * @param name The new DataSeries' name
     * @param seriesColor The new DataSeries' color
     */
    public DataSeries(String name, Color seriesColor) {
        if(name!=null){
            this.name = name;
        }else{
            this.name = StringsProvider.SERIES + "-" + (count++);
        }
        if(seriesColor!=null){
            this.seriesColor = seriesColor;
        }else{
            this.seriesColor = DEFAULT_COLOR;
        }
    }

    /**
     * Returns this series' name.
     */
    public String getName() {
        return name;
    }

    /**
     * Sets this series' name.
     * 
     * @param name The new name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Returns this series' color.
     */
    public Color getSeriesColor() {
        return seriesColor;
    }

    /**
     * Sets this series' color.
     * 
     * @param seriesColor The new color
     */
    public void setSeriesColor(Color seriesColor) {
        if(seriesColor!=null){
            this.seriesColor = seriesColor;
        }else{
            this.seriesColor = DEFAULT_COLOR;
        }
    }
    
    /**
     * Returns the maximum value of the entries in this series.
     * 
     * @return the maximum value of the entries in this series or 0 if the series is empty
     */
    public Double getMaxValue(){
        if(this.size()==0){return 0.0;}
        return Collections.max(this, new DataComparator()).getDataValue();
    }
    
    /**
     * Returns the minimum value of the entries in this series.
     * 
     * @return the minimum value of the entries in this series or 0 if the series is empty
     */
    public Double getMinValue(){
        if(this.size()==0){return 0.0;}
        return Collections.min(this, new DataComparator()).getDataValue();
    }

    
    /**
     * Returns the element at the specified position in this list.
     * 
     * @param index index of the element to return
     * 
     * @return the element at the specified position in this list or null if the index is out of bounds
     */
    @Override
    public DataSeriesDataProvider get(int index) {
        try{
            return super.get(index);
        }catch(IndexOutOfBoundsException iobe){
            return null;
        }
    }
    
    
}
