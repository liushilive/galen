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

import com.galenframework.parser.SyntaxException;
import com.galenframework.parser.Expectations;

public class StringCharReader {

    private String text;
    private int length;
    private int cursor = 0;
    

    public StringCharReader(String text) {
        this.text = text;
        this.length = text.length();
    }
    
    public void back() {
        cursor--;
        if (cursor < 0) {
            cursor = 0;
        }
    }

    public boolean hasMore() {
        return cursor < length;
    }

    public void moveToTheEnd() {
        cursor = length;
    }

    public char next() {
        if(cursor == length) {
            throw new IndexOutOfBoundsException();
        }
        char symbol = text.charAt(cursor);
        cursor++;
        return symbol;
    }

    public char currentSymbol() {
        if (cursor < length) {
            return text.charAt(cursor);
        }
        else return text.charAt(length - 1);
    }

    public String takeTheRest() {
        if (cursor < length) {
            String theRest = text.substring(cursor);
            cursor = length;
            return theRest;
        }
        else return "";
    }

    public String getTheRest() {
        if (cursor < length) {
            return text.substring(cursor);
        }
        else return ""; 
    }

    public char firstNonWhiteSpaceSymbol() {
        for (int i = cursor; i < length; i++) {
            char symbol = text.charAt(i);
            if (symbol != ' ' && symbol != '\t') {
                return symbol;
            }
        }
        return 0;
    }

    public String readUntilSymbol(char breakingSymbol) {
        return readUntilSymbol(breakingSymbol, true);
    }

    public String readSafeUntilSymbol(char breakingSymbol) {
        return readUntilSymbol(breakingSymbol, false);
    }

	private String readUntilSymbol(char breakingSymbol, boolean failIfSymbolNotFound) {
		StringBuilder builder = new StringBuilder();
		
		while(hasMore()) {
			char ch = next();
			if (ch == breakingSymbol) {
				return builder.toString();
			}
			else {
				builder.append(ch);
			}
		}

        if (failIfSymbolNotFound) {
            throw new SyntaxException("Missing symbol: " + breakingSymbol);
        }
		
		return builder.toString();
	}

    public boolean hasMoreNormalSymbols() {
        return firstNonWhiteSpaceSymbol() != 0;
    }

    public String readWord() {
        return Expectations.word().read(this);
    }

    public int currentCursorPosition() {
        return cursor;
    }

    public void moveCursorTo(int position) {
        cursor = position;
    }

    public void skipWord() {
        readWord();
    }
}
