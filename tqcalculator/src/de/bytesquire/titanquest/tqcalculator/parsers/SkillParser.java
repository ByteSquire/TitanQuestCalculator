package de.bytesquire.titanquest.tqcalculator.parsers;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.stream.Stream;

import de.bytesquire.titanquest.tqcalculator.main.*;

public class SkillParser {

    private LinkedHashMap<String, Object> mAttributes;
    private ArrayList<String> mAdditionalFiles;
    private File mSkill;
    private String mParentPath;
    private String mSkillTag;
    private ModStringsParser mMSParser;
    private IconsParser mIconsParser;
    private String mSkillDescriptionTag;
    private SkillIcon mSkillIcon;
    private ArrayList<String> mParentSkill;
    private boolean mModifier = false;
    private String mRace;
    private Boolean mDoesNotIncludeRacialDamage;
    private Boolean mExclusiveSkill;
    private Boolean mNotDispellable;
    private Boolean mProjectileUsesAllDamage;
    private ArrayList<String> mProtectsAgainst;

    public SkillParser(File aSkill, String aParentPath, ModStringsParser aMSParser, IconsParser aIconsParser) {

        if (aSkill == null)
            return;

        mParentPath = aParentPath;
        mMSParser = aMSParser;
        mIconsParser = aIconsParser;

        mAttributes = new LinkedHashMap<>();
        mAdditionalFiles = new ArrayList<String>();
        mParentSkill = new ArrayList<String>();

        mSkill = aSkill;
        mSkillIcon = mIconsParser.getIcon(mSkill.getAbsolutePath().split("database")[2].substring(1));

        initSkill();
    }

