package mtproject.parsers;

import java.io.IOException;
import java.util.regex.Pattern;
import java.util.regex.Matcher;
import java.util.List;
import java.util.ArrayList;
import mtproject.file.handlers.*;
import mtproject.objects.*;

public class RuleParser {
	
	public RuleParser() {
		// TODO Auto-generated constructor stub
		
	}
	
	/**
	 * @param args
	 */
	public static List<Rule> parsing_DRL() throws IOException {
		
		String drlFile = "/home/paloma/workspace/KRS_Prototype/Rules/Initial-rules-squid.drl";
		String conditionsPattern = "^\\D+:\\D+\\((.+)\\)";
		String argumentsPattern1 = "\\s*(.*)(==)\"(.+)\"";
		String argumentsPattern2 = "\\s*(.+)([>|<|=])(\\d+)";
		String argumentsPattern3 = "\\s*(.+) (.+) \"?\\*\\.(.+)\\.\\*\"?";
		String actionPattern = "^\\D+\\.(.+)\\(\\)\\;";
		
				
		try {
			
			// Leer DRL
			ReadFile file = new ReadFile(drlFile);
			String[] arrayLines = file.OpenFile();
			List<String> listOfTerms = new ArrayList<String>();
			List<Rule> listOfRules = new ArrayList<Rule>();
			String ruleAction = "deny";
			
			
			int i;
			for ( i = 0; i < arrayLines.length; i++ ) {
				// Parseo de Condiciones
				Pattern patternOfCondition = Pattern.compile(conditionsPattern);
				Matcher matcherLine = patternOfCondition.matcher(arrayLines[i]);
				
				// Parseo del 'ALLOW' o el 'DENY'
				Pattern patternOfAction = Pattern.compile(actionPattern);
				Matcher matcherAction = patternOfAction.matcher(arrayLines[i]);
				
				if (matcherLine.find()) {
					// Condiciones en matcherLine.group(1)
					
					String[] arrayConditions = matcherLine.group(1).split(",");
					listOfTerms.clear();
					
					int j;
					for ( j = 0; j < arrayConditions.length; j++){
						
						Pattern patternOfArgument1 = Pattern.compile(argumentsPattern1);
						Pattern patternOfArgument2 = Pattern.compile(argumentsPattern2);
						Pattern patternOfArgument3 = Pattern.compile(argumentsPattern3);
						
						Matcher matcherArguments11 = patternOfArgument1.matcher(arrayConditions[j]);
						Matcher matcherArguments12 = patternOfArgument2.matcher(arrayConditions[j]);
						Matcher matcherArguments13 = patternOfArgument3.matcher(arrayConditions[j]);
						
						if (matcherArguments11.find()) {
							//System.out.println("Data type: " + matcherArguments11.group(1) );
							listOfTerms.add(matcherArguments11.group(1));
							//System.out.println("Relationship: " + matcherArguments11.group(2) );
							listOfTerms.add(matcherArguments11.group(2));
							//System.out.println("Value: " + matcherArguments11.group(3) );
							listOfTerms.add(matcherArguments11.group(3));
						} else if(matcherArguments12.find()) {
							//System.out.println("Data type: " + matcherArguments12.group(1) );
							listOfTerms.add(matcherArguments12.group(1));
							//System.out.println("Relationship: " + matcherArguments12.group(2) );
							listOfTerms.add(matcherArguments12.group(2));
							//System.out.println("Value: " + matcherArguments12.group(3) );
							listOfTerms.add(matcherArguments12.group(3));						
						} else if(matcherArguments13.find()) {
							//System.out.println("Data type: " + matcherArguments13.group(1) );
							listOfTerms.add(matcherArguments13.group(1));
							//System.out.println("Relationship: " + matcherArguments13.group(2) );
							listOfTerms.add(matcherArguments13.group(2));
							//System.out.println("Value: " + matcherArguments13.group(3) );
							listOfTerms.add(matcherArguments13.group(3));						
						}
			              
			         } // for
						
				} else if (matcherAction.find()) {
					ruleAction = matcherAction.group(1);
					List<Condition> finalConditions = obtain_condition(listOfTerms);
					listOfRules.add(obtain_rule(finalConditions, ruleAction));
				}
			}
			
			return listOfRules;
		}
		catch (IOException e) {
			System.out.println(e.getMessage());
			return null;
		}
		        
        
    }
	
	// Esta función es para crear un objeto de tipo Condition(String dataType, String relationship, String value).
	// Luego ya se crea la regla con las condiciones que sea y 'allow' o 'deny'.
	
	public static List<Condition> obtain_condition(List<String> arrayTerms) {
		
		List<Condition> listOfConditions = new ArrayList<Condition>();
		String dataType;
		String relationship;
		String value;
		
		
		int k;
		for ( k = 0; k < arrayTerms.size(); k+=3) {
			dataType = dictionary(arrayTerms.get(k));
			relationship = arrayTerms.get(k+1);
			value = arrayTerms.get(k+2);
			Condition myCondition = new Condition(dataType, relationship, value);
			listOfConditions.add(myCondition);
		}
		
		return listOfConditions;
	}
	
	// Y aquí obtenemos la regla
	
	public static Rule obtain_rule(List<Condition> arrayConditions, String action) {
		
		Rule myRule = new Rule(arrayConditions, action);
		return myRule;
		
	}
	
	public static String dictionary (String word) {
		
				
		switch (word) {
		case "dif_code":
			return "http_code";
		case "dif_met":
			return "http_method";
		case "dif_MCT":
			return "content_type_MCT";
		case "dif_content":
			return "content_type";
		case "diff_content":
			return "content_type_MCT";
		case "dif_squid":
			return "squid_hierarchy";
		case "url":
			return "url";
		default:
			return word;
		}
	}

}
