package de.bytesquire.titanquest.tqcalculator.parsers;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.stream.Stream;

import de.bytesquire.titanquest.tqcalculator.main.Control;
import de.bytesquire.titanquest.tqcalculator.main.Skill;
import de.bytesquire.titanquest.tqcalculator.main.SkillIcon;

public class SkillParser {

    private HashMap<String, Object> mAttributes;
    private ArrayList<File> mAdditionalFiles;
    private File mSkill;
    private String mParentPath;
    private String mSkillTag;
    private ModStringsParser mMSParser;
    private IconsParser mIconsParser;
    private String mSkillDescriptionTag;
    private SkillIcon mSkillIcon;
    private ArrayList<String> mParentSkill;
    private boolean mModifier = false;

    public SkillParser(File aSkill, String aParentPath, ModStringsParser aMSParser, IconsParser aIconsParser) {

        if (aSkill == null)
            return;

        mParentPath = aParentPath;
        mMSParser = aMSParser;
        mIconsParser = aIconsParser;

        mAttributes = new HashMap<>();
        mAdditionalFiles = new ArrayList<File>();
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
                if (attributeName.endsWith("Modifier")) {
                    if (value.split(";").length > 1) {
                        ArrayList<String> localList = new ArrayList<>();
                        for (String e : value.split(";")) {
                            Double eValue = Double.parseDouble(e);
                            int eValueInt = eValue.intValue();
                            if (eValue - eValueInt == 0)
                                localList.add(eValue < 0 ? String.valueOf(eValueInt) : ("+" + eValueInt) + "%");
                            else
                                localList.add(eValue < 0 ? String.valueOf(eValue) : ("+" + eValue) + "%");
                        }
                        mAttributes.put(attributeName, localList);
                        return;
                    } else {
                        Double eValue = Double.parseDouble(value.split(";")[0]);
                        int eValueInt = eValue.intValue();
                        if (eValue == 0)
                            return;
                        if (eValue - eValueInt == 0)
                            mAttributes.put(attributeName, (eValue < 0 ? eValueInt : ("+" + eValueInt) + "%"));
                        else
                            mAttributes.put(attributeName, (eValue < 0 ? eValue : ("+" + eValue) + "%"));
                        return;
                    }
                }
                if (attributeName.contains("Percent") || attributeName.contains("Chance")) {
                    if (value.split(";").length > 1) {
                        ArrayList<String> localList = new ArrayList<>();
                        for (String e : value.split(";")) {
                            Double eValue = Double.parseDouble(e);
                            int eValueInt = eValue.intValue();
                            if (eValue - eValueInt == 0)
                                localList.add(eValueInt + "%");
                            else
                                localList.add(eValue + "%");
                        }
                        mAttributes.put(attributeName, localList);
                        return;
                    } else {
                        Double eValue = Double.parseDouble(value.split(";")[0]);
                        int eValueInt = eValue.intValue();
                        if (eValue == 0)
                            return;
                        if (eValue - eValueInt == 0)
                            mAttributes.put(attributeName, eValueInt + "%");
                        else
                            mAttributes.put(attributeName, eValue + "%");
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
                    mModifier = tmp.isModifier();
                    if (tmp.getParent() != null)
                        mParentSkill.addAll(Arrays.asList(tmp.getParent()));
                    mAdditionalFiles.addAll(tmp.getSkill());
                    return;
                }
                if (attributeName.equals("petBonusName")) {
                    Skill tmp = new Skill(
                            new File(Control.DATABASES_DIR + mParentPath.split("/")[0] + "/database/" + value), null,
                            mParentPath, mMSParser, mIconsParser);
                    for (String skillAttribute : tmp.getAttributes().keySet()) {
                        mAttributes.put("pet" + skillAttribute, tmp.getAttributes().get(skillAttribute));
                    }
                    mAdditionalFiles.addAll(tmp.getSkill());
                    return;
                }
                if (attributeName.equals("skillDependancy")) {
                    try {
                        String[] parentFiles = value.split(";");
                        for (String string : parentFiles) {
                            BufferedReader parentReader = new BufferedReader(new FileReader(new File(
                                    Control.DATABASES_DIR + mParentPath.split("/")[0] + "/database/" + string)));
                            Stream<String> parentFileStream = parentReader.lines();
                            parentFileStream.filter(str1 -> str1.split(",")[0].equals("skillDisplayName"))
                                    .forEach(name -> {
                                        mParentSkill.add(mMSParser.getTags().get(name.split(",")[1]));
                                    });
                        }
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                    return;
                }
                if (attributeName.equals("Class") && (value.endsWith("Modifier") || value.startsWith("SkillSecondary")))
                    mModifier = true;
            });
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }

    private boolean canBeIgnored(String attributeName) {
        if (attributeName.startsWith("camera"))
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
        switch (attributeName) {
        case "skillMasteryLevelRequired":
        case "projectileLaunchRotation":
        case "skillAllowsWarmUp":
        case "isPetDisplayable":
            return true;
        default:
            return false;
        }
    }

    public HashMap<String, Object> getAttributes() {
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

    public ArrayList<File> getAdditionalFiles() {
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
}
