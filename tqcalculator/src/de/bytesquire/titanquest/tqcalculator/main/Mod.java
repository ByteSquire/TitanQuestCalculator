package de.bytesquire.titanquest.tqcalculator.main;

import java.io.File;
import java.nio.file.Paths;
import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import de.bytesquire.titanquest.tqcalculator.parsers.IconsParser;
import de.bytesquire.titanquest.tqcalculator.parsers.ModParser;
import de.bytesquire.titanquest.tqcalculator.parsers.ModStringsParser;

@JsonIgnoreProperties({ "msparser", "character", "modDir", "links", "masteryLevel", "gameEngine", "iconsParser",
        "urlLegacy", "url", "questSkillPoints", "modParser" })
@JsonPropertyOrder({ "name", "mappedMasteries", "masteryLevels", "classNames", "masteries" })
public class Mod {

    private ModParser mModParser;
    private ModStringsParser mMSParser;
    private IconsParser mIconsParser;
    private ArrayList<Mastery> mMasteries;
    private LinkedHashMap<Integer, String> mMappedMasteries;
    private String mModName, mModDir;
    private Map<String, String> mLinks;
    private File mCharacter;
    private File mGameEngine;
    private ArrayList<Integer> mMasteryTiers;
    private Map<String, ArrayList<ArrayList<String>>> mQuestSkillPoints;
    private String[][] mClassNames;

    public Mod(String aModName, String aModDir) {
        if (aModName == null)
            return;
        mMasteries = new ArrayList<Mastery>();
        mMappedMasteries = new LinkedHashMap<Integer, String>();
        mModName = aModName;
        mModDir = aModDir;
        mMSParser = new ModStringsParser(aModDir + "/text/");
        initClassNames();

        mIconsParser = new IconsParser(aModDir + "/database/");
        mModParser = new ModParser(aModDir);
        mLinks = mModParser.getLinks();
        mCharacter = mModParser.getCharacter();
        mMasteryTiers = mModParser.getMasteryTiers();
        mQuestSkillPoints = mModParser.getQuestSkillPoints();
        mGameEngine = mModParser.getGameEngine();

        int i = 1;
        for (File skillTree : mModParser.getSkillTrees()) {
            if (!skillTree.getName().equals("QuestRewardSkillTree.dbr")) {
                Mastery tmp = new Mastery(skillTree,
                        mModDir + "database" + Paths.get("").getFileSystem().getSeparator(), mModName, mMSParser,
                        mIconsParser);
                mMasteries.add(tmp);
                mMappedMasteries.put(i++, tmp.getName());
            }
        }
    }

    private void initClassNames() {
        mClassNames = new String[11][11];

        mClassNames[0][0] = "Mastery parameters missing";

        setIfNotNull(mClassNames, 1, 0, "tagCClass02");
        setIfNotNull(mClassNames, 2, 0, "tagCClass01");
        setIfNotNull(mClassNames, 3, 0, "tagCClass05");
        setIfNotNull(mClassNames, 4, 0, "tagCClass06");
        setIfNotNull(mClassNames, 5, 0, "tagCClass03");
        setIfNotNull(mClassNames, 6, 0, "tagCClass07");
        setIfNotNull(mClassNames, 7, 0, "xtagCharacterClass01");
        setIfNotNull(mClassNames, 8, 0, "tagCClass08");
        setIfNotNull(mClassNames, 9, 0, "x2tag_class_rm_rm");
        setIfNotNull(mClassNames, 10, 0, "tagCClass04");

        setIfNotNull(mClassNames, 1, 2, "tagCClass36");
        setIfNotNull(mClassNames, 1, 3, "tagCClass28");
        setIfNotNull(mClassNames, 1, 4, "tagCClass23");
        setIfNotNull(mClassNames, 1, 5, "tagCClass35");
        setIfNotNull(mClassNames, 1, 6, "tagCClass17");
        setIfNotNull(mClassNames, 1, 7, "xtagCharacterClass08");
        setIfNotNull(mClassNames, 1, 8, "tagCClass10");
        setIfNotNull(mClassNames, 1, 9, "x2tag_class_nature_rm");
        setIfNotNull(mClassNames, 1, 10, "tagCClass32");

        setIfNotNull(mClassNames, 2, 3, "tagCClass27");
        setIfNotNull(mClassNames, 2, 4, "tagCClass22");
        setIfNotNull(mClassNames, 2, 5, "tagCClass34");
        setIfNotNull(mClassNames, 2, 6, "tagCClass16");
        setIfNotNull(mClassNames, 2, 7, "xtagCharacterClass09");
        setIfNotNull(mClassNames, 2, 8, "tagCClass09");
        setIfNotNull(mClassNames, 2, 9, "x2tag_class_spirit_rm");
        setIfNotNull(mClassNames, 2, 10, "tagCClass31");

        setIfNotNull(mClassNames, 3, 4, "tagCClass26");
        setIfNotNull(mClassNames, 3, 5, "tagCClass29");
        setIfNotNull(mClassNames, 3, 6, "tagCClass20");
        setIfNotNull(mClassNames, 3, 7, "xtagCharacterClass05");
        setIfNotNull(mClassNames, 3, 8, "tagCClass13");
        setIfNotNull(mClassNames, 3, 9, "x2tag_class_storm_rm");
        setIfNotNull(mClassNames, 3, 10, "tagCClass30");

        setIfNotNull(mClassNames, 4, 5, "tagCClass24");
        setIfNotNull(mClassNames, 4, 6, "tagCClass21");
        setIfNotNull(mClassNames, 4, 7, "xtagCharacterClass04");
        setIfNotNull(mClassNames, 4, 8, "tagCClass14");
        setIfNotNull(mClassNames, 4, 9, "x2tag_class_earth_rm");
        setIfNotNull(mClassNames, 4, 10, "tagCClass25");

        setIfNotNull(mClassNames, 5, 6, "tagCClass18");
        setIfNotNull(mClassNames, 5, 7, "xtagCharacterClass07");
        setIfNotNull(mClassNames, 5, 8, "tagCClass11");
        setIfNotNull(mClassNames, 5, 9, "x2tag_class_stealth_rm");
        setIfNotNull(mClassNames, 5, 10, "tagCClass33");

        setIfNotNull(mClassNames, 6, 7, "xtagCharacterClass03");
        setIfNotNull(mClassNames, 6, 8, "tagCClass15");
        setIfNotNull(mClassNames, 6, 9, "x2tag_class_defense_rm");
        setIfNotNull(mClassNames, 6, 10, "tagCClass19");

        setIfNotNull(mClassNames, 7, 8, "xtagCClass02");
        setIfNotNull(mClassNames, 7, 9, "x2tag_class_dream_rm");
        setIfNotNull(mClassNames, 7, 10, "xtagCClass06");

        setIfNotNull(mClassNames, 8, 9, "x2tag_class_warfare_rm");
        setIfNotNull(mClassNames, 8, 10, "tagCClass12");

        setIfNotNull(mClassNames, 9, 10, "x2tag_class_hunting_rm");
    }

    private void setIfNotNull(String[][] arr, int index0, int index1, String element) {
        String match = mMSParser.getMatch(element);
        if (match != null)
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

    public String getModDir() {
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

    public LinkedHashMap<Integer, String> getMappedMasteries() {
        return mMappedMasteries;
    }

    public ArrayList<Integer> getMasteryLevels() {
        return mMasteryTiers;
    }

    public File getGameEngine() {
        return mGameEngine;
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
}
