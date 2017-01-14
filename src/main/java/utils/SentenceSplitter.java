package utils;

import java.util.Arrays;
import java.util.List;

public class SentenceSplitter {
    private static String pattern = "[^\\p{L}]+";  //pozbywa sie calej interpunkcji i liczb ze zdania

    public static List<String> split(String line) {
        return Arrays.asList(line.split(pattern));
    }

    public static List<String> split(String line, String customPattern) {
        return Arrays.asList(line.split(customPattern));
    }
}
