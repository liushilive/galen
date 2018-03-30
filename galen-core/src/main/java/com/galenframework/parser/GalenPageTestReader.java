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
package com.galenframework.parser;

import static com.galenframework.utils.GalenUtils.isUrl;
import static com.galenframework.utils.GalenUtils.readSize;

import java.util.List;

import com.galenframework.browser.SeleniumGridBrowserFactory;
import com.galenframework.browser.JsBrowserFactory;
import com.galenframework.browser.SeleniumBrowserFactory;
import com.galenframework.specs.Place;
import com.galenframework.suite.GalenPageTest;
import com.galenframework.utils.GalenUtils;

import org.openqa.selenium.Platform;

public class GalenPageTestReader {

    public static GalenPageTest readFrom(String text, Place line) {
        
        String title = text.trim();
        
        if (text.contains("|")) {
            String[] values = text.split("\\|");
            title = values[0].trim();
            text = values[1];
        }
        
        String[] args = CommandLineParser.parseCommandLine(text);
        if (args.length == 0) {
            throw new SyntaxException(line, "Incorrect amount of arguments: " + text.trim());
        }
        
        
        if (isUrl(args[0])) {
            
            String size = null;
            if (args.length > 1) {
                size = args[1];
            }
            return defaultGalenPageTest(title, args[0], size);
        }
        else {
            String first = args[0].toLowerCase();
            if (first.equals("selenium")) {
                return seleniumGalenPageTest(title, args, text.trim(), line);
            }
            else if (first.equals("jsfactory")) {
                return jsBrowserFactory(title, args);
            }
            else   throw new SyntaxException(line, "Unknown browser factory: " + first);
        }
    }
    private static GalenPageTest jsBrowserFactory(String title, String[] args) {
        if (args.length < 2) {
            throw new SyntaxException("Missing script path");
        }
        return new GalenPageTest()
            .withBrowserFactory(new JsBrowserFactory(args[1], stripFirst(2, args)))
            .withTitle(title);
    }
    private static GalenPageTest seleniumGalenPageTest(String title, String[] args, String originalText, Place line) {
        if (args.length < 3) {
            throw new SyntaxException(line, "Incorrect amount of arguments: " + originalText);
        }
        String seleniumType = args[1].toLowerCase();
        if ("grid".equals(seleniumType)) {
            return gridGalenPageTest(stripFirst(2, args), originalText, line);
        }
        else {
            String size = null;
            if (args.length > 3) {
                size = args[3];
            }
            return seleniumSimpleGalenPageTest(title, seleniumType, args[2], size);
        }
    }
    private static String[] stripFirst(int number, String[] args) {
        if (number > 0 && number < args.length) {
             String[] newArgs = new String[args.length - number];
             for (int i=number; i < args.length; i++) {
                 newArgs[i-number] = args[i];
             }
             return newArgs;
        }
        else return args;
    }
    private static GalenPageTest gridGalenPageTest(String[] args, String originalText, Place place) {
        GalenCommand command = new GalenCommandLineParser().parse(args);
        List<String> leftovers = command.getLeftovers();
        
        if (leftovers.size() == 0) {
            throw new SyntaxException(place, "Cannot parse grid arguments: " + originalText);
        }

        String gridUrl = leftovers.get(0);
        
        String pageUrl = command.get("page");
        String size = command.get("size");
        
        SeleniumGridBrowserFactory browserFactory = new SeleniumGridBrowserFactory(gridUrl)
            .withBrowser(command.get("browser"))
            .withBrowserVersion(command.get("version"))
            .withPlatform(readPlatform(command.get("platform")));
        
        for (String parameter : command.getParameterNames()) {
            if (parameter.startsWith("dc.")) {
                String desiredCapaibility = parameter.substring(3);
                browserFactory.withDesiredCapability(desiredCapaibility, command.get(parameter));
            }
        }
        
        return new GalenPageTest()
            .withUrl(pageUrl)
            .withSize(readSize(size))
            .withBrowserFactory(browserFactory);
    }
    private static Platform readPlatform(String platformText) {
        if (platformText == null) {
            return null;
        }
        else return Platform.valueOf(platformText.toUpperCase());
    }
    private static GalenPageTest seleniumSimpleGalenPageTest(String title, String browser, String url, String screenSize) {
        if (url.equals("-")) {
            url = null;
        }
        return new GalenPageTest()
            .withTitle(title)
            .withUrl(url)
            .withSize(GalenUtils.readSize(screenSize))
            .withBrowserFactory(new SeleniumBrowserFactory(browser));
    }
    private static GalenPageTest defaultGalenPageTest(String title, String url, String sizeText) {
        
        if (url.equals("-")) {
            url = null;
        }
        
        return new GalenPageTest()
            .withTitle(title)
            .withUrl(url)
            .withSize(GalenUtils.readSize(sizeText))
            .withBrowserFactory(new SeleniumBrowserFactory());
    }
}
