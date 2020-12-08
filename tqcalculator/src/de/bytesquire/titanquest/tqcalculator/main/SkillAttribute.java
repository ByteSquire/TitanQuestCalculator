package de.bytesquire.titanquest.tqcalculator.main;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties({ "skill" })
public class SkillAttribute<T> {

    private T mValue;

    public SkillAttribute(T aValue) {
        this.mValue = aValue;
    }

    @Override
    public String toString() {
        return mValue.toString();
    }

    public boolean isSkill() {
        return (mValue instanceof Skill);
    }

    public T getValue() {
        return mValue;
    }
}
