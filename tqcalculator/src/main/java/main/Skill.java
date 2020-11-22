package main;

import java.io.File;
import java.util.HashMap;

import parsers.SkillParser;

public class Skill {

    private SkillParser mSkillParser;
    private String mSkillName;
    private HashMap<String, SkillAttribute<?>> mSkillAttributes;
//    private boolean isModifier;

    public Skill(File aSkill) {
        if (aSkill == null)
            return;
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

}
