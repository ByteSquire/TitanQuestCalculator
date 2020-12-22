package de.bytesquire.titanquest.tqcalculator.parsers;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.stream.Stream;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import de.bytesquire.titanquest.tqcalculator.main.MinMaxAttribute;

@JsonIgnoreProperties({ "skills" })
@JsonInclude(Include.NON_NULL)
public class PetParser {

    private HashMap<String, Object> mAttributes;
    private File[] mSkills;

    public PetParser(File[] files) {
        if (files == null)
            return;

        mAttributes = new HashMap<>();

        mSkills = files;

        initSkill();
    }

    private void initSkill() {
        for (File file : mSkills) {
            try (BufferedReader skillReader = new BufferedReader(new FileReader(file));) {
                Stream<String> fileStream = skillReader.lines();
                fileStream.forEach((str) -> {
                    String attributeName = str.split(",")[0];
                    String value = str.split(",")[1];
                    if (canBeIgnored(attributeName))
                        return;

                    attributeName = attributeName.replace("character", "Character").replace("handHit", "")
                            .replace("footHit", "");

                    try {
                        Double doubleValue = Double.parseDouble(value);
                        if (doubleValue == 0.0)
                            return;
                        if (attributeName.endsWith("Max") || attributeName.endsWith("Min")) {
                            String attributeType = attributeName.replace("Max", "").replace("Min", "");
                            if (mAttributes.containsKey(convertKey(attributeType))) {
                                if (mAttributes.get(convertKey(attributeType)) instanceof MinMaxAttribute) {
                                    MinMaxAttribute relevantObj = ((MinMaxAttribute) mAttributes.get(convertKey(attributeType)));
                                    if (attributeName.endsWith("Max"))
                                        relevantObj.addMax(doubleValue);
                                    else
                                        relevantObj.addMin(doubleValue);
                                }
                            } else {
                                MinMaxAttribute tmp = new MinMaxAttribute();
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
                    } catch (Exception e) {
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

    public HashMap<String, Object> getAttributes() {
        return mAttributes;
    }

    public File[] getSkills() {
        return mSkills;
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
}
