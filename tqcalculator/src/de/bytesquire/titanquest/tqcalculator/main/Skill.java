package de.bytesquire.titanquest.tqcalculator.main;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import de.bytesquire.titanquest.tqcalculator.parsers.IconsParser;
import de.bytesquire.titanquest.tqcalculator.parsers.ModStringsParser;
import de.bytesquire.titanquest.tqcalculator.parsers.SkillParser;

@JsonIgnoreProperties({ "skill", "buff", "skillTag", "skillDescriptionTag", "modifier", "skillTier", "urlLegacy",
        "requiredWeapons" })
@JsonInclude(Include.NON_NULL)
public class Skill {

    private SkillParser mSkillParser;
    private String mSkillName;
    private String mSkillDescription;
    private HashMap<String, Object> mSkillAttributes;
    private String mParentPath;
    private boolean isModifier;
    private String[] mParent;
    private ArrayList<File> mSkill;
    private int mSkillTier;
    private SkillIcon mSkillIcon;
    private StringBuilder mRequiredWeapons;

    public Skill(File aSkill, String[] aParent, String aParentPath, ModStringsParser aMSParser,
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

        if (mSkillParser.getParentSkill() != null) {
            mParent = mSkillParser.getParentSkill();
        }
        isModifier = mSkillParser.isModifier();

        if (mSkillParser.getAdditionalFiles().size() > 0) {
            mSkill.addAll(mSkillParser.getAdditionalFiles());
        }

        mRequiredWeapons = new StringBuilder();
        for (String skillAttribute : mSkillParser.getAttributes().keySet()) {
            switch (skillAttribute) {
            case "skillTier":
                mSkillTier = (int) mSkillParser.getAttributes().get(skillAttribute);
                break;
            case "Sword":
                mRequiredWeapons.append(skillAttribute);
                mRequiredWeapons.append(", ");
                break;
            case "Axe":
                mRequiredWeapons.append(skillAttribute);
                mRequiredWeapons.append(", ");
                break;
            case "Bow":
                mRequiredWeapons.append(skillAttribute);
                mRequiredWeapons.append(", ");
                break;
            case "Spear":
                mRequiredWeapons.append(skillAttribute);
                mRequiredWeapons.append(", ");
                break;
            case "Mace":
                mRequiredWeapons.append(skillAttribute);
                mRequiredWeapons.append(", ");
                break;
            case "Staff":
                mRequiredWeapons.append(skillAttribute);
                mRequiredWeapons.append(", ");
                break;
            case "Magical":
                mRequiredWeapons.append(skillAttribute);
                mRequiredWeapons.append(", ");
                break;
            case "Shield":
                mRequiredWeapons.append(skillAttribute);
                mRequiredWeapons.append(", ");
                break;
            case "RangedOneHand":
                mRequiredWeapons.append("Thrown Weapon");
                mRequiredWeapons.append(", ");
                break;
            case "dualWieldOnly":
                mRequiredWeapons.append(skillAttribute);
                mRequiredWeapons.append(", ");
                break;
            case "meleeOnly":
                // mRequiredWeapons.append("Melee Only, ");
                break;
            default:
                if (skillAttribute.endsWith("Min")) {
                    String skillAttributeType = skillAttribute.replace("Min", "");
                    if (mSkillAttributes.containsKey(skillAttributeType)) {
                        if (mSkillAttributes.get(skillAttributeType) instanceof MinMaxAttribute)
                            ((MinMaxAttribute) mSkillAttributes.get(skillAttributeType))
                                    .setMin(mSkillParser.getAttributes().get(skillAttribute));
                    } else {
                        MinMaxAttribute tmp = new MinMaxAttribute();
                        tmp.setMin(mSkillParser.getAttributes().get(skillAttribute));
                        mSkillAttributes.put(skillAttributeType, tmp);
                    }
                    break;
                } else if (skillAttribute.endsWith("Max")) {
                    String skillAttributeType = skillAttribute.replace("Max", "");
                    if (mSkillAttributes.containsKey(skillAttributeType)) {
                        if (mSkillAttributes.get(skillAttributeType) instanceof MinMaxAttribute)
                            ((MinMaxAttribute) mSkillAttributes.get(skillAttributeType))
                                    .setMax(mSkillParser.getAttributes().get(skillAttribute));
                    } else {
                        MinMaxAttribute tmp = new MinMaxAttribute();
                        tmp.setMax(mSkillParser.getAttributes().get(skillAttribute));
                        mSkillAttributes.put(skillAttributeType, tmp);
                    }
                    break;
                }
                if (skillAttribute.endsWith("Modifier")) {
                    mSkillAttributes.put(skillAttribute.replace("Modifier", ""),
                            mSkillParser.getAttributes().get(skillAttribute));
                    break;
                }
                if (skillAttribute.endsWith("Modifier")) {
                    mSkillAttributes.put(skillAttribute.replace("Modifier", ""),
                            mSkillParser.getAttributes().get(skillAttribute));
                    break;
                }
                if(skillAttribute.startsWith("skill")) {
                    Object attr = mSkillParser.getAttributes().get(skillAttribute);
                    skillAttribute = skillAttribute.replace("skill", "");
                    mSkillAttributes.put(skillAttribute, attr);
                    break;
                }
                mSkillAttributes.put(skillAttribute, mSkillParser.getAttributes().get(skillAttribute));
                break;
            }
        }
        if (mRequiredWeapons.length() != 0) {
            mRequiredWeapons.delete(mRequiredWeapons.length() - 2, mRequiredWeapons.length());
            mSkillAttributes.put("requiredWeapons", mRequiredWeapons.toString());
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

    public String[] getParent() {
        return mParent;
    }

    public void setParent(String[] mSkillParent) {
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

    public String getRequiredWeapons() {
        if (mRequiredWeapons.length() == 0)
            return null;
        return mRequiredWeapons.toString();
    }

    public void setParent(String name) {
        if (mParent == null)
            mParent = new String[] { name };
    }

}
