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
package com.galenframework.reports.model;

import java.util.LinkedList;
import java.util.List;

public class PageTest {

    public static class GlobalError {
        private Exception exception;
        private String screenshotPath;

        public GlobalError(Exception exception, String screenshotPath) {
            this.exception = exception;
            this.screenshotPath = screenshotPath;
        }

        public String getScreenshotPath() {
            return screenshotPath;
        }

        public Exception getException() {
            return exception;
        }
    }
    
    private String title = "";
    private List<LayoutReport> pageActions = new LinkedList<>();
    private List<GlobalError> globalErrors = new LinkedList<>();

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<GlobalError> getGlobalErrors() {
        return this.globalErrors;
    }

    public List<LayoutReport> getPageActions() {
        return pageActions;
    }

    public void setPageActions(List<LayoutReport> pageActions) {
        this.pageActions = pageActions;
    }
}
