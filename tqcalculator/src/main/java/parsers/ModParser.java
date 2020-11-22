package parsers;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.stream.Stream;

public class ModParser {

    private static final String SEPARATOR = Paths.get("").getFileSystem().getSeparator();

    private ArrayList<File> mSkillTrees;
    public static final int COUNT_MASTERIES = 10;
    public static final int COUNT_QUEST_REWARD_TREES = 1;
    private String mModdir;

    public ModParser(String aModdir) {
        mSkillTrees = new ArrayList<>();

        mModdir = aModdir;

        initSkillTrees();
    }

    private void initSkillTrees() {
        File character = new File(mModdir + "database" + SEPARATOR + "records" + SEPARATOR + "xpack" + SEPARATOR
                + "creatures" + SEPARATOR + "pc" + SEPARATOR + "malepc01.dbr");
        try (BufferedReader characterReader = new BufferedReader(new FileReader(character));) {
            Stream<String> fileStream = characterReader.lines();
            fileStream.filter((str) -> str.startsWith("skillTree")).forEach((str) -> {
                int index = Integer.parseInt(str.substring(9, 10));
                if (index == 1) {
                    try {
                        index = Integer.parseInt(str.substring(9, 11));
                    } catch (NumberFormatException e) {
                    }
                }
                mSkillTrees.add(/*index - 1, */new File(mModdir + "database" + SEPARATOR + str.split(",")[1]));
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

}
