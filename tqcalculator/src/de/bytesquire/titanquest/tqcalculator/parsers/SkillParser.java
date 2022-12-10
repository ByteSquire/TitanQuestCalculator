package de.bytesquire.titanquest.tqcalculator.parsers;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import de.bytesquire.titanquest.tqcalculator.logging.Util;
import de.bytesquire.titanquest.tqcalculator.main.*;
import de.bytesquire.titanquest.tqcalculator.util.FileNotFoundFormatter;

public class SkillParser {

    private LinkedHashMap<String, Object> mAttributes;
    private ArrayList<String> mAdditionalFiles;
    private File mSkill;
    private Path mDatabasePath;
    private String mParentPath;
    private String mSkillTag;
    private ModStringsParser mMSParser;
    private IconsParser mIconsParser;
    private String mSkillDescriptionTag;
    private SkillIcon mSkillIcon;
    private List<String> mParentSkill;
    private boolean mModifier = false;
    private String mRace;
    private Boolean mDoesNotIncludeRacialDamage;
    private Boolean mExclusiveSkill;
    private Boolean mNotDispellable;
    private Boolean mProjectileUsesAllDamage;
    private ArrayList<String> mProtectsAgainst;
    private ArrayList<Skill> mCastSkills;
    private Boolean mCastOnTarget;
    private TriggerDamage mCastOnDamage;
    private TriggerType mTrigger;
    private String mTriggeringSkill;

    private static final Logger LOGGER = Util.getLoggerForClass(SkillParser.class);

    public SkillParser(File aSkill, String aParentPath, Path aDatabasePath, ModStringsParser aMSParser,
            IconsParser aIconsParser) {
        if (aSkill == null)
            return;

        mParentPath = aParentPath;
        mDatabasePath = aDatabasePath;
        mMSParser = aMSParser;
        mIconsParser = aIconsParser;

        mAttributes = new LinkedHashMap<>();
        mAdditionalFiles = new ArrayList<String>();
        mParentSkill = new ArrayList<String>();
        mCastSkills = new ArrayList<>();

        mSkill = aSkill;
        String iconPath;
        SkillIcon test = null;
        if (!aParentPath.contains("Vanilla") && aSkill.getAbsolutePath().contains("Vanilla")) {
            iconPath = Control.VANILLA_DATABASE_DIR.relativize(aSkill.toPath()).toString();
            // TODO: implement vanilla fallback icon handling
        } else {
            iconPath = aDatabasePath.relativize(aSkill.toPath()).toString();
            test = mIconsParser.getIcon(iconPath);
        }
        mSkillIcon = test;

        initSkill();
    }

