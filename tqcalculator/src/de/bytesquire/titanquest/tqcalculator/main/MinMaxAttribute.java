package de.bytesquire.titanquest.tqcalculator.main;

import java.util.ArrayList;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
@JsonPropertyOrder({ "min", "max"})
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

    public void addMin(Object min) {
        if (this.mMin == null)
            this.mMin = new ArrayList<Object>();
        ((ArrayList<Object>) this.mMin).add(min);
    }

    public void addMax(Object max) {
        if (this.mMax == null)
            this.mMax = new ArrayList<Object>();
        ((ArrayList<Object>) this.mMax).add(max);
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
