package utils;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/*
This class is for retrieving text data from large corpus
(in this case NKJP corpus). The sctucture of the corpus is:
root directory -> sub directories -> group of files
(the same files in each sub folder). You have to localize which file
and which XML tag contains raw text, and set it to proper variables.
Whole content will be returned in one string object.
*/

public class CorpusTextRetriever {

    private final String rootDirectory;
    private List<String> subDirectories;
    private ContentReader reader;
    private int subDirsCount = 0;

    //file name with text
    private static final String CORPUS_TEXT_FILE_NAME = "text.xml";
    //a XML tag from which the text will be retrieved
    private final String textTag = "ab";

    public CorpusTextRetriever(String rootDirectory) {
        this.rootDirectory = rootDirectory;
        getSubDirectories();
    }


    public String getCorpusText() throws IOException {
        StringBuilder result = new StringBuilder();
        int i  = 0;

        for (String subDirectory : subDirectories) {
            String sourcePath = rootDirectory + "\\" + subDirectory + "\\" + CORPUS_TEXT_FILE_NAME;
            System.out.println("Processing file: " + sourcePath);
            File file = new File (sourcePath);
            reader = new ContentReader(file);
            result.append(reader.getFileContent());
            i++;
            float progress = ((float)i/(float)subDirsCount) *100;

            System.out.println("Processed " + i + " from " + subDirsCount + " (" + progress + "%)");
        }

        Document doc = Jsoup.parse(result.toString());
        Elements elements= doc.getElementsByTag(textTag);

        result = new StringBuilder();
        for (Element e: elements)
            result.append(e.text());

        return result.toString();
    }

    private void getSubDirectories() {
        if (subDirectories == null) {
            subDirectories = new ArrayList<>();
            File file = new File(rootDirectory);
            String[] names = file.list();

            for (String name : names) {
                if (new File(rootDirectory + "\\" + name).isDirectory()) {
                    subDirectories.add(name);
                }
            }
            subDirsCount = subDirectories.size();
        }
    }
}
