package de.bytesquire.titanquest.tqcalculator.parsers;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.stream.Stream;

import org.mozilla.universalchardet.UniversalDetector;

public class AttributeNameParser {

    public static final File ATTRIBUTE_NAME_FILE = new File(
            Paths.get("").toAbsolutePath().toString() + "/resources/ui.txt");

    private static HashMap<String, String> attributeNames = new HashMap<>();

    public static void parseAttributeNames() {
        try {
            String encoding = UniversalDetector.detectCharset(ATTRIBUTE_NAME_FILE);
            try (BufferedReader attributeNamesReader = new BufferedReader(
                    new InputStreamReader(new FileInputStream(ATTRIBUTE_NAME_FILE), encoding))) {
                Stream<String> attributeNames = attributeNamesReader.lines();

                attributeNames.forEach(str -> {
                    if (!str.startsWith("//") && str.contains("=") && !str.startsWith("tag"))
                        AttributeNameParser.attributeNames.put(str.split("=")[0], str.split("=")[1]);
                });

                attributeNamesReader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String getMatch(String key) {
        return attributeNames.get(key);
    }
}
