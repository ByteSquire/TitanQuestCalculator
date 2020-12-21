package de.bytesquire.titanquest.tqcalculator.main;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class MinMaxAttribute {
    private Object mMin;
    private Object mMax;

    public Object getMin() {
        return mMin;
    }

    public void setMin(Object min) {
        this.mMin = min;
    }

    public Object getMax() {
        return mMax;
    }

    public void setMax(Object max) {
        this.mMax = max;
    }

    @Override
    public String toString() {
        StringBuilder ret = new StringBuilder();
        if (mMin != null)
            ret.append("min: " + mMin.toString());
        if (mMax != null)
            ret.append("\nmax: " + mMax.toString());
        return ret.toString();
    }
}
