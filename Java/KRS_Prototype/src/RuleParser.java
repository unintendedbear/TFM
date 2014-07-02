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
		
		String drlFile = "/media/osica/166178457AA12F1B/Workspace/KRS_Prototype/Rules/Initial-rules-squid.drl";
		//String drlFile = "/home/paloma/workspace/KRS_Prototype/Rules/Initial-rules-squid.drl";
		String conditionsPattern = "^\\D+:\\D+\\((.+)\\)";
		String argumentsPattern1 = "\\s*(.*)(==)\"(.+)\"";
		String argumentsPattern2 = "\\s*(.+)([>|<|=])(\\d+)";
		String argumentsPattern3 = "\\s*(.+) (.+) \"?\\*\\.(.+)\\.\\*\"?";
		String actionPattern = "^\\D+\\.(.+)\\(\\)\\;";
				
		try {
			
			// Leer DRL
			ReadFile file = new ReadFile(drlFile);
			String[] aryLines = file.OpenFile();
			List<Condition> conditionList = new ArrayList<Condition>();
			List<Rule> ruleList = new ArrayList<Rule>();
			
			
			int i;
			for ( i=0; i < aryLines.length; i++ ) {
				// Parsear aquí ò_ó
				Pattern patternOfCondition = Pattern.compile(conditionsPattern);
				Matcher matcherLine = patternOfCondition.matcher(aryLines[i]);
				Pattern patternOfAction = Pattern.compile(actionPattern);
				Matcher matcherAction = patternOfAction.matcher(aryLines[i]);
				
				if (matcherLine.find()) {
					// Condiciones en matcherLine.group(1)
					
					String[] arrayConditions = matcherLine.group(1).split(",");
					
					Condition condition;
					
					int j;
					for ( j=0; j < arrayConditions.length; j++ ) {
						
						Pattern patternOfArgument1 = Pattern.compile(argumentsPattern1);
						Pattern patternOfArgument2 = Pattern.compile(argumentsPattern2);
						Pattern patternOfArgument3 = Pattern.compile(argumentsPattern3);
						
						Matcher matcherArguments1 = patternOfArgument1.matcher(arrayConditions[j]);
						Matcher matcherArguments2 = patternOfArgument2.matcher(arrayConditions[j]);
						Matcher matcherArguments3 = patternOfArgument3.matcher(arrayConditions[j]);
						
						// dataType en matcherArgumentsX.group(1)
						// relationship en matcherArgumentsX.group(2)
						// value en matcherArgumentsX.group(3)
						
						if (matcherArguments1.find()) {
							
							condition = new Condition(matcherArguments1.group(1),
									matcherArguments1.group(2),
									matcherArguments1.group(3));
							
							conditionList.add(condition);
							
						} else if(matcherArguments2.find()) {
							
							condition = new Condition(matcherArguments2.group(1),
									matcherArguments2.group(2),
									matcherArguments2.group(3));
							
							conditionList.add(condition);
							
						} else if(matcherArguments3.find()) {
							
							condition = new Condition(matcherArguments3.group(1),
									matcherArguments3.group(2),
									matcherArguments3.group(3));
							
							conditionList.add(condition);
							
						}
					
					}
					
						
				}
				
				if (matcherAction.find()) {
					// allow o deny en matcherAction.group(1)
					
					if (matcherAction.group(1).equals("allow")) {
						Rule rule = new Rule(conditionList, true);
						ruleList.add(rule);
					} else {
						Rule rule = new Rule(conditionList, false);
						ruleList.add(rule);
					}					
					
				}
			}
			System.out.println(ruleList.toString());
		}
		catch (IOException e) {
			System.out.println(e.getMessage());
		}
		        
        
    }

}
