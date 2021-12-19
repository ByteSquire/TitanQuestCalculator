package de.bytesquire.titanquest.tqcalculator.parsers;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.logging.Logger;
import java.util.stream.Stream;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import de.bytesquire.titanquest.tqcalculator.logging.Util;
import de.bytesquire.titanquest.tqcalculator.main.*;
import de.bytesquire.titanquest.tqcalculator.util.FileNotFoundFormatter;

@JsonIgnoreProperties({ "files", "additionalFiles" })
@JsonInclude(Include.NON_NULL)
@JsonPropertyOrder({ "attributes", "initialSkill", "petSkills", "petSkillLevels" })
public class PetParser {

    private LinkedHashMap<String, Object> mAttributes;
    private LinkedHashMap<String, Skill> mPetSkills;
    private Skill mInitialSkill;
    private LinkedHashMap<Integer, String> mSkillNameIndexMap;
    private LinkedHashMap<String, ArrayList<Integer>> mPetSkillLevels;
    private LinkedHashMap<Integer, ArrayList<Integer>> mSkillLevelIndexMap;
    private File[] mSkills;
    private ArrayList<String> mAdditionalFiles;
    private Path mDatabasePath;
    private String mParentPath;
    private ModStringsParser mMSParser;
    private IconsParser mIconsParser;
    private FileNotFoundException mLastMissingPetSkill = null;

    private static final Logger LOGGER = Util.getLoggerForClass(PetParser.class);

    public PetParser(File[] files, String aParentPath, Path aDatabasePath, ModStringsParser aMSParser,
            IconsParser aIconsParser) {
        if (files == null)
            return;

        mAttributes = new LinkedHashMap<>();
        mPetSkills = new LinkedHashMap<>();
        mAdditionalFiles = new ArrayList<>();
        mPetSkillLevels = new LinkedHashMap<>();
        mSkillNameIndexMap = new LinkedHashMap<>();
        mSkillLevelIndexMap = new LinkedHashMap<>();

        mSkills = files;
        mDatabasePath = aDatabasePath;
        mParentPath = aParentPath;
        mMSParser = aMSParser;
        mIconsParser = aIconsParser;

        initSkill();

        for (Integer i : mSkillNameIndexMap.keySet()) {
            if (mSkillLevelIndexMap.get(i) != null) {
                boolean canLearn = false;
                for (Integer a : mSkillLevelIndexMap.get(i)) {
                    if (a > 0) {
                        canLearn = true;
                        break;
                    }
                }
                if (canLearn)
                    mPetSkillLevels.put(mSkillNameIndexMap.get(i), mSkillLevelIndexMap.get(i));
            }
        }

        ArrayList<String> skillsToRemove = new ArrayList<>();
        for (String skillName : mPetSkills.keySet()) {
            if (!mPetSkillLevels.containsKey(skillName))
                skillsToRemove.add(skillName);
        }

        for (String skillName : skillsToRemove)
            mPetSkills.remove(skillName);
    }

