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

    @Override
    public String toString() {
        StringBuilder ret = new StringBuilder();
        if (chance == null)
            return "";
        ret.append(key.replace("${value}", chance.toString()));
        ret.append("\n");
        values.forEach((str, obj) -> {
            try {
                if (obj instanceof MinMaxAttribute || obj instanceof AttributeWithSecondValue) {
                    ret.append(obj.toString());
                    return;
                }
                if (str.contains("${value}"))
                    ret.append(str.replace("${value}", obj.toString()));
                else
                    ret.append(obj.toString()).append(str);
            } catch (NullPointerException e) {
            }
        });
        return ret.toString();
    }
}
