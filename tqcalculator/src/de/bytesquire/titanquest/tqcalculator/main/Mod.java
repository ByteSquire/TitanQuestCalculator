package de.bytesquire.titanquest.tqcalculator.main;

import java.io.File;
import java.nio.file.Path;
import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import de.bytesquire.titanquest.tqcalculator.logging.Util;
import de.bytesquire.titanquest.tqcalculator.parsers.IconsParser;
import de.bytesquire.titanquest.tqcalculator.parsers.ModParser;
import de.bytesquire.titanquest.tqcalculator.parsers.ModStringsParser;

@JsonIgnoreProperties({ "msparser", "character", "modDir", "links", "masteryLevel", "gameEngine", "playerLevels",
        "iconsParser", "urlLegacy", "url", "questSkillPoints", "modParser" })
@JsonPropertyOrder({ "name", "mappedMasteries", "masteryLevels", "skillPointIncrement", "maxLevel", "classNames",
        "masteries" })
public class Mod {

    private ModParser mModParser;
    private ModStringsParser mMSParser;
    private IconsParser mIconsParser;
    private ArrayList<Mastery> mMasteries;
    private ArrayList<String> mMappedMasteries;
    private String mModName;
    private Path mModDir;
    private Path mDatabaseDir;
    private Map<String, String> mLinks;
    private File mCharacter;
    private File mGameEngine;
    private File mPlayerLevels;
    private ArrayList<Integer> mMasteryTiers;
    private Integer mSkillPointIncrement;
    private int mMaxLevel = 85; // maybe THQ Nordic is nice
    private Map<String, ArrayList<ArrayList<String>>> mQuestSkillPoints;
    private String[][] mClassNames;

    private static final Logger LOGGER = Util.getLoggerForClass(Mod.class);

    public Mod(String aModName, Path aModDir) {
        if (aModName == null)
            return;
        mMasteries = new ArrayList<Mastery>();
        mMappedMasteries = new ArrayList<String>();
        mModName = aModName;
        mModDir = aModDir;
        mDatabaseDir = mModDir.resolve("database");
        mMSParser = new ModStringsParser(mModDir.resolve("text"));
        initClassNames();

        mIconsParser = new IconsParser(mDatabaseDir);
        mModParser = new ModParser(mDatabaseDir);
        mLinks = mModParser.getLinks();
        mCharacter = mModParser.getCharacter();
        mMasteryTiers = mModParser.getMasteryTiers();
        mQuestSkillPoints = mModParser.getQuestSkillPoints();
        mGameEngine = mModParser.getGameEngine();
        mPlayerLevels = mModParser.getPlayerLevels();
        mSkillPointIncrement = mModParser.getSkillPointIncrement();

        for (File skillTree : mModParser.getSkillTrees()) {
            if (!skillTree.getName().equals("QuestRewardSkillTree.dbr")) {
                Mastery tmp = new Mastery(skillTree, mDatabaseDir, mMSParser, mIconsParser);
                mMasteries.add(tmp);
                mMappedMasteries.add(tmp.getName());
            }
        }
    }

