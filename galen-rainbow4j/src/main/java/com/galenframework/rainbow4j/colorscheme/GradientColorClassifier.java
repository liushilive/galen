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
package com.galenframework.rainbow4j.colorscheme;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.awt.*;
import java.util.List;
import java.util.stream.Collectors;

public class GradientColorClassifier implements ColorClassifier {
    private final Integer [][] colors;
    private String name;

    public GradientColorClassifier(String name, List<Color> colors) {
        this.name = name;
        this.colors = colors.stream().map(c ->
                new Integer[]{c.getRed(), c.getGreen(), c.getBlue()}
        ).collect(Collectors.toList()).toArray(new Integer[][]{});
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public boolean holdsColor(int r, int g, int b, int maxColorSquareDistance) {
        for (int i = 1; i < colors.length; i++) {
            if (holdsColorBetweenPoints(r, g, b, colors[i-1], colors[i], maxColorSquareDistance)) {
                return true;
            }
        }
        return false;
    }

    private boolean holdsColorBetweenPoints(int r, int g, int b, Integer[] g1, Integer[] g2, int maxDistance) {
        double Gr = g2[0] - g1[0];
        double Gg = g2[1] - g1[1];
        double Gb = g2[2] - g1[2];
        double errorRate = 16;

        double Gsquare = Gr*Gr + Gg*Gg + Gb*Gb;

        if (Gsquare > 0) {
            double Vr = r - g1[0];
            double Vg = g - g1[1];
            double Vb = b - g1[2];

            double K = (Vr*Gr + Vg*Gg + Vb*Gb);

            // calculating projection vector

            double Vpr = K*Gr/Gsquare;
            double Vpg = K*Gg/Gsquare;
            double Vpb = K*Gb/Gsquare;

            // checking whether projection will be between the points
            double Vpsquare = Vpr*Vpr + Vpg*Vpg + Vpb*Vpb;
            if (Vpsquare  - errorRate > Gsquare) {
                if (Math.sqrt(Vpsquare) - Math.sqrt(Gsquare) > errorRate) {
                    return false;
                }
            }

            double VpPlusGsquare = (Vpr + Gr)*(Vpr + Gr) + (Vpg + Gg)*(Vpg + Gg) + (Vpb + Gb)*(Vpb + Gb);
            if (VpPlusGsquare < Gsquare) {
                if (Vpsquare > errorRate) {
                    return false;
                }
            }

            // Checking if distance from point to projection is within allowed range
            double Pr = Vpr + g1[0];
            double Pg = Vpg + g1[1];
            double Pb = Vpb + g1[2];

            double Dr = r - Pr;
            double Dg = g - Pg;
            double Db = b - Pb;

            double D = Dr*Dr + Dg*Dg + Db*Db;
            if (D < maxDistance) {
                return true;
            }
        }
        return false;
    }


    @Override
    public int hashCode() {
        return new HashCodeBuilder()
                .append(colors)
                .append(name)
                .toHashCode();
    }


    @Override
    public boolean equals(Object obj) {
        if (obj == null)
            return false;
        if (obj == this)
            return true;
        if (!(obj instanceof GradientColorClassifier))
            return false;

        GradientColorClassifier rhs = (GradientColorClassifier)obj;
        return new EqualsBuilder()
                .append(rhs.colors, this.colors)
                .append(rhs.name, this.name)
                .isEquals();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("colors", colors)
                .append("name", name)
                .toString();
    }
}
