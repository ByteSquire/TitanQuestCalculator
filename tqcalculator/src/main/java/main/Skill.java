package main;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import parsers.ModStringsParser;
import parsers.SkillParser;

@JsonIgnoreProperties({ "skill" })
public class Skill {

    private SkillParser mSkillParser;
    private String mSkillName;
    private String mSkillDescription;
    private HashMap<String, SkillAttribute<?>> mSkillAttributes;
    private String mParentPath;
    private String mBuffIndex;
    private boolean isModifier;
    private String mParent;
    private File mSkill;

    public Skill(File aSkill, String aParentPath, ModStringsParser aMSParser) {
        if (aSkill == null)
            return;
        mParentPath = aParentPath;
        mSkill = aSkill;
        mSkillParser = new SkillParser(aSkill, aParentPath, aMSParser);

        mSkillName = aMSParser.getTags().get(mSkillParser.getSkillTag());
        mSkillAttributes = mSkillParser.getAttributes();
        isModifier = mSkillParser.isModifier();

        for (String key : mSkillAttributes.keySet()) {
            if (mSkillAttributes.get(key).isSkill()) {
                mBuffIndex = key;
            }
        }
    }

    @Override
    public String toString() {
        if (mSkillName == null || mSkillAttributes == null)
            return "Skill slot not used";
        return mSkillName + ": " + mSkillAttributes.toString();
    }

    public String getName() {
        return mSkillName;
    }

    public String getSkillTag() {
        return mSkillParser.getSkillTag();
    }

    public Map<String, SkillAttribute<?>> getAttributes() {
        return mSkillAttributes;
    }

    public boolean isBuff() {
        return mBuffIndex != null;
    }

    public Skill getBuff() throws UnsupportedOperationException {
        if (mBuffIndex != null)
            return (Skill) mSkillAttributes.get(mBuffIndex).getValue();
        else
            return null;
//        throw new UnsupportedOperationException();
    }

    public String getUrl() {
        return Control.URL + "/mods/" + mParentPath + "/" + getName() + ".html";
    }

    public String getSkillDescription() {
        return mSkillDescription;
    }

    public boolean isModifier() {
        return isModifier;
    }

    public String getParent() {
        return mParent;
    }

    public void setParent(String mSkillParent) {
        this.mParent = mSkillParent;
    }

    public File getSkill() {
        return mSkill;
    }

}