    private void initClassNames() {
        mClassNames = new String[12][12];

        mClassNames[0][0] = "Mastery parameters missing or invalid";

        setIfNotNull(mClassNames, 1, 0, "tagCClass08");
        setIfNotNull(mClassNames, 2, 0, "tagCClass07");
        setIfNotNull(mClassNames, 3, 0, "tagCClass06");
        setIfNotNull(mClassNames, 4, 0, "tagCClass05");
        setIfNotNull(mClassNames, 5, 0, "tagCClass03");
        setIfNotNull(mClassNames, 7, 0, "tagCClass02");
        setIfNotNull(mClassNames, 8, 0, "tagCClass01");
        setIfNotNull(mClassNames, 6, 0, "tagCClass04");
        setIfNotNull(mClassNames, 9, 0, "xtagCharacterClass01");
        setIfNotNull(mClassNames, 10, 0, "x2tag_class_rm_rm");
        setIfNotNull(mClassNames, 11, 0, "x4tagNeidan");

        setIfNotNull(mClassNames, 1, 2, "tagCClass15");
        setIfNotNull(mClassNames, 1, 3, "tagCClass14");
        setIfNotNull(mClassNames, 1, 4, "tagCClass13");
        setIfNotNull(mClassNames, 1, 5, "tagCClass11");
        setIfNotNull(mClassNames, 1, 6, "tagCClass12");
        setIfNotNull(mClassNames, 1, 7, "tagCClass10");
        setIfNotNull(mClassNames, 1, 8, "tagCClass09");
        setIfNotNull(mClassNames, 1, 9, "xtagCharacterClass02");
        setIfNotNull(mClassNames, 1, 10, "x2tag_class_warfare_rm");
        setIfNotNull(mClassNames, 1, 11, "x4tagNeidanWarfare");

        setIfNotNull(mClassNames, 2, 3, "tagCClass21");
        setIfNotNull(mClassNames, 2, 4, "tagCClass20");
        setIfNotNull(mClassNames, 2, 5, "tagCClass18");
        setIfNotNull(mClassNames, 2, 6, "tagCClass19");
        setIfNotNull(mClassNames, 2, 7, "tagCClass17");
        setIfNotNull(mClassNames, 2, 8, "tagCClass16");
        setIfNotNull(mClassNames, 2, 9, "xtagCharacterClass03");
        setIfNotNull(mClassNames, 2, 10, "x2tag_class_defense_rm");
        setIfNotNull(mClassNames, 2, 11, "x4tagNeidanDefense");

        setIfNotNull(mClassNames, 3, 4, "tagCClass26");
        setIfNotNull(mClassNames, 3, 5, "tagCClass24");
        setIfNotNull(mClassNames, 3, 6, "tagCClass25");
        setIfNotNull(mClassNames, 3, 7, "tagCClass23");
        setIfNotNull(mClassNames, 3, 8, "tagCClass22");
        setIfNotNull(mClassNames, 3, 9, "xtagCharacterClass04");
        setIfNotNull(mClassNames, 3, 10, "x2tag_class_earth_rm");
        setIfNotNull(mClassNames, 3, 11, "x4tagNeidanEarth");

        setIfNotNull(mClassNames, 4, 5, "tagCClass29");
        setIfNotNull(mClassNames, 4, 6, "tagCClass30");
        setIfNotNull(mClassNames, 4, 7, "tagCClass28");
        setIfNotNull(mClassNames, 4, 8, "tagCClass27");
        setIfNotNull(mClassNames, 4, 9, "xtagCharacterClass05");
        setIfNotNull(mClassNames, 4, 10, "x2tag_class_storm_rm");
        setIfNotNull(mClassNames, 4, 11, "x4tagNeidanStorm");

        setIfNotNull(mClassNames, 5, 6, "tagCClass33");
        setIfNotNull(mClassNames, 5, 7, "tagCClass35");
        setIfNotNull(mClassNames, 5, 8, "tagCClass34");
        setIfNotNull(mClassNames, 5, 9, "xtagCharacterClass07");
        setIfNotNull(mClassNames, 5, 10, "x2tag_class_stealth_rm");
        setIfNotNull(mClassNames, 5, 11, "x4tagNeidanRogue");

        setIfNotNull(mClassNames, 6, 7, "tagCClass32");
        setIfNotNull(mClassNames, 6, 8, "tagCClass31");
        setIfNotNull(mClassNames, 6, 9, "xtagCharacterClass06");
        setIfNotNull(mClassNames, 6, 10, "x2tag_class_hunting_rm");
        setIfNotNull(mClassNames, 6, 11, "x4tagNeidanHunting");

        setIfNotNull(mClassNames, 7, 8, "tagCClass36");
        setIfNotNull(mClassNames, 7, 9, "xtagCharacterClass08");
        setIfNotNull(mClassNames, 7, 10, "x2tag_class_nature_rm");
        setIfNotNull(mClassNames, 7, 11, "x4tagNeidanNature");

        setIfNotNull(mClassNames, 8, 9, "xtagCharacterClass09");
        setIfNotNull(mClassNames, 8, 10, "x2tag_class_spirit_rm");
        setIfNotNull(mClassNames, 8, 11, "x4tagNeidanSpirit");

        setIfNotNull(mClassNames, 9, 10, "x2tag_class_dream_rm");
        setIfNotNull(mClassNames, 9, 11, "x4tagNeidanDream");

        setIfNotNull(mClassNames, 10, 11, "x4tagNeidanRune");

    }

    private void setIfNotNull(String[][] arr, int index0, int index1, String element) {
        String match = mMSParser.getMatch(element);
        if (match == null)
            match = element + " Not found in modstrings";
        arr[index0][index1] = match;
    }

    public List<Mastery> getMasteries() {
        return mMasteries;
    }

    public Mastery getMastery(int index) throws InvalidParameterException {
        if ((0 > index - ModParser.COUNT_MASTERIES + ModParser.COUNT_QUEST_REWARD_TREES))
            return mMasteries.get(index);
        else
            throw new InvalidParameterException(
                    "0 <= index <= " + (ModParser.COUNT_MASTERIES + ModParser.COUNT_QUEST_REWARD_TREES - 1));
    }

    public String getName() {
        return mModName;
    }

    public Path getModDir() {
        return mModDir;
    }

    public ModStringsParser getMSParser() {
        return mMSParser;
    }

    public String getUrlLegacy() {
        return Control.URL + "/mods/" + getName() + "/" + getName() + ".html";
    }

    public String getUrl() {
        return Control.URL + "/mods/" + getName() + "/"/* + getName() + ".json" */;
    }

    public Map<String, String> getLinks() {
        return mLinks;
    }

    public File getCharacter() {
        return mCharacter;
    }

    public List<String> getMappedMasteries() {
        return mMappedMasteries;
    }

    public ArrayList<Integer> getMasteryLevels() {
        return mMasteryTiers;
    }

    public File getGameEngine() {
        return mGameEngine;
    }

    public File getPlayerLevels() {
        return mPlayerLevels;
    }

    public IconsParser getIconsParser() {
        return mIconsParser;
    }

    public Map<String, ArrayList<ArrayList<String>>> getQuestSkillPoints() {
        return mQuestSkillPoints;
    }

    public ModParser getModParser() {
        return mModParser;
    }

    public String[][] getClassNames() {
        return mClassNames;
    }

    public Integer getSkillPointIncrement() {
        return mSkillPointIncrement;
    }

    public Integer getMaxLevel() {
        return mMaxLevel;
    }
}
