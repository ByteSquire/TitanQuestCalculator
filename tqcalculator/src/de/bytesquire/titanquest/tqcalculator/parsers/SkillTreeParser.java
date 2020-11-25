package de.bytesquire.titanquest.tqcalculator.parsers;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.stream.Stream;

public class SkillTreeParser {

    private ArrayList<File> mSkills;
    private ArrayList<Boolean> mSkillIsInnate;
    public static final int COUNT_SKILLS = 50;
    private File mSkillTree;
    private String mModDir;
    private String mMasteryTag;

    public SkillTreeParser(File aSkillTree, String aModDir) {
        mModDir = aModDir;
        mSkills = new ArrayList<File>();
        mSkillIsInnate = new ArrayList<Boolean>();

        for (int i = 0; i < COUNT_SKILLS; i++) {
            mSkills.add(null);
            mSkillIsInnate.add(null);
        }

        mSkillTree = aSkillTree;

        initSkills();
        filterSkills();
    }

    private void filterSkills() {
        try (BufferedReader skillTreeReader = new BufferedReader(new FileReader(mSkillTree));) {
            Stream<String> fileStream = skillTreeReader.lines();

            fileStream.filter((str) -> str.startsWith("skillLevel")).forEach((str) -> {
                int index = Integer.parseInt(str.substring(10, 11));
                try {
                    index = Integer.parseInt(str.substring(10, 12));
                } catch (NumberFormatException e) {
                }

                if (index > 1) {
                    mSkillIsInnate.set(index - 1, (str.split(",")[1].equals("1")));
                }
            });
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return;
        } catch (IOException e1) {
            e1.printStackTrace();
            return;
        }

        for (int i = 1; i < mSkills.size(); i++) {
            if (mSkillIsInnate.get(i) != null && mSkillIsInnate.get(i))
                mSkills.set(i, null);
        }
    }

    private void initSkills() {
        try (BufferedReader skillTreeReader = new BufferedReader(new FileReader(mSkillTree));) {
            Stream<String> fileStream = skillTreeReader.lines();

            fileStream.filter((str) -> str.startsWith("skillName")).forEach((str) -> {
                int index = Integer.parseInt(str.substring(9, 10));
                try {
                    index = Integer.parseInt(str.substring(9, 11));
                } catch (NumberFormatException e) {
                }

                if (index == 1) {
                    try {
                        BufferedReader masterySkillReader = new BufferedReader(
                                new FileReader(new File(mModDir + str.split(",")[1])));
                        Stream<String> fileStream1 = masterySkillReader.lines();
                        fileStream1.filter((str1) -> str1.startsWith("skillDisplayName")).forEach((str1) -> {
                            mMasteryTag = str1.split(",")[1];
                        });
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }
                mSkills.set(index - 1, new File(mModDir + str.split(",")[1]));
            });
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return;
        } catch (IOException e1) {
            e1.printStackTrace();
            return;
        }
    }

    public ArrayList<File> getSkills() {
        ArrayList<File> ret = new ArrayList<>();
        for (File file : mSkills)
            if (file != null)
                ret.add(file);
        return ret;
    }

    public String getMasteryTag() {
        return mMasteryTag;
    }
}
