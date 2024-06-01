import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Main {
    final static int BASE = 28;
    final static int NUM_SUG = 18;
    public record Couple(char character, int numOcc) implements Comparable<Couple>{
        @Override
        public String toString() {
            return character + ":"+ numOcc;
        }

        @Override
        public int compareTo(Couple o) {
            if(o.character == '*' || character == '*') {
                if(o.character == character)
                    return 0;
                return o.character == '*' ? -1 : 1;
            }
            return Integer.compare(o.numOcc, numOcc);
        }
    }

    public static void main(String[] args) throws IOException {
        String corpus = "C:\\Users\\dmari\\IdeaProjects\\SuggestionsCorpus\\files\\eng_news_2020_1M-sentences.txt";
        String result = "C:\\Users\\dmari\\IdeaProjects\\SuggestionsCorpus\\files\\result.txt";

        //sentences contains all strings in lowercase
        List<String> sentences = Files.readAllLines(Paths.get(corpus));
        //sentences = sentences.stream().map(String::toLowerCase).collect(Collectors.toList());

        sentences = sentences.stream()
                .map(frase -> frase.split("\t"))
                .filter(parti -> parti.length > 1)
                .map(parti -> "   " + parti[1].toLowerCase())
                .collect(Collectors.toList());

        int[][] occurrences = calculateSuggestions(sentences);

        PrintWriter pw = new PrintWriter(result);

        for(int i=0; i<occurrences.length; i++) {
            System.out.print(numToSequence(i, 4) +'\t');
            Couple[] oc = new Couple[occurrences[i].length];
            for(int j=0 ; j < occurrences[i].length; j++)
                oc[j] = new Couple(numToChar(j), occurrences[i][j]);
            Arrays.sort(oc);
            System.out.println(Arrays.toString(oc));
            for(int w = 0 ; w<NUM_SUG ; w++) {
                pw.print(oc[w].character);
            }
        }
        pw.close();
    }

    /*****************************************************/
    public static int[][] calculateSuggestions(List<String> sentences) {
        int[][] res = new int[BASE*BASE*BASE*BASE][];

        for( int i=0; i<res.length; i++)
            res[i] = new int[BASE];

        for(String s : sentences) {
            for(int i=0; i < s.length(); i++) {
                int p = i - 4;
                StringBuilder spaces = new StringBuilder();
                for(; p<0 ; p++)
                    spaces.append(" ");
                String cur = spaces + s.substring(p, i);
                res[sequenceToNum(cur)][charToNum(s.charAt(i))]++;
            }
        }
        return res;
    }

    public static String getSuggestions(String context, String suggestionData) {
        int sequenceNumber = sequenceToNum(context);
        return suggestionData.substring(sequenceNumber*NUM_SUG,(sequenceNumber*NUM_SUG)+NUM_SUG);
    }

    /*****************************************************/
    public static int sequenceToNum(String sequence) {
        int num = 0;
        for (int i = 0; i < sequence.length(); i++) {
            char c = sequence.charAt(i);
            int n = charToNum(c);
            num += n * Math.pow(BASE, i);
        }
        return num;
    }

    public static String numToSequence(int num, int sequenceLength) {
        StringBuilder sequence = new StringBuilder();
        for (int i = 0; i < sequenceLength; i++) {
            int n = num % BASE;
            char c = numToChar(n);
            sequence.append(c);
            num /= BASE;
        }

        return sequence.toString();
    }

    /*****************************************************/
    public static char numToChar(int n) {
        if (n >= 0 && n <= 25)
            return (char) (n + 'a');
        else if (n == 26)
            return ' ';
        else
            return '*';
    }

    public static int charToNum(char c) {
        if (c == ' ')
            return 26;
        else if (c >= 'a' && c <= 'z')
            return c - 'a';
        else
            return 27;
    }
}