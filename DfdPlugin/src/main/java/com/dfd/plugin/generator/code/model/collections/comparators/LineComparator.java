package com.dfd.plugin.generator.code.model.collections.comparators;


import com.dfd.plugin.generator.code.model.blocks.Line;

import java.util.Comparator;

public class LineComparator implements Comparator<Line> {

    @Override
    public int compare(Line p1, Line p2) {
        if (p1.getSource() < p2.getSource()) {
            return -1;
        }
        else if (p1.getSource() > p2.getSource()) {
            return 1;
        }
        else {
            if (p1.getTarget() < p2.getTarget()) {
                return -1;
            } else if (p1.getTarget() > p2.getTarget()){
                return 1;
            }
        }
        return 0;
    }

}

