package utils;

import java.io.*;

public class ContentReader {

    protected BufferedReader reader;
    protected String filename;
    protected File sourceFile;

    public ContentReader(File file) throws FileNotFoundException, UnsupportedEncodingException {
        sourceFile = file;
        reader = new BufferedReader(
                new InputStreamReader(new FileInputStream(sourceFile),"UTF-8"));
        this.filename = sourceFile.getName();
    }

    public ContentReader(String filePath) throws FileNotFoundException {
        sourceFile = new File (filePath);
        reader = new BufferedReader(
                new InputStreamReader(new FileInputStream(sourceFile)));
        this.filename = sourceFile.getName();
    }

    public String getFileContent() throws IOException {
        StringBuilder result = new StringBuilder();
        String line = "";
        while ((line = reader.readLine()) != null) {
            result.append("\n").append(line);
        }
        return result.toString();
    }

    public void close() throws IOException {
        reader.close();
    }
}
