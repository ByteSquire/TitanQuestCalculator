package de.bytesquire.titanquest.tqcalculator.main;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import de.bytesquire.titanquest.tqcalculator.parsers.IconsParser;
import de.bytesquire.titanquest.tqcalculator.parsers.ModStringsParser;
import de.bytesquire.titanquest.tqcalculator.parsers.SkillParser;

@JsonIgnoreProperties({ "skill", "buff", "skillTag", "skillDescriptionTag", "modifier", "skillTier" })
public class Skill {

    private SkillParser mSkillParser;
    private String mSkillName;
    private String mSkillDescription;
    private HashMap<String, SkillAttribute<?>> mSkillAttributes;
    private String mParentPath;
    private String mBuffIndex;
    private boolean isModifier;
    private String mParent;
    private File mSkill;
    private int mSkillTier;
    private SkillIcon mSkillIcon;

    public Skill(File aSkill, String aParent, String aParentPath, ModStringsParser aMSParser,
            IconsParser aIconsParser) {
        if (aSkill == null)
            return;
        mSkillAttributes = new HashMap<>();
        mParentPath = aParentPath;
        mParent = aParent;
        mSkill = aSkill;
        mSkillParser = new SkillParser(aSkill, aParentPath, aMSParser, aIconsParser);

        mSkillName = aMSParser.getTags().get(mSkillParser.getSkillTag());
        mSkillDescription = aMSParser.getTags().get(mSkillParser.getSkillDescriptionTag());
        mSkillIcon = mSkillParser.getSkillIcon();
        isModifier = mSkillParser.isModifier();

        for (String skillAttribute : mSkillParser.getAttributes().keySet()) {
            if (skillAttribute.equals("skillTier") || skillAttribute.equals("skillMasteryLevelRequired"))
                mSkillTier = (int) mSkillParser.getAttributes().get(skillAttribute).getValue();
            else
                mSkillAttributes.put(skillAttribute, mSkillParser.getAttributes().get(skillAttribute));
        }

        for (String key : mSkillAttributes.keySet()) {
            if (mSkillAttributes.get(key).isSkill()) {
                mBuffIndex = key;
            }
        }
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

    public String getSkillTag() {
        return mSkillParser.getSkillTag();
    }

    public String getSkillDescriptionTag() {
        return mSkillParser.getSkillDescriptionTag();
    }

    public Map<String, SkillAttribute<?>> getAttributes() {
        return mSkillAttributes;
    }

    public boolean isBuff() {
        return mBuffIndex != null;
    }

    public Skill getBuff() throws UnsupportedOperationException {
        if (mBuffIndex != null)
            return (Skill) mSkillAttributes.get(mBuffIndex).getValue();
        else
            return null;
//        throw new UnsupportedOperationException();
    }

    public String getUrlLegacy() {
        return Control.URL + "/mods/" + mParentPath + "/" + getName() + ".html";
    }

    public String getDescription() {
        return mSkillDescription;
    }

    public boolean isModifier() {
        return isModifier;
    }

    public String getParent() {
        return mParent;
    }

    public void setParent(String mSkillParent) {
        this.mParent = mSkillParent;
    }

    public File getSkill() {
        return mSkill;
    }

    public int getSkillTier() {
        if (mSkillTier == 0)
            return getBuff().getSkillTier();
        else
            return mSkillTier;
    }

    public SkillIcon getSkillIcon() {
        return mSkillIcon;
    }

}
