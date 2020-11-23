package main;

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

import freemarker.template.*;

//import templates.*;

public class Control {

    public static final String URL = "https://bytesquire.github.io/TitanQuestCalculator";

    public static final String DATABASES_DIR = Paths.get("").toAbsolutePath().toString() + "/databases/";

    private static ArrayList<Mod> mMods = new ArrayList<>();

    private static Configuration mCfg = new Configuration(Configuration.VERSION_2_3_30);

    private static Template home, mod, mastery, skill;

    private static Map<String, Object> rootHome, rootMod, rootMastery, rootSkill;

    public static void main(String[] args) {

        configFreemarker();
        getTemplates();

        DirectoryStream<Path> databaseDir;
        try {
            databaseDir = Files.newDirectoryStream(Path.of(DATABASES_DIR));

            databaseDir.forEach((mod) -> {
                if (mod != null)
                    mMods.add(new Mod(mod.getFileName().toString(), mod.toAbsolutePath().toString() + "/"));
            });
        } catch (IOException e) {
            e.printStackTrace();
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
            Writer outHome = new FileWriter("../index.md");
            home.process(rootHome, outHome);
        } catch (Exception e) {
            e.printStackTrace();
        }

        for (Mod mod : mMods) {
            for (Mastery mastery : mod.getMasteries()) {
                try {
                    Path out = Path.of("../mods/" + mod.getName() + "/" + mastery.getName());
                    Files.createDirectories(out);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        writeTemplates();

    }

    private static void writeTemplates() {
        try {
            for (Mod mod : mMods) {
                Writer outMod = new FileWriter("../mods/" + mod.getName() + ".html");
                rootMod = new HashMap<>();
                rootMod.put("masteries", mod.getMasteries());
                rootMod.put("name", mod.getName());
                Control.mod.process(rootMod, outMod);
                for (Mastery mastery : mod.getMasteries()) {
                    Writer outMastery = new FileWriter("../mods/" + mod.getName() + "/" + mastery.getName() + ".html");
                    rootMastery = new HashMap<>();
                    rootMastery.put("skills", mastery.getSkills());
                    rootMastery.put("name", mastery.getName());
                    Control.mastery.process(rootMastery, outMastery);
                    for (Skill skill : mastery.getSkills()) {
                        Writer outSkill = new FileWriter(
                                "../mods/" + mod.getName() + "/" + mastery.getName() + "/" + skill.getName() + ".html");
                        rootSkill = new HashMap<>();
                        if (skill.isBuff()) {
                            rootSkill.put("buffName", skill.getBuff().getName());
                            rootSkill.put("buffAttributes", skill.getBuff().getAttributes());
                        } else
                            rootSkill.put("attributes", skill.getAttributes());
                        rootSkill.put("name", skill.getName());
                        Control.skill.process(rootSkill, outSkill);
                    }
                }
            }
        } catch (IOException | TemplateException e1) {
            e1.printStackTrace();
        }

    }

    private static void getTemplates() {
        try {
            home = mCfg.getTemplate("home.ftlh");
            mod = mCfg.getTemplate("mod.ftlh");
            mastery = mCfg.getTemplate("mastery.ftlh");
            skill = mCfg.getTemplate("skill.ftlh");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void configFreemarker() {
        try {
            mCfg.setDirectoryForTemplateLoading(
                    new File(Paths.get("").toAbsolutePath().toString() + "/src/main/resources/templates"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        mCfg.setDefaultEncoding("UTF-8");

        mCfg.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);

        mCfg.setLogTemplateExceptions(false);

        mCfg.setWrapUncheckedExceptions(true);

        mCfg.setFallbackOnNullLoopVariable(false);
    }

}
