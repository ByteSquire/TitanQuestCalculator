package de.bytesquire.titanquest.tqcalculator.parsers;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
//import java.nio.file.DirectoryStream;
import java.nio.file.Files;
//import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.stream.Stream;

import de.bytesquire.titanquest.tqcalculator.main.Control;
import de.bytesquire.titanquest.tqcalculator.main.SkillIcon;

public class IconsParser {

    private static final Path VANILLA_DATABASE = Paths.get(Control.DATABASES_DIR, "Vanilla", "database");
    private static final Path SKILLSWINDOW_PATH = Paths.get("records", "xpack4", "ui", "skills", "skillswindow.dbr");
    private static final Path SKILLSWINDOW_PATH_FALLBACK = Paths.get("records", "xpack3", "ui", "skills",
            "skillswindow.dbr");
    private ArrayList<File> mIconFiles;
    private LinkedHashMap<String, SkillIcon> mIcons;
    private String mModPath;
    private Path mSkillsWindowPath;

    public IconsParser(String aModPath) {
        mIconFiles = new ArrayList<>();
        mIcons = new LinkedHashMap<>();
        mModPath = aModPath;

        initPath();
        initFiles();
        initIcons();
    }

    private void initPath() {
        Path modPath = Paths.get(mModPath);
        File test = modPath.resolve(SKILLSWINDOW_PATH).toFile();
        File testFallback = modPath.resolve(SKILLSWINDOW_PATH_FALLBACK).toFile();
        if (test.exists())
            mSkillsWindowPath = Paths.get(test.getAbsolutePath());
        else if (testFallback.exists())
            mSkillsWindowPath = Paths.get(testFallback.getAbsolutePath());
        else {
            System.err.println("No skillswindow found, falling back to vanilla...");
            mSkillsWindowPath = VANILLA_DATABASE.resolve(SKILLSWINDOW_PATH);
        }
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
            Stream<String> skillsWindowLines = Files.lines(mSkillsWindowPath);

            ArrayList<String> ctrlPanes = new ArrayList<>();
            skillsWindowLines.forEach(line -> {
                String key = line.split(",", -1)[0];
                String value = line.split(",", -1)[1];
                if (key.startsWith("skillCtrlPane")) {
                    ctrlPanes.add(value);
                }
            });

            ctrlPanes.forEach(ctrlPane -> {
                File test = Paths.get(mModPath, ctrlPane).toFile();
                Path ctrlPanePath;
                if (test.exists())
                    ctrlPanePath = Paths.get(test.getAbsolutePath());
                else {
                    System.err.println(test.toString() + " not found, falling back to vanilla...");
                    ctrlPanePath = VANILLA_DATABASE.resolve(ctrlPane);
                }

                try {
                    Stream<String> ctrlPaneLines = Files.lines(ctrlPanePath);

                    ArrayList<String> skillButtons = new ArrayList<>();
                    ctrlPaneLines.forEach(line -> {
                        String key = line.split(",", -1)[0];
                        String value = line.split(",", -1)[1];
                        if (key.equals("tabSkillButtons")) {
                            skillButtons.addAll(Arrays.asList(value.split(";", -1)));
                        }
                    });

                    skillButtons.forEach(skillButton -> {
                        File skillButtonFile = Paths.get(mModPath, skillButton).toFile();

                        if (skillButtonFile.exists())
                            mIconFiles.add(skillButtonFile);
                        else
                            mIconFiles.add(VANILLA_DATABASE.resolve(skillButton).toFile());
                    });
                    ctrlPaneLines.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });

            skillsWindowLines.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        /*
         * try { DirectoryStream<Path> playerSkills = Files
         * .newDirectoryStream(Path.of(mModPath + "records/ingameui/player skills/"));
         * playerSkills.forEach((masteryPath) -> { if
         * (masteryPath.getFileName().toString().startsWith("mastery")) { try {
         * DirectoryStream<Path> masterySkills = Files.newDirectoryStream(masteryPath);
         * masterySkills.forEach((skillPath) -> { if
         * (skillPath.getFileName().toString().startsWith("skill")) { mIconFiles.add(new
         * File(skillPath.toAbsolutePath().toString())); } }); } catch (IOException e) {
         * e.printStackTrace(); } } }); } catch (IOException e) { e.printStackTrace(); }
         * 
         * try { DirectoryStream<Path> playerSkills = Files
         * .newDirectoryStream(Path.of(mModPath + "records/xpack/ui/skills/"));
         * playerSkills.forEach((masteryPath) -> { if
         * (masteryPath.getFileName().toString().startsWith("mastery")) { try {
         * DirectoryStream<Path> masterySkills = Files.newDirectoryStream(masteryPath);
         * masterySkills.forEach((skillPath) -> { if
         * (skillPath.getFileName().toString().startsWith("skill")) { mIconFiles.add(new
         * File(skillPath.toAbsolutePath().toString())); } }); } catch (IOException e) {
         * e.printStackTrace(); } } }); } catch (IOException e) { e.printStackTrace(); }
         * 
         * try { DirectoryStream<Path> playerSkills = Files
         * .newDirectoryStream(Path.of(mModPath + "records/xpack2/ui/skills/"));
         * playerSkills.forEach((masteryPath) -> { if
         * (masteryPath.getFileName().toString().startsWith("mastery")) { try {
         * DirectoryStream<Path> masterySkills = Files.newDirectoryStream(masteryPath);
         * masterySkills.forEach((skillPath) -> { if
         * (skillPath.getFileName().toString().startsWith("skill")) { mIconFiles.add(new
         * File(skillPath.toAbsolutePath().toString())); } }); } catch (IOException e) {
         * e.printStackTrace(); } } }); } catch (IOException e) { e.printStackTrace(); }
         * 
         * try { DirectoryStream<Path> playerSkills = Files
         * .newDirectoryStream(Path.of(mModPath + "records/xpack3/ui/skills/"));
         * playerSkills.forEach((masteryPath) -> { if
         * (masteryPath.getFileName().toString().startsWith("mastery")) { try {
         * DirectoryStream<Path> masterySkills = Files.newDirectoryStream(masteryPath);
         * masterySkills.forEach((skillPath) -> { if
         * (skillPath.getFileName().toString().startsWith("skill")) { mIconFiles.add(new
         * File(skillPath.toAbsolutePath().toString())); } }); } catch (IOException e) {
         * e.printStackTrace(); } } }); } catch (IOException e) { if (!(e instanceof
         * NoSuchFileException)) e.printStackTrace(); }
         * 
         * try { DirectoryStream<Path> playerSkills = Files
         * .newDirectoryStream(Path.of(mModPath + "records/xpack4/ui/skills/"));
         * playerSkills.forEach((masteryPath) -> { if
         * (masteryPath.getFileName().toString().startsWith("mastery")) { try {
         * DirectoryStream<Path> masterySkills = Files.newDirectoryStream(masteryPath);
         * masterySkills.forEach((skillPath) -> { if
         * (skillPath.getFileName().toString().startsWith("skill")) { mIconFiles.add(new
         * File(skillPath.toAbsolutePath().toString())); } }); } catch (IOException e) {
         * e.printStackTrace(); } } }); } catch (IOException e) { if (!(e instanceof
         * NoSuchFileException)) e.printStackTrace(); }
         */
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