    private void initSkill() {
        for (File file : mSkills) {
            try (BufferedReader skillReader = new BufferedReader(new FileReader(file));) {
                Stream<String> fileStream = skillReader.lines();
                fileStream.forEach((str) -> {
                    String attributeName = str.split(",", -1)[0];
                    String value = str.split(",", -1)[1];
                    if (value.isEmpty())
                        return;
                    if (canBeIgnored(attributeName))
                        return;

                    attributeName = attributeName.replace("character", "Character");

                    if (attributeName.startsWith("skillName")) {
                        if (value.contains(";"))
                            return;
//                        if (value.contains(" "))
//                            return;
                        Skill tmp;
                        try {
                            tmp = new Skill(mDatabasePath.resolve(value).toFile(), null, mParentPath, mDatabasePath,
                                    mMSParser, mIconsParser);
                        } catch (FileNotFoundException e) {
                            if (mLastMissingPetSkill != null
                                    && mLastMissingPetSkill.getMessage().equals(e.getMessage()))
                                return; // can get really spam-y otherwise
                            else
                                mLastMissingPetSkill = e;
                            Util.logError(LOGGER, "Pet skill missing: "
                                    + FileNotFoundFormatter.relativizeExceptionPath(e, Control.DATABASES_DIR));
//                            System.err.println("Pet skill: "
//                                    + e.getMessage()
//                                            .split(Control.DATABASES_DIR.replace("\\", "\\\\").replace("/", "\\\\"))[1]
//                                    + " not found");
                            return;
                        }
                        if (tmp.getName() == null) {
                            return;
                        }
                        mPetSkills.put(tmp.getName(), tmp);

                        mSkillNameIndexMap.put(Integer.parseInt(attributeName.split("skillName")[1]), tmp.getName());

                        mAdditionalFiles.addAll(tmp.getFiles());
                        return;
                    }

                    if (attributeName.startsWith("initialSkillName")) {
                        if (value.contains(";"))
                            return;
//                        if (value.contains(" "))
//                            return;
                        Skill tmp;
                        try {
                            tmp = new Skill(mDatabasePath.resolve(value).toFile(), null, mParentPath, mDatabasePath,
                                    mMSParser, mIconsParser);
                        } catch (FileNotFoundException e) {
                            Util.logError(LOGGER, "Pet initial skill missing: "
                                    + FileNotFoundFormatter.relativizeExceptionPath(e, Control.DATABASES_DIR));
//                            System.err.println("Pet initial skill: "
//                                    + e.getMessage()
//                                            .split(Control.DATABASES_DIR.replace("\\", "\\\\").replace("/", "\\\\"))[1]
//                                    + " not found");
                            return;
                        }
                        if (tmp.getName() == null) {
                            return;
                        }
                        mInitialSkill = tmp;

                        mAdditionalFiles.addAll(tmp.getFiles());
                        return;
                    }

                    if (attributeName.startsWith("skillLevel")) {
                        Integer lvl = Integer.parseInt(value.split(";")[0]); // only supports normal difficulty
//                        if (lvl == 0)
//                            return;
                        if (mSkillLevelIndexMap.containsKey(Integer.parseInt(attributeName.split("skillLevel")[1])))
                            mSkillLevelIndexMap.get(Integer.parseInt(attributeName.split("skillLevel")[1])).add(lvl);
                        else {
                            ArrayList<Integer> tmp = new ArrayList<>();
                            tmp.add(lvl);
                            mSkillLevelIndexMap.put(Integer.parseInt(attributeName.split("skillLevel")[1]), tmp);
                        }
                        return;
                    }

                    try {
                        if (value.contains(";"))
                            value = value.split(";")[0];
                        Double doubleValue = Double.parseDouble(value);
                        if (doubleValue == 0.0)
                            return;
                        if (attributeName.endsWith("Max") || attributeName.endsWith("Min")) {
                            String attributeType = attributeName.replace("Max", "").replace("Min", "");
                            if (mAttributes.containsKey(convertKey(attributeType))) {
                                if (mAttributes.get(convertKey(attributeType)) instanceof MinMaxAttribute) {
                                    MinMaxAttribute relevantObj = ((MinMaxAttribute) mAttributes
                                            .get(convertKey(attributeType)));
                                    if (attributeName.endsWith("Max"))
                                        relevantObj.addMax(doubleValue);
                                    else
                                        relevantObj.addMin(doubleValue);
                                }
                            } else {
                                MinMaxAttribute tmp = new MinMaxAttribute(null, null);
                                if (attributeName.endsWith("Max"))
                                    tmp.addMax(doubleValue);
                                else
                                    tmp.addMin(doubleValue);
                                putAttribute(attributeType, tmp);
                            }
                        } else {
                            if (mAttributes.containsKey(convertKey(attributeName)))
                                ((ArrayList<Double>) mAttributes.get(convertKey(attributeName))).add(doubleValue);
                            else {
                                ArrayList<Double> tmp = new ArrayList<>();
                                tmp.add(doubleValue);
                                putAttribute(attributeName, tmp);
                            }
                        }
                        return;
                    } catch (NumberFormatException e) {
                        // System.err.println("Pet attribute Number error: " + e.getMessage());
                    } catch (Exception e) {
                        Util.logError(LOGGER, e);
                    }
                });
            } catch (IOException e) {
                Util.logError(LOGGER, e);
            }
        }
    }

    private boolean canBeIgnored(String attributeName) {
        if (attributeName.startsWith("character"))
            return false;
        if (attributeName.startsWith("skillName"))
            return false;
        if (attributeName.startsWith("initialSkillName"))
            return false;
        if (attributeName.startsWith("skillLevel"))
            return false;
        switch (attributeName) {
        case "handHitDamageMax":
        case "handHitDamageMin":
        case "footHitDamageMax":
        case "footHitDamageMin":
            return false;
        default:
            return true;
        }
    }

    private void putAttribute(String key, Object value) {
        mAttributes.put(convertKey(key), value);
    }

    private String convertKey(String key) {
        if (AttributeNameParser.getMatch(key) != null)
            key = AttributeNameParser.getMatch(key);
        else if (key.endsWith("Modifier")) {
            if (AttributeNameParser.getMatch(key.substring(0, key.length() - "Modifier".length())) != null)
                key = key.replace(key.substring(0, key.length() - "Modifier".length()),
                        AttributeNameParser.getMatch(key.substring(0, key.length() - "Modifier".length())) + " ");
        }
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

    public LinkedHashMap<String, Object> getAttributes() {
        return mAttributes;
    }

    public ArrayList<String> getFiles() {
        ArrayList<String> ret = new ArrayList<>();
        for (String string : mAdditionalFiles) {
            if (!ret.contains(string))
                ret.add(string);
        }
        for (File f : mSkills) {
            if (!ret.contains(f.getAbsolutePath()))
                ret.add(f.getAbsolutePath());
        }
        return ret;
    }

    @Override
    public String toString() {
        StringBuilder build = new StringBuilder();
        build.append("Pet Attributes:\n");
        mAttributes.forEach((key, value) -> {
            build.append(key + ": " + value);
            build.append("\n");
        });
        return build.toString();
    }

    public LinkedHashMap<String, Skill> getPetSkills() {
        return mPetSkills;
    }

    public LinkedHashMap<String, ArrayList<Integer>> getPetSkillLevels() {
        return mPetSkillLevels;
    }

    public Skill getInitialSkill() {
        return mInitialSkill;
    }
}