    private void initSkill() {
        try (BufferedReader skillReader = new BufferedReader(new FileReader(mSkill));) {
            Stream<String> fileStream = skillReader.lines();
            fileStream.forEach((str) -> {
                String attributeName = str.split(",")[0];
                String value = str.split(",")[1];
                if (canBeIgnored(attributeName))
                    return;
                if (attributeName.equals("skillDisplayName")) {
                    mSkillTag = value;
                    return;
                }
                if (attributeName.equals("skillBaseDescription")) {
                    mSkillDescriptionTag = value;
                    return;
                }

                attributeName = attributeName.replace("character", "Character").replace("defensive", "Defense")
                        .replace("projectile", "Projectile").replace("retaliation", "Retaliation")
                        .replace("explosion", "Explosion").replace("racial", "Racial").replace("spark", "Spark")
                        .replace("damage", "Damage").replace("life", "Life").replace("refresh", "Refresh")
                        .replace("spawnObjects", "SkillPet").replace("numProjectiles", "ProjectileNumber")
                        .replace("RatioAdder", "Modifier").replace("offensive", "Damage").replace("Slow", "Duration");

                if (attributeName.startsWith("skill")) {
                    if (attributeName.equals("skillDependancy")) {
                        String[] parentFiles = value.split(";");
                        for (String string : parentFiles) {
                            try {
                                BufferedReader parentReader = new BufferedReader(new FileReader(new File(
                                        Control.DATABASES_DIR + mParentPath.split("/")[0] + "/database/" + string)));
                                Stream<String> parentFileStream = parentReader.lines();
                                parentFileStream.filter(str1 -> str1.split(",")[0].equals("skillDisplayName"))
                                        .forEach(name -> {
                                            mParentSkill.add(mMSParser.getMatch(name.split(",")[1]));
                                        });
                            } catch (FileNotFoundException e) {
                                System.err.println("missing file: " + e.getMessage()
                                        .split(Control.DATABASES_DIR.replace("\\", "\\\\").replace("/", "\\\\"))[1]
                                                .split(".dbr ")[0]);
                                if (getSkillTag() == null)
                                    System.err.println("But that's fine, as the skill is unused\n");
                            }
                        }
                        return;
                    }
                }

                try {
                    Integer intValue = Integer.parseInt(value);
                    if (intValue == 0)
                        return;
                    mAttributes.put(attributeName, intValue);
                    return;
                } catch (Exception e) {
                }
                try {
                    Double doubleValue = Double.parseDouble(value);
                    if (doubleValue == 0.0)
                        return;
                    mAttributes.put(attributeName, doubleValue);
                    return;
                } catch (Exception e) {
                }
                try {
                    if (value.split(";").length == 0)
                        return;
                    ArrayList<Double> values = new ArrayList<Double>();
                    for (String e : value.split(";")) {
                        values.add(Double.parseDouble(e));
                    }
                    mAttributes.put(attributeName, values);
                    return;
                } catch (Exception e) {
                }
                if (attributeName.equals("petSkillName") || attributeName.equals("buffSkillName")) {
                    Skill tmp = new Skill(
                            new File(Control.DATABASES_DIR + mParentPath.split("/")[0] + "/database/" + value), null,
                            mParentPath, mMSParser, mIconsParser);
                    for (String skillAttribute : tmp.getAttributes().keySet()) {
                        mAttributes.put(skillAttribute, tmp.getAttributes().get(skillAttribute));
                    }
                    mAttributes.put("skillTier", tmp.getSkillTier());
                    mSkillTag = tmp.getSkillTag();
                    mSkillDescriptionTag = tmp.getSkillDescriptionTag();
                    mSkillIcon = tmp.getSkillIcon();
                    mModifier = tmp.isModifier() || mModifier;
                    mDoesNotIncludeRacialDamage = tmp.isDoesNotIncludeRacialDamage();
                    mExclusiveSkill = tmp.isExclusiveSkill();
                    mNotDispellable = tmp.isNotDispellable();
                    mProtectsAgainst = tmp.getProtectsAgainst();
                    mProjectileUsesAllDamage = tmp.getProjectileUsesAllDamage();
                    if (tmp.getParent() != null)
                        mParentSkill.addAll(Arrays.asList(tmp.getParent()));
                    mAdditionalFiles.addAll(tmp.getFiles());
                    if (tmp.getRace() != null)
                        mRace = tmp.getRace();
                    return;
                }
                if (attributeName.equals("SkillPet")) {
                    File[] files = new File[value.split(";").length];
                    int i = 0;
                    for (String file : value.split(";")) {
                        files[i++] = new File(Control.DATABASES_DIR + mParentPath.split("/")[0] + "/database/" + file);
                    }
                    PetParser tmp = new PetParser(files, mParentPath, mMSParser, mIconsParser);
                    mAttributes.put("Pet", tmp);
                    mAdditionalFiles.addAll(tmp.getFiles());
                }
                if (attributeName.equals("petBonusName")) {
                    Skill tmp = new Skill(
                            new File(Control.DATABASES_DIR + mParentPath.split("/")[0] + "/database/" + value), null,
                            mParentPath, mMSParser, mIconsParser);
                    if (tmp.getName() == null)
                        return;
                    mAttributes.put("Bonus to all Pets:", tmp.getAttributes());
                    mAdditionalFiles.addAll(tmp.getFiles());
                    return;
                }
                if (attributeName.equals("RacialBonusRace")) {
                    mRace = value;
                }
                if (attributeName.equals("Class")) {
                    if (value.endsWith("Modifier") || value.startsWith("SkillSecondary"))
                        mModifier = true;
                }
            });
        } catch (FileNotFoundException e) {
            System.err.println("missing file: "
                    + e.getMessage().split(Control.DATABASES_DIR.replace("\\", "\\\\").replace("/", "\\\\"))[1]
                            .split(".dbr ")[0]);
            if (getSkillTag() == null)
                System.err.println("But that's fine, as the skill is unused\n");
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }

    private boolean canBeIgnored(String attributeName) {
        if (attributeName.startsWith("camera"))
            return true;
        if (attributeName.startsWith("drift"))
            return true;
        if (attributeName.startsWith("ragDoll"))
            return true;
        if (attributeName.startsWith("skillWeaponTint"))
            return true;
        if (attributeName.startsWith("drop"))
            return true;
        if (attributeName.startsWith("skillConnection"))
            return true;
        if (attributeName.startsWith("spawnObjectsDistance"))
            return true;
        if (attributeName.startsWith("wave"))
            return true;
        if (attributeName.startsWith("skillProjectile") && attributeName.endsWith("TimeToLive"))
            return true;
        if (attributeName.endsWith("NumberOfRings"))
            return true;
        if (attributeName.endsWith("AngleToCaster"))
            return true;
        if (attributeName.endsWith("SpacingAngle"))
            return true;
        if (attributeName.endsWith("RandomRotation"))
            return true;
        if (attributeName.endsWith("DurationMax")) // doesn't work in game
            return true;
        switch (attributeName) {
        case "skillMasteryLevelRequired":
        case "projectileLaunchRotation":
        case "skillAllowsWarmUp":
        case "isPetDisplayable":
        case "expansionTime":
        case "skipSkillLinking":
        case "instantCast":
        case "targetCaster":
        case "actorScaleTime":
        case "projectileHitTimeToLive":
        case "projectileMissTimeToLive":
        case "debufSkill":
        case "alwaysUseSpecialAnimation":
        case "hideFromUI":
        case "headVelocity":
        case "tailVelocity":
        case "skillProjectileTargetGroundOnly":
        case "numRings":
        case "distanceIncrement":
        case "spacingAngle":
        case "defensiveAbsorption": //doesn't work in game
            return true;
        default:
            return false;
        }
    }

    public LinkedHashMap<String, Object> getAttributes() {
        return mAttributes;
    }

    public String getSkillTag() {
        return mSkillTag;
    }

    public String getSkillDescriptionTag() {
        return mSkillDescriptionTag;
    }

    public SkillIcon getSkillIcon() {
        if (mIconsParser.getIcon(mSkill.getAbsolutePath().split("database")[2].substring(1)) != null) {
            return mIconsParser.getIcon(mSkill.getAbsolutePath().split("database")[2].substring(1).toLowerCase());
        } else {
//            System.out.println(mSkill.getAbsolutePath().split("database")[2].substring(1).toLowerCase());
        }
        return mSkillIcon;
    }

    public ArrayList<String> getAdditionalFiles() {
        return mAdditionalFiles;
    }

    public String[] getParentSkill() {
        if (mParentSkill.size() == 0)
            return null;
        String[] tmp = new String[mParentSkill.size()];
        /* tmp = */mParentSkill.toArray(tmp);
        return tmp;
    }

    public boolean isModifier() {
        return mModifier;
    }

    public String getRace() {
        return mRace;
    }

    public Boolean getNotDispellable() {
        return mNotDispellable;
    }

    public Boolean getExclusiveSkill() {
        return mExclusiveSkill;
    }

    public Boolean getDoesNotIncludeRacialDamage() {
        return mDoesNotIncludeRacialDamage;
    }

    public Boolean getProjectileUsesAllDamage() {
        return mProjectileUsesAllDamage;
    }

    public ArrayList<String> getProtectsAgainst() {
        return mProtectsAgainst;
    }
}
