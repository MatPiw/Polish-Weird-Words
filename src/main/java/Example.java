import utils.ContentReader;
import utils.ContentWriter;
import utils.PolishErrorDetector;
import utils.SentenceSplitter;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class Example {

    private static final String FILE_NAME = "result";

    public static void main(String[] args) {
        File sourceFile = new File("content.txt");
        ContentReader reader = null;
        String content = null;
        try {
            reader = new ContentReader(sourceFile);
            content = reader.getFileContent();
        } catch (IOException e) {
            e.printStackTrace();
        }
        List<String> errors = SentenceSplitter.split(content);

        errors = dictionaryErrors(errors);
        System.out.println("Dictionary words removed.");
        errors = regexErrors(errors);
        System.out.println("Regex matches removed.");

        List<String> correctWords = findComplexWords(errors);
        System.out.println("Complex words detected.");
        errors.removeAll(correctWords);
        //correctWords = typingErrors(correctWords);
        //System.out.println("Typos removed.");

    }

    private static List<String> findComplexWords(List<String> words) {
        List<String> resultList = new ArrayList<>();

        for (String w: words) {
            if (PolishErrorDetector.isComplex(w))
                resultList.add(w);
        }

        try {
            saveResult(resultList, FILE_NAME + "-complex.txt");
        } catch (IOException e) {
            e.printStackTrace();
        }

        return resultList;
    }

    private static List<String> typingErrors(List<String> words) {
        List<String> resultList = new ArrayList<>();
        for (String w : words) {
            if (PolishErrorDetector.getWordSuggestions(w).size() == 0)
                resultList.add(w);
        }

        try {
            saveResult(resultList, FILE_NAME + "-no-typos.txt");
        } catch (IOException e) {
            e.printStackTrace();
        }

        return resultList;
    }

    private static List<String> regexErrors(List<String> words) {
        List<String> resultList = new ArrayList<>();
        for (String w: words) {
            if (!PolishErrorDetector.isRegexMatch(w))
                resultList.add(w);
        }

        try {
            saveResult(resultList, FILE_NAME + "-regex.txt");
        } catch (IOException e) {
            e.printStackTrace();
        }

        return resultList;
    }

    private static List<String> dictionaryErrors(List<String> words) {
        List<String> resultList = new ArrayList<>();
        try {

            Set<String> errorSet = new HashSet<>();
            for(String w: words) {
                if (!PolishErrorDetector.isWordInDictionary(w)) {
                    errorSet.add(w);
                }
            }

            resultList = new ArrayList<>(errorSet);
            Collections.sort(resultList);

            saveResult(resultList, FILE_NAME + "-no-regex.txt");

        } catch (IOException e) {
            e.printStackTrace();
        }
        return resultList;
    }

    private static void saveResult(List<String> resultList, String fileName) throws IOException {
        ContentWriter writer = new ContentWriter(fileName);
        writer.writeContentToFile(resultList);
        writer.close();
    }


}
