package de.bytesquire.titanquest.tqcalculator.parsers;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Stream;

import de.bytesquire.titanquest.tqcalculator.main.Control;

public class ModParser {

    private static final String SEPARATOR = Paths.get("").getFileSystem().getSeparator();

    private ArrayList<File> mSkillTrees;
    private File mCharacter;
    private File mGameEngine;
    public static final int COUNT_MASTERIES = 10;
    public static final int COUNT_QUEST_REWARD_TREES = 1;
    private String mModdir;
    private LinkedHashMap<String, String> mLinks;
    private ArrayList<Integer> mMasteryTiers;

    public ModParser(String aModdir) {
        mSkillTrees = new ArrayList<>();
        mLinks = new LinkedHashMap<>();
        mMasteryTiers = new ArrayList<>();
        mModdir = aModdir;

        initSkillTrees();
        initLinks();
        initEngine();
    }

    private void initEngine() {
        mGameEngine = new File(mModdir + "database/records/game/gameengine.dbr");
        try (BufferedReader characterReader = new BufferedReader(new FileReader(mGameEngine));) {
            Stream<String> fileStream = characterReader.lines();
            fileStream.filter((str) -> str.startsWith("skillMasteryTierLevel")).forEach((str) -> {
                for (String tierLevel : str.split(",")[1].split(";")) {
                    mMasteryTiers.add(Integer.parseInt(tierLevel));
                }
            });
        } catch (FileNotFoundException e) {
            if (!initEngine(true))
                e.printStackTrace();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }

    private boolean initEngine(boolean vanilla) {
        mGameEngine = new File(Control.DATABASES_DIR + "Vanilla/database/records/game/gameengine.dbr");
        try (BufferedReader characterReader = new BufferedReader(new FileReader(mGameEngine));) {
            Stream<String> fileStream = characterReader.lines();
            fileStream.filter((str) -> str.startsWith("skillMasteryTierLevel")).forEach((str) -> {
                for (String tierLevel : str.split(",")[1].split(";")) {
                    mMasteryTiers.add(Integer.parseInt(tierLevel));
                }
            });
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return false;
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        return true;
    }

    private void initSkillTrees() {
        mCharacter = new File(mModdir + "database/records/xpack/creatures/pc/malepc01.dbr");
        try (BufferedReader characterReader = new BufferedReader(new FileReader(mCharacter));) {
            Stream<String> fileStream = characterReader.lines();
            fileStream.filter((str) -> str.startsWith("skillTree")).forEach((str) -> {
                int index = Integer.parseInt(str.substring(9, 10));
                if (index == 1) {
                    try {
                        index = Integer.parseInt(str.substring(9, 11));
                    } catch (NumberFormatException e) {
                    }
                }
                mSkillTrees.add(/* index - 1, */new File(mModdir + "database" + SEPARATOR + str.split(",")[1]));
            });
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }

    private void initLinks() {
        File links = new File(mModdir + "links.txt");
        if (!links.exists())
            return;
        try (BufferedReader linksReader = new BufferedReader(new FileReader(links));) {
            Stream<String> fileStream = linksReader.lines();
            fileStream.filter((str) -> !str.isBlank()).forEach((str) -> {
                mLinks.put(str.split("->")[0], str.split("->")[1]);
            });
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }

    public ArrayList<File> getSkillTrees() {
        return mSkillTrees;
    }

    public Map<String, String> getLinks() {
        return mLinks;
    }

    public File getCharacter() {
        return mCharacter;
    }

    public ArrayList<Integer> getMasteryTiers() {
        return mMasteryTiers;
    }

    public File getGameEngine() {
        return mGameEngine;
    }

}
