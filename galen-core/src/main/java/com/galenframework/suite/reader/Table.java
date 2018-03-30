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


import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.galenframework.parser.SyntaxException;
import com.galenframework.specs.Place;

public class Table {

    private List<String> headers;
    private List<List<String>> rows = new LinkedList<>();

    public Table() {
    }

    public void addRow(List<String> row, Place place) {
        if (headers == null) {
            headers = row;
        }
        else {
            if (row.size() != headers.size()) {
                throw new SyntaxException(place, "Amount of cells in a row is not the same in header");
            }
            rows.add(row);
        }
        
    }

    public void mergeWith(Table table) {
        if (table.headers != null && table.rows.size() > 0) {
            if (table.headers.size() != headers.size()) {
                throw new SyntaxException("Cannot merge tables. Amount of columns should be same");
            }
            else {
                for (List<String> row : table.rows) {
                    rows.add(row);
                }
            }
        }
    }

    public void forEach(RowVisitor visitor) {
        for (List<String> row : rows) {
            int index = -1;
            Map<String, String> values = new HashMap<>();
            for (String cell : row) {
                index++;
                values.put(headers.get(index), cell);
            }
            
            visitor.visit(values);
        }
    }
    
    

}
