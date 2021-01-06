package de.bytesquire.titanquest.tqcalculator.main;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import de.bytesquire.titanquest.tqcalculator.parsers.AttributeNameParser;
import de.bytesquire.titanquest.tqcalculator.parsers.IconsParser;
import de.bytesquire.titanquest.tqcalculator.parsers.ModStringsParser;
import de.bytesquire.titanquest.tqcalculator.parsers.PetParser;
import de.bytesquire.titanquest.tqcalculator.parsers.SkillParser;

@JsonIgnoreProperties({ "files", "buff", "skillTag", "skillDescriptionTag", "modifier", "skillTier", "urlLegacy",
        "requiredWeapons", "race" })
@JsonInclude(Include.NON_NULL)
@JsonPropertyOrder({ "name", "description", "parent", "skillIcon", "attributes" })
public class Skill {

    private SkillParser mSkillParser;
    private String mSkillName;
    private String mSkillDescription;
    private LinkedHashMap<String, Object> mSkillAttributes;
    private PetParser mPet;
    private String mParentPath;
    private boolean isModifier;
    private String[] mParent;
    private ArrayList<File> mFiles;
    private int mSkillTier;
    private SkillIcon mSkillIcon;
    private StringBuilder mRequiredWeapons;
    private String mRace;
    private ChanceBasedAttributes mCBA;
    private ArrayList<String> mCBANames;
    private boolean mCBAXOR;

