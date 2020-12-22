package de.bytesquire.titanquest.tqcalculator.main;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class Cleaner {

    private List<Mod> mMods;

    public Cleaner(List<Mod> aMods) {
        mMods = aMods;

        for (int i = 0; i < mMods.size(); i++) {
            if (mMods.get(i).getModDir().endsWith("cleaned/"))
                mMods.remove(i);
        }

        cleanMods();
    }

    private void cleanMods() {
        for (Mod mod : mMods) {
            Path originPath = mod.getCharacter().toPath();
//            System.out.println(originPath);
            Path targetPath = Path.of(mod.getModDir().substring(0, mod.getModDir().length() - 1)
                    + "-cleaned/database/records/xpack/creatures/pc/malepc01.dbr");
//            System.out.println(targetPath);
            try {
                String tp = targetPath.toString();
                Path tmp = Path.of(tp.substring(0, tp.lastIndexOf(targetPath.getFileSystem().getSeparator())));
                Files.createDirectories(tmp);
                Files.copy(originPath, targetPath);
            } catch (IOException e) {
                if (!(e instanceof java.nio.file.FileAlreadyExistsException))
                    e.printStackTrace();
            }

            Path originLinksPath = Path.of(mod.getModDir() + "links.txt");
//          System.out.println(originLinksPath);
            Path targetLinksPath = Path
                    .of(mod.getModDir().substring(0, mod.getModDir().length() - 1) + "-cleaned/links.txt");
//          System.out.println(targetLinksPath);
            try {
                String tp = targetLinksPath.toString();
                Path tmp = Path.of(tp.substring(0, tp.lastIndexOf(targetPath.getFileSystem().getSeparator())));
                Files.createDirectories(tmp);
                Files.copy(originLinksPath, targetLinksPath);
            } catch (IOException e) {
                if (!(e instanceof java.nio.file.FileAlreadyExistsException))
                    e.printStackTrace();
            }

            Path originGameEnginePath = mod.getGameEngine().toPath();
//          System.out.println(originLinksPath);
            Path targetGameEnginePath = Path.of(mod.getModDir().substring(0, mod.getModDir().length() - 1)
                    + "-cleaned/database/records/game/gameengine.dbr");
//          System.out.println(targetLinksPath);
            try {
                String tp = targetGameEnginePath.toString();
                Path tmp = Path.of(tp.substring(0, tp.lastIndexOf(targetPath.getFileSystem().getSeparator())));
                Files.createDirectories(tmp);
                Files.copy(originGameEnginePath, targetGameEnginePath);
            } catch (IOException e) {
                if (!(e instanceof java.nio.file.FileAlreadyExistsException))
                    e.printStackTrace();
            }

            cleanModStrings(mod);
            cleanIcons(mod);

            for (Mastery mastery : mod.getMasteries()) {
                cleanMastery(mastery, mod);
            }
        }
    }

    private void cleanIcons(Mod mod) {
        for (File f : mod.getIconsParser().getIconFiles()) {
            Path originPath = f.toPath();
//          System.out.println(originLinksPath);
            Path targetPath = Path.of(mod.getModDir().substring(0, mod.getModDir().length() - 1) + "-cleaned/"
                    + f.getAbsolutePath().split(mod.getName())[1]);
//          System.out.println(targetLinksPath);
            try {
                String tp = targetPath.toString();
                Path tmp = Path.of(tp.substring(0, tp.lastIndexOf(targetPath.getFileSystem().getSeparator())));
                Files.createDirectories(tmp);
                Files.copy(originPath, targetPath);
            } catch (IOException e) {
                if (!(e instanceof java.nio.file.FileAlreadyExistsException))
                    e.printStackTrace();
            }
        }
    }

    private void cleanModStrings(Mod mod) {
        ArrayList<File> modStrings = mod.getMSParser().getModStrings();
        for (File modString : modStrings) {
            Path originStringsPath = modString.toPath();
//            System.out.println(originStringsPath);
            Path targetStringsPath = Path.of(mod.getModDir().substring(0, mod.getModDir().length() - 1)
                    + "-cleaned/text/" + modString.getName());
//            System.out.println(targetStringsPath);
            try {
                String tp = targetStringsPath.toString();
                Path tmp = Path.of(tp.substring(0, tp.lastIndexOf(targetStringsPath.getFileSystem().getSeparator())));
                Files.createDirectories(tmp);
                Files.copy(originStringsPath, targetStringsPath);
            } catch (IOException e) {
                if (!(e instanceof java.nio.file.FileAlreadyExistsException))
                    e.printStackTrace();
            }
        }
    }

    private void cleanMastery(Mastery mastery, Mod mod) {
        Path originMasteryPath = mastery.getMastery().toPath();
//        System.out.println(originPath);
        Path targetMasteryPath = Path.of(mod.getModDir().substring(0, mod.getModDir().length() - 1) + "-cleaned/"
                + mastery.getMastery().toPath().toString().split(mod.getName())[1]);
//        System.out.println(targetPath);
        try {
            String tp = targetMasteryPath.toString();
            Path tmp = Path.of(tp.substring(0, tp.lastIndexOf(targetMasteryPath.getFileSystem().getSeparator())));
            Files.createDirectories(tmp);
            Files.copy(originMasteryPath, targetMasteryPath);
        } catch (IOException e) {
            if (!(e instanceof java.nio.file.FileAlreadyExistsException))
                e.printStackTrace();
        }

        Path originTreePath = mastery.getSkillTree().toPath();
//      System.out.println(originPath);
        Path targetTreePath = Path.of(mod.getModDir().substring(0, mod.getModDir().length() - 1) + "-cleaned/"
                + mastery.getSkillTree().toPath().toString().split(mod.getName())[1]);
//      System.out.println(targetPath);
        try {
            String tp = targetTreePath.toString();
            Path tmp = Path.of(tp.substring(0, tp.lastIndexOf(targetMasteryPath.getFileSystem().getSeparator())));
            Files.createDirectories(tmp);
            Files.copy(originTreePath, targetTreePath);
        } catch (IOException e) {
            if (!(e instanceof java.nio.file.FileAlreadyExistsException))
                e.printStackTrace();
        }

        for (int i = 0; i < mastery.getSkillTiers().size(); i++)
            for (Skill skill : mastery.getSkillTier(i)) {
                cleanSkill(skill, mod, mastery);
            }
    }

    private void cleanSkill(Skill skill, Mod mod, Mastery mastery) {
        for (File skillFile : skill.getFiles()) {
            Path originPath = skillFile.toPath();
//            System.out.println(originPath);
            Path targetPath = Path.of(mod.getModDir().substring(0, mod.getModDir().length() - 1) + "-cleaned/"
                    + skillFile.toPath().toString().split(mod.getName())[1]);
//            System.out.println(targetPath);
            try {
                String tp = targetPath.toString();
                Path tmp = Path.of(tp.substring(0, tp.lastIndexOf(targetPath.getFileSystem().getSeparator())));
                Files.createDirectories(tmp);
                Files.copy(originPath, targetPath);
            } catch (IOException e) {
                if (!(e instanceof java.nio.file.FileAlreadyExistsException))
                    e.printStackTrace();
            }
        }
    }

}
