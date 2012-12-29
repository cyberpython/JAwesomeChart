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
package com.gmigdos.jawesomechart.core.dataproviders;

import com.gmigdos.jawesomechart.core.DataSeriesDataProvider;
import com.gmigdos.jawesomechart.util.Observer;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Georgios Migdos <cyberpython@gmail.com>
 */
public class SimpleDataProvider implements DataSeriesDataProvider{
    
    private List<Observer> observers;
    private Double value;

    public SimpleDataProvider(Double d){
        this.value = d;
        this.observers = new ArrayList<Observer>();
    }

    @Override
    public Double getDataValue() {
        return value;
    }

    public Double getValue() {
        return value;
    }
    
    @Override
    public String toString(){
        if(value==null){
            return "";
        }else{
            return value.toString();
        }
    }

    @Override
    public String toString(DecimalFormat decimalFormat) {
        if(value==null){
            return "";
        }else{
            return String.format(decimalFormat.format(value.doubleValue()));
        }
    }

    public void setValue(Double value) {
        this.value = value;
        notifyObservers(this);
    }

    @Override
    public void addObserver(Observer o) {
        this.observers.add(o);
    }

    @Override
    public void deleteObserver(Observer o){
        this.observers.remove(o);
    }
    
    @Override
    public void clearObservers(){
        this.observers.clear();
    }
    
    @Override
    public void notifyObservers(Object o){
        for (Observer observer : observers) {
            observer.update(this, o);
        }
    }
    
}
