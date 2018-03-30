/*******************************************************************************
* Copyright 2017 Ivan Shubin http://galenframework.com
* 
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
* 
*   http://www.apache.org/licenses/LICENSE-2.0
* 
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
******************************************************************************/
package com.galenframework.rainbow4j;

import com.galenframework.rainbow4j.filters.ImageFilter;

import java.awt.*;
import java.util.LinkedList;
import java.util.List;

public class ComparisonOptions {
    private int tolerance;
    private boolean stretchToFit = false;

    private List<ImageFilter> originalFilters = new LinkedList<>();
    private List<ImageFilter> sampleFilters = new LinkedList<>();
    private List<ImageFilter> mapFilters = new LinkedList<>();
    private int analyzeOffset = 0;
    private List<Rectangle> ignoreRegions;

    public void setTolerance(int tolerance) {
        this.tolerance = tolerance;
    }

    public int getTolerance() {
        return tolerance;
    }

    public boolean isStretchToFit() {
        return stretchToFit;
    }

    public void setStretchToFit(boolean stretchToFit) {
        this.stretchToFit = stretchToFit;
    }



    public void addFilterBoth(ImageFilter filter) {
        addFilterOriginal(filter);
        addFilterSample(filter);
    }

    public void addFilterSample(ImageFilter filter) {
        if (sampleFilters == null) {
            sampleFilters = new LinkedList<>();
        }
        sampleFilters.add(filter);
    }

    public void addFilterOriginal(ImageFilter filter) {
        if (originalFilters == null) {
            originalFilters = new LinkedList<>();
        }
        originalFilters.add(filter);
    }

    public List<ImageFilter> getMapFilters() {
        return mapFilters;
    }

    public void setMapFilters(List<ImageFilter> mapFilters) {
        this.mapFilters = mapFilters;
    }

    public void addMapFilter(ImageFilter imageFilter) {
        if (mapFilters == null) {
            mapFilters = new LinkedList<>();
        }

        mapFilters.add(imageFilter);
    }

    public List<ImageFilter> getOriginalFilters() {
        return originalFilters;
    }

    public void setOriginalFilters(List<ImageFilter> originalFilters) {
        this.originalFilters = originalFilters;
    }

    public List<ImageFilter> getSampleFilters() {
        return sampleFilters;
    }

    public void setSampleFilters(List<ImageFilter> sampleFilters) {
        this.sampleFilters = sampleFilters;
    }

    public void setAnalyzeOffset(int analyzeOffset) {
        this.analyzeOffset = analyzeOffset;
    }

    public int getAnalyzeOffset() {
        return analyzeOffset;
    }

    public void setIgnoreRegions(List<Rectangle> ignoreRegions) {
        this.ignoreRegions = ignoreRegions;
    }

    public List<Rectangle> getIgnoreRegions() {
        return ignoreRegions;
    }
}
