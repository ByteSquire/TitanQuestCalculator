package main;

import java.io.File;
import java.nio.file.Paths;
import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.List;

import parsers.ModParser;

public class Mod {

    private ModParser mModParser;
    private ArrayList<Mastery> mMasteries;
    private String mModName, mModDir;

    public Mod(String aModName, String aModDir) {
        if (aModName == null)
            return;
        mMasteries = new ArrayList<Mastery>();
        mModName = aModName;
        mModDir = aModDir;

        mModParser = new ModParser(aModDir);

//        int i = 0;
        for (File skillTree : mModParser.getSkillTrees()) {
            if (!skillTree.getName().equals("QuestRewardSkillTree.dbr"))
                mMasteries.add(/* i++, */new Mastery(skillTree,
                        aModDir + "database" + Paths.get("").getFileSystem().getSeparator(), aModName));
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

    public String getUrl() {
        return Control.URL + "/mods/" + getName() + ".html";
    }

}
