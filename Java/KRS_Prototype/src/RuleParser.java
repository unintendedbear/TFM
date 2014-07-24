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
					
					for (int j=0; j<arrayConditions.length; j++){
						
						Pattern patternOfArgument1 = Pattern.compile(argumentsPattern1);
						Pattern patternOfArgument2 = Pattern.compile(argumentsPattern2);
						Pattern patternOfArgument3 = Pattern.compile(argumentsPattern3);
						
						Matcher matcherArguments11 = patternOfArgument1.matcher(arrayConditions[j]);
						Matcher matcherArguments12 = patternOfArgument2.matcher(arrayConditions[j]);
						Matcher matcherArguments13 = patternOfArgument3.matcher(arrayConditions[j]);
						
						if (matcherArguments11.find()) {
							System.out.println("Found value: " + matcherArguments11.group(1) );
							System.out.println("Found value: " + matcherArguments11.group(2) );
							System.out.println("Found value: " + matcherArguments11.group(3) );
						} else if(matcherArguments12.find()) {
							System.out.println("Found value: " + matcherArguments12.group(1) );
							System.out.println("Found value: " + matcherArguments12.group(2) );
							System.out.println("Found value: " + matcherArguments12.group(3) );						
						} else if(matcherArguments13.find()) {
							System.out.println("Found value: " + matcherArguments13.group(1) );
							System.out.println("Found value: " + matcherArguments13.group(2) );
							System.out.println("Found value: " + matcherArguments13.group(3) );						
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
