import utils.ContentWriter;
import utils.CorpusTextRetriever;

import java.io.IOException;

/*Execute this code when you have to get raw text from XMLs. Just change the root directory.*/

public class CorpusRawTextGetter {

    public static final String ROOT_DIRECTORY = "C:\\Korpus milionowy\\korpus";

    public static void main(String[] args) {

        CorpusTextRetriever retriever = new CorpusTextRetriever(ROOT_DIRECTORY);
        try {
            String result = retriever.getCorpusText();
            System.out.println(ROOT_DIRECTORY + "\\content.txt");
            ContentWriter writer = new ContentWriter(ROOT_DIRECTORY + "\\content.txt");
            writer.writeLine(result);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
