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
    private File mSkillTree;
    private File mMastery;

    public Mastery(File aSkillTree, String aModDir, String aModName, ModStringsParser aMSParser) {
        mParentModName = aModName;
        mSkillTree = aSkillTree;
        mSkillTreeParser = new SkillTreeParser(aSkillTree, aModDir);
        mSkills = new ArrayList<Skill>();

        mName = aMSParser.getTags().get(mSkillTreeParser.getMasteryTag());

        for (File skill : mSkillTreeParser.getSkills()) {
            if (!(mSkillTreeParser.getSkills().indexOf(skill) == 0))
                mSkills.add(new Skill(skill, (mParentModName + "/" + mName), aMSParser));
            else
                mMastery = skill;
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

    public File getMastery() {
        return mMastery;
    }

    public File getSkillTree() {
        return mSkillTree;
    }

}
