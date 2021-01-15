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
        Map<String, String> modStrings = mMSParser.getTags();

        mClassNames[0][0] = "Mastery parameters missing";
        
        mClassNames[1][0] = modStrings.get("tagCClass02");
        mClassNames[2][0] = modStrings.get("tagCClass01");
        mClassNames[3][0] = modStrings.get("tagCClass05");
        mClassNames[4][0] = modStrings.get("tagCClass06");
        mClassNames[5][0] = modStrings.get("tagCClass03");
        mClassNames[6][0] = modStrings.get("tagCClass07");
        mClassNames[7][0] = modStrings.get("xtagCharacterClass01");
        mClassNames[8][0] = modStrings.get("tagCClass08");
        mClassNames[9][0] = modStrings.get("x2tagCClass01");
        mClassNames[10][0] = modStrings.get("tagCClass04");

        mClassNames[1][2] = modStrings.get("tagCClass36");
        mClassNames[1][3] = modStrings.get("tagCClass28");
        mClassNames[1][4] = modStrings.get("tagCClass23");
        mClassNames[1][5] = modStrings.get("tagCClass35");
        mClassNames[1][6] = modStrings.get("tagCClass17");
        mClassNames[1][7] = modStrings.get("xtagCharacterClass08");
        mClassNames[1][8] = modStrings.get("tagCClass10");
        mClassNames[1][9] = modStrings.get("x2tagCClass08");
        mClassNames[1][10] = modStrings.get("tagCClass32");

        mClassNames[2][3] = modStrings.get("tagCClass27");
        mClassNames[2][4] = modStrings.get("tagCClass22");
        mClassNames[2][5] = modStrings.get("tagCClass34");
        mClassNames[2][6] = modStrings.get("tagCClass16");
        mClassNames[2][7] = modStrings.get("xtagCharacterClass09");
        mClassNames[2][8] = modStrings.get("tagCClass09");
        mClassNames[2][9] = modStrings.get("x2tagCClass09");
        mClassNames[2][10] = modStrings.get("tagCClass31");

        mClassNames[3][4] = modStrings.get("tagCClass26");
        mClassNames[3][5] = modStrings.get("tagCClass29");
        mClassNames[3][6] = modStrings.get("tagCClass20");
        mClassNames[3][7] = modStrings.get("xtagCharacterClass05");
        mClassNames[3][8] = modStrings.get("tagCClass13");
        mClassNames[3][9] = modStrings.get("x2tagCClass05");
        mClassNames[3][10] = modStrings.get("tagCClass30");

        mClassNames[4][5] = modStrings.get("tagCClass24");
        mClassNames[4][6] = modStrings.get("tagCClass21");
        mClassNames[4][7] = modStrings.get("xtagCharacterClass04");
        mClassNames[4][8] = modStrings.get("tagCClass14");
        mClassNames[4][9] = modStrings.get("x2tagCClass04");
        mClassNames[4][10] = modStrings.get("tagCClass25");

        mClassNames[5][6] = modStrings.get("tagCClass18");
        mClassNames[5][7] = modStrings.get("xtagCharacterClass07");
        mClassNames[5][8] = modStrings.get("tagCClass11");
        mClassNames[5][9] = modStrings.get("x2tagCClass07");
        mClassNames[5][10] = modStrings.get("tagCClass33");

        mClassNames[6][7] = modStrings.get("xtagCharacterClass03");
        mClassNames[6][8] = modStrings.get("tagCClass15");
        mClassNames[6][9] = modStrings.get("x2tagCClass03");
        mClassNames[6][10] = modStrings.get("tagCClass19");

        mClassNames[7][8] = modStrings.get("xtagCClass02");
        mClassNames[7][9] = modStrings.get("x2tagCClass10");
        mClassNames[7][10] = modStrings.get("xtagCClass06");

        mClassNames[8][9] = modStrings.get("x2tagCClass02");
        mClassNames[8][10] = modStrings.get("tagCClass12");

        mClassNames[9][10] = modStrings.get("x2tagCClass06");
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
