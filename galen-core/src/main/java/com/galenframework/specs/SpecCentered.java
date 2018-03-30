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
package com.galenframework.specs;

public class SpecCentered extends SpecObjectWithErrorRate {

	public enum Alignment {
		ALL, VERTICALLY, HORIZONTALLY;
		
		public static Alignment fromString(String alignmentText) {
		    if ("vertically".equals(alignmentText)) {
                return VERTICALLY;
            }
		    else if ("horizontally".equals(alignmentText)) {
                return HORIZONTALLY;
            }
		    else if ("all".equals(alignmentText)) {
                return ALL;
            }
		    else throw new IllegalArgumentException("Can't read alignment: " + alignmentText);
		}
	}
	
	public enum Location {
		ON("on"), INSIDE("inside");

        private String name;
        private Location(String name) {
            this.name = name;
        }

		public static Location fromString(String locationText) {
            for (Location location: Location.values()) {
                if (location.name.equals(locationText)) {
                    return location;
                }
            }

			throw new IllegalArgumentException("Can't read location: " + locationText);
		}

        public static boolean isValid(String locationText) {
            for (Location location: Location.values()) {
                if (location.name.equals(locationText)) {
                    return true;
                }
            }
            return false;
        }
    }
	
	private Alignment alignment;
	private Location location;
	
	public SpecCentered() {
		
	}
	public SpecCentered(String object, Alignment alignment, Location location) {
		this.setObject(object);
		this.alignment = alignment;
		this.location = location;
	}
	
	public Alignment getAlignment() {
		return alignment;
	}
	public void setAlignment(Alignment alignment) {
		this.alignment = alignment;
	}
	public Location getLocation() {
		return location;
	}
	public void setLocation(Location location) {
		this.location = location;
	}
    public SpecCentered withErrorRate(int errorRate) {
        this.setErrorRate(errorRate);
        return this;
    }
}
