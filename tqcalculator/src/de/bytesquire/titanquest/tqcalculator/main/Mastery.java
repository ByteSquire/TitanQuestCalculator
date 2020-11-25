package de.bytesquire.titanquest.tqcalculator.main;

import java.io.File;
import java.util.ArrayList;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import de.bytesquire.titanquest.tqcalculator.parsers.ModStringsParser;
import de.bytesquire.titanquest.tqcalculator.parsers.SkillTreeParser;

@JsonIgnoreProperties({ "mastery", "skillTree", "mSkills", "mSkillTreeParser" })
public class Mastery {

    private SkillTreeParser mSkillTreeParser;
    private ArrayList<Skill> mSkills;
    private ArrayList<ArrayList<Skill>> mSkillTiers;
    private String mName;
    private String mParentModName;
    private File mSkillTree;
    private File mMastery;

    public Mastery(File aSkillTree, String aModDir, String aModName, ModStringsParser aMSParser) {
        mParentModName = aModName;
        mSkillTree = aSkillTree;
        mSkillTreeParser = new SkillTreeParser(aSkillTree, aModDir);
        mSkillTiers = new ArrayList<>();
        mSkills = new ArrayList<>();

        mName = aMSParser.getTags().get(mSkillTreeParser.getMasteryTag());

        for (File skill : mSkillTreeParser.getSkills()) {
            if (!(mSkillTreeParser.getSkills().indexOf(skill) == 0)) {
                Skill tmp = new Skill(skill, (mParentModName + "/" + mName), aMSParser);
                mSkills.add(tmp);
            } else
                mMastery = skill;
        }

        for (Skill skill : mSkills) {
            if (skill.isModifier()) {
                skill.setParent(mSkills.get(mSkills.indexOf(skill) - 1).getName());
            }
            while (true) {
                try {
                    mSkillTiers.get(skill.getSkillTier() - 1).add(skill);
                    break;
                } catch (IndexOutOfBoundsException e) {
                    mSkillTiers.add(new ArrayList<>());
                }
            }
        }
    }

    public ArrayList<ArrayList<Skill>> getSkillTiers() {
        return mSkillTiers;
    }

    public ArrayList<Skill> getSkillTier(int aSkillTier) {
        return mSkillTiers.get(aSkillTier);
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
