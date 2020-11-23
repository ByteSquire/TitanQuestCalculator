package main;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import parsers.ModStringsParser;
import parsers.SkillTreeParser;

public class Mastery {

    private SkillTreeParser mSkillTreeParser;
    private ArrayList<Skill> mSkills;
    private String mName;
    private String mParentModName;

    public Mastery(File aSkillTree, String aModDir, String aModName, ModStringsParser aMSParser) {
        mParentModName = aModName;
        mSkillTreeParser = new SkillTreeParser(aSkillTree, aModDir);
        mSkills = new ArrayList<Skill>();

        mName = aMSParser.getTags().get(mSkillTreeParser.getMasteryTag());

        for (File skill : mSkillTreeParser.getSkills()) {
            if (!skill.getName().endsWith("Mastery.dbr"))
                mSkills.add(new Skill(skill, (mParentModName + "/" + mName), aMSParser));
        }

        for (Skill skill : mSkills) {
            if (skill.isModifier())
                skill.setParent(mSkills.get(mSkills.indexOf(skill) - 1).getName());
        }
    }

    public List<Skill> getSkills() {
        return mSkills;
    }

    public String getName() {
        return mName;
    }

    public String getUrl() {
        return Control.URL + "/mods/" + mParentModName + "/" + getName() + ".html";
    }

}
