package de.bytesquire.titanquest.tqcalculator.parsers;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.logging.Logger;
import java.util.stream.Stream;

import org.mozilla.universalchardet.UniversalDetector;

import de.bytesquire.titanquest.tqcalculator.logging.Util;
import de.bytesquire.titanquest.tqcalculator.main.Control;
import de.bytesquire.titanquest.tqcalculator.util.FileNotFoundFormatter;

public class ModParser {

    private TreeMap<Integer, File> mSkillTrees;
    private File mCharacter;
    private File mGameEngine;
    private File mPlayerLevels;
    public static final int COUNT_MASTERIES = 11;
    public static final int COUNT_QUEST_REWARD_TREES = 1;
    private Path mModDir;
    private Path mDatabaseDir;
    private LinkedHashMap<String, String> mLinks;
    private TreeMap<String, ArrayList<ArrayList<String>>> mQuestSkillPoints;
    private ArrayList<Integer> mMasteryTiers;
    private Integer mSkillPointIncrement;
    // private int mMaxLevel;
    private ArrayList<File> mQuestSkillFiles;

    private static final Logger LOGGER = Util.getLoggerForClass(ModParser.class);

    public ModParser(Path aDatabaseDir) {
        mSkillTrees = new TreeMap<>();
        mLinks = new LinkedHashMap<>();
        mMasteryTiers = new ArrayList<>();
        mQuestSkillFiles = new ArrayList<>();
        mQuestSkillPoints = new TreeMap<>((s1, s2) -> compareQuestFiles(s1, s2));
        mDatabaseDir = aDatabaseDir;
        mModDir = aDatabaseDir.getParent();

        initSkillTrees();
        initLinks();
        initQuestSkillPoints();
        initEngine();
        initPlayerLevels();
    }

    private int compareQuestFiles(String s1, String s2) {
        ArrayList<String> order = new ArrayList<>(
                Arrays.asList(new String[] { "Titan Quest", "Immortal Throne", "Ragnarok", "ALL", "CUSTOM" }));

        int index1 = 0;
        int index2 = 0;
        if (order.indexOf(s1) > -1)
            index1 = order.size() - order.indexOf(s1);
        if (order.indexOf(s2) > -1)
            index2 = order.size() - order.indexOf(s2);
        if (index1 == 0 && index2 == 0)
            return s1.compareTo(s2);
        return index2 - index1;
    }

    private void initQuestSkillPoints() {
        try {
            DirectoryStream<Path> questSkillDir = Files.newDirectoryStream(mModDir.resolve("questSkillPoints"));

            questSkillDir.forEach((textFile) -> {
                if (textFile != null) {
                    mQuestSkillFiles.add(new File(textFile.toString()));
                    String encoding;
                    try {
                        encoding = UniversalDetector.detectCharset(textFile);
                        ArrayList<ArrayList<String>> tmp = new ArrayList<>();
                        try (InputStreamReader isr = new InputStreamReader(new FileInputStream(textFile.toString()),
                                encoding);) {
                            BufferedReader skillReader = new BufferedReader(isr);
                            Stream<String> fileStream = skillReader.lines();
                            fileStream.forEach((str) -> {
                                if (!str.isBlank()) {
                                    ArrayList<String> _tmp = new ArrayList<>();
                                    if (str.split(" - ").length > 1) {
                                        _tmp.add(str.split(" - ")[0]);
                                        _tmp.add(str);
                                    } else {
                                        _tmp.add(str);
                                        _tmp.add(str);
                                    }
                                    tmp.add(_tmp);
                                }
                            });
                        } catch (Exception e) {
                            Util.logError(LOGGER, e);
                        }
                        mQuestSkillPoints.put(textFile.getFileName().toString().replace(".txt", ""), tmp);
                    } catch (IOException e1) {
                        Util.logError(LOGGER, e1);
                    }
                }
            });
        } catch (IOException e1) {
            Util.logError(LOGGER, e1);
        }
    }

    private void initEngine() {
        initEngine(false);
    }

    private boolean initEngine(boolean vanilla) {
        if (vanilla) {
            Util.logDebug(LOGGER, "No custom gameengine.dbr found, falling back to vanilla...");
            mGameEngine = Control.VANILLA_DATABASE_DIR.resolve(Paths.get("records", "game", "gameengine.dbr")).toFile();
        } else
            mGameEngine = mDatabaseDir.resolve(Paths.get("records", "game", "gameengine.dbr")).toFile();

        try (BufferedReader characterReader = new BufferedReader(new FileReader(mGameEngine));) {
            Stream<String> fileStream = characterReader.lines();
            fileStream.filter((str) -> str.startsWith("skillMasteryTierLevel")).forEach((str) -> {
                for (String tierLevel : str.split(",", -1)[1].split(";")) {
                    mMasteryTiers.add(Integer.parseInt(tierLevel));
                }
            });
        } catch (FileNotFoundException e) {
            if (!vanilla)
                return initEngine(true);
            else {
                Util.logError(LOGGER,
                        "Gameengine: " + FileNotFoundFormatter.relativizeExceptionPath(e, Control.DATABASES_DIR));
                return false;
            }
        } catch (IOException e1) {
            Util.logError(LOGGER, e1);
        }
        return true;
    }

