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
package com.galenframework.suite.reader;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;

import com.galenframework.parser.SyntaxException;
import com.galenframework.specs.Place;
import com.galenframework.utils.GalenUtils;
import com.galenframework.parser.VarsContext;
import com.galenframework.tests.GalenBasicTest;

public class GalenSuiteLineProcessor {

    private RootNode rootNode = new RootNode();
    private Node<?> currentNode = rootNode;
    private boolean disableNextSuite = false;
    private String contextPath;
    private Properties properties;
    private List<String> groupsForNextTest;

    public GalenSuiteLineProcessor(Properties properties, String contextPath) {
        this.contextPath = contextPath;
        this.properties = properties;
    }

    public void processLine(String text, Place place) throws IOException {
        if (!isBlank(text) && !isCommented(text) && !isSeparator(text)) {
            if (text.startsWith("@@")) {
                Node<?> node = processSpecialInstruction(text.substring(2).trim(), place);
                if (node != null) {
                    currentNode = node;
                }
            }
            else {
                int spaces = calculateIndentationSpaces(text);
                
                Node<?> processingNode = currentNode.findProcessingNodeByIndentation(spaces);
                Node<?> newNode = processingNode.processNewNode(text, place);
                
                if (newNode instanceof TestNode) {
                    if (disableNextSuite) {
                        disableNextSuite = false; 
                        ((TestNode)newNode).setDisabled(true);
                    }

                    if (groupsForNextTest != null) {
                        ((TestNode)newNode).setGroups(groupsForNextTest);
                        groupsForNextTest = null;
                    }
                }
                
                currentNode = newNode;
            }
        }
    }
    
    private Node<?> processSpecialInstruction(String text, Place place) throws IOException {
        currentNode = rootNode;
        int indexOfFirstSpace = text.indexOf(' ');
        
        String firstWord;
        String leftover;
        if (indexOfFirstSpace > 0) {
            firstWord = text.substring(0, indexOfFirstSpace).toLowerCase();
            leftover = text.substring(indexOfFirstSpace).trim();
        }
        else {
            firstWord = text.toLowerCase();
            leftover = "";
        }
        
        if (firstWord.equals("set")) {
            return processInstructionSet(leftover, place);
        }
        else if (firstWord.equals("table")){
            return processTable(leftover, place);
        }
        else if (firstWord.equals("parameterized")){
            return processParameterized(leftover, place);
        }
        else if (firstWord.equals("disabled")) {
            markNextSuiteAsDisabled();
            return null;
        }
        else if (firstWord.equals("groups")) {
            markNextSuiteGroupedWith(leftover);
            return null;
        }
        else if (firstWord.equals("import")) {
            List<Node<?>> nodes = importSuite(leftover, place);
            rootNode.getChildNodes().addAll(nodes);
            return null;
        }
        else throw new SuiteReaderException("Unknown instruction: " + firstWord);
    }

    private List<Node<?>> importSuite(String path, Place place) throws IOException {
        if (path.isEmpty()) {
            throw new SyntaxException(place, "No path specified for importing");
        }
        
        String fullChildPath = contextPath + File.separator + path;
        String childContextPath = new File(fullChildPath).getParent();
        GalenSuiteLineProcessor childProcessor = new GalenSuiteLineProcessor(properties, childContextPath);
        
        File file = new File(fullChildPath);
        if (!file.exists()) {
            throw new SyntaxException(place, "File doesn't exist: " + file.getAbsolutePath());
        }
        childProcessor.readLines(new FileInputStream(file), fullChildPath);
        return childProcessor.rootNode.getChildNodes();
    }

    private void markNextSuiteGroupedWith(String commaSeparatedGroups) {

        String[] groupsArray = commaSeparatedGroups.split(",");

        List<String> groups = new LinkedList<>();
        for (String group : groupsArray) {
            String trimmedGroup = group.trim();
            if (!trimmedGroup.isEmpty()) {
                groups.add(trimmedGroup);
            }
        }

        this.groupsForNextTest = groups;
    }

    private void markNextSuiteAsDisabled() {
        this.disableNextSuite = true;
    }

    private Node<?> processParameterized(String text, Place place) {
        ParameterizedNode parameterizedNode = new ParameterizedNode(text, place);
        
        if (disableNextSuite) {
            parameterizedNode.setDisabled(true);
            disableNextSuite = false;
        }

        if (groupsForNextTest != null) {
            parameterizedNode.setGroups(groupsForNextTest);
            groupsForNextTest = null;
        }
        
        currentNode.add(parameterizedNode);
        return parameterizedNode;
    }

    private Node<?> processTable(String text, Place place) {
        TableNode tableNode = new TableNode(text, place);
        currentNode.add(tableNode);
        return tableNode;
    }

    private Node<?> processInstructionSet(String text, Place place) {
        SetNode newNode = new SetNode(text, place);
        currentNode.add(newNode);
        return newNode;
    }

    public List<GalenBasicTest> buildSuites() {
        return rootNode.build(new VarsContext(properties));
    }

    public static int calculateIndentationSpaces(String text) {
        int spacesCount = 0;
        for (int i=0; i< text.length(); i++) {
            if (text.charAt(i) == ' ') {
                spacesCount++;
            } else if (text.charAt(i) == '\t') {
                spacesCount += 4;
            }
            else {
                return spacesCount;
            }
        }
        return 0;
    }
    
    private boolean isSeparator(String text) {
        text = text.trim();
        if (text.length() > 3) {
            char ch = text.charAt(0);
            for (int i = 1; i < text.length(); i++) {
                if (ch != text.charAt(i)) {
                    return false;
                }
            }
            return true;
        }
        else return false;
    }

    private boolean isBlank(String text) {
        return text.trim().isEmpty();
    }

    private boolean isCommented(String text) {
        return text.trim().startsWith("#");
    }

    public void readLines(InputStream inputStream, String sourceName) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
        
        String lineText = bufferedReader.readLine();
        int lineNumber = 0;
        while(lineText != null){
            lineNumber++;

            lineText = GalenUtils.removeNonPrintableControlSymbols(lineText);
            processLine(lineText, new Place(sourceName, lineNumber));
            lineText = bufferedReader.readLine();
        }
    }

}
