package de.bytesquire.titanquest.tqcalculator.main;

import java.util.ArrayList;

public class ChanceBasedAttributes {

    protected double value;
    protected String key;
    protected ArrayList<Object> values;

    public Double getValue() {
        return value;
    }

    public String getKey() {
        return key;
    }

    public ArrayList<Object> getValues() {
        return values;
    }

    public void setChance(double chance) {
        this.value = chance;
    }

    public void setValues(ArrayList<Object> values) {
        this.values = values;
    }

    public void setXOR(boolean xor) {
        if (xor)
            key = "{%.0f}% Chance for one of the following:";
        else
            key = "{%.0f}% Chance for all of the following:";
    }
}
