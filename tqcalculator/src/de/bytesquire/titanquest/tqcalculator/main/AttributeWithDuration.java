package de.bytesquire.titanquest.tqcalculator.main;

public class AttributeWithDuration extends AttributeWithSecondValue {

    @Override
    public String getKey() {
        if (value0 == null || value1 == null)
            return null;
        if (value0 instanceof HasKey)
            return "${value0}% Chance of "
                    + ((HasKey) value0).getKey().replace("value1", "value2").replace("value0", "value1");
        return "${value0}" + key + " over ${value1} Second(s)";
    }

}
