import java.io.IOException;
import java.util.regex.Pattern;
import java.util.regex.Matcher;
import java.util.List;
import java.util.ArrayList;

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
			List<Rule> myList = new ArrayList<Rule>();
			
			
			int i;
			for ( i=0; i < aryLines.length; i++ ) {
				// Parsear aquí ò_ó
				Pattern patternOfCondition = Pattern.compile(conditionsPattern);
				Matcher matcherLine = patternOfCondition.matcher(aryLines[i]);
				
				if (matcherLine.find()) {
					// Condiciones en matcherLine.group(1)
					
					String[] arrayConditions = matcherLine.group(1).split(",");
					
					Pattern patternOfArgument1 = Pattern.compile(argumentsPattern1);
					Pattern patternOfArgument2 = Pattern.compile(argumentsPattern2);
					Pattern patternOfArgument3 = Pattern.compile(argumentsPattern3);
					
					Matcher matcherArguments1 = patternOfArgument1.matcher(matcherLine.group(1));
					Matcher matcherArguments2 = patternOfArgument2.matcher(matcherLine.group(1));
					Matcher matcherArguments3 = patternOfArgument3.matcher(matcherLine.group(1));
					
					if (arrayConditions.length > 1) {
						
					} else {
						if (matcherArguments1.find()) {
							System.out.println("Found value: " + matcherArguments1.group(1) );
							System.out.println("Found value: " + matcherArguments1.group(2) );
							System.out.println("Found value: " + matcherArguments1.group(3) );
						} else if(matcherArguments2.find()) {
							System.out.println("Found value: " + matcherArguments2.group(1) );
							System.out.println("Found value: " + matcherArguments2.group(2) );
							System.out.println("Found value: " + matcherArguments2.group(3) );						
						} else if(matcherArguments3.find()) {
							System.out.println("Found value: " + matcherArguments3.group(1) );
							System.out.println("Found value: " + matcherArguments3.group(2) );
							System.out.println("Found value: " + matcherArguments3.group(3) );						
						}
					}
					
						
				}
			}
		}
		catch (IOException e) {
			System.out.println(e.getMessage());
		}
		        
        
    }
	
	public String[] compare(Pattern pattern, Matcher matcher, int size) {
		
		int arraySize = size*3;
		String[] arrayArguments = new String[arraySize];
		
		
		
		return arrayArguments;
	}

}
