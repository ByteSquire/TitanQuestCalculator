package de.bytesquire.titanquest.tqcalculator.parsers;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedHashMap;
import java.util.stream.Stream;

import org.mozilla.universalchardet.UniversalDetector;

public class AttributeNameParser {

    public static final Path ATTRIBUTE_NAME_DIR = Path
            .of(Paths.get("").toAbsolutePath().toString() + "/resources/attributeNames");

    private static LinkedHashMap<String, String> attributeNames = new LinkedHashMap<>();

    public static void parseAttributeNames() {
        try {
            DirectoryStream<Path> files = Files.newDirectoryStream(ATTRIBUTE_NAME_DIR);
            files.forEach(file -> {
                try {
                    String encoding = UniversalDetector.detectCharset(file);
                    try (BufferedReader attributeNamesReader = new BufferedReader(
                            new InputStreamReader(new FileInputStream(file.toString()), encoding))) {
                        Stream<String> attributeNames = attributeNamesReader.lines();

                        attributeNames.forEach(str -> {
                            if (!str.startsWith("//") && str.split("=").length > 1) {
                                String key = str.split("=")[0];
                                if(key.startsWith("xtag"))
                                    key = key.substring(4, key.length());
                                if(key.startsWith("tag"))
                                    key = key.substring(3, key.length());
                                String value = str.split("=")[1];
                                AttributeNameParser.attributeNames.put(key, value);
                            }
                        });

                        attributeNamesReader.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String getMatch(String key) {
        String ret = attributeNames.get(key);
        return ret;
    }
}
