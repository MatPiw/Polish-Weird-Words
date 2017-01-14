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
        List<String> errors = onlyDictionaryErrors();
        System.out.println("Dictionary words removed.");
        errors = typingErrors(errors);
        System.out.println("Typos removed.");
        errors = regexErrors(errors);
        System.out.println("Names removed.");


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
            if (!PolishErrorDetector.isName(w))
                resultList.add(w);
        }

        try {
            saveResult(resultList, FILE_NAME + "-no-names.txt");
        } catch (IOException e) {
            e.printStackTrace();
        }

        return resultList;
    }

    private static List<String> onlyDictionaryErrors() {
        List<String> resultList = new ArrayList<>();
        try {
            File sourceFile = new File("content.txt");
            System.out.println(sourceFile.getAbsolutePath());
            ContentReader reader = new ContentReader(sourceFile);
            String content = reader.getFileContent();
            List<String> words = SentenceSplitter.split(content);
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