    private void initPlayerLevels() {
        initPlayerLevels(false);
    }

    private boolean initPlayerLevels(boolean vanilla) {
        if (vanilla) {
            Util.logDebug(LOGGER, "No custom playerlevels.dbr found, falling back to vanilla...");
            mPlayerLevels = Control.VANILLA_DATABASE_DIR
                    .resolve(Paths.get("records", "creature", "pc", "playerlevels.dbr")).toFile();
        } else
            mPlayerLevels = mDatabaseDir.resolve(Paths.get("records", "creature", "pc", "playerlevels.dbr")).toFile();

        try (BufferedReader characterReader = new BufferedReader(new FileReader(mPlayerLevels));) {
            Stream<String> fileStream = characterReader.lines();
            fileStream.forEach((str) -> {
                if (str.startsWith("skillModifierPoints")) {
                    String value = str.split(",", -1)[1];
                    if (value.isEmpty())
                        Util.logError(LOGGER, "skillModifierPoints missing in " + mPlayerLevels.toString());
                    try {
                        mSkillPointIncrement = Integer.parseInt(value);
                    } catch (NumberFormatException e) {
                        Util.logError(LOGGER, "skillModifierPoints not a valid integer in " + mPlayerLevels.toString());
                    }
                }
                // Maybe implement in the future if THQ Nordic is nice
                /*
                 * if (str.startsWith("maxPlayerLevel")) { String value = str.split(",", -1)[1];
                 * if (value.isEmpty()) return; try { mSkillPointIncrement =
                 * Integer.parseInt(value); } catch (NumberFormatException e) {
                 * e.printStackTrace(); } }
                 */
            });
            if (mSkillPointIncrement == null)
                throw new FileNotFoundException("Could not read skillModifierPoints from playerlevels.dbr");
        } catch (FileNotFoundException e) {
            if (!vanilla)
                return initPlayerLevels(true);
            else {
                Util.logError(LOGGER,
                        "Playerlevels: " + FileNotFoundFormatter.relativizeExceptionPath(e, Control.DATABASES_DIR));
                return false;
            }
        } catch (IOException e1) {
            Util.logError(LOGGER, e1);
        }
        return true;
    }

    private void initSkillTrees() {
        mCharacter = mDatabaseDir.resolve(Paths.get("records", "xpack", "creatures", "pc", "malepc01.dbr")).toFile();
        try (BufferedReader characterReader = new BufferedReader(new FileReader(mCharacter));) {
            Stream<String> fileStream = characterReader.lines();
            fileStream
                    .filter((str) -> str.startsWith("skillTree")
                            && !(str.contains("Records\\Quests") || str.contains("records\\quests")
                                    || str.contains("quest") || str.contains("QuestRewardSkillTree")))
                    .forEach((str) -> {
                        String key = str.split(",", -1)[0];
                        String value = str.split(",", -1)[1];
                        if (value == null)
                            return;
                        int index = Integer.parseInt(key.substring(9, key.length()));
                        mSkillTrees.put(index, mDatabaseDir.resolve(value).toFile());
                    });
        } catch (IOException e1) {
            Util.logError(LOGGER, e1);
        }
    }

    private void initLinks() {
        File links = mModDir.resolve("links.txt").toFile();
        if (!links.exists())
            return;
        try (BufferedReader linksReader = new BufferedReader(new FileReader(links));) {
            Stream<String> fileStream = linksReader.lines();
            fileStream.filter((str) -> !str.isBlank()).forEach((str) -> {
                mLinks.put(str.split("->")[0], str.split("->")[1]);
            });
        } catch (IOException e1) {
            Util.logError(LOGGER, e1);
        }
    }

    public List<File> getSkillTrees() {
        List<File> ret = new ArrayList<File>(mSkillTrees.size());
        for (File file : mSkillTrees.values())
            ret.add(file);

        return ret;
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

    public File getPlayerLevels() {
        return mPlayerLevels;
    }

    public Integer getSkillPointIncrement() {
        return mSkillPointIncrement;
    }

    public Map<String, ArrayList<ArrayList<String>>> getQuestSkillPoints() {
        return mQuestSkillPoints;
    }

    public ArrayList<File> getQuestSkillFiles() {
        return mQuestSkillFiles;
    }

}
