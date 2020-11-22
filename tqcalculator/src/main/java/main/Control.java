package main;

import java.io.File;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import freemarker.*;
import freemarker.template.*;

//import templates.*;

public class Control {

    private static final String mSeparator = Paths.get("").getFileSystem().getSeparator();

    private static final String mDatabasesDir = Paths.get("").toAbsolutePath().toString() + mSeparator + "databases"
            + mSeparator;

    private static ArrayList<Mod> mMods = new ArrayList<>();

    private static Configuration mCfg = new Configuration(Configuration.VERSION_2_3_30);

    private static Template home, mod, mastery, skill;

    private static Map<String, Object> root = new HashMap();

    public static void main(String[] args) {

        configFreemarker();
        getTemplates();

        DirectoryStream<Path> databaseDir;
        try {
            databaseDir = Files.newDirectoryStream(Path.of(mDatabasesDir));

            databaseDir.forEach((mod) -> {
                if (mod != null)
                    mMods.add(new Mod(mod.getFileName().toString(), mod.toAbsolutePath().toString() + mSeparator));
            });
        } catch (IOException e) {
            e.printStackTrace();
        }

        root.put("mods", (List<Mod>) mMods);

//        for (Mod m : mMods) {
//            for (SkillTree st : m.getSkillTrees()) {
//                for (Skill s : st.getSkills()) {
//                    System.out.println(s);
//                }
//            }
//        }

        writeTemplates();

    }

    private static void writeTemplates() {
        Writer out = new OutputStreamWriter(System.out);
        try {
            home.process(root, out);
        } catch (TemplateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
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
            mCfg.setDirectoryForTemplateLoading(new File(Paths.get("").toAbsolutePath().toString() + mSeparator + "src"
                    + mSeparator + "main" + mSeparator + "resources" + mSeparator + "templates"));
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
