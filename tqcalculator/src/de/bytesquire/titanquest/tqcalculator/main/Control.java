/* MIT License
 * 
 * Copyright (c) 2020 Felix Körner
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package de.bytesquire.titanquest.tqcalculator.main;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.logging.Logger;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import com.fasterxml.jackson.databind.ObjectMapper;

import de.bytesquire.titanquest.tqcalculator.logging.Util;
import de.bytesquire.titanquest.tqcalculator.parsers.AttributeNameParser;
import freemarker.template.*;

//import templates.*;

public class Control {

    public static final String URL = "https://bytesquire.github.io/TitanQuestCalculator";

    public static final Path DATABASES_DIR = Paths.get("").toAbsolutePath().resolve("resources").resolve("databases");

    public static final Path VANILLA_DATABASE_DIR = Paths.get("").toAbsolutePath()
            .resolve(Paths.get("resources", "databases", "Vanilla", "database"));

    public static final Path VANILLA_MOD_DIR = Paths.get("").toAbsolutePath()
            .resolve(Paths.get("resources", "databases", "Vanilla"));

    public static final Path REPOSITORY_DIR = Paths.get("").toAbsolutePath().getParent().getParent();

    private static final Logger LOGGER = Util.getLoggerForClass(Control.class);

    private static ArrayList<Mod> mMods = new ArrayList<>();

    private static Configuration mCfg = new Configuration(Configuration.VERSION_2_3_30);

    private static Template home, mod, mastery, skill, mod_fancy, skill_js, mastery_fancy, json_handler;

    private static Map<String, Object> rootHome, rootMod, rootMastery, rootSkill;

    private static boolean mSuccess;

    public static void main(String[] args) {
        System.err.println("Repo dir:" + REPOSITORY_DIR.toString());
        Util.init();
        configFreemarker();
        getTemplates();
        AttributeNameParser.parseAttributeNames();

        mSuccess = true;

        DirectoryStream<Path> databaseDir;
        try {
            databaseDir = Files.newDirectoryStream(DATABASES_DIR);

            databaseDir.forEach((mod) -> {
                if (mod != null && !mod.getFileName().toString().startsWith(".")
                        && !mod.getFileName().toString().endsWith("-cleaned"))
                    mMods.add(new Mod(mod.getFileName().toString(), mod));
            });
        } catch (IOException e) {
            Util.logError(LOGGER, e);
            mSuccess = false;
        } catch (Exception e) {
            Util.logError(LOGGER, e);
            return;
        }

        LinkedHashMap<String, String> links = new LinkedHashMap<>();
        for (Mod mod : mMods) {
            for (String linkString : mod.getLinks().keySet()) {
                links.put(linkString, mod.getLinks().get(linkString));
            }
        }

        rootHome = new LinkedHashMap<>();
        rootHome.put("links", links);
        rootHome.put("mods", mMods);
        try {
            Writer outHome = new FileWriter(REPOSITORY_DIR.resolve("index.md").toFile());
            home.process(rootHome, outHome);
        } catch (Exception e) {
            Util.logError(LOGGER, e);
            mSuccess = false;
        }

        for (Mod mod : mMods) {
            Path out = REPOSITORY_DIR.resolve("mods").resolve(mod.getName());
            try {
                Files.createDirectories(out);

                writeModToJSON(mod, out);
            } catch (IOException e) {
                Util.logError(LOGGER, e);
                mSuccess = false;
            }
            for (Mastery mastery : mod.getMasteries()) {
                try {
                    Path outMastery = out.resolve("Masteries").resolve(mastery.getName());
                    Files.createDirectories(outMastery);

                    outMastery = out.resolve("js");
                    Files.createDirectories(outMastery);

                    outMastery = out.resolve("images");
                    Files.createDirectories(outMastery);
                } catch (IOException e) {
                    Util.logError(LOGGER, e);
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

    private static void writeModToJSON(Mod mod, Path modPath) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            mapper.writerWithDefaultPrettyPrinter()
                    .writeValue(new File(modPath.toString() + "/" + mod.getName() + ".json"), mod);
        } catch (IOException e) {
            Util.logError(LOGGER, e);
            mSuccess = false;
        }
    }

    private static void writeTemplatesLegacy() {
        try {
            for (Mod mod : mMods) {
                Path modPath = REPOSITORY_DIR.resolve("mods").resolve(mod.getName());
                Writer outMod = new FileWriter(modPath.resolve(mod.getName() + ".html").toFile());
                rootMod = new LinkedHashMap<>();
                rootMod.put("masteries", mod.getMasteries());
                rootMod.put("name", mod.getName());
                Control.mod.process(rootMod, outMod);

                for (Mastery mastery : mod.getMasteries()) {
                    Path masteriesPath = modPath.resolve("Masteries");
                    Writer outMastery = new FileWriter(masteriesPath.resolve(mastery.getName() + ".html").toFile());
                    rootMastery = new LinkedHashMap<>();
                    rootMastery.put("skills", mastery.getSkillTiers());
                    rootMastery.put("name", mastery.getName());
                    Control.mastery.process(rootMastery, outMastery);

                    for (int i = 0; i < mastery.getSkillTiers().size(); i++)
                        for (Skill skill : mastery.getSkillTier(i)) {
                            Writer outSkill = new FileWriter(masteriesPath.resolve(mastery.getName())
                                    .resolve((skill.getName() == null) ? skill.toString()
                                            : skill.getName().replace(":", "") + ".html")
                                    .toFile());
                            rootSkill = new LinkedHashMap<>();
                            rootSkill.put("attributes", skill.getAttributes());
                            rootSkill.put("name", skill.getName());
                            rootSkill.put("description", skill.getDescription());
                            rootSkill.put("ArrayList", ArrayList.class);
                            rootSkill.put("LinkedHashMap", LinkedHashMap.class);
                            rootSkill.put("requiredWeapons", skill.getRequiredWeapons());
                            Control.skill.process(rootSkill, outSkill);
                        }
                }
            }
        } catch (IOException | TemplateException e) {
            Util.logError(LOGGER, e);
            mSuccess = false;
        }

    }

    private static void writeTemplates() {
        for (Mod mod : mMods) {
            Path modPath = REPOSITORY_DIR.resolve("mods").resolve(mod.getName());
            try {
                Writer outMod = new FileWriter(modPath.resolve("index.html").toFile());
                rootMod = new LinkedHashMap<>();
                rootMod.put("name", mod.getName());
                rootMod.put("masteries", mod.getMasteries());
                Control.mod_fancy.process(rootMod, outMod);

                Writer outMastery = new FileWriter(modPath.resolve("Masteries.html").toFile());
                rootMastery = new LinkedHashMap<>();
                rootMastery.put("name", mod.getName());
                rootMastery.put("questSkillPoints", mod.getQuestSkillPoints());
                Control.mastery_fancy.process(rootMastery, outMastery);

                Writer skillJs = new FileWriter(modPath.resolve("js").resolve("skill.js").toFile());
                rootMastery = new LinkedHashMap<>();
                rootMastery.put("name", mod.getName());
                Control.skill_js.process(rootMastery, skillJs);

                Writer json_handlerJs = new FileWriter(modPath.resolve("js").resolve("JSON_handler.js").toFile());
//                rootMastery = new LinkedHashMap<>();
                rootMastery.put("name", mod.getName());
                Control.json_handler.process(rootMastery, json_handlerJs);

                DirectoryStream<Path> javascripts = Files.newDirectoryStream(Path.of("resources/js/"), "*.js");
                javascripts.forEach(script -> {
                    try {
                        Files.copy(script, REPOSITORY_DIR.resolve("mods").resolve(mod.getName()).resolve("js")
                                .resolve(script.getFileName().toString()), StandardCopyOption.REPLACE_EXISTING);
                    } catch (IOException e) {
                        Util.logError(LOGGER, e);
                    }
                });
            } catch (IOException | TemplateException e) {
                Util.logError(LOGGER, e);
                mSuccess = false;
            }
        }
    }

    private static void getTemplates() {
        try {
            home = mCfg.getTemplate("home.ftlh");
            mod = mCfg.getTemplate("mod.ftlh");
            mod_fancy = mCfg.getTemplate("mod_fancy.ftlh");
            mastery = mCfg.getTemplate("mastery.ftlh");
            mastery_fancy = mCfg.getTemplate("mastery_fancy.ftlh");
            skill = mCfg.getTemplate("skill.ftlh");
            skill_js = mCfg.getTemplate("skill_js.ftlh");
            json_handler = mCfg.getTemplate("JSON_handler.ftlh");
        } catch (IOException e) {
            Util.logError(LOGGER, e);
            mSuccess = false;
        }
    }

    private static void configFreemarker() {
        try {
            mCfg.setDirectoryForTemplateLoading(
                    new File(Paths.get("").toAbsolutePath().toString() + "/resources/templates"));
        } catch (IOException e) {
            Util.logError(LOGGER, e);
            mSuccess = false;
        }

        mCfg.setDefaultEncoding("UTF-8");

        mCfg.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);

        mCfg.setLogTemplateExceptions(false);

        mCfg.setWrapUncheckedExceptions(true);

        mCfg.setFallbackOnNullLoopVariable(false);
    }

}
