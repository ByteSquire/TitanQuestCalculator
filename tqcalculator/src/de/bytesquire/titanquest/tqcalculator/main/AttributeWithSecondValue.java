package de.bytesquire.titanquest.tqcalculator.main;

public class AttributeWithSecondValue {

    private Object value0;
    private Object value1;
    private String key;

    public Object getValue0() {
        return value0;
    }

    public Object getValue1() {
        return value1;
    }
    
    public String getKey() {
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
}
