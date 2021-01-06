package de.bytesquire.titanquest.tqcalculator.main;

import java.util.LinkedHashMap;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonIgnoreProperties({ "key" })
@JsonInclude(Include.NON_NULL)
public class ChanceBasedAttributes {

    protected Object chance;
    protected String key;
    protected LinkedHashMap<String, Object> values;

    public ChanceBasedAttributes() {
        this.values = new LinkedHashMap<String, Object>();
    }

    public Object getChance() {
        return chance;
    }

    public String getKey() {
        return key;
    }

    public LinkedHashMap<String, Object> getValues() {
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
            key = "${value}% Chance for one of the following:";
        else
            key = "${value}% Chance for all of the following:";
    }
}
