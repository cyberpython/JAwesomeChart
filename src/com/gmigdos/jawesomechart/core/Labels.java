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

import java.util.ArrayList;
import java.util.List;

/**
 * {@link List} of {@link String}s to be used as labels for the chart's x-axis.
 * @author Georgios Migdos <cyberpython@gmail.com>
 */
public class Labels extends ArrayList<String> {
    
    /**
     * Returns the longest String in this List.
     * @return 
     */
    public String getLongestItem(){
        int max = 0;
        int length = 0;
        String result = null;
        for (String item : this) {
            if( (length=item.length())>max){
                max = length;
                result = item;
            }
        }
        return result;
    }
    
    /**
     * Returns the element at the specified position in this list.
     * 
     * @param index index of the element to return
     * 
     * @return the element at the specified position in this list or null if the index is out of bounds
     */
    @Override
    public String get(int index) {
        try{
            return super.get(index);
        }catch(IndexOutOfBoundsException iobe){
            return null;
        }
    }
    
}
