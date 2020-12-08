package de.bytesquire.titanquest.tqcalculator.main;

import java.io.File;
import java.nio.file.Paths;
import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import de.bytesquire.titanquest.tqcalculator.parsers.IconsParser;
import de.bytesquire.titanquest.tqcalculator.parsers.ModParser;
import de.bytesquire.titanquest.tqcalculator.parsers.ModStringsParser;

@JsonIgnoreProperties({ "msparser", "character", "modDir", "links", "masteryLevel", "gameEngine", "iconsParser" })
public class Mod {

    private ModParser mModParser;
    private ModStringsParser mMSParser;
    private IconsParser mIconsParser;
    private ArrayList<Mastery> mMasteries;
    private HashMap<Integer, String> mMappedMasteries;
    private String mModName, mModDir;
    private Map<String, String> mLinks;
    private File mCharacter;
    private File mGameEngine;
    private ArrayList<Integer> mMasteryTiers;

    public Mod(String aModName, String aModDir) {
        if (aModName == null)
            return;
        mMasteries = new ArrayList<Mastery>();
        mMappedMasteries = new HashMap<Integer, String>();
        mModName = aModName;
        mModDir = aModDir;
        mMSParser = new ModStringsParser(aModDir + "/text/");
        mIconsParser = new IconsParser(aModDir + "/database/");
        mModParser = new ModParser(aModDir);
        mLinks = mModParser.getLinks();
        mCharacter = mModParser.getCharacter();
        mMasteryTiers = mModParser.getMasteryTiers();
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

    public HashMap<Integer, String> getMappedMasteries() {
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
}
