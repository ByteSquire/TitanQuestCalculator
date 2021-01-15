package de.bytesquire.titanquest.tqcalculator.parsers;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Stream;

import org.mozilla.universalchardet.UniversalDetector;

public class ModStringsParser {

    private LinkedHashMap<String, String> tags;
    private ArrayList<File> mModStrings;

    public ModStringsParser(String aModStringsPath) {
        tags = new LinkedHashMap<>();
        mModStrings = new ArrayList<File>();

        try {
            DirectoryStream<Path> modStringsDir = Files.newDirectoryStream(Path.of(aModStringsPath));

            modStringsDir.forEach((textFile) -> {
                if (textFile != null) {
                    String encoding;
                    try {
                        encoding = UniversalDetector.detectCharset(textFile);

                        try (InputStreamReader isr = new InputStreamReader(new FileInputStream(textFile.toString()),
                                encoding);) {
                            BufferedReader skillReader = new BufferedReader(isr);
                            Stream<String> fileStream = skillReader.lines();
                            fileStream.forEach((str) -> {
                                if (!str.isBlank())
                                    if (str.split("=").length > 1) {
                                        String key = str.split("=")[0];
                                        String value = str.split("=")[1];
                                        if (value.split("//").length > 0)
                                            tags.put(key, value.split("//")[0]);
                                        else
                                            tags.put(key, value);
                                    }
                            });
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                    mModStrings.add(textFile.toFile());
                }
            });
        } catch (IOException e1) {
            e1.printStackTrace();
        }

    }

    public Map<String, String> getTags() {
        return tags;
    }

    public ArrayList<File> getModStrings() {
        return mModStrings;
    }
}