    private void initSkill() {
        try (BufferedReader skillReader = new BufferedReader(new FileReader(mSkill));) {
            Stream<String> fileStream = skillReader.lines();
            fileStream.forEach((str) -> {
                String attributeName = str.split(",", -1)[0];
                String value = str.split(",", -1)[1];
                if (value.isEmpty())
                    return;
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

                if (attributeName.equals("skillDependancy") || attributeName.equals("triggeringSkill")) {
                    String[] parentFiles = value.split(";");
                    for (String string : parentFiles) {
                        BufferedReader parentReader;
                        try {
                            parentReader = new BufferedReader(new FileReader(
                                    Control.DATABASES_DIR.resolve(mDatabasePath).resolve(string).toFile()));
                        } catch (FileNotFoundException e) {
                            try {
                                parentReader = new BufferedReader(
                                        new FileReader(Control.VANILLA_DATABASE_DIR.resolve(string).toFile()));
                            } catch (FileNotFoundException ex) {
                                if (getSkillTag() == null)
                                    Util.logWarning(LOGGER, "Parent/Dependency skill: "
                                            + FileNotFoundFormatter.relativizeExceptionPath(ex, Control.DATABASES_DIR));
                                else
                                    Util.logError(LOGGER, "Parent/Dependency skill: "
                                            + FileNotFoundFormatter.relativizeExceptionPath(ex, Control.DATABASES_DIR));
//                                    System.err.println("Parent/Dependency skill: " + e.getMessage().split(
//                                            Control.DATABASES_DIR.replace("\\", "\\\\").replace("/", "\\\\"))[1]);
                                return;
                            }
                        }
                        final boolean isTriggering = attributeName.equals("triggeringSkill");
                        Stream<String> parentFileStream = parentReader.lines();
                        parentFileStream.filter(str1 -> str1.split(",", -1)[0].equals("skillDisplayName"))
                                .forEach(name -> {
                                    String skillName = mMSParser.getMatch(name.split(",", -1)[1]);
                                    mParentSkill.add(skillName);
                                    if (isTriggering)
                                        mTriggeringSkill = skillName;
                                });
                    }
                    return;
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
                    if (!values.stream().allMatch(doubleValue -> doubleValue == 0.0))
                        mAttributes.put(attributeName, values);
                    return;
                } catch (Exception e) {
                }
                if (attributeName.equals("petSkillName") || attributeName.equals("buffSkillName")) {
                    Skill tmp;
                    try {
                        tmp = new Skill(mDatabasePath.resolve(value).toFile(), null, mParentPath, mDatabasePath,
                                mMSParser, mIconsParser);
                    } catch (FileNotFoundException e) {
                        Util.logError(LOGGER, "Pet or buff skill: "
                                + FileNotFoundFormatter.relativizeExceptionPath(e, Control.DATABASES_DIR));
//                        System.err.println("Pet or buff skill: "
//                                + e.getLocalizedMessage()
//                                        .split(Control.DATABASES_DIR.replace("\\", "\\\\").replace("/", "\\\\"))[1]
//                                + " not found");
                        return;
                    }
                    for (String skillAttribute : tmp.getAttributes().keySet()) {
                        mAttributes.put(skillAttribute, tmp.getAttributes().get(skillAttribute));
                    }
                    try {
                        mAttributes.put("skillTier", tmp.getSkillTier());
                    } catch (Exception e) {
                        Util.logDebug(LOGGER, "Probably fine because it is a pet or buff skill:\n" + e.getMessage());
                    }
                    mSkillTag = tmp.getSkillTag();
                    mSkillDescriptionTag = tmp.getSkillDescriptionTag();
                    if (mSkillIcon == null)
                        mSkillIcon = tmp.getSkillIcon();
                    mModifier = tmp.isModifier() || mModifier;
                    mDoesNotIncludeRacialDamage = tmp.isDoesNotIncludeRacialDamage();
                    mExclusiveSkill = tmp.isExclusiveSkill();
                    mNotDispellable = tmp.isNotDispellable();
                    mProtectsAgainst = tmp.getProtectsAgainst();
                    mProjectileUsesAllDamage = tmp.getProjectileUsesAllDamage();
                    mCastOnDamage = tmp.getCastOnDamage();
                    mCastOnTarget = tmp.getCastOnTarget();
                    if (tmp.getParent() != null)
                        mParentSkill.addAll(Arrays.asList(tmp.getParent()));
                    mAdditionalFiles.addAll(tmp.getFiles());
                    if (tmp.getRace() != null)
                        mRace = tmp.getRace();
                    return;
                }
                if (attributeName.equals("skillCastName")) {
                    if (value.contains(";")) {
                        String[] paths = value.split(";");
                        for (String path : paths) {
                            Skill tmp;
                            try {
                                tmp = new Skill(mDatabasePath.resolve(path).toFile(), null, mParentPath, mDatabasePath,
                                        mMSParser, mIconsParser);
                            } catch (FileNotFoundException e) {
                                Util.logError(LOGGER, "Cast skill: "
                                        + FileNotFoundFormatter.relativizeExceptionPath(e, Control.DATABASES_DIR));
                                return;
                            }
                            mCastSkills.add(tmp);
                            mAdditionalFiles.addAll(tmp.getFiles());
                        }
                    } else {
                        Skill tmp;
                        try {
                            tmp = new Skill(mDatabasePath.resolve(value).toFile(), null, mParentPath, mDatabasePath,
                                    mMSParser, mIconsParser);
                        } catch (FileNotFoundException e) {
                            Util.logError(LOGGER, "Cast skill: "
                                    + FileNotFoundFormatter.relativizeExceptionPath(e, Control.DATABASES_DIR));
                            return;
                        }
                        mCastSkills.add(tmp);
                        mAdditionalFiles.addAll(tmp.getFiles());
                    }
                    return;
                }
                if (attributeName.startsWith("skillNameCastOnDamage")) {
                    String dmgTypeStr = attributeName.split("_")[1];
                    TriggerDamage dmgType = null;
                    switch (dmgTypeStr) {
                    case "Lightning":
                        dmgType = TriggerDamage.LIGHTNING;
                        break;
                    case "Fire":
                        dmgType = TriggerDamage.FIRE;
                        break;
                    case "Cold":
                        dmgType = TriggerDamage.COLD;
                        break;
                    case "Poison":
                        dmgType = TriggerDamage.POISON;
                        break;
                    default:
                        Util.logError(LOGGER, "Skill Cast damage trigger: " + dmgTypeStr + " unknown!");
                        break;
                    }
                    if (value.contains(";")) {
                        String[] paths = value.split(";");
                        for (String path : paths) {
                            Skill tmp;
                            try {
                                tmp = new Skill(mDatabasePath.resolve(path).toFile(), null, mParentPath, mDatabasePath,
                                        mMSParser, mIconsParser);
                            } catch (FileNotFoundException e) {
                                Util.logError(LOGGER,
                                        FileNotFoundFormatter.relativizeExceptionPath(e, Control.DATABASES_DIR));
                                return;
                            }
                            if (dmgType != null)
                                tmp.setCastOnDamage(dmgType);
                            mCastSkills.add(tmp);
                            mAdditionalFiles.addAll(tmp.getFiles());
                        }
                    } else {
                        Skill tmp;
                        try {
                            tmp = new Skill(mDatabasePath.resolve(value).toFile(), null, mParentPath, mDatabasePath,
                                    mMSParser, mIconsParser);
                        } catch (FileNotFoundException e) {
                            Util.logError(LOGGER, "Cast skill: "
                                    + FileNotFoundFormatter.relativizeExceptionPath(e, Control.DATABASES_DIR));
                            return;
                        }
                        if (dmgType != null)
                            tmp.setCastOnDamage(dmgType);
                        mCastSkills.add(tmp);
                        mAdditionalFiles.addAll(tmp.getFiles());
                    }
                    return;
                }
                if (attributeName.equals("SkillPet")) {
                    File[] files = new File[value.split(";").length];
                    int i = 0;
                    for (String file : value.split(";")) {
                        files[i++] = mDatabasePath.resolve(file).toFile();
                    }
                    PetParser tmp = new PetParser(files, mParentPath, mDatabasePath, mMSParser, mIconsParser);
                    mAttributes.put("Pet", tmp);
                    mAdditionalFiles.addAll(tmp.getFiles());
                }
                if (attributeName.equals("petBonusName")) {
                    Skill tmp;
                    try {
                        tmp = new Skill(mDatabasePath.resolve(value).toFile(), null, mParentPath, mDatabasePath,
                                mMSParser, mIconsParser);
                    } catch (FileNotFoundException e) {
                        Util.logError(LOGGER, "Pet bonus skill: "
                                + FileNotFoundFormatter.relativizeExceptionPath(e, Control.DATABASES_DIR));
                        return;
                    }
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
                    if (value.contains("OnItemUse"))
                        mTrigger = TriggerType.ON_ITEM_USE;
                    if (value.contains("OnTargetKilled"))
                        mTrigger = TriggerType.ON_TARGET_KILLED;
                    if (value.contains("OnTargetHit"))
                        mTrigger = TriggerType.ON_TARGET_HIT;
                }
            });
        } catch (FileNotFoundException e) {
            if (getSkillTag() == null)
                Util.logWarning(LOGGER, FileNotFoundFormatter.relativizeExceptionPath(e, Control.DATABASES_DIR));
            else
                Util.logError(LOGGER, FileNotFoundFormatter.relativizeExceptionPath(e, Control.DATABASES_DIR));
//            System.err.println("missing file: "
//                    + e.getMessage().split(Control.DATABASES_DIR.replace("\\", "\\\\").replace("/", "\\\\"))[1]
//                            .split(".dbr ")[0]);
//                System.err.println("But that's fine, as the skill is unused\n");
        } catch (IOException e1) {
            Util.logError(LOGGER, e1);
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
        case "forceHideIconFromQuickSlot":
        case "skillCastNameIsBuff":
        case "defensiveAbsorption": // doesn't work in game
        case "offensivePierceRatioMin": // doesn't work for skills
        case "offensivePierceRatioMax": // doesn't work for skills
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
        return mSkillIcon;
    }

    public ArrayList<String> getAdditionalFiles() {
        return mAdditionalFiles;
    }

    public String[] getParentSkill() {
        if (mParentSkill.isEmpty())
            return null;
        mParentSkill = mParentSkill.stream().distinct().collect(Collectors.toList());
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

    public ArrayList<Skill> getCastSkills() {
        return mCastSkills;
    }

    public TriggerType getTriggerType() {
        return mTrigger;
    }

    public Boolean getCastOnTarget() {
        return mCastOnTarget;
    }

    public TriggerDamage getCastOnDamage() {
        return mCastOnDamage;
    }

    public String getTriggeringSkill() {
        return mTriggeringSkill;
    }
}
