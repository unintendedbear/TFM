import java.io.IOException;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

public class RuleParser {

	/**
	 * @param args
	 */
	public static void main(String[] args) throws IOException {
		
		String drlFile = "/home/paloma/workspace/KRS_Prototype/Rules/Initial-rules-squid.drl";
		String conditionsPattern = "^\\D+:\\D+\\((.+)\\)";
		String argumentsPattern1 = "\\s*(.*)(==)\"(.+)\"";
		String argumentsPattern2 = "\\s*(.+)([>|<|=])(\\d+)";
		String argumentsPattern3 = "\\s*(.+) (.+) \"?\\*\\.(.+)\\.\\*\"?";
		
		try {
			
			// Leer DRL
			ReadFile file = new ReadFile(drlFile);
			String[] aryLines = file.OpenFile();
			
			int i;
			for ( i=0; i < aryLines.length; i++ ) {
				// Parsear aquí ò_ó
				Pattern pattern = Pattern.compile(conditionsPattern);
				Matcher matcher = pattern.matcher(aryLines[i]);
			}
		}
		catch (IOException e) {
			System.out.println(e.getMessage());
		}
        
        while (true) {

            Pattern pattern = 
            Pattern.compile(console.readLine("%nEnter your regex: "));

            Matcher matcher = 
            pattern.matcher(console.readLine("Enter input string to search: "));

            boolean found = false;
            while (matcher.find()) {
                console.format("I found the text" +
                    " \"%s\" starting at " +
                    "index %d and ending at index %d.%n",
                    matcher.group(),
                    matcher.start(),
                    matcher.end());
                found = true;
            }
            if(!found){
                console.format("No match found.%n");
            }
        }
    }

}
