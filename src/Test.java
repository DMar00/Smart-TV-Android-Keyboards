import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class Test {
    private static String dataString;
    public static void main(String[] args) throws IOException {
        String result = "C:\\Users\\dmari\\IdeaProjects\\SuggestionsCorpus\\files\\result.txt";

        List<String> listData = Files.readAllLines(Paths.get(result));
        dataString = listData.get(0);
        String input = "   a";

        char[] ss = getCheckedSuggestion(input);
        for(int i=0; i<ss.length; i++){
            System.out.print(ss[i]+" - ");
        }
    }

    protected static char[] getCheckedSuggestion(String ctx) {
        char[] s = generateSuggestions(ctx);
        char[] suggestions = new char[Main.NUM_SUG];
        for(int i=0, j=0; i<s.length && j<suggestions.length; i++){
            if(s[i]!= ctx.charAt(ctx.length()-1)){
                suggestions[j] = s[i];
                j++;
            }
        }
        return suggestions;
    }

    public static char[] generateSuggestions(String lastLetters){
        char suggestions[] = new char[Main.NUM_SUG];
        String suggestionsString = Main.getSuggestions(lastLetters, dataString);
        for (int i=0; i<Main.NUM_SUG ; i++){
            suggestions[i] = suggestionsString.charAt(i);
        }
        return suggestions;
    }
}
