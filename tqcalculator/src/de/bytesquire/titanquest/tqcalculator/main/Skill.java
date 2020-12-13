package de.bytesquire.titanquest.tqcalculator.main;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import de.bytesquire.titanquest.tqcalculator.parsers.IconsParser;
import de.bytesquire.titanquest.tqcalculator.parsers.ModStringsParser;
import de.bytesquire.titanquest.tqcalculator.parsers.SkillParser;

@JsonIgnoreProperties({ "skill", "buff", "skillTag", "skillDescriptionTag", "modifier", "skillTier", "urlLegacy" })
public class Skill {

    private SkillParser mSkillParser;
    private String mSkillName;
    private String mSkillDescription;
    private HashMap<String, Object> mSkillAttributes;
    private String mParentPath;
    private boolean isModifier;
    private String mParent;
    private ArrayList<File> mSkill;
    private int mSkillTier;
    private SkillIcon mSkillIcon;

    public Skill(File aSkill, String aParent, String aParentPath, ModStringsParser aMSParser,
            IconsParser aIconsParser) {
        if (aSkill == null)
            return;
        mSkillAttributes = new HashMap<>();
        mParentPath = aParentPath;
        mParent = aParent;
        mSkill = new ArrayList<>();
        mSkill.add(aSkill);
        mSkillParser = new SkillParser(aSkill, aParentPath, aMSParser, aIconsParser);

        mSkillName = aMSParser.getTags().get(mSkillParser.getSkillTag());
        mSkillDescription = aMSParser.getTags().get(mSkillParser.getSkillDescriptionTag());
        mSkillIcon = mSkillParser.getSkillIcon();
        isModifier = mSkillParser.isModifier();

        if (mSkillParser.getAdditionalFiles().size() > 0) {
            mSkill.addAll(mSkillParser.getAdditionalFiles());
        }

        for (String skillAttribute : mSkillParser.getAttributes().keySet()) {
            if (skillAttribute.equals("skillTier"))
                mSkillTier = (int) mSkillParser.getAttributes().get(skillAttribute);
            else
                mSkillAttributes.put(skillAttribute, mSkillParser.getAttributes().get(skillAttribute));
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

    public Map<String, Object> getAttributes() {
        return mSkillAttributes;
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

    public ArrayList<File> getSkill() {
        return mSkill;
    }

    public int getSkillTier() {
        return mSkillTier;
    }

    public SkillIcon getSkillIcon() {
        if (mSkillIcon != null)
            return mSkillIcon;
        else
            return null;
    }

}
