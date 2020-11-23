package main;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import parsers.ModStringsParser;
import parsers.SkillParser;

public class Skill {

    private SkillParser mSkillParser;
    private String mSkillName;
    private HashMap<String, SkillAttribute<?>> mSkillAttributes;
    private String mParentPath;
    private String mBuffIndex;
//    private boolean isModifier;

    public Skill(File aSkill, String aParentPath, ModStringsParser aMSParser) {
        if (aSkill == null)
            return;
        mParentPath = aParentPath;
        mSkillParser = new SkillParser(aSkill, aParentPath, aMSParser);

        mSkillName = aMSParser.getTags().get(mSkillParser.getSkillTag());

        mSkillAttributes = mSkillParser.getAttributes();
    }

    @Override
    public String toString() {
        if (mSkillName == null || mSkillAttributes == null)
            return "Skill slot not used";
        return mSkillName + ": " + mSkillAttributes.toString();
    }

    public String getName() {
        return /*mParentPath.split("/")[1] + "/" +*/ mSkillName;
    }

    public String getSkillTag() {
        return mSkillParser.getSkillTag();
    }

    public Map<String, SkillAttribute<?>> getAttributes() {
        return mSkillAttributes;
    }

    public boolean isBuff() {
        for (String key : mSkillAttributes.keySet()) {
            if (mSkillAttributes.get(key).isSkill()) {
                mBuffIndex = key;
                return true;
            }
        }
        return false;
    }

    public Skill getBuff() throws UnsupportedOperationException {
        if (mBuffIndex != null)
            return (Skill) mSkillAttributes.get(mBuffIndex).getValue();
        throw new UnsupportedOperationException();
    }

    public String getUrl() {
        return Control.URL + "/mods/" + mParentPath + "/" + getName() + ".html";
    }

}
