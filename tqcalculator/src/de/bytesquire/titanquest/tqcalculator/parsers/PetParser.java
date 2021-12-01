package de.bytesquire.titanquest.tqcalculator.parsers;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.stream.Stream;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import de.bytesquire.titanquest.tqcalculator.main.*;

@JsonIgnoreProperties({ "files", "additionalFiles" })
@JsonInclude(Include.NON_NULL)
@JsonPropertyOrder({ "attributes", "petSkills", "petSkillLevels" })
public class PetParser {

    private LinkedHashMap<String, Object> mAttributes;
    private LinkedHashMap<String, Skill> mPetSkills;
    private LinkedHashMap<Integer, String> mSkillNameIndexMap;
    private LinkedHashMap<String, ArrayList<Integer>> mPetSkillLevels;
    private LinkedHashMap<Integer, ArrayList<Integer>> mSkillLevelIndexMap;
    private File[] mSkills;
    private ArrayList<String> mAdditionalFiles;
    private String mParentPath;
    private ModStringsParser mMSParser;
    private IconsParser mIconsParser;

    public PetParser(File[] files, String aParentPath, ModStringsParser aMSParser, IconsParser aIconsParser) {
        if (files == null)
            return;

        mAttributes = new LinkedHashMap<>();
        mPetSkills = new LinkedHashMap<>();
        mAdditionalFiles = new ArrayList<>();
        mPetSkillLevels = new LinkedHashMap<>();
        mSkillNameIndexMap = new LinkedHashMap<>();
        mSkillLevelIndexMap = new LinkedHashMap<>();

        mSkills = files;
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
                        if (value.contains(" "))
                            return;
                        Skill tmp;
                        try {
                            tmp = new Skill(
                                    new File(Control.DATABASES_DIR + mParentPath.split("/")[0] + "/database/" + value),
                                    null, mParentPath, mMSParser, mIconsParser);
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
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

                    if (attributeName.startsWith("skillLevel")) {
                        Integer lvl = Integer.parseInt(value.split(";")[0]);
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
                        System.err.println("Pet error: " + e.getMessage());
                    }
                });
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
    }

    private boolean canBeIgnored(String attributeName) {
        if (attributeName.startsWith("character"))
            return false;
        if (attributeName.startsWith("skillName"))
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
        mAttributes.put(key, value);
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
}
