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
package com.galenframework.tests.browser;

import java.awt.Dimension;

import com.galenframework.components.MockedBrowser;
import com.galenframework.browser.JsBrowserFactory;
import com.galenframework.browser.SeleniumBrowser;
import com.galenframework.components.DummyDriver;
import com.galenframework.components.MockedBrowser;

import org.junit.Assert;
import org.testng.annotations.Test;

public class JsBrowserFactoryTest {

    @Test
    public void shouldProvide_webDriver_withJsFactory() {
        JsBrowserFactory browserFactory = new JsBrowserFactory(getClass().getResource("/browser/js-driver-init.js").getFile(), new String[]{"http://example.com", "640x480"});
        
        SeleniumBrowser browser = (SeleniumBrowser)browserFactory.openBrowser();
        
        DummyDriver driver = (DummyDriver)browser.getDriver();
        
        Assert.assertEquals("http://example.com", driver.getCurrentUrl());
    }
    
    @Test
    public void shouldProvide_browser_withJsFactory() {
        JsBrowserFactory browserFactory = new JsBrowserFactory(getClass().getResource("/browser/js-browser-init.js").getFile(), new String[]{"http://example2.com"});
        
        MockedBrowser browser = (MockedBrowser)browserFactory.openBrowser();
        Assert.assertEquals("http://example2.com", browser.getUrl());
        Assert.assertEquals(new Dimension(320, 240), browser.getScreenSize());
    }
}
