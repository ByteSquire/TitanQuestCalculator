package main;

import java.io.File;
import java.util.ArrayList;

import parsers.SkillTreeParser;

public class SkillTree {

    private SkillTreeParser mSkillTreeParser;
    private ArrayList<Skill> mSkills;

    public SkillTree(File aSkillTree, String mModDir) {
        mSkillTreeParser = new SkillTreeParser(aSkillTree, mModDir);
        mSkills = new ArrayList<Skill>();

//        int i = 0;
        for (File skill : mSkillTreeParser.getSkills()) {
            mSkills.add(/*i++, */new Skill(skill));
        }
    }

    public ArrayList<Skill> getSkills() {
        return mSkills;
    }

}
