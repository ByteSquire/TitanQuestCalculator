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

//        int i = 0;
        for (File skill : mSkillTreeParser.getSkills()) {
            if (!skill.getName().endsWith("Mastery.dbr"))
                mSkills.add(/* i++, */new Skill(skill, (mParentModName + "/" + mName), aMSParser));
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
