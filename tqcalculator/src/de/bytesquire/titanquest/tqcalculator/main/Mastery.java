package de.bytesquire.titanquest.tqcalculator.main;

import java.io.File;
import java.io.FileNotFoundException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import de.bytesquire.titanquest.tqcalculator.logging.Util;
import de.bytesquire.titanquest.tqcalculator.parsers.IconsParser;
import de.bytesquire.titanquest.tqcalculator.parsers.ModStringsParser;
import de.bytesquire.titanquest.tqcalculator.parsers.SkillTreeParser;
import de.bytesquire.titanquest.tqcalculator.util.FileNotFoundFormatter;

@JsonIgnoreProperties({ "masteryFiles", "skillTree", "mSkills", "mSkillTreeParser", "urlLegacy" })
@JsonPropertyOrder({ "name", "masteryAttributes", "skillTiers" })
public class Mastery {

    private SkillTreeParser mSkillTreeParser;
    private ArrayList<Skill> mSkills;
    private ArrayList<ArrayList<Skill>> mSkillTiers;
    private Map<String, Object> mMasteryAttributes;
    private String mName;
    private File mSkillTree;
    private ArrayList<String> mMasteryFiles;

    private static final Logger LOGGER = Util.getLoggerForClass(Mastery.class);

    public Mastery(File aSkillTree, Path aDatabaseDir, ModStringsParser aMSParser, IconsParser aIconsParser) {

        mSkillTree = aSkillTree;
        mSkillTreeParser = new SkillTreeParser(aSkillTree, aDatabaseDir);
        mSkillTiers = new ArrayList<>();
        mSkills = new ArrayList<>();

        mName = aMSParser.getMatch(mSkillTreeParser.getMasteryTag()).split(" Mastery")[0];

        for (File skill : mSkillTreeParser.getSkills()) {
            if (mSkillTreeParser.getSkills().indexOf(skill) != 0) {
                Skill tmp;
                try {
                    tmp = new Skill(skill, null, aDatabaseDir.getParent().getFileName() + "/" + mName, aDatabaseDir,
                            aMSParser, aIconsParser);
                } catch (FileNotFoundException e) {
                    Util.logError(LOGGER,
                            "Skill: " + FileNotFoundFormatter.relativizeExceptionPath(e, Control.DATABASES_DIR));
//                    System.err.println("Skill: "
//                            + e.getMessage().split(Control.DATABASES_DIR.replace("\\", "\\\\").replace("/", "\\\\"))[1]
//                            + " not found");
                    continue;
                }
                if (tmp.getSkillIcon() == null)
                    Util.logWarning(LOGGER,
                            "No skill icon found for skill: " + aDatabaseDir.getParent() + mName + "/" + tmp.getName());
                mSkills.add(tmp);
            } else {
                Skill tmp;
                try {
                    tmp = new Skill(skill, null, aDatabaseDir.getParent().getFileName() + "/" + mName, aDatabaseDir,
                            aMSParser, aIconsParser);
                } catch (FileNotFoundException e) {
                    Util.logError(LOGGER,
                            "Skill: " + FileNotFoundFormatter.relativizeExceptionPath(e, Control.DATABASES_DIR));
                    continue;
                }
                mMasteryAttributes = tmp.getAttributes();
                mMasteryFiles = tmp.getFiles();
            }
        }

        List<Skill> skillsWithIcons = mSkills.stream().filter(skill -> skill.getSkillIcon() != null)
                .collect(Collectors.toList());

        mSkills.clear();
        mSkills.addAll(skillsWithIcons);

        for (Skill skill : mSkills) {
            if (skill.getParent() != null) {
                ArrayList<String> validParents = new ArrayList<>();
                for (String parent : skill.getParent()) {
                    boolean containsParent = false;
                    for (Skill masterySkill : mSkills) {
                        try {
                            if (masterySkill.getName().equals(parent)) {
                                containsParent = true;
                            }
                        } catch (NullPointerException e) {
                            Util.logWarning(LOGGER,
                                    "Unused skill: " + mSkills.indexOf(masterySkill) + ", in mastery: " + mName);
                            continue;
                        }
                    }
                    if (containsParent) {
                        validParents.add(parent);
                    }
                }
                skill.setParent(validParents);
            }
            if (skill.isModifier()) {
                if (skill.getParent() == null) {
                    int skillIndex = mSkills.indexOf(skill);
                    int i = 1;
                    Skill tmp = mSkills.get(skillIndex - i++);
                    while (tmp.isModifier()) {
                        tmp = mSkills.get(skillIndex - i++);
                    }
                    skill.setParent(tmp.getName());
                }
            }
            while (true) {
                try {
                    mSkillTiers.get(skill.getSkillTier() - 1).add(skill);
                    break;
                } catch (IndexOutOfBoundsException e) {
                    mSkillTiers.add(new ArrayList<>());
                } catch (Exception e) {
                    Util.logError(LOGGER, e);
                    break;
                }
            }
        }
    }

    public ArrayList<ArrayList<Skill>> getSkillTiers() {
        return mSkillTiers;
    }

    public ArrayList<Skill> getSkillTier(int aSkillTier) {
        return mSkillTiers.get(aSkillTier);
    }

    public String getName() {
        return mName;
    }

    public String getUrlLegacy() {
        return "Masteries/" + getName() + ".html";
    }

    public ArrayList<String> getMasteryFiles() {
        return mMasteryFiles;
    }

    public File getSkillTree() {
        return mSkillTree;
    }

    public Map<String, Object> getMasteryAttributes() {
        return mMasteryAttributes;
    }

}
