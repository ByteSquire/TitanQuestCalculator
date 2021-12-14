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
import java.util.logging.Logger;
import java.util.stream.Stream;

import de.bytesquire.titanquest.tqcalculator.logging.Util;
import de.bytesquire.titanquest.tqcalculator.main.Control;
import de.bytesquire.titanquest.tqcalculator.main.SkillIcon;

public class IconsParser {

    private static final Path SKILLSWINDOW_PATH = Paths.get("records", "xpack4", "ui", "skills", "skillswindow.dbr");
    private static final Path SKILLSWINDOW_PATH_FALLBACK = Paths.get("records", "xpack3", "ui", "skills",
            "skillswindow.dbr");
    private ArrayList<File> mIconFiles;
    private ArrayList<File> mAdditionalFiles;
    private LinkedHashMap<String, SkillIcon> mIcons;
    private Path mModPath;
    private Path mSkillsWindowPath;

    private static final Logger LOGGER = Util.getLoggerForClass(IconsParser.class);

    public IconsParser(Path aModPath) {
        mIconFiles = new ArrayList<>();
        mAdditionalFiles = new ArrayList<>();
        mIcons = new LinkedHashMap<>();
        mModPath = aModPath;

        initPath();
        initFiles();
        initIcons();
    }

    private void initPath() {
        File test = mModPath.resolve(SKILLSWINDOW_PATH).toFile();
        File testFallback = mModPath.resolve(SKILLSWINDOW_PATH_FALLBACK).toFile();
        if (test.exists())
            mSkillsWindowPath = Paths.get(test.getAbsolutePath());
        else if (testFallback.exists())
            mSkillsWindowPath = Paths.get(testFallback.getAbsolutePath());
        else {
            Util.logDebug(LOGGER, "No skillswindow found, falling back to vanilla...");
            mSkillsWindowPath = Control.VANILLA_DATABASE_DIR.resolve(SKILLSWINDOW_PATH);
        }
        mAdditionalFiles.add(mSkillsWindowPath.toFile());
    }

    private void initIcons() {
        for (File uiSkill : mIconFiles) {
            try (InputStreamReader isr = new InputStreamReader(new FileInputStream(uiSkill))) {
                BufferedReader skillReader = new BufferedReader(isr);
                Stream<String> fileStream = skillReader.lines();
                SkillIcon tmp = new SkillIcon();
                fileStream.forEach((str) -> {
                    String key = str.split(",", -1)[0];
                    String value = str.split(",", -1)[1];
                    if (key.equals("skillName")) {
                        tmp.setName(value.toLowerCase());
                    }
                    if (key.equals("bitmapPositionX")) {
                        tmp.setPosX(Integer.parseInt(value));
                    }
                    if (key.equals("bitmapPositionY")) {
                        tmp.setPosY(Integer.parseInt(value));
                    }
                    if (key.equals("isCircular")) {
                        tmp.setCircular(value.equals("1"));
                    }
                });
                if (tmp.getName() != null && !tmp.getName().isEmpty())
                    mIcons.put(tmp.getName(), tmp);
                else
                    Util.logError(LOGGER, "Ui element: " + uiSkill.toString() + " missing skillName");
            } catch (Exception e) {
                Util.logError(LOGGER, e);
            }
        }
    }

    private void initFiles() {
        try (Stream<String> skillsWindowLines = Files.lines(mSkillsWindowPath)) {
            ArrayList<String> ctrlPanes = new ArrayList<>();
            skillsWindowLines.forEach(line -> {
                String key = line.split(",", -1)[0];
                String value = line.split(",", -1)[1];
                if (key.startsWith("skillCtrlPane")) {
                    ctrlPanes.add(value);
                }
            });

            ctrlPanes.forEach(ctrlPane -> {
                File test = mModPath.resolve(ctrlPane).toFile();
                Path ctrlPanePath;
                if (test.exists())
                    ctrlPanePath = Paths.get(test.getAbsolutePath());
                else {
                    Util.logDebug(LOGGER, test.toString() + " not found, falling back to vanilla...");
                    ctrlPanePath = Control.VANILLA_DATABASE_DIR.resolve(ctrlPane);
                }
                mAdditionalFiles.add(ctrlPanePath.toFile());

                try (Stream<String> ctrlPaneLines = Files.lines(ctrlPanePath)) {
                    ArrayList<String> skillButtons = new ArrayList<>();
                    ctrlPaneLines.forEach(line -> {
                        String key = line.split(",", -1)[0];
                        String value = line.split(",", -1)[1];
                        if (key.equals("tabSkillButtons")) {
                            skillButtons.addAll(Arrays.asList(value.split(";", -1)));
                        }
                    });

                    skillButtons.forEach(skillButton -> {
                        File skillButtonFile = mModPath.resolve(skillButton).toFile();

                        if (skillButtonFile.exists())
                            mIconFiles.add(skillButtonFile);
                        else {
                            Util.logDebug(LOGGER, "UI element: " + skillButtonFile.toString()
                                    + " not found, falling back to vanilla...");
                            mIconFiles.add(Control.VANILLA_DATABASE_DIR.resolve(skillButton).toFile());
                        }
                    });
                } catch (IOException e) {
                    Util.logError(LOGGER, e);
                }
            });
        } catch (IOException e) {
            Util.logError(LOGGER, e);
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
        mAdditionalFiles.addAll(mIconFiles);
        return mAdditionalFiles;
    }

}
