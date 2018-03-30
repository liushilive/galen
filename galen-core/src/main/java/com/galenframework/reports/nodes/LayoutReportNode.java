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
package com.galenframework.reports.nodes;

import com.fasterxml.jackson.annotation.JsonUnwrapped;
import com.galenframework.reports.model.*;
import com.galenframework.reports.TestStatistic;

import java.util.List;

public class LayoutReportNode extends TestReportNode {

    @JsonUnwrapped
    private LayoutReport layoutReport;

    public LayoutReportNode(FileTempStorage parentStorage, LayoutReport layoutReport, String name) {
        super(parentStorage);
        this.setLayoutReport(layoutReport);

        parentStorage.registerStorage(layoutReport.getFileStorage());
        setName(name);
    }


    @Override
    public String getType() {
        return "layout";
    }

    public LayoutReport getLayoutReport() {
        return layoutReport;
    }

    public void setLayoutReport(LayoutReport layoutReport) {
        this.layoutReport = layoutReport;
    }
    
    @Override
    public TestStatistic fetchStatistic(TestStatistic testStatistic) {
        if (layoutReport.getSections() != null) {
            fetchStatisticForSections(layoutReport.getSections(), testStatistic);
        }
        
        return testStatistic;
    }

    private void fetchStatisticForSections(List<LayoutSection> sections, TestStatistic testStatistic) {
        for (LayoutSection section : sections) {
            if (section.getSections() != null) {
                fetchStatisticForSections(section.getSections(), testStatistic);
            }


            if (section.getObjects() != null) {
                for (LayoutObject object: section.getObjects()) {
                    fetchStatisticForObject(object, testStatistic);
                }
            }
        }
    }

    private void fetchStatisticForObject(LayoutObject object, TestStatistic testStatistic) {
        if (object.getSpecs() != null) {
            fetchStatisticFromSpecs(testStatistic, object.getSpecs());
        }

        if (object.getSpecGroups() != null) {
            for (LayoutSpecGroup specGroup : object.getSpecGroups()) {
                if (specGroup.getSpecs() != null) {
                    fetchStatisticFromSpecs(testStatistic, specGroup.getSpecs());
                }
            }
        }
    }

    private void fetchStatisticFromSpecs(TestStatistic testStatistic, List<LayoutSpec> specs) {
        for (LayoutSpec spec : specs) {

            /*
             Checking if it was a component spec and if yes - than it will not take it into account
             but rather will go into its child spec list
              */
            if (spec.getSubLayout() != null && spec.getSubLayout().getSections() != null) {
               fetchStatisticForSections(spec.getSubLayout().getSections(), testStatistic);
            }
            else {
                testStatistic.setTotal(testStatistic.getTotal() + 1);

                if (spec.getStatus() == Status.WARN) {
                    testStatistic.setWarnings(testStatistic.getWarnings() + 1);
                } else if (spec.getStatus() == Status.ERROR) {
                    testStatistic.setErrors(testStatistic.getErrors() + 1);
                } else {
                    testStatistic.setPassed(testStatistic.getPassed() + 1);
                }
            }
        }
    }


}
