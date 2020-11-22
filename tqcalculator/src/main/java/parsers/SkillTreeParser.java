package parsers;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.stream.Stream;

public class SkillTreeParser {

    private ArrayList<File> mSkills;
    public static final int COUNT_SKILLS = 26;
    private File mSkillTree;
    private String mModDir;

    public SkillTreeParser(File aSkillTree, String aModDir) {
        mModDir = aModDir;
        mSkills = new ArrayList<File>();

        mSkillTree = aSkillTree;

        initSkills();
    }

    private void initSkills() {
        try (BufferedReader skillTreeReader = new BufferedReader(new FileReader(mSkillTree));) {
            Stream<String> fileStream = skillTreeReader.lines();
            fileStream.filter((str) -> str.startsWith("skillName")).forEach((str) -> {
                int index = Integer.parseInt(str.substring(9, 10));
                if (index == 1 || index == 2) {
                    try {
                        index = Integer.parseInt(str.substring(9, 11));
                    } catch (NumberFormatException e) {
                    }
                }
                mSkills.add(/* index - 1, */new File(mModDir + str.split(",")[1]));
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
        return mSkills;
    }
}
