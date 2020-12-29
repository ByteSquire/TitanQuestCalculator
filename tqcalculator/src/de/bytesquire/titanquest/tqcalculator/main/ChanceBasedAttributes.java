package de.bytesquire.titanquest.tqcalculator.main;

import java.util.HashMap;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties({ "key" })
public class ChanceBasedAttributes {

    protected Object chance;
    protected String key;
    protected HashMap<String, Object> values;

    public ChanceBasedAttributes() {
        this.values = new HashMap<String, Object>();
    }

    public Object getChance() {
        return chance;
    }

    public String getKey() {
        return key;
    }

    public HashMap<String, Object> getValues() {
        return values;
    }

    public void setChance(Object chance) {
        this.chance = chance;
    }

    public void addValue(String key, Object value) {
        this.values.put(key, value);
    }

    public void setXOR(boolean xor) {
        if (xor)
            key = "{%.0f}% Chance for one of the following:";
        else
            key = "{%.0f}% Chance for all of the following:";
    }
}
