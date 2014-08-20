package mtproject.parsers;

import java.io.IOException;
import java.util.regex.Pattern;
import java.util.regex.Matcher;
import java.util.List;
import java.util.ArrayList;
import mtproject.objects.*;

public class DataParser {
	
	public DataParser() {
		// TODO Auto-generated constructor stub
		
	}
	
	/**
	 * @param args
	 */
	public static List<Entry> parsing_Log() throws IOException {
		
		String logFile = "/home/paloma/workspace/KRS_Prototype/Logs/data_100k_instances_url_log.csv";
		String cleaningPattern = "\"(.+)\"\n?";
		String contentTypePattern = "^(\\w+\-*\\w+)[\\/?]\\w+";
		String timePattern = "^\\d{1,2}\\:\\d{2}\\:\\d{2}";
		String urlPattern = "(https?:\\/\\/([\\-\\w\\.]+)+(\\:\\d+)?(\\/([\\w\\/\\_\\-\\.]*(\\\?\\S+)?)?)?)";
		
				
		try {
			
			// Leer DRL
			ReadFile file = new ReadFile(drlFile);
			String[] arrayLines = file.OpenFile();
			List<String> listOfTerms = new ArrayList<String>();
			//List<Rule> listOfRules = new ArrayList<Rule>();
			//String ruleAction = "deny";
			
			
			int i;
			// i empieza en 1 porque la primera línea son los nombres de los campos
			for ( i = 1; i < arrayLines.length; i++ ) {

				// Obtener valores de los campos por separado
				String[] fieldValues = arrayLines[i].split(";");
				
				// Valores uno a uno
				int j;
				for ( j = 0; j < fieldValues.length; j++){
					// Dejamos limpios los que estén entre comillas ""
					Pattern patternForCleaning = Pattern.compile(cleaningPattern);
					Matcher matcherLine = patternForCleaning.matcher(fieldValues[j]);
					
					if (matcherLine.find()) {
						// Valores en matcherLine.group(1)
						fieldValues[j] = matcherLine.group(1);
					}
					
					// Patrones para detectar qué es qué
					Pattern patternForContentType = Pattern.compile(contentTypePattern);
					Pattern patternForTime = Pattern.compile(timePattern);
					Pattern patternForUrl = Pattern.compile(urlPattern);
					
					Matcher matcherContentType = patternForContentType.matcher(fieldValues[j]);
					Matcher matcherTime = patternForTime.matcher(fieldValues[j]);
					Matcher matcherUrl = patternForUrl.matcher(fieldValues[j]);
					
					if (matcherContentType.find()) {
						System.out.println("Found: " + matcherContentType.group(1) );
						//listOfTerms.add(matcherArguments11.group(1));
						//System.out.println("Relationship: " + matcherArguments11.group(2) );
						//listOfTerms.add(matcherArguments11.group(2));
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
			dataType = arrayTerms.get(k);
			relationship = arrayTerms.get(k+1);
			value = arrayTerms.get(k+2);
			Condition myCondition = new Condition(dataType, relationship, value);
			listOfConditions.add(myCondition);
		}
		
		return listOfConditions;
	}
	
	// Y aquí obtenemos la regla
	
	public static Rule obtain_rule(List<Condition> arrayConditions, String action) {
		
		if ( action.compareTo("allow") == 0) {
			Rule myRule = new Rule(arrayConditions, true);
			return myRule;
		} else {
			Rule myRule = new Rule(arrayConditions, false);
			return myRule;
		}	
	}

}
