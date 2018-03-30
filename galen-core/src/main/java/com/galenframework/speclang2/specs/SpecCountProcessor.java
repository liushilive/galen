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
package com.galenframework.speclang2.specs;

import com.galenframework.parser.ExpectRange;
import com.galenframework.parser.SyntaxException;
import com.galenframework.specs.Range;
import com.galenframework.specs.Spec;
import com.galenframework.specs.SpecCount;
import com.galenframework.parser.StringCharReader;

import static com.galenframework.parser.Expectations.*;

public class SpecCountProcessor implements SpecProcessor {
    @Override
    public Spec process(StringCharReader reader, String contextPath) {
        SpecCount.FetchType fetchType = SpecCount.FetchType.parse(word().read(reader));

        String pattern = null;
        if (reader.firstNonWhiteSpaceSymbol() == '\"') {
            pattern = doubleQuotedText().read(reader);
        } else {
            pattern = word().read(reader);
        }

        if (pattern == null || pattern.isEmpty()) {
            throw new SyntaxException("Pattern should not be empty");
        }

        expectNextWord("is", reader);

        ExpectRange rangeExpectation = new ExpectRange();
        rangeExpectation.setNoEndingWord();
        Range range = rangeExpectation.read(reader);

        return new SpecCount(fetchType, pattern, range);
    }
}
