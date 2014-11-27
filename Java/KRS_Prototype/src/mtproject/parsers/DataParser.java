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
		String IPPattern = "\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}";
		//String urlPattern = "(https?)?:\\/\\/(www\\.)?([\\-\\w\\.]+)*\\/?(\\w*)\\/?(\\w*)\\/?([\\?\\%\\&\\:\\w\\/\\_\\-\\.\\:]*\\.([^.]+)+)?";
		String urlPattern = "(https?)?:\\/\\/(www\\.)?([\\-\\w\\.]+)*\\/?(.*)";
		String fileExtension = "(.*)\\.([a-zA-Z]{3,4})$";
		
				
		try {
			
			// Leer DRL
			ReadFile file = new ReadFile(logFile);
			String[] arrayLines = file.OpenFile();
			Vector listOfValues = new Vector();
			List<LogEntry> listOfEntries = new ArrayList<LogEntry>();
			List<LogEntry> cleanedListOfEntries = new ArrayList<LogEntry>();
			
			
			int indexLogLines;
			// i empieza en 1 porque la primera línea son los nombres de los campos
			for ( indexLogLines = 1; indexLogLines < arrayLines.length; indexLogLines++ ) {

				// Obtener valores de los campos por separado
				String[] fieldValues = arrayLines[indexLogLines].split(";");
				
				// Limpiamos el vector de valores
				listOfValues.clear();
				
				// Valores uno a uno
				int indexLogFields;
				for ( indexLogFields = 0; indexLogFields < fieldValues.length; indexLogFields++){
					// Dejamos limpios los que estén entre comillas ""
					Pattern patternForCleaning = Pattern.compile(cleaningPattern);
					Matcher matcherLine = patternForCleaning.matcher(fieldValues[indexLogFields]);
					
					if (matcherLine.find()) {
						// Valores en matcherLine.group(1)
						fieldValues[indexLogFields] = matcherLine.group(1);
					}
					
					// Patrones para detectar qué es qué
					Pattern patternForContentType = Pattern.compile(contentTypePattern);
					Pattern patternForTime = Pattern.compile(timePattern);
					Pattern patternForUrl = Pattern.compile(urlPattern);
					
					Matcher matcherContentType = patternForContentType.matcher(fieldValues[indexLogFields]);
					Matcher matcherTime = patternForTime.matcher(fieldValues[indexLogFields]);
					Matcher matcherUrl = patternForUrl.matcher(fieldValues[indexLogFields]);
					
					if (matcherContentType.find()) {
						//System.out.println("Found MCT: " + matcherContentType.group(1) );
						if (matcherContentType.group(1) != null) {
							listOfValues.addElement(matcherContentType.group(1));
							listOfValues.addElement(fieldValues[indexLogFields]);
						} else {
							listOfValues.addElement(fieldValues[indexLogFields]);
							listOfValues.addElement(fieldValues[indexLogFields]);
						}
						
					} else if(matcherTime.find()) {
						//System.out.println("Found time: " + fieldValues[j] );
						listOfValues.addElement(fieldValues[indexLogFields]);						
					} else if(matcherUrl.find()) {
						/*
						 * Complete URL at matcherUrl.group(0);
						 * Protocol (http, https, etc) at matcherUrl.group(1);
						 * www (if exists) at matcherUrl.group(2);
						 * Subdomains, coredomain, and TLD at matcherUrl.group(3);
						 * URL path after the / (if exists) at matcherUrl.group(4);
						 * File extension (if exists) at matcherUrl.group(5);
						 */
						
						listOfValues.addElement(fieldValues[indexLogFields]);
						
						/********************************************************
						 * LETTERS, DIGITS, AND OTHER NON-ALPHANUMERIC CHARACTERS
						 ********************************************************/
						
						int charInURL = 0;
						int numInURL = 0;
						int otherCharInURL = 0;
						char temp;
						
						int charsInURL;
						for (charsInURL = 0; charsInURL < matcherUrl.group(0).length(); charsInURL++) {
							
							temp = matcherUrl.group(0).charAt(charsInURL);
							
							if(Character.isLetter(temp)) {
								charInURL++;
							} else if (Character.isDigit(temp)){
								numInURL++;
							} else {
								otherCharInURL++;
							}
						}
						
						listOfValues.addElement(matcherUrl.group(0).length());
						listOfValues.addElement(charInURL);
						listOfValues.addElement(numInURL);
						listOfValues.addElement(otherCharInURL);
						
						/*********************************
						 * URL DOMAIN, SUBDOMAINS, AND TLD
						 *********************************/
						if (matcherUrl.group(3) != null) {
							
							Pattern patternForIP = Pattern.compile(IPPattern);
							Matcher matcherIP = patternForIP.matcher(matcherUrl.group(3));					
							
							/* If URL domain is an IP */
							if (matcherIP.find()) {
								
								listOfValues.addElement(true); // Is IP
								listOfValues.addElement(false); // No subdomains
								listOfValues.addElement(0); // No subdomains (num_subdomains = 0)
								
								int indexUrlValues;
								for ( indexUrlValues = 0; indexUrlValues < 7; indexUrlValues++){
									listOfValues.addElement("?");
								}
								
							} else {
								
								listOfValues.addElement(false); // Is not an IP
								
								/* Max subdomains in URL at the Log File is 5, then, max domain length is 7 */
								
								String[] urlValues = matcherUrl.group(3).split("\\.");
								/*
								 * TLD at urlValues[urlValues.length-1];
								 * Core Domain at urlValues[urlValues.length-2]:
								 */
								
								String noSubdomain = "?";
								int numSubdomains = urlValues.length-2;
								if (numSubdomains >= 1) {
									listOfValues.addElement(true); // Has subdomains
									listOfValues.addElement(numSubdomains); // Number of subdomains
								} else {
									listOfValues.addElement(false); // No subdomains
									listOfValues.addElement(0); // No subdomains (num_subdomains = 0)
								}
								while (numSubdomains < 5) {
									listOfValues.addElement(noSubdomain);
									numSubdomains++;
								}
								
								int indexSubdomains;
								for ( indexSubdomains = 0; indexSubdomains < urlValues.length; indexSubdomains++){
									listOfValues.addElement(urlValues[indexSubdomains]);
								}
							}
						} else {
							listOfValues.addElement(false); // Is not IP
							listOfValues.addElement(false); // No subdomains
							listOfValues.addElement(0); // No subdomains (num_subdomains = 0)
							
							int indexNoSubdomains;
							for ( indexNoSubdomains = 0; indexNoSubdomains < 7; indexNoSubdomains++){
								listOfValues.addElement("?");
							}
						}
						
						/****************************
						 * PATH
						 ****************************/
						String[] pathValues = null;
						
						if (matcherUrl.group(4) != null) {
							
							listOfValues.addElement(true); // Has path
							pathValues = matcherUrl.group(4).split("\\/");
							if (pathValues.length >= 2) {
								if (pathValues[0].matches("[a-zA-Z0-9]*")) { // Folder 1 is not a filename
									listOfValues.addElement(pathValues[0]);
								} else {
									listOfValues.addElement("?"); //Folder 1 is a filename
								}
								if (pathValues[1].matches("[a-zA-Z-0-9]*")) { // Folder 2 is not a filename
									listOfValues.addElement(pathValues[1]);
								} else {
									listOfValues.addElement("?"); // Folder 2 is a filename
								}
							} else if (pathValues.length >= 1) {
								if (pathValues[0].matches("[a-zA-Z-0-9]*")) { // Folder 1 is not a filename
									listOfValues.addElement(pathValues[0]);
								} else {
									listOfValues.addElement("?"); // Folder 1 is a filename
								}
								listOfValues.addElement("?");
							}
							
						} else {
							
							listOfValues.addElement(false); // No path
							listOfValues.addElement("?"); // No folder 1
							listOfValues.addElement("?"); // No folder 2
						}
						
						/****************************
						 * PARAMETERS
						 ****************************/
						
						int indexParameters;
						int countParameters = 0;
						boolean areThereParameters = false;
						for (indexParameters = 0; indexParameters < pathValues.length; indexParameters++) {
							if (pathValues[indexParameters].contains("?")) {
								areThereParameters = true;
								String[] parameterValues = pathValues[indexParameters].split("\\?");
								int i;
								for (i=0;i<parameterValues.length;i++){									
									if (parameterValues[i].contains("&")) {
										String[] valuePairs = parameterValues[i].split("\\&");
										int j;
										for (j=0;j<valuePairs.length;j++){
											countParameters++;
										}										
									}
									
								}
								
							}
						}
						listOfValues.addElement(areThereParameters); // If there are parameters or not
						listOfValues.addElement(countParameters); // Number of parameters
						
						/****************************
						 * FILE EXTENSION
						 ****************************/
						
						Pattern patternForFileExtension = Pattern.compile(fileExtension);
						
						// File extension should be at the last place of pathValues
						Matcher matcherFileExtension = patternForFileExtension.matcher(pathValues[pathValues.length-1]);
						
						if (matcherFileExtension.find()) {
								
							listOfValues.addElement(true); // Has file extension
							listOfValues.addElement(matcherFileExtension.group(1).length()); // Length of the file
							// LETTERS, DIGITS, AND OTHER NON-ALPHANUMERIC CHARACTERS
							int charInFileExt = 0;
							int numInFileExt = 0;
							int otherCharInFileExt = 0;
							
							int charsInFileExt;
							for (charsInFileExt = 0; charsInFileExt < matcherFileExtension.group(1).length(); charsInFileExt++) {
								
								temp = matcherFileExtension.group(1).charAt(charsInFileExt);
								
								if(Character.isLetter(temp)) {
									charInFileExt++;
								} else if (Character.isDigit(temp)){
									numInFileExt++;
								} else {
									otherCharInFileExt++;
								}
							}
							listOfValues.addElement(charInFileExt);
							listOfValues.addElement(numInFileExt);
							listOfValues.addElement(otherCharInFileExt);
							listOfValues.addElement(matcherFileExtension.group(2));
								
						} else {
							listOfValues.addElement(false); // No file extension
							listOfValues.addElement(0); // File length = 0
							listOfValues.addElement(0); // Num letters = 0
							listOfValues.addElement(0); // Num digits = 0
							listOfValues.addElement(0); // Num other characters = 0
							listOfValues.addElement("?"); // No file extension
						}
						
						/****************************
						 * REQUEST PROTOCOL
						 ****************************/
						if (matcherUrl.group(1) != null) {	
							listOfValues.addElement(matcherUrl.group(1));
						} else {
							listOfValues.addElement(" "); // No protocol
						}
						
												
					} else if (is_integer(fieldValues[indexLogFields])) {						
						listOfValues.addElement(Integer.parseInt(fieldValues[indexLogFields]));						
					} else if (indexLogFields == 3){
						listOfValues.addElement(fieldValues[indexLogFields]);
						listOfValues.addElement(fieldValues[indexLogFields]);
					} else if (indexLogFields == 8){
						listOfValues.addElement(fieldValues[indexLogFields]);
						listOfValues.addElement(fieldValues[indexLogFields]);
						listOfValues.addElement(fieldValues[indexLogFields]);
					} else {						
						listOfValues.addElement(fieldValues[indexLogFields]);
					}
				}
				
				listOfEntries.add(obtain_log(listOfValues));
			} // for
			
			//System.out.println("Cleaning...");
			//cleanedListOfEntries = erase_repeated_entries(listOfEntries);
			return listOfEntries;
				
		} catch (IOException e) {
			System.out.println(e.getMessage());
			return null;
		}		        
        
    }
	
    public static boolean is_integer(String value) {
    	
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
		
		if (listOfValues.size() == 37) {			
			
			String http_reply_code = ""+listOfValues.elementAt(0);
			String http_method = (String)listOfValues.elementAt(1);
			int duration_milliseconds = (int)listOfValues.elementAt(2);
			String content_type_MCT = (String)listOfValues.elementAt(3);
			String content_type = (String)listOfValues.elementAt(4);
			String server_or_cache_address = (String)listOfValues.elementAt(5);
			String time = (String)listOfValues.elementAt(6);
			String squid_hierarchy = (String)listOfValues.elementAt(7);
			int bytes = (int)listOfValues.elementAt(8);
			String url = (String)listOfValues.elementAt(9);
			int url_length = (int)listOfValues.elementAt(10);
			int num_letters = (int)listOfValues.elementAt(11);
			int num_digits = (int)listOfValues.elementAt(12);
			int num_characters = (int)listOfValues.elementAt(13);
			Boolean url_is_IP = (Boolean)listOfValues.elementAt(14);
			Boolean url_has_subdomains = (Boolean)listOfValues.elementAt(15);
			int num_subdomains = (int)listOfValues.elementAt(16);
			String subdomain5 = (String)listOfValues.elementAt(17);
			String subdomain4 = (String)listOfValues.elementAt(18);
			String subdomain3 = (String)listOfValues.elementAt(19);
			String subdomain2 = (String)listOfValues.elementAt(20);
			String subdomain1 = (String)listOfValues.elementAt(21);
			String url_core = (String)listOfValues.elementAt(22);
			String url_TLD = (String)listOfValues.elementAt(23);
			Boolean url_has_path = (Boolean)listOfValues.elementAt(24);
			String folder1 = (String)listOfValues.elementAt(25);
			String folder2 = (String)listOfValues.elementAt(26);
			Boolean path_has_parameters = (Boolean)listOfValues.elementAt(27);
			int num_parameters = (int)listOfValues.elementAt(28);
			Boolean url_has_file_extension = (Boolean)listOfValues.elementAt(29);
			int file_char_length = (int)listOfValues.elementAt(30);
			int file_num_letters = (int)listOfValues.elementAt(31);
			int file_num_digits = (int)listOfValues.elementAt(32);
			int file_num_other_char = (int)listOfValues.elementAt(33);
			String file_extension = (String)listOfValues.elementAt(34);
			String url_protocol = (String)listOfValues.elementAt(35);
			String client_address = (String)listOfValues.elementAt(36);
			// elementos en listOfValues.elementAt(número)
		
			LogEntry myEntry = new LogEntry(http_reply_code,
					http_method,
					duration_milliseconds,
					content_type_MCT,
					content_type,
					server_or_cache_address,
					time,
					squid_hierarchy,
					bytes,
					url,
					url_length,
					num_letters,
					num_digits,
					num_characters,
					url_is_IP,
					url_has_subdomains,
					num_subdomains,
					subdomain5,
					subdomain4,
					subdomain3,
					subdomain2,
					subdomain1,
					url_core,
					url_TLD,
					url_has_path,
					folder1,
					folder2,
					path_has_parameters,
					num_parameters,
					url_has_file_extension,
					file_char_length,
					file_num_letters,
					file_num_digits,
					file_num_other_char,
					file_extension,
					url_protocol,
					client_address);
		
			return myEntry;
		} else {
			System.out.println("Number of values required for creating a log entry is: "+listOfValues.size()+", so insufficient");
			for(int n=0; n<listOfValues.size(); n++){
	            System.out.println("-"+n+" "+listOfValues.elementAt(n)+"\t");
	        }
			return null;
		}
	}
	
	public static List<LogEntry> erase_repeated_entries(List<LogEntry> initial_list) {
		
		List<LogEntry> cleaned_list = initial_list;
		
		int indexEntry, indexComp;
		for ( indexEntry = 0; indexEntry < initial_list.size(); indexEntry++) {
			
			for ( indexComp = 0; indexComp < cleaned_list.size(); indexComp++) {
				
				if ( indexComp == indexEntry) {
					continue;
				} else if ( initial_list.get(indexEntry).getURL().contentEquals(cleaned_list.get(indexComp).getURL()) ) {
					if ( cleaned_list.get(indexComp).getIP_client().contentEquals(initial_list.get(indexEntry).getIP_server()) ) {
						cleaned_list.remove(indexComp);
					}
				}
			}
		}
		
		return cleaned_list;
	}

}
