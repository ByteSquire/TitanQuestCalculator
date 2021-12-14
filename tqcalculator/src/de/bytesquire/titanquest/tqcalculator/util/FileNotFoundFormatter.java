package de.bytesquire.titanquest.tqcalculator.util;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FileNotFoundFormatter {

    public static String relativizeExceptionPath(IOException aException, Path aDatabaseDir) {
        String ret;
        String msg = aException.getMessage();
        String fullPath = msg.contains("(") ? msg.substring(0, msg.indexOf(" (")) : msg;
        Path relativePath = aDatabaseDir.relativize(Paths.get(fullPath));
        ret = relativePath.toString();
        return ret + " not found";
    }
}
