package main;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import parsers.SkillParser;

public class Skill {

    private SkillParser mSkillParser;
    private String mSkillName;
    private HashMap<String, SkillAttribute<?>> mSkillAttributes;
    private String mParentPath;
//    private boolean isModifier;

    public Skill(File aSkill, String aParentPath) {
        if (aSkill == null)
            return;
        mParentPath = aParentPath;
        mSkillParser = new SkillParser(aSkill);
        mSkillName = aSkill.getName();
        mSkillName = mSkillName.substring(0, mSkillName.length() - 4);

        mSkillAttributes = mSkillParser.getAttributes();
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

    public Map<String, SkillAttribute<?>> getAttributes() {
        return mSkillAttributes;
    }

    public String getUrl() {
        return Control.URL + "/" + mParentPath + "/" + getName() + ".html";
    }

}
