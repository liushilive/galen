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
package com.galenframework.components;

import java.awt.Dimension;
import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

import com.galenframework.browser.Browser;
import com.google.common.io.Files;

import com.galenframework.browser.Browser;
import com.galenframework.components.validation.MockedPage;
import com.galenframework.page.Page;

public class MockedBrowser implements Browser {

    
    private List<String> recordedActions = new LinkedList<>();
    
    private String url;
    private Dimension screenSize;
    private Page mockedPage;

    public MockedBrowser(String url, Dimension screenSize, MockedPage mockedPage) {
        this.url = url;
        this.screenSize = screenSize;
        this.mockedPage = mockedPage;
    }

    @Override
    public void quit() {
    }

    @Override
    public void changeWindowSize(Dimension screenSize) {
    }

    @Override
    public void load(String url) {
    }

    @Override
    public Object executeJavascript(String javascript) {
        recordedActions.add("executeJavascript\n" + javascript);
        return null;
    }

    @Override
    public Page getPage() {
        return getMockedPage();
    }

    @Override
    public String getUrl() {
        return this.url;
    }

    @Override
    public Dimension getScreenSize() {
        return this.screenSize;
    }

    public Page getMockedPage() {
        return mockedPage;
    }

    public void setMockedPage(Page mockedPage) {
        this.mockedPage = mockedPage;
    }

    public List<String> getRecordedActions() {
        return recordedActions;
    }

    public void setRecordedActions(List<String> recordedActions) {
        this.recordedActions = recordedActions;
    }

    @Override
    public void refresh() {
        recordedActions.add("refresh");
    }

}
