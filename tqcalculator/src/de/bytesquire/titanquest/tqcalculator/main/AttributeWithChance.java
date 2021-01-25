package de.bytesquire.titanquest.tqcalculator.main;

public class AttributeWithChance extends AttributeWithSecondValue {

    @Override
    public String getKey() {
        if (value0 == null || value1 == null)
            return null;
        if (value1 instanceof HasKey) {
            if (((HasKey) value1).getKey() == null)
                return null;
            return "${value0}% Chance of "
                    + ((HasKey) value1).getKey().replace("value1", "value2").replace("value0", "value1");
        }
        if (!key.contains("${value1}")) {
            if (!key.contains("${value}"))
                key = "${value1}" + key;
            else
                key = key.replace("value", "value1");
        }
        return "${value0}% Chance of " + key;
    }

}
