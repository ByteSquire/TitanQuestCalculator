package de.bytesquire.titanquest.tqcalculator.main;

public class AttributeWithSecondValue implements HasKey{

    private Object value0;
    private Object value1;
    private String key;

    public Object getValue0() {
        return value0;
    }

    public Object getValue1() {
        return value1;
    }

    @Override
    public String getKey() {
        if (value0 == null || value1 == null)
            return null;
        return key;
    }

    public void setValue1(Object value) {
        this.value1 = value;
    }

    public void setValue0(Object value) {
        this.value0 = value;
    }

    public void setKey(String value) {
        this.key = value;
    }

    @Override
    public String toString() {
        try {
            return key.replace("${value0}", value0.toString()).replace("${value1}", value1.toString());
        } catch (NullPointerException e) {
            return "";
        }
    }
}
