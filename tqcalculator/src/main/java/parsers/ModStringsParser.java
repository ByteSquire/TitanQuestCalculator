package parsers;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

import org.mozilla.universalchardet.UniversalDetector;

public class ModStringsParser {

    private HashMap<String, String> tags;

    public ModStringsParser(String aModStringsPath) {
        tags = new HashMap<>();

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
                                    if (str.split("=").length > 1)
                                        tags.put(str.split("=")[0], str.split("=")[1]);
                            });
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                }
            });
        } catch (IOException e1) {
            e1.printStackTrace();
        }

    }

    public Map<String, String> getTags() {
        return tags;
    }
}
