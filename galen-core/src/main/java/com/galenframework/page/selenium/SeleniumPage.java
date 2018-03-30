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
package com.galenframework.page.selenium;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.galenframework.config.GalenConfig;
import com.galenframework.config.GalenProperty;
import com.galenframework.page.Rect;
import com.galenframework.page.AbsentPageElement;
import com.galenframework.page.Page;
import com.galenframework.page.PageElement;
import com.galenframework.specs.page.Locator;
import com.galenframework.rainbow4j.Rainbow4J;

import com.galenframework.utils.GalenUtils;
import org.openqa.selenium.*;

import static com.galenframework.page.selenium.ByChain.fromLocator;

public class SeleniumPage implements Page {

    private WebDriver driver;
    
    private Map<String, PageElement> cachedPageElements = new HashMap<>();
    
    private PageElement parentObject;

    private BufferedImage cachedScreenshotImage;
    private File cachedScreenshotFile;
    private int offsetLeft = 0;
    private int offsetTop = 0;

    private final SearchContext driverSearchContext;

    public SeleniumPage(WebDriver driver) {
        this(driver, driver);
    }

    private SeleniumPage(WebDriver driver, SearchContext driverSearchContext) {
        this.driver = driver;
        this.driverSearchContext = driverSearchContext;
    }

    private SeleniumPage(WebDriver driver, SearchContext searchContext, Locator objectContextLocator) {
        this.driver = driver;

        WebElement contextElement = findObjectContext(searchContext, objectContextLocator);
        this.driverSearchContext = contextElement;
        this.parentObject = new WebPageElement(driver, "parent", contextElement, objectContextLocator)
                .withOffset(offsetLeft, offsetTop);
    }

    
    private WebElement findObjectContext(SearchContext searchContext, Locator objectContextLocator) {
        if (objectContextLocator != null) {
            ByChain byChain = fromLocator(objectContextLocator);
            if (byChain == null) {
                throw new RuntimeException("Cannot convert locator " + objectContextLocator.prettyString());
            }
            
            return byChain.findElement(searchContext);
        }  else {
            throw new IllegalArgumentException("objectContextLocator cannot be null");
        }
    }


    @Override
    public PageElement getObject(Locator objectLocator) {
        return locatorToElement("unnamed", objectLocator);
    }

    @Override
    public PageElement getObject(String objectName, Locator objectLocator) {
        if (objectName != null) {
            PageElement pageElement = cachedPageElements.get(objectName);

            if (pageElement == null) {
                pageElement = getObject(objectLocator);
                cachedPageElements.put(objectName, pageElement);
                return pageElement;
            } else {
                return pageElement;
            }
        } else {
            return locatorToElement("unnamed", objectLocator);
        }

    }

    private List<WebElement> driverFindElements(ByChain byChain) {
        return byChain.findElements(driverSearchContext);
    }

    private WebElement driverFindElement(ByChain byChain) {
        return byChain.findElement(driverSearchContext);
    }

    private PageElement locatorToElement(String objectName, Locator objectLocator) {
        PageElement pageElement;
        ByChain byChain = fromLocator(objectLocator);

        try {
            WebElement webElement = driverFindElement(byChain);
            pageElement = new WebPageElement(driver, objectName, webElement, objectLocator).withOffset(offsetLeft, offsetTop);
        } catch (NoSuchElementException e) {
            pageElement = new AbsentPageElement();
        }
        return pageElement;
    }



    @Override
    public PageElement getSpecialObject(String objectName) {
        if ("screen".equals(objectName)) {
            return new ScreenElement(driver).withOffset(offsetLeft, offsetTop);
        }
        else if ("viewport".equals(objectName)) {
            return new ViewportElement(driver);
        }
        else if ("parent".equals(objectName) || "self".equals(objectName)) {
            if (parentObject != null) {
                return parentObject;
            }
            else throw new RuntimeException("There is no " + objectName + " object defined on page");
        }
        else return null;
    }


    @Override
    public int getObjectCount(Locator locator) {
        return driverFindElements(fromLocator(locator)).size();
    }

    @Override
    public Page createObjectContextPage(Locator objectContextLocator) {
        return new SeleniumPage(this.driver, this.driverSearchContext, objectContextLocator);
    }

    @Override
    public File getScreenshotFile() {
        if (this.cachedScreenshotFile == null) {
            cachedScreenshotFile = createNewScreenshot();
        }

        return this.cachedScreenshotFile;
    }

    private File createNewScreenshot() {
        try {
            if (GalenConfig.getConfig().getBooleanProperty(GalenProperty.SCREENSHOT_FULLPAGE)) {
                return GalenUtils.makeFullScreenshot(driver);
            }
            else return makeSimpleScreenshot();
        } catch (Exception e) {
            throw new RuntimeException("Error making screenshot", e);
        }
    }

    private File makeSimpleScreenshot() throws IOException {
        return GalenUtils.takeScreenshot(driver);
    }

    @Override
    public void setScreenshot(File screenshotFile) {
        this.cachedScreenshotFile = screenshotFile;
    }

    @Override
    public BufferedImage getScreenshotImage() {
        if (this.cachedScreenshotImage == null) {
            try {
                cachedScreenshotImage = Rainbow4J.loadImage(getScreenshotFile().getAbsolutePath());
            } catch (Exception e) {
                throw new RuntimeException("Couldn't take screenshot for page", e);
            }
        }
        return this.cachedScreenshotImage;
    }

    @Override
    public String getTitle() {
        return driver.getTitle();
    }

    @Override
    public void switchToFrame(PageElement mainObject) {
        WebPageElement webPageElement = (WebPageElement)mainObject;
        driver.switchTo().frame(webPageElement.getWebElement());
    }

    @Override
    public void switchToParentFrame() {
        driver.switchTo().parentFrame();
    }

    @Override
    public Page createFrameContext(PageElement frameElement) {
        SeleniumPage framePage = new SeleniumPage(driver);

        Rect mainObjectArea = frameElement.getArea();
        framePage.setOffset(mainObjectArea.getLeft(), mainObjectArea.getTop());
        framePage.switchToFrame(frameElement);
        framePage.setParentObject(frameElement);
        return framePage;
    }

    private void setOffset(int offsetLeft, int offsetTop) {
        this.offsetLeft = offsetLeft;
        this.offsetTop = offsetTop;
    }

    public PageElement getParentObject() {
        return parentObject;
    }

    public void setParentObject(PageElement parentObject) {
        this.parentObject = parentObject;
    }

    public WebDriver getDriver() {
        return driver;
    }
}
