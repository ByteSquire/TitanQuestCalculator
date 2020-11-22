package parsers;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.stream.Stream;

import main.SkillAttribute;

public class SkillParser {

    private HashMap<String, SkillAttribute<?>> mAttributes;
    public static final int COUNT_SKILLS = 26;
    private File mSkill;

    public SkillParser(File aSkill) {

        if (aSkill == null)
            return;

        mAttributes = new HashMap<>();

        mSkill = aSkill;

        initSkill();
    }

    private void initSkill() {
        try (BufferedReader skillReader = new BufferedReader(new FileReader(mSkill));) {
            Stream<String> fileStream = skillReader.lines();
            fileStream.forEach((str) -> {
                try {
                    Integer value = Integer.parseInt(str.split(",")[1]);
                    if (value == 0)
                        return;
                    mAttributes.put(str.split(",")[0], new SkillAttribute<Integer>(value));
                    return;
                } catch (Exception e) {
                }
                try {
                    Double value = Double.parseDouble(str.split(",")[1]);
                    if (value == 0.0)
                        return;
                    mAttributes.put(str.split(",")[0], new SkillAttribute<Double>(value));
                    return;
                } catch (Exception e) {
                }
                try {
                    if (str.split(",")[1].split(";").length == 0)
                        return;
                    ArrayList<Double> value = new ArrayList<Double>();
                    for (String e : str.split(",")[1].split(";")) {
                        value.add(Double.parseDouble(e));
                    }
                    mAttributes.put(str.split(",")[0], new SkillAttribute<ArrayList<Double>>(value));
                    return;
                } catch (Exception e) {
                }
            });
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }

    public HashMap<String, SkillAttribute<?>> getAttributes() {
        return mAttributes;
    }
}
