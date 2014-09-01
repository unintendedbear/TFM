package mtproject.parsers;

import java.io.IOException;
import java.util.regex.Pattern;
import java.util.regex.Matcher;
import java.util.List;
import java.util.ArrayList;
import mtproject.file.handlers.*;
import mtproject.objects.*;

import java.util.Date;
import java.util.Vector;
import java.text.*;

public class DataParser {
	
	public DataParser() {
		// TODO Auto-generated constructor stub
		
	}
	
	/**
	 * @param args
	 */
	public static List<LogEntry> parsing_Log() throws IOException {
		
		String logFile = "/home/paloma/workspace/KRS_Prototype/CSV/data_100k_instances_url_log.csv";
		String cleaningPattern = "\"(.+)\"\n?";
		String contentTypePattern = "^(\\w+\\-*\\w+)[\\/?]\\w+";
		String timePattern = "^\\d{1,2}\\:\\d{2}\\:\\d{2}";
		String urlPattern = "(https?:\\/\\/([-\\w\\.]+)+(\\:\\d+)?(\\/([\\w\\/\\_\\-\\.]*(\\\\?\\S+)?)?)?)";
		
				
		try {
			
			// Leer DRL
			ReadFile file = new ReadFile(logFile);
			String[] arrayLines = file.OpenFile();
			Vector listOfValues = new Vector();
			List<LogEntry> listOfEntries = new ArrayList<LogEntry>();
			
			
			int i;
			// i empieza en 1 porque la primera línea son los nombres de los campos
			for ( i = 1; i < arrayLines.length; i++ ) {

				// Obtener valores de los campos por separado
				String[] fieldValues = arrayLines[i].split(";");
				
				// Limpiamos el vector de valores
				listOfValues.clear();
				
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
						//System.out.println("Found MCT: " + matcherContentType.group(1) );
						if (matcherContentType.group(1) != null) {
							listOfValues.addElement(matcherContentType.group(1));
							listOfValues.addElement(fieldValues[j]);
						} else {
							listOfValues.addElement(fieldValues[j]);
							listOfValues.addElement(fieldValues[j]);
						}
						
					} else if(matcherTime.find()) {
						//System.out.println("Found time: " + fieldValues[j] );
						listOfValues.addElement(fieldValues[j]);						
					} else if(matcherUrl.find()) {
						//System.out.println("Found url: " + matcherUrl.group(2) );
						
						listOfValues.addElement(fieldValues[j]);
						// Obtener dominios de la url por separado
						String[] urlValues = matcherUrl.group(2).split("\\.");
						//System.out.println("Found TLD: " + urlValues[urlValues.length-1] );
						//System.out.println("Found core domain: " + urlValues[urlValues.length-2] );
												
						listOfValues.addElement(urlValues[urlValues.length-2]);
						listOfValues.addElement(urlValues[urlValues.length-1]);						
					} else if (is_integer(fieldValues[j])) {						
						listOfValues.addElement(Integer.parseInt(fieldValues[j]));						
					} else if (j == 3){
						listOfValues.addElement(fieldValues[j]);
						listOfValues.addElement(fieldValues[j]);
					} else if (j == 8){
						listOfValues.addElement(fieldValues[j]);
						listOfValues.addElement(fieldValues[j]);
						listOfValues.addElement(fieldValues[j]);
					} else {						
						listOfValues.addElement(fieldValues[j]);
					}
				}
				
				listOfEntries.add(obtain_log(listOfValues));
			} // for
		
			return listOfEntries;
				
		} catch (IOException e) {
			System.out.println(e.getMessage());
			return null;
		}		        
        
    }
	
    private static boolean is_integer(String value) {
    	
    	if (value == null || value.isEmpty()) {
    		return false;
    	}
    	
    	int m = 0;
    	if (value.charAt(0) == '-') {
    		if (value.length() > 1) {
    			m++;
    		} else {
    			return false;
    		}
    	}
    	
    	for ( ; m < value.length(); m++) {
    		if (!Character.isDigit(value.charAt(m))) {
    			return false;
    		}
    	}
    	
    	return true;
    }
	
	public static Date string_to_date(String time) {
		
		SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");
	 
		try {
	 
			Date timeDate = formatter.parse(time);
			//System.out.println(timeDate);
			//System.out.println(formatter.format(timeDate));
			return timeDate;
	 
		} catch (ParseException e) {
			e.printStackTrace();
			return null;
		}
				
	}
	
	public static LogEntry obtain_log(Vector listOfValues) {
		
		if (listOfValues.size() == 13) {			
			
			int http_reply_code = (int)listOfValues.elementAt(0);
			String http_method = (String)listOfValues.elementAt(1);
			int duration_milliseconds = (int)listOfValues.elementAt(2);
			String content_type_MCT = (String)listOfValues.elementAt(3);
			String content_type = (String)listOfValues.elementAt(4);
			String server_or_cache_address = (String)listOfValues.elementAt(5);
			String time = (String)listOfValues.elementAt(6);
			String squid_hierarchy = (String)listOfValues.elementAt(7);
			int bytes = (int)listOfValues.elementAt(8);
			String url = (String)listOfValues.elementAt(9);
			String url_core = (String)listOfValues.elementAt(10);
			String url_TLD = (String)listOfValues.elementAt(11);
			String client_address = (String)listOfValues.elementAt(12);
			// elementos en listOfValues.elementAt(número)
		
			LogEntry myEntry = new LogEntry(http_reply_code, http_method, duration_milliseconds, content_type_MCT, content_type, server_or_cache_address, time, squid_hierarchy, bytes, url, url_core, url_TLD, client_address);
		
			return myEntry;
		} else {
			System.out.println("Number of values required for creating a log entry is: "+listOfValues.size()+", so insufficient");
			for(int n=0; n<listOfValues.size(); n++){
	            System.out.println("-"+n+" "+listOfValues.elementAt(n)+"\t");
	        }
			return null;
		}
	}

}
