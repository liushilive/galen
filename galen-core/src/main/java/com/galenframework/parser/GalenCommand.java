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

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class GalenCommand {
    
    public GalenCommand(List<String> leftovers, Map<String, String> parameters) {
        this.leftovers = leftovers;
        this.parameters = parameters;
    }
    private List<String> leftovers = new LinkedList<>();
    private Map<String, String> parameters = new HashMap<>();
    
    public List<String> getLeftovers() {
        return leftovers;
    }

    public String get(String parameter) {
        return parameters.get(parameter);
    }

    public Set<String> getParameterNames() {
        return parameters.keySet();
    }

}
