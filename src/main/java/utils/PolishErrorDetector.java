package utils;

import morfologik.speller.Speller;
import morfologik.stemming.WordData;
import morfologik.stemming.polish.PolishStemmer;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class PolishErrorDetector {
    private static final int MAX_DISTANCE = 1;
    private static PolishStemmer stemmer = new PolishStemmer();
    private static Speller speller = new Speller(stemmer.getDictionary(), MAX_DISTANCE);

    private static List<String> regularExpressions;

    private static List<String> prefixes;


    static {
        try {
            ContentReader reader = new ContentReader("regexes.txt");
            String content = reader.getFileContent();
            regularExpressions = SentenceSplitter.split(content, "\\n");
            System.out.println("Found " + regularExpressions.size() + " regular expressions in file.");

            for (String regex: regularExpressions)
                System.out.println(regex);

            reader.close();

            reader = new ContentReader("prefixes.txt");
            content = reader.getFileContent();
            prefixes = SentenceSplitter.split(content, "\\n");
            System.out.println("Found " + prefixes.size() + " prefixes in file.");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static boolean isWordInDictionary(String word) {
        return !stemmer.lookup(word.toLowerCase()).isEmpty();
    }

    public static List<String> getWordBaseForms(String word) {
        List<String> results = new ArrayList<>();
        List<WordData> words = stemmer.lookup(word.toLowerCase());
        words.forEach(w -> results.add(w.getStem().toString()));
        return results;
    }

    public static List<String> getWordSuggestions(String word) {
        return speller.findReplacements(word);
    }

    public static boolean isRegexMatch(String word) {
        for (String regex : regularExpressions)
            if (Pattern.matches(regex, word))
                return true;
        return false;
    }

    //check if the word has a prefix
    public static boolean isComplex(String word) {
        for (String prefix : prefixes) {
            if (word.startsWith(prefix)) {
                String subword = word.substring(prefix.length(), word.length());
                if (isWordInDictionary(subword) && subword.length() > 4)
                    return true;
            }
        }
        return false;
    }
}
