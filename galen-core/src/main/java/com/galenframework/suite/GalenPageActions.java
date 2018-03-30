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
package com.galenframework.suite;

import com.galenframework.suite.actions.*;
import com.galenframework.suite.actions.GalenPageActionCheck;
import com.galenframework.suite.actions.GalenPageActionCookie;
import com.galenframework.suite.actions.GalenPageActionInjectJavascript;
import com.galenframework.suite.actions.GalenPageActionOpen;
import com.galenframework.suite.actions.GalenPageActionResize;
import com.galenframework.suite.actions.GalenPageActionRunJavascript;

public class GalenPageActions {

    public static GalenPageActionInjectJavascript injectJavascript(String javascriptFilePath) {
        return new GalenPageActionInjectJavascript(javascriptFilePath);
    }

    public static GalenPageActionCheck check(String specFilePath) {
        return new GalenPageActionCheck().withSpec(specFilePath);
    }

    public static GalenPageActionRunJavascript runJavascript(String javascriptPath) {
        return new GalenPageActionRunJavascript(javascriptPath);
    }

    public static GalenPageActionOpen open(String url) {
        return new GalenPageActionOpen(url);
    }

    public static GalenPageAction resize(int width, int height) {
        return new GalenPageActionResize(width, height);
    }

    public static GalenPageAction cookie(String cookie) {
        return new GalenPageActionCookie().withCookies(cookie);
    }

}
