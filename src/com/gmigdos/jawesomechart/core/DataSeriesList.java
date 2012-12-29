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

import com.gmigdos.jawesomechart.core.dataproviders.SimpleDataProvider;
import com.gmigdos.jawesomechart.renderers.ChartRenderer;
import java.awt.Color;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * DataSeriesList is a {@link List} of {@link DataSeries} objects and is the actual data model for 
 * the {@link JAwesomeChart} class.
 * 
 * @author Georgios Migdos <cyberpython@gmail.com>
 */
public class DataSeriesList extends ArrayList<DataSeries>{
    
    /**
     * Creates new DataSeries with the given name, color and values and adds it 
     * to this list.
     * 
     * @param name The new series name
     * @param values The new series values (entries)
     * @param color The new series color
     */
    public void addSeries(String name, double[] values, Color color){
        DataSeries ds = new DataSeries(name, color);
        for (double d : values) {
            DataSeriesDataProvider dsdp = new SimpleDataProvider(d);
            ds.add(dsdp);
        }
        this.add(ds);
    }
    
    /**
     * Creates new DataSeries with the given name, values and default color and adds it 
     * to this list.
     * 
     * @param name The new series name
     * @param values The new series values (entries)
     * @param color The new series color
     */
    public void addSeries(String name, double[] values){
        DataSeries ds = new DataSeries(name, null);
        for (double d : values) {
            DataSeriesDataProvider dsdp = new SimpleDataProvider(d);
            ds.add(dsdp);
        }
        this.add(ds);
    }
    
    /**
     * Returns the maximum entry of all the series in this list.
     * 
     * @return the maximum entry of all the series in this list
     */
    public Double getMaxValue(){
        if(size()==0){return 0.0;}
        List<Double> maxVals = new ArrayList<Double>();
        for (DataSeries dataSeries : this) {
            maxVals.add(dataSeries.getMaxValue());
        }
        return Collections.max(maxVals);
    }
    
    /**
     * Returns the maximum of the first elements of each series 
     * (first column if each series is considered to be a row) in this list.
     * This method can be useful for {@link ChartRenderer}s that use only a single 
     * value from each series.
     * 
     * @return the maximum of the first elements of each series in this list.
     */
    public Double getMaxValueOnFirstColumn(){
        if(size()==0){return 0.0;}
        List<Double> vals = new ArrayList<Double>();
        for (DataSeries dataSeries : this) {
            DataSeriesDataProvider dp = dataSeries.get(0);
            if(dp!=null){
                vals.add(dp.getDataValue());
            }
        }
        return Collections.max(vals);
    }
    
    /**
     * Returns the minimum entry of all the series in this list.
     * 
     * @return the minimum entry of all the series in this list
     */
    public Double getMinValue(){
        if(size()==0){return 0.0;}
        List<Double> minVals = new ArrayList<Double>();
        for (DataSeries dataSeries : this) {
            minVals.add(dataSeries.getMinValue());
        }
        return Collections.min(minVals);
    }
    
    /**
     * Returns the minimum of the first elements of each series 
     * (first column if each series is considered to be a row) in this list.
     * This method can be useful for {@link ChartRenderer}s that use only a single 
     * value from each series.
     * 
     * @return the minimum of the first elements of each series in this list.
     */
    public Double getMinValueOnFirstColumn(){
        if(size()==0){return 0.0;}
        List<Double> vals = new ArrayList<Double>();
        for (DataSeries dataSeries : this) {
            DataSeriesDataProvider dp = dataSeries.get(0);
            if(dp!=null){
                vals.add(dp.getDataValue());
            }
        }
        return Collections.min(vals);
    }
    
    /**
     * Returns the sum of the positive first elements of each series 
     * (first column if each series is considered to be a row) in this list.
     * This method can be useful for {@link ChartRenderer}s that use only a single 
     * value from each series.
     * 
     * @return the sum of the positive first elements of each series in this list.
     */
    public double getSumOfPositivesOnFirstColumn(){
        double sum = 0;
        double value;
        for (DataSeries dataSeries : this) {
            DataSeriesDataProvider dp = dataSeries.get(0);
            if(dp!=null){
                value = dp.getDataValue();
                if(value>0){
                    sum+=value;
                }
            }
        }
        return sum;
    }
    
    /**
     * Returns the number of the positive first elements of each series 
     * (first column if each series is considered to be a row) in this list.
     * This method can be useful for {@link ChartRenderer}s that use only a single 
     * value from each series.
     * 
     * @return the number of the positive first elements of each series in this list.
     */
    public int getPositiveCountOnFirstColumn(){
        int count = 0;
        for (DataSeries dataSeries : this) {
            DataSeriesDataProvider dp = dataSeries.get(0);
            if(dp!=null){
                if(dp.getDataValue()>=0){
                    count++;
                }
            }
        }
        return count;
    }
    
    /**
     * Returns the length of the longest series in this list.
     * @return the length of the longest series in this list
     */
    public int getMaxDataSeriesLength(){
        int max = 0;
        int size;
        for (DataSeries dataSeries : this) {
            size = dataSeries.size();
            if(dataSeries.size()>max){
                max = size;
            }
        }
        return max;
    }
    
    /**
     * Returns the length of the shortest series in this list.
     * @return the length of the shortest series in this list
     */
    public int getMinDataSeriesLength(){
        int min = 0;
        int size;
        for (DataSeries dataSeries : this) {
            size = dataSeries.size();
            if(dataSeries.size()<min){
                min = size;
            }
        }
        return min;
    }
    
    /**
     * Returns the longest series name in this list.
     * @return the longest series name in this list
     */
    public String getLongestName(){
        int max = 0;
        int length;
        String result = null;
        String name;
        for (DataSeries series : this) {
            name = series.getName();
            if( (length=name.length())>max){
                max = length;
                result = name;
            }
        }
        return result;
    }
    
}
