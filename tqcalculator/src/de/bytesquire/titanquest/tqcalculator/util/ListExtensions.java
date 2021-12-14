package de.bytesquire.titanquest.tqcalculator.util;

import java.util.List;

public class ListExtensions {
    public static <T> void setElementInListAtIndex(T aElement, List<T> aList, int index) {
        if (aList.size() - 1 < index) {
            do {
                aList.add(null);
            } while (aList.size() - 1 < index);
        }
        aList.set(index, aElement);
    }
}
