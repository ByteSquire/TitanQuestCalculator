package de.bytesquire.titanquest.tqcalculator.parsers;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import de.bytesquire.titanquest.tqcalculator.logging.Util;
import de.bytesquire.titanquest.tqcalculator.util.ListExtensions;

public class SkillTreeParser {

    private ArrayList<File> mSkills;
    private ArrayList<Boolean> mSkillIsInnate;
    private File mSkillTree;
    private Path mModDir;
    private String mMasteryTag;

    private static final Logger LOGGER = Util.getLoggerForClass(SkillTreeParser.class);

    public SkillTreeParser(File aSkillTree, Path aModDir) {
        mModDir = aModDir;
        mSkills = new ArrayList<File>();
        mSkillIsInnate = new ArrayList<Boolean>();

        mSkillTree = aSkillTree;

        initSkills();
        filterSkills();
    }

    private void filterSkills() {
        try (BufferedReader skillTreeReader = new BufferedReader(new FileReader(mSkillTree))) {
            Stream<String> fileStream = skillTreeReader.lines();

            fileStream.filter((str) -> str.startsWith("skillLevel")).forEach((str) -> {
                int index = Integer.parseInt(str.substring(10, 11));
                try {
                    index = Integer.parseInt(str.substring(10, 12));
                } catch (NumberFormatException e) {
                }

                if (index > 1) {
                    ListExtensions.setElementInListAtIndex((str.split(",", -1)[1].equals("1")), mSkillIsInnate,
                            index - 1);
                }
            });
        } catch (IOException e) {
            Util.logError(LOGGER, e);
            return;
        }

        for (int i = 1; i < mSkills.size(); i++) {
            if (mSkillIsInnate.get(i) != null && mSkillIsInnate.get(i))
                ListExtensions.setElementInListAtIndex(null, mSkills, i);
        }
    }

    private void initSkills() {
        try (BufferedReader skillTreeReader = new BufferedReader(new FileReader(mSkillTree))) {
            Stream<String> fileStream = skillTreeReader.lines();

            fileStream.filter((str) -> str.startsWith("skillName")).forEach((str) -> {
                String key = str.split(",", -1)[0];
                String skillName = str.split(",", -1)[1];

                int index = Integer.parseInt(key.substring(9, key.length()));

                if (skillName.isEmpty())
                    return;
                if (index == 1) {
                    try (Stream<String> fileStream1 = Files.lines(mModDir.resolve(skillName))) {
                        fileStream1.filter((str1) -> str1.startsWith("skillDisplayName")).forEach((str1) -> {
                            mMasteryTag = str1.split(",", -1)[1];
                        });
                    } catch (IOException e) {
                        Util.logError(LOGGER, e);
                    }
                }
                if (!skillName.contains("taunt")) {
                    ListExtensions.setElementInListAtIndex(mModDir.resolve(skillName).toFile(), mSkills, index - 1);
                }
            });
        } catch (IOException e) {
            Util.logError(LOGGER, e);
            return;
        }
    }

    public List<File> getSkills() {
        return mSkills.stream().filter(skill -> skill != null).collect(Collectors.toList());
    }

    public String getMasteryTag() {
        return mMasteryTag;
    }
}
