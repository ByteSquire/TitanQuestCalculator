package de.bytesquire.titanquest.tqcalculator.main;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import de.bytesquire.titanquest.tqcalculator.logging.Util;
import de.bytesquire.titanquest.tqcalculator.util.FileNotFoundFormatter;

public class Cleaner {

    private List<Mod> mMods;

    private static final Logger LOGGER = Util.getLoggerForClass(Cleaner.class);

    public Cleaner(List<Mod> aMods) {
        mMods = aMods;

        for (int i = 0; i < mMods.size(); i++) {
            if (mMods.get(i).getModDir().endsWith("cleaned/"))
                mMods.remove(i);
        }

        cleanMods();
    }

    private void cleanPath(Path aParent, Path aPath, Path aTargetPath) {
        Path relative = aParent.relativize(aPath);
        if (aTargetPath == null)
            aTargetPath = Paths.get(aParent.toAbsolutePath().toString() + "-cleaned");
        Path originPath = aPath;
        Path targetPath = aTargetPath;
        if (originPath.toString().contains("Vanilla"))
            targetPath = Control.DATABASES_DIR.resolve("Vanilla-cleaned");
        targetPath = targetPath.resolve(relative);
        try {
            String tp = targetPath.toString();
            Path tmp = Path.of(tp.substring(0, tp.lastIndexOf(targetPath.getFileSystem().getSeparator())));
            Files.createDirectories(tmp);
            Files.copy(originPath, targetPath);
        } catch (NoSuchFileException | FileNotFoundException e) {
            Util.logError(LOGGER, FileNotFoundFormatter.relativizeExceptionPath(e, Control.DATABASES_DIR));
        } catch (FileAlreadyExistsException e) {

        } catch (IOException e) {
            Util.logError(LOGGER, e);
        }
    }

    private void cleanPath(Path aParent, Path aPath) {
        cleanPath(aParent, aPath, null);
    }

    private void cleanMods() {
        for (Mod mod : mMods) {
            cleanPath(mod.getModDir(), mod.getCharacter().toPath());
//            Path originPath = mod.getCharacter().toPath();
////            System.out.println(originPath);
//            Path relative = mod.getModDir().relativize(originPath);
//            Path targetPath = Path.of(mod.getModDir().substring(0, mod.getModDir().length() - 1)
//                    + "-cleaned/database/records/xpack/creatures/pc/malepc01.dbr");
////            System.out.println(targetPath);
//            try {
//                String tp = targetPath.toString();
//                Path tmp = Path.of(tp.substring(0, tp.lastIndexOf(targetPath.getFileSystem().getSeparator())));
//                Files.createDirectories(tmp);
//                Files.copy(originPath, targetPath);
//            } catch (IOException e) {
//                if (!(e instanceof java.nio.file.FileAlreadyExistsException))
//                    e.printStackTrace();
//            }

            Path originLinksPath = mod.getModDir().resolve("links.txt");
            cleanPath(mod.getModDir(), originLinksPath);
////          System.out.println(originLinksPath);
//            Path targetLinksPath = Path
//                    .of(mod.getModDir().substring(0, mod.getModDir().length() - 1) + "-cleaned/links.txt");
////          System.out.println(targetLinksPath);
//            try {
//                String tp = targetLinksPath.toString();
//                Path tmp = Path.of(tp.substring(0, tp.lastIndexOf(targetPath.getFileSystem().getSeparator())));
//                Files.createDirectories(tmp);
//                Files.copy(originLinksPath, targetLinksPath);
//            } catch (IOException e) {
//                if (!(e instanceof java.nio.file.FileAlreadyExistsException))
//                    e.printStackTrace();
//            }

            Path originGameEnginePath = mod.getGameEngine().toPath();
            cleanPath(mod.getModDir(), originGameEnginePath);
////          System.out.println(originLinksPath);
//            Path targetGameEnginePath = Path.of(mod.getModDir().substring(0, mod.getModDir().length() - 1)
//                    + "-cleaned/database/records/game/gameengine.dbr");
////          System.out.println(targetLinksPath);
//            try {
//                String tp = targetGameEnginePath.toString();
//                Path tmp = Path.of(tp.substring(0, tp.lastIndexOf(targetPath.getFileSystem().getSeparator())));
//                Files.createDirectories(tmp);
//                Files.copy(originGameEnginePath, targetGameEnginePath);
//            } catch (IOException e) {
//                if (!(e instanceof java.nio.file.FileAlreadyExistsException))
//                    e.printStackTrace();
//            }

            Path originPlayerLevelsPath = mod.getPlayerLevels().toPath();
            cleanPath(mod.getModDir(), originPlayerLevelsPath);
////          System.out.println(originLinksPath);
//            Path targetPlayerLevelsPath = Path.of(mod.getModDir().substring(0, mod.getModDir().length() - 1)
//                    + "-cleaned/database/records/creature/pc/playerlevels.dbr");
////          System.out.println(targetLinksPath);
//            try {
//                String tp = targetPlayerLevelsPath.toString();
//                Path tmp = Path.of(tp.substring(0, tp.lastIndexOf(targetPath.getFileSystem().getSeparator())));
//                Files.createDirectories(tmp);
//                Files.copy(originPlayerLevelsPath, targetPlayerLevelsPath);
//            } catch (IOException e) {
//                if (!(e instanceof java.nio.file.FileAlreadyExistsException))
//                    e.printStackTrace();
//            }

            cleanModStrings(mod);
            cleanIcons(mod);
            cleanQuestSkillPoints(mod);

            for (Mastery mastery : mod.getMasteries()) {
                cleanMastery(mastery, mod);
            }
        }
    }

