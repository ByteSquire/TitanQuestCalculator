package de.bytesquire.titanquest.tqcalculator.parsers;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.stream.Stream;

import de.bytesquire.titanquest.tqcalculator.main.SkillIcon;

public class IconsParser {

    private ArrayList<File> mIconFiles;
    private LinkedHashMap<String, SkillIcon> mIcons;
    private String mModPath;

    public IconsParser(String aModPath) {
        mIconFiles = new ArrayList<>();
        mIcons = new LinkedHashMap<>();
        mModPath = aModPath;

        initFiles();
        initIcons();
    }

    private void initIcons() {
        for (File uiSkill : mIconFiles) {
            try (InputStreamReader isr = new InputStreamReader(new FileInputStream(uiSkill))) {
                BufferedReader skillReader = new BufferedReader(isr);
                Stream<String> fileStream = skillReader.lines();
                SkillIcon tmp = new SkillIcon();
                fileStream.forEach((str) -> {
                    if (str.split(",", -1)[0].equals("skillName")) {
                        tmp.setName(str.split(",", -1)[1].toLowerCase());
                    }
                    if (str.split(",", -1)[0].equals("bitmapPositionX")) {
                        tmp.setPosX(Integer.parseInt(str.split(",", -1)[1]));
                    }
                    if (str.split(",", -1)[0].equals("bitmapPositionY")) {
                        tmp.setPosY(Integer.parseInt(str.split(",", -1)[1]));
                    }
                    if (str.split(",", -1)[0].equals("isCircular")) {
                        tmp.setCircular(str.split(",", -1)[1].equals("1"));
                    }
                });
                mIcons.put(tmp.getName(), tmp);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void initFiles() {
        try {
            DirectoryStream<Path> playerSkills = Files
                    .newDirectoryStream(Path.of(mModPath + "records/ingameui/player skills/"));
            playerSkills.forEach((masteryPath) -> {
                if (masteryPath.getFileName().toString().startsWith("mastery")) {
                    try {
                        DirectoryStream<Path> masterySkills = Files.newDirectoryStream(masteryPath);
                        masterySkills.forEach((skillPath) -> {
                            if (skillPath.getFileName().toString().startsWith("skill")) {
                                mIconFiles.add(new File(skillPath.toAbsolutePath().toString()));
                            }
                        });
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            DirectoryStream<Path> playerSkills = Files
                    .newDirectoryStream(Path.of(mModPath + "records/xpack/ui/skills/"));
            playerSkills.forEach((masteryPath) -> {
                if (masteryPath.getFileName().toString().startsWith("mastery")) {
                    try {
                        DirectoryStream<Path> masterySkills = Files.newDirectoryStream(masteryPath);
                        masterySkills.forEach((skillPath) -> {
                            if (skillPath.getFileName().toString().startsWith("skill")) {
                                mIconFiles.add(new File(skillPath.toAbsolutePath().toString()));
                            }
                        });
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            DirectoryStream<Path> playerSkills = Files
                    .newDirectoryStream(Path.of(mModPath + "records/xpack2/ui/skills/"));
            playerSkills.forEach((masteryPath) -> {
                if (masteryPath.getFileName().toString().startsWith("mastery")) {
                    try {
                        DirectoryStream<Path> masterySkills = Files.newDirectoryStream(masteryPath);
                        masterySkills.forEach((skillPath) -> {
                            if (skillPath.getFileName().toString().startsWith("skill")) {
                                mIconFiles.add(new File(skillPath.toAbsolutePath().toString()));
                            }
                        });
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            DirectoryStream<Path> playerSkills = Files
                    .newDirectoryStream(Path.of(mModPath + "records/xpack3/ui/skills/"));
            playerSkills.forEach((masteryPath) -> {
                if (masteryPath.getFileName().toString().startsWith("mastery")) {
                    try {
                        DirectoryStream<Path> masterySkills = Files.newDirectoryStream(masteryPath);
                        masterySkills.forEach((skillPath) -> {
                            if (skillPath.getFileName().toString().startsWith("skill")) {
                                mIconFiles.add(new File(skillPath.toAbsolutePath().toString()));
                            }
                        });
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
        } catch (IOException e) {
            if (!(e instanceof NoSuchFileException))
                e.printStackTrace();
        }
    }

    public SkillIcon getIcon(String skillName) {
        if (mIcons.containsKey(skillName)) {
            return mIcons.get(skillName);
        } else if (mIcons.containsKey(skillName.toLowerCase())) {
            return mIcons.get(skillName.toLowerCase());
        } else {
            return null;
        }
    }

    public ArrayList<File> getIconFiles() {
        return mIconFiles;
    }

}
