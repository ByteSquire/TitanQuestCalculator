package de.bytesquire.titanquest.tqcalculator.parsers;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.stream.Stream;

import de.bytesquire.titanquest.tqcalculator.main.Control;
import de.bytesquire.titanquest.tqcalculator.main.Skill;
import de.bytesquire.titanquest.tqcalculator.main.SkillAttribute;
import de.bytesquire.titanquest.tqcalculator.main.SkillIcon;

public class SkillParser {

    private HashMap<String, SkillAttribute<?>> mAttributes;
    private ArrayList<File> mAdditionalFiles;
    private File mSkill;
    private String mParentPath;
    private String mSkillTag;
    private boolean isModifier = false;
    private ModStringsParser mMSParser;
    private IconsParser mIconsParser;
    private String mSkillDescriptionTag;
    private SkillIcon mSkillIcon;

    public SkillParser(File aSkill, String aParentPath, ModStringsParser aMSParser, IconsParser aIconsParser) {

        if (aSkill == null)
            return;

        mParentPath = aParentPath;
        mMSParser = aMSParser;
        mIconsParser = aIconsParser;

        mAttributes = new HashMap<>();
        mAdditionalFiles = new ArrayList<File>();

        mSkill = aSkill;
        mSkillIcon = mIconsParser.getIcon(mSkill.getAbsolutePath().split("database")[2].substring(1));

        initSkill();
    }

    private void initSkill() {
        try (BufferedReader skillReader = new BufferedReader(new FileReader(mSkill));) {
            Stream<String> fileStream = skillReader.lines();
            fileStream.forEach((str) -> {
                if (str.split(",")[0].equals("skillDisplayName")) {
                    mSkillTag = str.split(",")[1];
                }
                if (str.split(",")[0].equals("skillBaseDescription")) {
                    mSkillDescriptionTag = str.split(",")[1];
                }
                try {
                    Integer value = Integer.parseInt(str.split(",")[1]);
                    if (value == 0)
                        return;
                    mAttributes.put(str.split(",")[0], new SkillAttribute<Integer>(value));
                    return;
                } catch (Exception e) {
                }
                try {
                    Double value = Double.parseDouble(str.split(",")[1]);
                    if (value == 0.0)
                        return;
                    mAttributes.put(str.split(",")[0], new SkillAttribute<Double>(value));
                    return;
                } catch (Exception e) {
                }
                try {
                    if (str.split(",")[1].split(";").length == 0)
                        return;
                    ArrayList<Double> value = new ArrayList<Double>();
                    for (String e : str.split(",")[1].split(";")) {
                        value.add(Double.parseDouble(e));
                    }
                    mAttributes.put(str.split(",")[0], new SkillAttribute<ArrayList<Double>>(value));
                    return;
                } catch (Exception e) {
                }
                if (str.split(",")[0].equals("petSkillName") || str.split(",")[0].equals("buffSkillName")) {
                    Skill tmp = new Skill(
                            new File(Control.DATABASES_DIR + mParentPath.split("/")[0] + "/database/"
                                    + str.split(",")[1]),
                            mParentPath.split("/Masteries/")[0], mParentPath, mMSParser, mIconsParser);
                    for (String skillAttribute : tmp.getAttributes().keySet()) {
                        mAttributes.put(skillAttribute, tmp.getAttributes().get(skillAttribute));
                    }
                    mAttributes.put("skillTier", new SkillAttribute<>(tmp.getSkillTier()));
                    mSkillTag = tmp.getSkillTag();
                    mSkillDescriptionTag = tmp.getSkillDescriptionTag();
                    mSkillIcon = tmp.getSkillIcon();
                    mAdditionalFiles.addAll(tmp.getSkill());
                }
                if (str.split(",")[0].equals("Class") && str.split(",")[1].equals("Skill_Modifier"))
                    isModifier = true;

            });
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }

    public HashMap<String, SkillAttribute<?>> getAttributes() {
        return mAttributes;
    }

    public String getSkillTag() {
        return mSkillTag;
    }

    public boolean isModifier() {
        return isModifier;
    }

    public String getSkillDescriptionTag() {
        return mSkillDescriptionTag;
    }

    public SkillIcon getSkillIcon() {
        if (mIconsParser.getIcon(mSkill.getAbsolutePath().split("database")[2].substring(1)) != null) {
            return mIconsParser.getIcon(mSkill.getAbsolutePath().split("database")[2].substring(1).toLowerCase());
        } else {
            System.out.println(mSkill.getAbsolutePath().split("database")[2].substring(1).toLowerCase());
        }
        return mSkillIcon;
    }

    public ArrayList<File> getAdditionalFiles() {
        return mAdditionalFiles;
    }
}
