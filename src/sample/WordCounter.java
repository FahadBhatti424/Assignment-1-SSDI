package sample;
import java.io.*;
import java.util.*;

public class WordCounter {
    private Map<String, Integer> wordCounts;
    private Set<String> set  = new HashSet<>();
    public WordCounter() {
        wordCounts = new TreeMap<>();
    }
    public Map<String, Integer> getWordCount(){
        return wordCounts;
    }

    public void parseFile(File file) throws IOException {
        //System.out.println("Starting parsing the file:" + file.getAbsolutePath());

        if (file.isDirectory()) {
            //parse each file inside the directory
            File[] content = file.listFiles();
            for (File current : content) {
                parseFile(current);
            }
        } else {
            Scanner scanner = new Scanner(file);
            // scanning token by token
            while (scanner.hasNext()) {
                String token = scanner.next();
                if (isValidWord(token)) {
                    countWord(token);
                }
            }
        }

    }

    private boolean isValidWord(String word) {
        String allLetters = "^[a-zA-Z]+$";
        // returns true if the word is composed by only letters otherwise returns false;
        return word.matches(allLetters);

    }

    private void countWord(String word) {
        if (wordCounts.containsKey(word)) {
            int previous = wordCounts.get(word);
            wordCounts.put(word, previous + 1);
        } else {
            wordCounts.put(word, 1);
        }
    }

    public void outputWordCount(int minCount, File output) throws IOException {
        System.out.println("Saving word counts to file:" + output.getAbsolutePath());
        System.out.println("Total words:" + wordCounts.keySet().size());

        if (!output.exists()) {
            output.createNewFile();
            if (output.canWrite()) {
                PrintWriter fileOutput = new PrintWriter(output);

                Set<String> keys = wordCounts.keySet();
                Iterator<String> keyIterator = keys.iterator();

                while (keyIterator.hasNext()) {
                    String key = keyIterator.next();
                    int count = wordCounts.get(key);
                    // testing minimum number of occurances
                    if (count >= minCount) {
                        fileOutput.println(key + ": " + count);
                    }
                }

                fileOutput.close();
            }
        } else {
            System.out.println("Error: the output file already exists: " + output.getAbsolutePath());
        }
    }
}