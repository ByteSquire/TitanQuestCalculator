package de.bytesquire.titanquest.tqcalculator.main;

import java.io.File;
import java.io.FileNotFoundException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.logging.Logger;
import java.util.Map.Entry;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import de.bytesquire.titanquest.tqcalculator.logging.Util;
import de.bytesquire.titanquest.tqcalculator.parsers.*;

@JsonIgnoreProperties({ "files", "buff", "skillTag", "skillDescriptionTag", "modifier", "skillTier", "urlLegacy",
        "requiredWeapons", "race" })
@JsonInclude(Include.NON_NULL)
@JsonPropertyOrder({ "name", "description", "doesNotIncludeRacialDamage", "exclusiveSkill", "notDispellable",
        "projectileUsesAllDamage", "protectsAgainst", "castOnTarget", "castOnDamage", "triggerType", "triggeringSkill",
        "parent", "skillIcon", "attributes", "pet", "skillCast" })
public class Skill {

    private SkillParser mSkillParser;
    private String mSkillName;
    private String mSkillDescription;
    private LinkedHashMap<String, SkillAttribute> mAttributeBuilder;
    private LinkedHashMap<String, Object> mSkillAttributes;
    private PetParser mPet;
    private SkillCast mSkillCast;
    private String mParentPath;
    private boolean isModifier;
    private String[] mParent;
    private ArrayList<String> mFiles;
    private int mSkillTier;
    private SkillIcon mSkillIcon;
    private StringBuilder mRequiredWeapons;
    private String mRace;
    private ChanceBasedAttributes mCBA;
    private ArrayList<String> mCBANames;
    private boolean mCBAXOR;
    private Boolean mDoesNotIncludeRacialDamage;
    private Boolean mExclusiveSkill;
    private Boolean mNotDispellable;
    private Boolean mProjectileUsesAllDamage;
    private Boolean mCastOnTarget;
    private TriggerDamage mCastOnDamage;
    private TriggerType mTriggerType;
    private String mTriggeringSkill;
    private ArrayList<String> mProtectsAgainst;

    private static final Logger LOGGER = Util.getLoggerForClass(Skill.class);

