package de.bytesquire.titanquest.tqcalculator.main;

import java.io.File;
import java.nio.file.Paths;
import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import de.bytesquire.titanquest.tqcalculator.parsers.ModParser;
import de.bytesquire.titanquest.tqcalculator.parsers.ModStringsParser;

public class Mod {

    private ModParser mModParser;
    private ModStringsParser mMSParser;
    private ArrayList<Mastery> mMasteries;
    private String mModName, mModDir;
    private Map<String, String> mLinks;
    private File mCharacter;

    public Mod(String aModName, String aModDir) {
        if (aModName == null)
            return;
        mMasteries = new ArrayList<Mastery>();
        mModName = aModName;
        mModDir = aModDir;
        mMSParser = new ModStringsParser(aModDir + "/text/");

        mModParser = new ModParser(aModDir);
        mLinks = mModParser.getLinks();
        mCharacter = mModParser.getCharacter();

        for (File skillTree : mModParser.getSkillTrees()) {
            if (!skillTree.getName().equals("QuestRewardSkillTree.dbr"))
                mMasteries.add(/* i++, */new Mastery(skillTree,
                        mModDir + "database" + Paths.get("").getFileSystem().getSeparator(), mModName, mMSParser));
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

    public String getUrl() {
        return Control.URL + "/mods/" + getName() + ".html";
    }

    public Map<String, String> getLinks() {
        return mLinks;
    }

    public File getCharacter() {
        return mCharacter;
    }

}
