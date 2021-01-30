package de.bytesquire.titanquest.tqcalculator.main;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import de.bytesquire.titanquest.tqcalculator.parsers.AttributeNameParser;

@JsonInclude(Include.NON_NULL)
@JsonIgnoreProperties({ "key", "type", "object" })
@JsonPropertyOrder({ "chance", "value", "duration" })
public class SkillAttribute {

    private String mKey, mRace;
    private Object mChance, mValue, mDuration;
    private AttributeType mType;

    public SkillAttribute(String aRace, String aKey, AttributeType aType) {
        mRace = aRace;
        mType = aType;
        setKey(aKey);
    }

    public SkillAttribute(String aRace, String aKey, Object aValue, AttributeType aType) {
        mRace = aRace;
        mType = aType;
        setKey(aKey);
        switch (mType) {
        case CHANCE:
            setChance(aValue);
            break;
        case DURATION:
            setDuration(aValue);
            break;
        default:
            break;
        }
    }

    public SkillAttribute(String aRace, String aKey, Object aDamage) {
        mRace = aRace;
        mType = AttributeType.DEFAULT;
        setKey(aKey);
        setValue(aDamage);
    }

    public void setKey(String key) {
        if (key == null)
            return;
        if (AttributeNameParser.getMatch(key) != null)
            key = AttributeNameParser.getMatch(key);
        this.mKey = formatKey(key);
    }

    protected String formatKey(String key) {
        if (!key.contains("value")) {
            if (key.indexOf("{") > -1) {
                if (key.contains("{%+"))
                    key = key.substring(0, key.indexOf("{")) + "+${value}"
                            + key.substring(key.indexOf("}") + 1, key.length());
                else
                    key = key.substring(0, key.indexOf("{")) + "${value}"
                            + key.substring(key.indexOf("}") + 1, key.length());
            } else
                key = "${value}" + key;
        }
        if (key.contains("{%s1}")) {
            if (mRace != null)
                key = key.replace("{%s1}", mRace);
        }
        switch (mType) {
        case DURATION:
            if (!key.contains("duration"))
                key = key + " over ${duration} Second(s)";
            break;
        case CHANCE:
            if (!key.contains("chance"))
                key = "${chance}% Chance of " + key;
            break;
        case CHANCE_DURATION:
            if (!key.contains("chance")) {
                if (!key.contains("duration"))
                    key = "${chance}% Chance of " + key + " over ${duration} Second(s)";
                else
                    key = "${chance}% Chance of " + key;
            } else if (!key.contains("duration"))
                key = key + " over ${duration} Second(s)";
            break;
        case DEFAULT:
            break;
        }
        return key;
    }

    @Override
    public String toString() {
        if (getKey() != null) {
            switch (mType) {
            case DEFAULT:
                if (mValue == null)
                    return "";
                return getKey().replace("${value}", mValue.toString());
            case CHANCE_DURATION:
                if (mValue == null || mDuration == null || mChance == null)
                    return "";
                return getKey().replace("${chance}", mChance.toString()).replace("${value}", mValue.toString())
                        .replace("${duration}", mDuration.toString());
            case CHANCE:
                if (mValue == null || mChance == null)
                    return "";
                return getKey().replace("${chance}", mChance.toString()).replace("${value}", mValue.toString());
            case DURATION:
                if (mValue == null || mDuration == null)
                    return "";
                return getKey().replace("${value}", mValue.toString()).replace("${duration}", mDuration.toString());
            }
        }
        return "";
    }

    public String getKey() {
        if (isValid())
            return mKey;
        else
            return null;
    }

    private boolean isValid() {
        if (mValue == null)
            return false;
        switch (mType) {
        case CHANCE:
            if (mChance == null)
                return false;
            break;
        case DURATION:
            if (mDuration == null)
                return false;
            break;
        case CHANCE_DURATION:
            if (mChance == null || mDuration == null)
                return false;
            break;
        default:
            break;
        }
        return true;
    }

    public AttributeType getType() {
        return mType;
    }

    public void setType(AttributeType aType) {
        switch (mType) {
        case CHANCE:
            if (aType.equals(AttributeType.DURATION))
                mType = AttributeType.CHANCE_DURATION;
            break;
        case DURATION:
            if (aType.equals(AttributeType.CHANCE))
                mType = AttributeType.CHANCE_DURATION;
            break;
        case CHANCE_DURATION:
            break;
        default:
            this.mType = aType;
            break;
        }
        mKey = formatKey(mKey);
    }

    public Object getObject() {
        if (mType.equals(AttributeType.DEFAULT))
            return mValue;
        return this;
    }

    public Object getValue() {
        return mValue;
    }

    public void setValue(Object aValue) {
        mValue = aValue;
        setType(AttributeType.DEFAULT);
    }

    public Object getChance() {
        return mChance;
    }

    public void setChance(Object aChance) {
        this.mChance = aChance;
        setType(AttributeType.CHANCE);
    }

    public Object getDuration() {
        return mDuration;
    }

    public void setDuration(Object aDuration) {
        this.mDuration = aDuration;
        setType(AttributeType.DURATION);
    }
}