    public Skill(File aSkill, String[] aParent, String aParentPath, Path aDatabasePath, ModStringsParser aMSParser,
            IconsParser aIconsParser) throws FileNotFoundException {
        if (aSkill.isDirectory())
            throw new FileNotFoundException(aSkill.getAbsolutePath());
        if (!aSkill.exists()) {
            String relativePath = aDatabasePath.relativize(aSkill.toPath()).toString();
            Path vanillaPath = Control.VANILLA_DATABASE_DIR.resolve(relativePath);
            File vanillaSkill = vanillaPath.toFile();
            if (!vanillaSkill.exists())
                throw new FileNotFoundException(aSkill.getAbsolutePath());
            else {
                Util.logDebug(LOGGER, relativePath + " not found, falling back to vanilla...");
                aSkill = vanillaSkill;
            }
        }

        mAttributeBuilder = new LinkedHashMap<>();
        mSkillAttributes = new LinkedHashMap<>();
        mParentPath = aParentPath;
        mParent = aParent;
        mFiles = new ArrayList<>();
        mFiles.add(aSkill.getAbsolutePath());

        mSkillParser = new SkillParser(aSkill, aParentPath, aDatabasePath, aMSParser, aIconsParser);

        String name;
        if ((name = aMSParser.getMatch(mSkillParser.getSkillTag())) != null)
            mSkillName = name;
        else
            mSkillName = mSkillParser.getSkillTag();
        if (mSkillName == null)
            mSkillName = aSkill.getName().replace(".dbr", "");

        mSkillDescription = aMSParser.getMatch(mSkillParser.getSkillDescriptionTag());
        mSkillIcon = mSkillParser.getSkillIcon();
        if (mSkillParser.getRace() != null)
            mRace = mSkillParser.getRace().replace(";", " & ");

        if (mSkillParser.getParentSkill() != null) {
            mParent = mSkillParser.getParentSkill();
        }
        isModifier = mSkillParser.isModifier();
        mTriggerType = mSkillParser.getTriggerType();
        mCastOnTarget = mSkillParser.getCastOnTarget();
        mCastOnDamage = mSkillParser.getCastOnDamage();
        mTriggeringSkill = mSkillParser.getTriggeringSkill();
        mNotDispellable = mSkillParser.getNotDispellable();
        mDoesNotIncludeRacialDamage = mSkillParser.getDoesNotIncludeRacialDamage();
        mExclusiveSkill = mSkillParser.getExclusiveSkill();
        mProjectileUsesAllDamage = mSkillParser.getProjectileUsesAllDamage();
        mProtectsAgainst = mSkillParser.getProtectsAgainst();
        if (mProtectsAgainst == null)
            mProtectsAgainst = new ArrayList<>();

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
            case "Axe":
            case "Bow":
            case "Spear":
            case "Mace":
            case "Staff":
            case "Magical":
            case "Shield":
                mRequiredWeapons.append(skillAttribute);
                mRequiredWeapons.append(", ");
                break;
            case "RangedOneHand":
                mRequiredWeapons.append("Thrown Weapon");
                mRequiredWeapons.append(", ");
                break;
            case "dualWieldOnly":
                mRequiredWeapons.append("Dual Wield Only");
                mRequiredWeapons.append(", ");
                break;
            case "meleeOnly":
                // mRequiredWeapons.append("Melee Only, ");
                break;
            case "Pet":
                mPet = (PetParser) mSkillParser.getAttributes().get(skillAttribute);
                break;
            case "notDispelable":
                mNotDispellable = true;
                break;
            case "excludeRacialDamage":
                mDoesNotIncludeRacialDamage = true;
                break;
            case "exclusiveSkill":
                mExclusiveSkill = true;
                break;
            case "ProjectileUsesAllDamage":
                mProjectileUsesAllDamage = true;
                break;
            case "skillCastOnTargetLocation":
                mCastOnTarget = true;
                break;
            case "skillCastOnAllDamageTypes":
                mCastOnDamage = TriggerDamage.ALL;
                break;
            default:
                Object value = mSkillParser.getAttributes().get(skillAttribute);
                if (skillAttribute.endsWith("GlobalChance")) {
                    mCBA.setChance(value);
                    break;
                }
                if (skillAttribute.endsWith("Global")) {
                    String skillAttributeType = skillAttribute.replace("Global", "");
                    String skillAttributeTypeModifier = skillAttributeType + "Modifier";
                    mCBANames.add(skillAttributeType);
                    mCBANames.add(skillAttributeTypeModifier);
                    break;
                }
                if (skillAttribute.endsWith("XOR")) {
                    mCBAXOR = true;
                    break;
                }
                if (skillAttribute.endsWith("Qualifier")) {
                    String dmgKey = skillAttribute.replace("Qualifier", "");
                    dmgKey = dmgKey.substring(0, 1).toUpperCase() + dmgKey.substring(1, dmgKey.length());
                    dmgKey = dmgKey.replace("Bleeding", "DurationBleeding");
                    dmgKey = "Damage" + dmgKey.replace("Damage", "");
                    String dmgQualifier;
                    if ((dmgQualifier = AttributeNameParser.getMatch(dmgKey)) != null)
                        mProtectsAgainst.add(dmgQualifier);
                    else
                        mProtectsAgainst.add(skillAttribute.replace("Qualifier", ""));
                    break;
                }
                if (skillAttribute.startsWith("skill")) {
                    skillAttribute = skillAttribute.replace("skill", "");
                }
                if (skillAttribute.startsWith("pet")) {
                    skillAttribute = skillAttribute.replace("pet", "SkillPet");
                }
                if (skillAttribute.equals("MaxLevel") || skillAttribute.equals("UltimateLevel")
                        || skillAttribute.equals("Bonus to all Pets:") || skillAttribute.equals("requiredWeapons")) {
                    mSkillAttributes.put(skillAttribute, value);
                    break;
                }
                if (skillAttribute.contains("CastLevel")) {
                    ArrayList<Skill> castSkills = mSkillParser.getCastSkills();
                    if (castSkills == null)
                        castSkills = new ArrayList<>();
                    mSkillCast = new SkillCast(value, castSkills);
                    break;
                }
                putAttribute(skillAttribute, value);
                break;
            }
        }
        if (mRequiredWeapons.length() > 0) {
            mRequiredWeapons.delete(mRequiredWeapons.length() - 2, mRequiredWeapons.length());
            mSkillAttributes.put("requiredWeapons", mRequiredWeapons.toString());
        }
        if (mCBANames.size() > 0) {
            mCBA.setXOR(mCBAXOR);
            for (String string : mCBANames) {
                SkillAttribute attr = mAttributeBuilder.get(string);
                if (attr == null)
                    continue;
                mCBA.addValue(attr.getKey(), attr.getObject());
                mAttributeBuilder.remove(string);
            }
            mSkillAttributes.put(mCBA.getKey(), mCBA);
        }
        for (Entry<String, SkillAttribute> entry : mAttributeBuilder.entrySet()) {
            SkillAttribute tmp = entry.getValue();
            if (tmp.getKey() == null)
                continue;
            String key = tmp.getKey();
            if (isModifier && key.startsWith("Life Time"))
                key = "Extra " + key;
            mSkillAttributes.put(key, tmp.getObject());
        }
    }

    private void putAttribute(String key, Object value) {
        if (key.contains("$")) {
            mSkillAttributes.put(key, value);
            return;
        }
        if (key.endsWith("Min")) {
            key = key.substring(0, key.length() - "Min".length());
            MinMaxAttribute tmp = new MinMaxAttribute(value, null);
            if (key.endsWith("Duration")) {
                key = key.substring(0, key.length() - "Duration".length());
                putAttributeMinMaxDuration(key, tmp);
                return;
            }
            if (key.endsWith("Chance")) {
                key = key.substring(0, key.length() - "Chance".length());
                putAttributeMinMaxChance(key, tmp);
                return;
            }
            if (key.startsWith("DamageDuration")
                    || key.contains("Fumble") || key.startsWith("DamageTotalResistanceReduction")) {
                putAttributeMinMaxDurationDamage(key, tmp);
                return;
            }
            putAttributeMinMaxDamage(key, tmp);
            return;
        }
        if (key.endsWith("Max")) {
            key = key.substring(0, key.length() - "Max".length());
            MinMaxAttribute tmp = new MinMaxAttribute(null, value);
            if (key.endsWith("Duration")) {
                key = key.substring(0, key.length() - "Duration".length());
                putAttributeMinMaxDuration(key, tmp);
                return;
            }
            if (key.endsWith("Chance")) {
                key = key.substring(0, key.length() - "Chance".length());
                putAttributeMinMaxChance(key, tmp);
                return;
            }
            if (key.startsWith("DamageDuration")
                    || key.contains("Fumble") || key.startsWith("DamageTotalResistanceReduction")) {
                putAttributeMinMaxDurationDamage(key, tmp);
                return;
            }
            putAttributeMinMaxDamage(key, tmp);
            return;
        }
        if (key.contains("Damage") && key.endsWith("Duration") && !key.endsWith(" Duration")) {
            key = key.substring(0, key.length() - "Duration".length());
            putAttributeDuration(key, value);
            return;
        }
        if (key.endsWith("Chance") && !key.equals("projectilePiercingChance") && !key.equals("onHitActivationChance")
                && !key.equals("CastChance")) {
            key = key.substring(0, key.length() - "Chance".length());
            putAttributeChance(key, value);
            return;
        }
        if (key.startsWith("Damage") && key.endsWith("DurationModifier")) {
            if (AttributeNameParser.getMatch(key.substring(0, key.length() - "DurationModifier".length())) != null)
                key = "{%+.0f0}% Improved"
                        + AttributeNameParser.getMatch(key.substring(0, key.length() - "DurationModifier".length()))
                        + " Duration";
        }
        if (!key.endsWith("Modifier") && (key.startsWith("DamageDuration")
                || key.contains("Fumble")) || key.startsWith("DamageTotalResistanceReduction")) {
            putAttributeDurationDamage(key, value);
            return;
        }
        putAttributeDamage(key, value);
    }

    private void putAttributeMinMaxDamage(String key, MinMaxAttribute value) {
        if (mAttributeBuilder.containsKey(key)) {
            SkillAttribute curr = mAttributeBuilder.get(key);
            curr.setType(AttributeType.DEFAULT);
            if (curr.getValue() instanceof MinMaxAttribute)
                ((MinMaxAttribute) curr.getValue()).setMinMax(value);
            else
                curr.setValue(value);
        } else {
            SkillAttribute tmp = new SkillAttribute(mRace, key, value);
            mAttributeBuilder.put(key, tmp);
        }
    }

    private void putAttributeMinMaxDurationDamage(String key, MinMaxAttribute value) {
        if (mAttributeBuilder.containsKey(key)) {
            SkillAttribute curr = mAttributeBuilder.get(key);
            curr.setType(AttributeType.DURATION);
            if (curr.getValue() instanceof MinMaxAttribute)
                ((MinMaxAttribute) curr.getValue()).setMinMax(value);
            else
                curr.setValue(value);
        } else {
            SkillAttribute tmp = new SkillAttribute(mRace, key, value);
            tmp.setType(AttributeType.DURATION);
            mAttributeBuilder.put(key, tmp);
        }
    }

    private void putAttributeMinMaxDuration(String key, MinMaxAttribute value) {
        if (mAttributeBuilder.containsKey(key)) {
            SkillAttribute curr = mAttributeBuilder.get(key);
            curr.setType(AttributeType.DURATION);
            if (curr.getDuration() instanceof MinMaxAttribute)
                ((MinMaxAttribute) curr.getDuration()).setMinMax(value);
            else
                curr.setDuration(value);
        } else {
            SkillAttribute tmp = new SkillAttribute(mRace, key, value, AttributeType.DURATION);
            mAttributeBuilder.put(key, tmp);
        }
    }

    private void putAttributeMinMaxChance(String key, MinMaxAttribute value) {
        if (mAttributeBuilder.containsKey(key)) {
            SkillAttribute curr = mAttributeBuilder.get(key);
            curr.setType(AttributeType.CHANCE);
            if (curr.getChance() instanceof MinMaxAttribute)
                ((MinMaxAttribute) curr.getChance()).setMinMax(value);
            else
                curr.setChance(value);
        } else {
            SkillAttribute tmp = new SkillAttribute(mRace, key, value, AttributeType.CHANCE);
            mAttributeBuilder.put(key, tmp);
        }
    }

    private void putAttributeChance(String key, Object value) {
        if (mAttributeBuilder.containsKey(key)) {
            SkillAttribute curr = mAttributeBuilder.get(key);
            curr.setChance(value);
        } else {
            SkillAttribute tmp = new SkillAttribute(mRace, key, value, AttributeType.CHANCE);
            mAttributeBuilder.put(key, tmp);
        }
    }

    private void putAttributeDuration(String key, Object value) {
        if (mAttributeBuilder.containsKey(key)) {
            SkillAttribute curr = mAttributeBuilder.get(key);
            curr.setDuration(value);
        } else {
            SkillAttribute tmp = new SkillAttribute(mRace, key, value, AttributeType.DURATION);
            mAttributeBuilder.put(key, tmp);
        }
    }

    private void putAttributeDamage(String key, Object value) {
        if (mAttributeBuilder.containsKey(key)) {
            SkillAttribute curr = mAttributeBuilder.get(key);
            curr.setValue(value);
        } else {
            SkillAttribute tmp = new SkillAttribute(mRace, key, value);
            mAttributeBuilder.put(key, tmp);
        }
    }

    private void putAttributeDurationDamage(String key, Object value) {
        if (mAttributeBuilder.containsKey(key)) {
            SkillAttribute curr = mAttributeBuilder.get(key);
            curr.setValue(value);
            curr.setType(AttributeType.DURATION);
        } else {
            SkillAttribute tmp = new SkillAttribute(mRace, key, AttributeType.DURATION);
            tmp.setValue(value);
            mAttributeBuilder.put(key, tmp);
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
        Map<String, Object> ret = sortAttributes();
        return ret;
    }

    public String getUrlLegacy() {
        String parentMasteryName = mParentPath.split("/")[1];
        return parentMasteryName + "/" + getName() + ".html";
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

    public ArrayList<String> getFiles() {
        return mFiles;
    }

    public int getSkillTier() throws Exception {
        if (mSkillTier <= 0)
            throw new Exception("Skill tier missing for skill: " + mSkillName + " ("
                    + Control.DATABASES_DIR.relativize(Path.of(mFiles.get(0))) + ")");
        return mSkillTier;
    }

    public SkillIcon getSkillIcon() {
        return mSkillIcon;
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

    public SkillCast getSkillCast() {
        return mSkillCast;
    }

    public Boolean isDoesNotIncludeRacialDamage() {
        return mDoesNotIncludeRacialDamage;
    }

    public Boolean isExclusiveSkill() {
        return mExclusiveSkill;
    }

    public Boolean isNotDispellable() {
        return mNotDispellable;
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

    public ArrayList<String> getProtectsAgainst() {
        if (mProtectsAgainst.size() == 0)
            return null;
        return mProtectsAgainst;
    }

    public Boolean getProjectileUsesAllDamage() {
        return mProjectileUsesAllDamage;
    }

    private Map<String, Object> sortAttributes() {
        ArrayList<Entry<String, Object>> entries = new ArrayList<>(mSkillAttributes.entrySet());
        Collections.sort(entries, new AttributesComparator());
        Map<String, Object> sortedMap = new LinkedHashMap<String, Object>();
        for (Entry<String, Object> entry : entries) {
            sortedMap.put(entry.getKey(), entry.getValue());
        }

        return sortedMap;
    }

    public Boolean getCastOnTarget() {
        return mCastOnTarget;
    }

    public TriggerDamage getCastOnDamage() {
        return mCastOnDamage;
    }

    public void setCastOnDamage(TriggerDamage dmgType) {
        mCastOnDamage = dmgType;
    }

    public TriggerType getTriggerType() {
        return mTriggerType;
    }

    public String getTriggeringSkill() {
        return mTriggeringSkill;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        boolean equals = false;
        if (obj instanceof Skill) {
            Skill other = (Skill) obj;
            equals = other.getName().equals(mSkillName);
            if (!equals)
                return equals;
            equals &= getAttributes().keySet().equals(other.getAttributes().keySet());
            if (!equals)
                return equals;
//            equals &= getAttributes().values().equals(other.getAttributes().values());
//            if (!equals)
//                return equals;
        }
        return equals;
    }
}

class AttributesComparator implements Comparator<Entry<String, Object>> {

    private static final ArrayList<String> order = new ArrayList<String>(Arrays.asList("^a${value} Second(s) Recharge",
            "${value}% Chance of Activating", "${value} Energy Reserved", "${value} Energy Cost",
            "${value} Active Energy Cost per Second", "${value}% Chance to be Used", "${value} Second Duration",
            "${value}m Radius", "${value} Charge Levels", "Launches ${value} Projectile(s)", "+${value} Health",
            "+${value} Energy", "+${value} Strength", "+${value} Intelligence", "+${value} Dexterity",
            "+${value}% Strength", "+${value}% Intelligence", "+${value}% Dexterity"));

    private static final ArrayList<String> orderBottomUp = new ArrayList<String>(Arrays.asList("Bonus to all Pets:",
            "${chance}% Chance for one of the following:", "${chance}% Chance for all of the following:"));

    public int compare(Entry<String, Object> entry1, Entry<String, Object> entry2) {
        String str1 = entry1.getKey();
        String str2 = entry2.getKey();

        if (entry1.getValue() instanceof SkillAttribute) {
            String tmp = ((SkillAttribute) entry1.getValue()).getKey();
            if (tmp != null)
                str1 = tmp;
        }
        if (entry2.getValue() instanceof SkillAttribute) {
            String tmp = ((SkillAttribute) entry2.getValue()).getKey();
            if (tmp != null)
                str2 = tmp;
        }

        int index1 = 0;
        int index2 = 0;
        if (order.indexOf(str1) > -1)
            index1 = order.size() - order.indexOf(str1);
        if (order.indexOf(str2) > -1)
            index2 = order.size() - order.indexOf(str2);
        if (orderBottomUp.indexOf(str1) > -1)
            index1 = (-1) * (orderBottomUp.size() - orderBottomUp.indexOf(str1));
        if (orderBottomUp.indexOf(str2) > -1)
            index2 = (-1) * (orderBottomUp.size() - orderBottomUp.indexOf(str2));
        if (index1 == 0 && index2 == 0)
            return str1.replaceAll("\\$\\{value[0-1]\\}", "").compareTo(str2.replaceAll("\\$\\{value[0-1]\\}", ""));
        return index2 - index1;
    }

}
