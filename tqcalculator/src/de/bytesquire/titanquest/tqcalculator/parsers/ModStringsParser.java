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
import java.util.logging.Logger;
import java.util.stream.Stream;

import org.mozilla.universalchardet.UniversalDetector;

import de.bytesquire.titanquest.tqcalculator.logging.Util;
import de.bytesquire.titanquest.tqcalculator.main.Control;

public class ModStringsParser {

    private LinkedHashMap<String, String> tags;
    private LinkedHashMap<String, String> defaultTags;
    private ArrayList<File> mModStrings;

    private static final Logger LOGGER = Util.getLoggerForClass(ModStringsParser.class);

    public ModStringsParser(Path aModStringsPath) {
        tags = new LinkedHashMap<>();
        mModStrings = new ArrayList<File>();
        defaultTags = new LinkedHashMap<>();

        tags = parseTextFile(aModStringsPath, true);
        defaultTags = parseTextFile(Control.VANILLA_MOD_DIR.resolve("text"));
    }

    private LinkedHashMap<String, String> parseTextFile(Path filePath) {
        return parseTextFile(filePath, false);
    }

    private LinkedHashMap<String, String> parseTextFile(Path filePath, boolean save) {
        LinkedHashMap<String, String> _tags = new LinkedHashMap<String, String>();
        try (DirectoryStream<Path> modStringsDir = Files.newDirectoryStream(filePath)) {
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
                                            _tags.put(key, value.split("//")[0]);
                                        else
                                            _tags.put(key, value);
                                    }
                            });
                        } catch (Exception e) {
                            Util.logError(LOGGER, e);
                        }
                    } catch (IOException e1) {
                        Util.logError(LOGGER, e1);
                    }
                    if (save)
                        mModStrings.add(textFile.toFile());
                }
            });
        } catch (IOException e1) {
            Util.logError(LOGGER, e1);
        }
        return _tags;
    }

    public ArrayList<File> getModStrings() {
        return mModStrings;
    }

    public String getMatch(String element) {
        if (tags.containsKey(element))
            return tags.get(element);
        else
            return defaultTags.get(element);
    }
}
