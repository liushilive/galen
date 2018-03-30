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
package com.galenframework.reports.json;

import com.galenframework.reports.GalenTestInfo;
import com.galenframework.reports.TestReport;

/**
 * Created by ishubin on 2015/02/15.
 */
public class JsonTestReport {
    private final String testId;
    private String name;
    private TestReport report;

    public JsonTestReport(String testId, GalenTestInfo testInfo) {
        this.testId = testId;
        this.name = testInfo.getName();
        this.report = testInfo.getReport();
    }

    public String getName() {
        return name;
    }

    public TestReport getReport() {
        return report;
    }

    public String getTestId() {
        return testId;
    }
}
