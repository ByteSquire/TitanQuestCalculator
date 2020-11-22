package main;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import parsers.SkillTreeParser;

public class Mastery {

    private SkillTreeParser mSkillTreeParser;
    private ArrayList<Skill> mSkills;
    private String name;
    private String mParentModName;

    public Mastery(File aSkillTree, String aModDir, String aModName) {
        mParentModName = aModName;
        mSkillTreeParser = new SkillTreeParser(aSkillTree, aModDir);
        mSkills = new ArrayList<Skill>();

//        int i = 0;
        for (File skill : mSkillTreeParser.getSkills()) {
            if (skill.getName().endsWith("Mastery.dbr"))
                name = skill.getName().substring(0, skill.getName().length() - 11);
            else
                mSkills.add(/* i++, */new Skill(skill, (mParentModName + "/" + name)));
        }
    }

    public List<Skill> getSkills() {
        return mSkills;
    }

    public String getName() {
        return name;
    }

    public String getUrl() {
        return Control.URL + "/" + mParentModName + "/" + getName() + ".html";
    }

}