    private void cleanIcons(Mod mod) {
        for (File f : mod.getIconsParser().getIconFiles()) {
            Path originPath = f.toPath();
            cleanPath(mod.getModDir(), originPath);
////          System.out.println(originLinksPath);
//            Path targetPath;
//            Path relative = mod.getModDir().relativize(originPath);
//            Path test = mod.getModDir();
//            if (!originPath.toString().contains("Vanilla")) {
//                targetPath = Path.of(mod.getModDir().substring(0, mod.getModDir().length() - 1) + "-cleaned/"
//                        + f.getAbsolutePath().split(mod.getName())[1]);
//            } else {
//                targetPath = Control.DATABASES_DIR.resolve("Vanilla-cleaned")
//                        .resolve(f.getAbsolutePath().split("Vanilla")[1]);
//            }
////          System.out.println(targetLinksPath);
//            try {
//                String tp = targetPath.toString();
//                Path tmp = Path.of(tp.substring(0, tp.lastIndexOf(targetPath.getFileSystem().getSeparator())));
//                Files.createDirectories(tmp);
//                Files.copy(originPath, targetPath);
//            } catch (IOException e) {
//                if (!(e instanceof java.nio.file.FileAlreadyExistsException))
//                    e.printStackTrace();
//            }
        }
    }

    private void cleanModStrings(Mod mod) {
        ArrayList<File> modStrings = mod.getMSParser().getModStrings();
        for (File modString : modStrings) {
            Path originStringsPath = modString.toPath();
            cleanPath(mod.getModDir(), originStringsPath);
////            System.out.println(originStringsPath);
//            Path targetStringsPath = Path.of(mod.getModDir().substring(0, mod.getModDir().length() - 1)
//                    + "-cleaned/text/" + modString.getName());
////            System.out.println(targetStringsPath);
//            try {
//                String tp = targetStringsPath.toString();
//                Path tmp = Path.of(tp.substring(0, tp.lastIndexOf(targetStringsPath.getFileSystem().getSeparator())));
//                Files.createDirectories(tmp);
//                Files.copy(originStringsPath, targetStringsPath);
//            } catch (IOException e) {
//                if (!(e instanceof java.nio.file.FileAlreadyExistsException))
//                    e.printStackTrace();
//            }
        }
    }

