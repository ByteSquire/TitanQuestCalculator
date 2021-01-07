package de.bytesquire.titanquest.tqcalculator.main;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedHashMap;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import de.bytesquire.titanquest.tqcalculator.parsers.IconsParser;
import de.bytesquire.titanquest.tqcalculator.parsers.ModStringsParser;
import de.bytesquire.titanquest.tqcalculator.parsers.SkillTreeParser;

@JsonIgnoreProperties({ "mastery", "skillTree", "mSkills", "mSkillTreeParser", "urlLegacy" })
@JsonPropertyOrder({ "name", "masteryAttributes", "skillTiers" })
public class Mastery {

    private SkillTreeParser mSkillTreeParser;
    private ArrayList<Skill> mSkills;
    private ArrayList<ArrayList<Skill>> mSkillTiers;
    private LinkedHashMap<String, Object> mMasteryAttributes;
    private String mName;
    private String mParentModName;
    private File mSkillTree;
    private File mMastery;

    public Mastery(File aSkillTree, String aModDir, String aModName, ModStringsParser aMSParser,
            IconsParser aIconsParser) {
        mParentModName = aModName;
        mSkillTree = aSkillTree;
        mSkillTreeParser = new SkillTreeParser(aSkillTree, aModDir);
        mSkillTiers = new ArrayList<>();
        mSkills = new ArrayList<>();

        mName = aMSParser.getTags().get(mSkillTreeParser.getMasteryTag()).split(" Mastery")[0];

        for (File skill : mSkillTreeParser.getSkills()) {
            if (!(mSkillTreeParser.getSkills().indexOf(skill) == 0)) {
                Skill tmp = new Skill(skill, null, (mParentModName + "/Masteries/" + mName), aMSParser, aIconsParser);
                mSkills.add(tmp);
            } else {
                mMastery = skill;
                mMasteryAttributes = (LinkedHashMap<String, Object>) new Skill(skill, null,
                        (mParentModName + "/Masteries/" + mName), aMSParser, aIconsParser).getAttributes();
            }
        }

        for (Skill skill : mSkills) {
            if (skill.isModifier()) {
                if (skill.getParent() != null) {
                    ArrayList<String> validParents = new ArrayList<>();
                    for (String parent : skill.getParent()) {
                        boolean containsParent = false;
                        for (Skill masterySkill : mSkills) {
                            if (masterySkill.getName().equals(parent)) {
                                containsParent = true;
                            }
                        }
                        if (containsParent) {
                            validParents.add(parent);
                        }
                    }
                    skill.setParent(validParents);
                }
                if (skill.getParent() == null) {
                    int i = 1;
                    Skill tmp = mSkills.get(mSkills.indexOf(skill) - i++);
                    while (tmp.isModifier()) {
                        tmp = mSkills.get(mSkills.indexOf(skill) - i++);
                    }
                    skill.setParent(tmp.getName());
                }
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

    public String getUrlLegacy() {
        return Control.URL + "/mods/" + mParentModName + "/Masteries/" + getName() + ".html";
    }

    public File getMastery() {
        return mMastery;
    }

    public File getSkillTree() {
        return mSkillTree;
    }

    public LinkedHashMap<String, Object> getMasteryAttributes() {
        return mMasteryAttributes;
    }

}
