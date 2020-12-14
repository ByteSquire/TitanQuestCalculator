package de.bytesquire.titanquest.tqcalculator.main;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import com.fasterxml.jackson.databind.ObjectMapper;

import freemarker.template.*;

//import templates.*;

public class Control {

    public static final String URL = "https://bytesquire.github.io/TitanQuestCalculator";

    public static final String DATABASES_DIR = Paths.get("").toAbsolutePath().toString() + "/resources/databases/";

    public static final String REPOSITORY_DIR = Paths.get("").toAbsolutePath().toString().split("tqcalculator")[0];

    private static ArrayList<Mod> mMods = new ArrayList<>();

    private static Configuration mCfg = new Configuration(Configuration.VERSION_2_3_30);

    private static Template home, mod, mastery, skill, mod_fancy, mod_fancy_js, mastery_fancy, mastery_fancy_js,
            skill_fancy_js;

    private static Map<String, Object> rootHome, rootMod, rootMastery, rootSkill;

    private static boolean mSuccess;

    public static void main(String[] args) {

        configFreemarker();
        getTemplates();

        mSuccess = true;

        DirectoryStream<Path> databaseDir;
        try {
            databaseDir = Files.newDirectoryStream(Path.of(DATABASES_DIR));

            databaseDir.forEach((mod) -> {
                if (mod != null && !mod.getFileName().toString().startsWith(".")
                        && !mod.getFileName().toString().endsWith("-cleaned"))
                    mMods.add(new Mod(mod.getFileName().toString(), mod.toAbsolutePath().toString() + "/"));
            });
        } catch (IOException e) {
            e.printStackTrace();
            mSuccess = false;
        }

        HashMap<String, String> links = new HashMap<>();
        for (Mod mod : mMods) {
            for (String linkString : mod.getLinks().keySet()) {
                links.put(linkString, mod.getLinks().get(linkString));
            }
        }

        rootHome = new HashMap<>();
        rootHome.put("links", links);
        rootHome.put("mods", mMods);
        try {
            Writer outHome = new FileWriter(REPOSITORY_DIR + "index.md");
            home.process(rootHome, outHome);
        } catch (Exception e) {
            e.printStackTrace();
            mSuccess = false;
        }

        for (Mod mod : mMods) {
            try {
                Path out = Path.of(REPOSITORY_DIR + "mods/" + mod.getName());
                Files.createDirectories(out);

                writeModToJSON(mod, out);
            } catch (IOException e) {
                e.printStackTrace();
                mSuccess = false;
            }
            for (Mastery mastery : mod.getMasteries()) {
                try {
                    Path out = Path.of(REPOSITORY_DIR + "mods/" + mod.getName() + "/Masteries/" + mastery.getName());
                    Files.createDirectories(out);
                } catch (IOException e) {
                    e.printStackTrace();
                    mSuccess = false;
                }
            }
        }

        new Cleaner(mMods);

        writeTemplatesLegacy();

        writeTemplates();

        showSuccess();
    }

    private static void showSuccess() {
        JFrame tmp = new JFrame();
        if (mSuccess)
            JOptionPane.showMessageDialog(tmp, "Success", "Parse Database", JOptionPane.INFORMATION_MESSAGE);
        else
            JOptionPane.showMessageDialog(tmp, "Error", "Parse Database", JOptionPane.ERROR_MESSAGE);
        tmp.dispose();
    }

    /*
     * private static void writeMasteryToJSON(Mastery mastery, Path masteryPath) {
     * ObjectMapper mapper = new ObjectMapper(); try {
     * mapper.writerWithDefaultPrettyPrinter().writeValue(new
     * File(masteryPath.toString() + ".json"), mastery); } catch (IOException e) {
     * e.printStackTrace(); mSuccess = false; } }
     */