    private void cleanQuestSkillPoints(Mod mod) {
        ArrayList<File> questSkillFiles = mod.getModParser().getQuestSkillFiles();
        for (File questSkillFile : questSkillFiles) {
            Path originQuestSkillFilePath = questSkillFile.toPath();
            cleanPath(mod.getModDir(), originQuestSkillFilePath);
////            System.out.println(originStringsPath);
//            Path targetQuestSkillFilePath = Path.of(mod.getModDir().substring(0, mod.getModDir().length() - 1)
//                    + "-cleaned/questSkillPoints/" + questSkillFile.getName());
////            System.out.println(targetStringsPath);
//            try {
//                String tp = targetQuestSkillFilePath.toString();
//                Path tmp = Path
//                        .of(tp.substring(0, tp.lastIndexOf(targetQuestSkillFilePath.getFileSystem().getSeparator())));
//                Files.createDirectories(tmp);
//                Files.copy(originQuestSkillFilePath, targetQuestSkillFilePath);
//            } catch (IOException e) {
//                if (!(e instanceof java.nio.file.FileAlreadyExistsException))
//                    e.printStackTrace();
//            }
        }
    }

    private void cleanMastery(Mastery mastery, Mod mod) {
        for (String path : mastery.getMasteryFiles()) {
            Path originMasteryPath = Path.of(path);
            cleanPath(mod.getModDir(), originMasteryPath);
////        System.out.println(originPath);
//            Path targetMasteryPath = Path.of(mod.getModDir().substring(0, mod.getModDir().length() - 1) + "-cleaned/"
//                    + path.split(mod.getName())[1]);
////        System.out.println(targetPath);
//            try {
//                String tp = targetMasteryPath.toString();
//                Path tmp = Path.of(tp.substring(0, tp.lastIndexOf(targetMasteryPath.getFileSystem().getSeparator())));
//                Files.createDirectories(tmp);
//                Files.copy(originMasteryPath, targetMasteryPath);
//            } catch (IOException e) {
//                if (!(e instanceof java.nio.file.FileAlreadyExistsException))
//                    e.printStackTrace();
//            }
        }

        Path originTreePath = mastery.getSkillTree().toPath();
        cleanPath(mod.getModDir(), originTreePath);
////      System.out.println(originPath);
//        Path targetTreePath = Path.of(mod.getModDir().substring(0, mod.getModDir().length() - 1) + "-cleaned/"
//                + mastery.getSkillTree().toPath().toString().split(mod.getName())[1]);
////      System.out.println(targetPath);
//        try {
//            String tp = targetTreePath.toString();
//            Path tmp = Path.of(tp.substring(0, tp.lastIndexOf(targetTreePath.getFileSystem().getSeparator())));
//            Files.createDirectories(tmp);
//            Files.copy(originTreePath, targetTreePath);
//        } catch (IOException e) {
//            if (!(e instanceof java.nio.file.FileAlreadyExistsException))
//                e.printStackTrace();
//        }

        for (int i = 0; i < mastery.getSkillTiers().size(); i++)
            for (Skill skill : mastery.getSkillTier(i)) {
                cleanSkill(skill, mod, mastery);
            }
    }

    private void cleanSkill(Skill skill, Mod mod, Mastery mastery) {
        for (String skillFile : skill.getFiles()) {
            Path originPath = Paths.get(skillFile);
            cleanPath(mod.getModDir(), originPath);
////            System.out.println(originPath);
//            Path targetPath;
//            if (!skillFile.contains("Vanilla")) {
//                targetPath = Paths.get(mod.getModDir().substring(0, mod.getModDir().length() - 1) + "-cleaned",
//                        skillFile.split(mod.getName())[1]);
//            } else {
//                targetPath = Paths.get(Path.of(Control.DATABASES_DIR, "Vanilla") + "-cleaned",
//                        skillFile.split("Vanilla")[1]);
//            }
////            System.out.println(targetPath);
//            try {
//                String tp = targetPath.toString();
//                Path tmp = Path.of(tp.substring(0, tp.lastIndexOf(targetPath.getFileSystem().getSeparator())));
//                Files.createDirectories(tmp);
//                Files.copy(originPath, targetPath);
//            } catch (IOException e) {
//                if (!(e instanceof java.nio.file.FileAlreadyExistsException))
//                    e.printStackTrace();
//            }
        }
    }

}
