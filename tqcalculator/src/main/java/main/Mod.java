package main;

import java.io.File;
import java.nio.file.Paths;
import java.security.InvalidParameterException;
import java.util.ArrayList;

import parsers.ModParser;

public class Mod {

    private ModParser mModParser;
    private ArrayList<SkillTree> mSkillTrees;
    private String mModName, mModDir;

    public Mod(String mModName, String mModDir) {
        super();
        this.mSkillTrees = new ArrayList<SkillTree>();
        this.mModName = mModName;
        this.mModDir = mModDir;

        mModParser = new ModParser(mModDir);

//        int i = 0;
        for (File skillTree : mModParser.getSkillTrees()) {
            if (!skillTree.getName().equals("QuestRewardSkillTree.dbr"))
                mSkillTrees.add(/* i++, */new SkillTree(skillTree,
                        mModDir + "database" + Paths.get("").getFileSystem().getSeparator()));
        }
    }

    public ArrayList<SkillTree> getSkillTrees() {
        return mSkillTrees;
    }

    public SkillTree getSkillTree(int index) throws InvalidParameterException {
        if ((0 > index - ModParser.COUNT_MASTERIES + ModParser.COUNT_QUEST_REWARD_TREES))
            return mSkillTrees.get(index);
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
        return getName() + "/";
    }

}