    private static void writeModToJSON(Mod mod, Path modPath) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            mapper.writerWithDefaultPrettyPrinter()
                    .writeValue(new File(modPath.toString() + "/" + mod.getName() + ".json"), mod);
        } catch (IOException e) {
            e.printStackTrace();
            mSuccess = false;
        }
    }

    private static void writeTemplatesLegacy() {
        try {
            for (Mod mod : mMods) {
                Writer outMod = new FileWriter(
                        REPOSITORY_DIR + "mods/" + mod.getName() + "/" + mod.getName() + ".html");
                rootMod = new HashMap<>();
                rootMod.put("masteries", mod.getMasteries());
                rootMod.put("name", mod.getName());
                Control.mod.process(rootMod, outMod);

                for (Mastery mastery : mod.getMasteries()) {
                    Writer outMastery = new FileWriter(
                            REPOSITORY_DIR + "mods/" + mod.getName() + "/Masteries/" + mastery.getName() + ".html");
                    rootMastery = new HashMap<>();
                    rootMastery.put("skills", mastery.getSkillTiers());
                    rootMastery.put("name", mastery.getName());
                    Control.mastery.process(rootMastery, outMastery);

                    for (int i = 0; i < mastery.getSkillTiers().size(); i++)
                        for (Skill skill : mastery.getSkillTier(i)) {
                            Writer outSkill = new FileWriter(REPOSITORY_DIR + "mods/" + mod.getName() + "/Masteries/"
                                    + mastery.getName() + "/"
                                    + ((skill.getName() == null) ? skill.toString() : skill.getName().replace(":", "")) + ".html");
                            rootSkill = new HashMap<>();
//                            if (skill.isBuff()) {
//                                rootSkill.put("buffName", skill.getBuff().getName());
//                                rootSkill.put("buffAttributes", skill.getBuff().getAttributes());
//                            } else
                            rootSkill.put("attributes", skill.getAttributes());
                            rootSkill.put("name", skill.getName());
                            rootSkill.put("description", skill.getDescription());
                            rootSkill.put("ArrayList", ArrayList.class);
                            rootSkill.put("requiredWeapons", skill.getRequiredWeapons());
                            Control.skill.process(rootSkill, outSkill);
                        }
                }
            }
        } catch (IOException | TemplateException e1) {
            e1.printStackTrace();
            mSuccess = false;
        }

    }

    private static void writeTemplates() {
        for (Mod mod : mMods) {
            try {
                Writer outMod = new FileWriter(REPOSITORY_DIR + "mods/" + mod.getName() + "/index.html");
                rootMod = new HashMap<>();
                rootMod.put("name", mod.getName());
                rootMod.put("masteries", mod.getMasteries());
                Control.mod_fancy.process(rootMod, outMod);
                Writer outModJs = new FileWriter(REPOSITORY_DIR + "mods/" + mod.getName() + "/js/mod.js");
                rootMod = new HashMap<>();
                Control.mod_fancy_js.process(rootMod, outModJs);

                Writer outMastery = new FileWriter(REPOSITORY_DIR + "mods/" + mod.getName() + "/Masteries.html");
                rootMastery = new HashMap<>();
                rootMastery.put("name", mod.getName());
                Control.mastery_fancy.process(rootMastery, outMastery);
                Writer outMasteryJs = new FileWriter(REPOSITORY_DIR + "mods/" + mod.getName() + "/js/mastery.js");
                rootMastery = new HashMap<>();
                rootMastery.put("name", mod.getName());
                Control.mastery_fancy_js.process(rootMastery, outMasteryJs);

                Writer outSkillJs = new FileWriter(REPOSITORY_DIR + "mods/" + mod.getName() + "/js/skill.js");
                rootMastery = new HashMap<>();
                Control.skill_fancy_js.process(rootMastery, outSkillJs);
            } catch (IOException | TemplateException e) {
                e.printStackTrace();
                mSuccess = false;
            }
        }
    }

    private static void getTemplates() {
        try {
            home = mCfg.getTemplate("home.ftlh");
            mod = mCfg.getTemplate("mod.ftlh");
            mod_fancy = mCfg.getTemplate("mod_fancy.ftlh");
            mod_fancy_js = mCfg.getTemplate("mod_fancy_js.ftlh");
            mastery = mCfg.getTemplate("mastery.ftlh");
            mastery_fancy = mCfg.getTemplate("mastery_fancy.ftlh");
            mastery_fancy_js = mCfg.getTemplate("mastery_fancy_js.ftlh");
            skill = mCfg.getTemplate("skill.ftlh");
            skill_fancy_js = mCfg.getTemplate("skill_fancy_js.ftlh");
        } catch (IOException e) {
            e.printStackTrace();
            mSuccess = false;
        }
    }

    private static void configFreemarker() {
        try {
            mCfg.setDirectoryForTemplateLoading(
                    new File(Paths.get("").toAbsolutePath().toString() + "/resources/templates"));
        } catch (IOException e) {
            e.printStackTrace();
            mSuccess = false;
        }

        mCfg.setDefaultEncoding("UTF-8");

        mCfg.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);

        mCfg.setLogTemplateExceptions(false);

        mCfg.setWrapUncheckedExceptions(true);

        mCfg.setFallbackOnNullLoopVariable(false);
    }

}