    public Skill(File aSkill, String[] aParent, String aParentPath, ModStringsParser aMSParser,
            IconsParser aIconsParser) {
        if (aSkill == null)
            return;
        mSkillAttributes = new LinkedHashMap<>();
        mParentPath = aParentPath;
        mParent = aParent;
        mFiles = new ArrayList<>();
        mFiles.add(aSkill);
        mSkillParser = new SkillParser(aSkill, aParentPath, aMSParser, aIconsParser);

        String name;
        if ((name = aMSParser.getTags().get(mSkillParser.getSkillTag())) != null)
            mSkillName = name;
        else
            mSkillName = mSkillParser.getSkillTag();
        mSkillDescription = aMSParser.getTags().get(mSkillParser.getSkillDescriptionTag());
        mSkillIcon = mSkillParser.getSkillIcon();
        mRace = mSkillParser.getRace();

        if (mSkillParser.getParentSkill() != null) {
            mParent = mSkillParser.getParentSkill();
        }
        isModifier = mSkillParser.isModifier();

        if (mSkillParser.getAdditionalFiles().size() > 0) {
            mFiles.addAll(mSkillParser.getAdditionalFiles());
        }

        mCBA = new ChanceBasedAttributes();
        mCBANames = new ArrayList<String>();
        mCBAXOR = false;

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
            case "Pet":
                mPet = (PetParser) mSkillParser.getAttributes().get(skillAttribute);
                break;
            default:
                if (skillAttribute.equals("DamageGlobalChance")) {
                    mCBA.setChance(mSkillParser.getAttributes().get(skillAttribute));
                    break;
                }
                if (skillAttribute.endsWith("Global")) {
                    String skillAttributeType = skillAttribute.replace("Global", "");
                    String skillAttributeTypeModifier = skillAttributeType + "Modifier";
                    if (AttributeNameParser.getMatch(skillAttributeType) != null)
                        skillAttributeType = AttributeNameParser.getMatch(skillAttributeType);
                    else if (AttributeNameParser.getMatch(skillAttributeType) != null)
                        skillAttributeType = AttributeNameParser.getMatch(skillAttributeType);

                    skillAttributeType = formatKey(skillAttributeType);
                    mCBANames.add(skillAttributeType);
                    if (AttributeNameParser.getMatch(skillAttributeTypeModifier) != null) {
                        skillAttributeTypeModifier = AttributeNameParser.getMatch(skillAttributeTypeModifier);
                        skillAttributeTypeModifier = formatKey(skillAttributeTypeModifier);
                        mCBANames.add(skillAttributeTypeModifier);
                    }
                    break;
                }
                if (skillAttribute.endsWith("XOR")) {
                    mCBAXOR = true;
                    break;
                }
                if (skillAttribute.endsWith("Min")) {
                    String skillAttributeType = skillAttribute.replace("Min", "");
                    if (AttributeNameParser.getMatch(skillAttributeType) != null)
                        skillAttributeType = AttributeNameParser.getMatch(skillAttributeType);
                    if (mSkillAttributes.containsKey(skillAttributeType)) {
                        if (mSkillAttributes.get(skillAttributeType) instanceof MinMaxAttribute)
                            ((MinMaxAttribute) mSkillAttributes.get(skillAttributeType))
                                    .setMin(mSkillParser.getAttributes().get(skillAttribute));
                        else {
                            MinMaxAttribute tmp = new MinMaxAttribute();
                            tmp.setMin(mSkillParser.getAttributes().get(skillAttribute));
                            putAttributeWithSecondValue(skillAttributeType, tmp);
                        }
                    } else {
                        MinMaxAttribute tmp = new MinMaxAttribute();
                        tmp.setMin(mSkillParser.getAttributes().get(skillAttribute));
                        putAttribute(skillAttributeType, tmp);
                    }
                    break;
                } else if (skillAttribute.endsWith("Max")) {
                    String skillAttributeType = skillAttribute.replace("Max", "");
                    if (AttributeNameParser.getMatch(skillAttributeType) != null)
                        skillAttributeType = AttributeNameParser.getMatch(skillAttributeType);
                    if (mSkillAttributes.containsKey(skillAttributeType)) {
                        if (mSkillAttributes.get(skillAttributeType) instanceof MinMaxAttribute)
                            ((MinMaxAttribute) mSkillAttributes.get(skillAttributeType))
                                    .setMax(mSkillParser.getAttributes().get(skillAttribute));
                        else {
                            MinMaxAttribute tmp = new MinMaxAttribute();
                            tmp.setMax(mSkillParser.getAttributes().get(skillAttribute));
                            putAttributeWithSecondValue(skillAttributeType, tmp);
                        }
                    } else {
                        MinMaxAttribute tmp = new MinMaxAttribute();
                        tmp.setMax(mSkillParser.getAttributes().get(skillAttribute));
                        putAttribute(skillAttributeType, tmp);
                    }
                    break;
                }
                if (skillAttribute.endsWith("Qualifier")) {
                    putAttribute("Protects against: " + skillAttribute.replace("Qualifier", ""),
                            mSkillParser.getAttributes().get(skillAttribute));
                    break;
                }
                if (skillAttribute.startsWith("skill")) {
                    Object attr = mSkillParser.getAttributes().get(skillAttribute);
                    skillAttribute = skillAttribute.replace("skill", "");
                    putAttribute(skillAttribute, attr);
                    break;
                }
                if (skillAttribute.startsWith("pet")) {
                    Object attr = mSkillParser.getAttributes().get(skillAttribute);
                    skillAttribute = skillAttribute.replace("pet", "SkillPet");
                    putAttribute(skillAttribute, attr);
                    break;
                }
                if (mSkillAttributes.containsKey(skillAttribute)) {
                    if (mSkillAttributes.get(skillAttribute) instanceof AttributeWithSecondValue)
                        putAttributeWithSecondValue(skillAttribute, mSkillParser.getAttributes().get(skillAttribute));
                }
                putAttribute(skillAttribute, mSkillParser.getAttributes().get(skillAttribute));
                break;
            }
        }
        if (mRequiredWeapons.length() > 0)

        {
            mRequiredWeapons.delete(mRequiredWeapons.length() - 2, mRequiredWeapons.length());
            putAttribute("requiredWeapons", mRequiredWeapons.toString());
        }
        if (mCBANames.size() > 0) {
            mCBA.setXOR(mCBAXOR);
            for (String string : mCBANames) {
                Object attr = mSkillAttributes.get(string);
                if (attr == null)
                    continue;
                mCBA.addValue(string, attr);
                mSkillAttributes.remove(string);
            }
            mSkillAttributes.put(mCBA.getKey(), mCBA);
        }
    }

    private void putAttribute(String key, Object value) {
        if (AttributeNameParser.getMatch(key.replace("skill", "")) != null)
            key = AttributeNameParser.getMatch(key.replace("skill", ""));
        else if (AttributeNameParser.getMatch(key) != null)
            key = AttributeNameParser.getMatch(key);
        else if (key.endsWith("Duration") && !key.endsWith(" Duration")) {
            if (AttributeNameParser.getMatch(key.substring(0, key.length() - "Duration".length())) != null)
                key = AttributeNameParser.getMatch(key.substring(0, key.length() - "Duration".length()));
            putAttributeWithSecondValue1(key, value);
            return;
        } else if (key.startsWith("pet") || key.startsWith("SkillPet")) {
            if (AttributeNameParser.getMatch(key.replace("pet", "").replace("SkillPet", "")) != null)
                key = AttributeNameParser.getMatch(key.replace("pet", "").replace("SkillPet", ""));
        } else if (key.endsWith("Chance") && !key.equals("projectilePiercingChance")) {
            if (AttributeNameParser.getMatch(key.substring(0, key.length() - "Chance".length())) != null)
                key = AttributeNameParser.getMatch(key.substring(0, key.length() - "Chance".length()));
            putAttributeWithSecondValue(key, value);
            return;
        } else if (key.endsWith("DurationModifier")) {
            if (AttributeNameParser.getMatch(key.substring(0, key.length() - "DurationModifier".length())) != null)
                key = "{%+.0f0}% Improved"
                        + AttributeNameParser.getMatch(key.substring(0, key.length() - "DurationModifier".length()))
                        + " Duration";
        } else if (key.endsWith("Modifier")) {
            if (AttributeNameParser.getMatch(key.substring(0, key.length() - "Modifier".length())) != null)
                key = key.replace(key.substring(0, key.length() - "Modifier".length()),
                        AttributeNameParser.getMatch(key.substring(0, key.length() - "Modifier".length())) + " ");
        }
        key = formatKey(key);
        if (key.contains("{%s1}")) {
            if (mRace == null)
//                mRace = "Race not found";
                return;
            key = key.replace("{%s1}", mRace);
        }
        mSkillAttributes.put(key, value);
    }

    private String formatKey(String key) {
        if (!key.contains("${value}") && key.indexOf("{") > -1) {
            if (key.contains("{%+"))
                key = key.substring(0, key.indexOf("{")) + "+${value}"
                        + key.substring(key.indexOf("}") + 1, key.length());
            else
                key = key.substring(0, key.indexOf("{")) + "${value}"
                        + key.substring(key.indexOf("}") + 1, key.length());
        }
        return key;
    }

    private void putAttributeWithSecondValue1(String key, Object value1) {
        if (!key.contains("${value}") && key.indexOf("{") > -1) {
            if (key.contains("{%+"))
                key = key.substring(0, key.indexOf("{")) + "+${value}"
                        + key.substring(key.indexOf("}") + 1, key.length());
            else
                key = key.substring(0, key.indexOf("{")) + "${value}"
                        + key.substring(key.indexOf("}") + 1, key.length());
        }
        if (mSkillAttributes.containsKey(key)) {
            if (mSkillAttributes.get(key) instanceof AttributeWithSecondValue)
                ((AttributeWithSecondValue) mSkillAttributes.get(key)).setValue1(value1);
            else {
                Object value0 = mSkillAttributes.get(key);
                AttributeWithSecondValue tmp = new AttributeWithSecondValue();
                tmp.setValue0(value0);
                tmp.setValue1(value1);
                tmp.setKey("${value0}" + key + " over ${value1} Second(s)");
                mSkillAttributes.put(key, tmp);
            }
        } else {
            AttributeWithSecondValue tmp = new AttributeWithSecondValue();
            tmp.setValue1(value1);
            tmp.setKey("${value0}" + key + " over ${value1} Second(s)");
            mSkillAttributes.put(key, tmp);
        }
    }

    private void putAttributeWithSecondValue(String key, Object value) {
        if (!key.contains("${value}") && key.indexOf("{") > -1) {
            if (key.contains("{%+"))
                key = key.substring(0, key.indexOf("{")) + "+${value}"
                        + key.substring(key.indexOf("}") + 1, key.length());
            else
                key = key.substring(0, key.indexOf("{")) + "${value}"
                        + key.substring(key.indexOf("}") + 1, key.length());
        }
        if (mSkillAttributes.containsKey(key)) {
            if (mSkillAttributes.get(key) instanceof AttributeWithSecondValue) {
                AttributeWithSecondValue curr = (AttributeWithSecondValue) mSkillAttributes.get(key);
                if (curr.getValue0() == null)
                    curr.setValue0(value);
                else
                    curr.setValue1(value);
            }
        } else {
            AttributeWithSecondValue tmp = new AttributeWithSecondValue();
            tmp.setValue0(value);
            String descKey;
            if (!key.contains("${value}"))
                descKey = "${value1}" + key;
            else
                descKey = key.replace("value", "value1");
            tmp.setKey("${value0}% Chance of: " + descKey);
            mSkillAttributes.put(key, tmp);
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

    public ArrayList<File> getFiles() {
        return mFiles;
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
        mParent = new String[] { name };
    }

    public String getRace() {
        return mRace;
    }

    public PetParser getPet() {
        return mPet;
    }

    public void setParent(ArrayList<String> validParents) {
        if (validParents.size() == 0) {
            mParent = null;
            return;
        }
        String[] tmp = new String[validParents.size()];
        tmp = validParents.toArray(tmp);
        setParent(tmp);
    }

}
