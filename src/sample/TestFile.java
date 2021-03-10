import java.io.*;
import java.util.*;
import java.text.DecimalFormat;


public class TestFile{

    private static int hamLength = 0;
    private static int spamLength = 0;

    private List<String> temp;

    private Map<String, Double> wordCounts;
    public double n = 0.0;

    private Map<String, Double> trainHamFreq;
    private Map<String, Double> trainSpamFreq;

    private Map<String, Double> PrSWi;
    private Map<String, Double> PrWiS;
    private Map<String, Double> PrWiH;
    
    public static double numTruePos = 0.0;
    public static double numTrueNeg = 0.0;
    public static double numFalsePos = 0.0;


    private Map<String, String> PrSF;

    public TestFile(){
        wordCounts = new TreeMap<>();
        trainHamFreq = new TreeMap<>();
        trainSpamFreq = new TreeMap<>();
        PrSWi = new TreeMap<>();
        PrWiS = new TreeMap<>();
        PrWiH = new TreeMap<>();
        temp = new ArrayList<>();
        PrSF = new TreeMap<>();
    }

    //trains frequencies and probability maps 
    public void parseFileTrain(File file) throws IOException{
        String ham = "ham";
        String spam = "spam";
        if(file.isDirectory()){
            //parse each file inside the directory
            File[] content = file.listFiles();
            for(File current: content){
                parseFileTrain(current);
            }
        }else{
            Scanner scanner = new Scanner(file);
            // scanning token by token
            while (scanner.hasNext()){
                String  token = scanner.next();
                if (isValidWord(token)){
                    countWord(token);
                }
            }
            String path = file.getParentFile().getName();
            if(path.equals(spam)){
                spamLength = file.getParentFile().listFiles().length;

            } else{


                hamLength = file.getParentFile().listFiles().length;
            }
            // used Geeks for Geeks as resource:
            if(path.equals(ham)){
                wordCounts.forEach((key, value) -> trainHamFreq.merge(key, value, (v1, v2) -> v1 + v2));
            } else{
                wordCounts.forEach((key, value) -> trainSpamFreq.merge(key, value, (v1, v2) -> v1 + v2));
            }
            wordCounts.clear();
        }
        for (Map.Entry<String, Double> entry : trainHamFreq.entrySet()) {
            Double prob = (entry.getValue() / Double.valueOf(hamLength));
            PrWiH.put(entry.getKey(), prob);
        }
        for (Map.Entry<String, Double> entry : trainSpamFreq.entrySet()) {
            Double prob = (entry.getValue() / Double.valueOf(spamLength));
            PrWiS.put(entry.getKey(), prob);
        }
        for (Map.Entry<String, Double> entry : trainSpamFreq.entrySet()) {
            try{
                Double probabiltiesFiles = (PrWiS.get(entry.getKey())) / ( PrWiS.get(entry.getKey()) + PrWiH.get(entry.getKey()) );
                PrSWi.put(entry.getKey(), probabiltiesFiles);
            } catch(Exception e){
            }
        }
    }

    //tests probabilties
    public void parseFileTest(File file) throws IOException{
        String ham = "ham";
        String spam = "spam";
        if(file.isDirectory()){
            //parse each file inside the directory
            File[] content = file.listFiles();
            for(File current: content){
                parseFileTest(current);
            }
        }else{
            Scanner scanner = new Scanner(file);
            // scanning token by token
            while (scanner.hasNext()){
                String  token = scanner.next().toLowerCase();
                if (!temp.contains(token)&&isValidWord(token)){
                    try{

                        temp.add(token);
                        n = n + Math.log(1 - PrSWi.get(token))- Math.log(PrSWi.get(token));

                    }catch(Exception e){
                    }
                }
            }
            temp.clear();
            double SF = ( 1 / ( 1 + Math.pow(Math.E, n) ) );
            DecimalFormat df = new DecimalFormat("0.0000000000000000000000000");
            String convert = df.format(SF);
            PrSF.put(file.getName(),convert);

            String path = file.getParentFile().getName();
            if(path.equals(ham)){
                hamLength = file.getParentFile().listFiles().length;
            }
            if(path.equals(spam)){
                spamLength = file.getParentFile().listFiles().length;
            }
            if(path.equals(ham)&&SF > 0.3){
                numTrueNeg = 1.0 + numTrueNeg;
            }
            if(path.equals(spam)&&SF > 0.3){
                numTruePos = 1.0 + numTruePos;
            }
            if(path.equals(spam)&&SF < 0.3){
                numFalsePos = 1.0 + numFalsePos;
            }
            n=0;
        }
    }

    private void countWord(String word) {
        wordCounts.put(word.toLowerCase(), 1.0);
    }

    private boolean isValidWord(String word){
        String allLetters = "^[a-zA-Z]+$";
        // returns true if the word is composed by only letters otherwise returns false;
        return word.matches(allLetters);
    }

    //prints probabilities
    public void outputWordCount() throws IOException{
        File output = new File("TotalProbabilties.txt");
        output.createNewFile();
        if (output.canWrite()) {
            PrintWriter fileOutput = new PrintWriter(output);


            PrSF.entrySet().forEach(entry -> {
                fileOutput.println(entry.getKey() + ":" + entry.getValue());
            });
            fileOutput.close();
        }
    }

    //main method
    public static void main(String[] args) {
        File trainHam = new File(args[0]);
        File trainSpam = new File(args[1]);
        File testHam = new File(args[2]);
        File testSpam = new File(args[3]);
        TestFile wordCounter = new TestFile();

        try{

            wordCounter.parseFileTrain(trainHam);
            wordCounter.parseFileTrain(trainSpam);
            wordCounter.parseFileTest(testHam);
            wordCounter.parseFileTest(testSpam);
            wordCounter.outputWordCount();

        }catch(FileNotFoundException e){

            e.printStackTrace();

        }catch(IOException e){
            e.printStackTrace();
        }

        System.out.println("Accuarcy = "  + (numTrueNeg + numTruePos) / (spamLength+hamLength));

        System.out.println("Precision = " + (numTruePos) / (numFalsePos+numTruePos));
    }
}