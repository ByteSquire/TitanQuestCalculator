package de.bytesquire.titanquest.tqcalculator.main;

public abstract class AttributeWithSecondValue implements HasKey {

    protected Object value0;
    protected Object value1;
    protected String key;

    public Object getValue0() {
        return value0;
    }

    public Object getValue1() {
        return value1;
    }

    @Override
    public abstract String getKey();

    public void setKey(String key) {
        this.key = key;
    }

    public void setValue1(Object value) {
        this.value1 = value;
    }

    public void setValue0(Object value) {
        this.value0 = value;
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
