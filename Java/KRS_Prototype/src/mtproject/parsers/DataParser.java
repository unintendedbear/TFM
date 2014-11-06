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
		String fileExtension = "(.*)\\.([^.]+)?";
		
				
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
						/*
						 * Complete URL at matcherUrl.group(0);
						 * Protocol (http, https, etc) at matcherUrl.group(1);
						 * www (if exists) at matcherUrl.group(2);
						 * Subdomains, coredomain, and TLD at matcherUrl.group(3);
						 * URL path after the / (if exists) at matcherUrl.group(4);
						 * File extension (if exists) at matcherUrl.group(5);
						 */
						
						listOfValues.addElement(fieldValues[j]);
						
						/****************************
						 * URL DOMAIN, SUBDOMAINS, AND TLD
						 ****************************/
						if (matcherUrl.group(3) != null) {
							
							Pattern patternForIP = Pattern.compile(IPPattern);
							Matcher matcherIP = patternForIP.matcher(matcherUrl.group(3));					
							
							/* If URL domain is an IP */
							if (matcherIP.find()) {
								
								listOfValues.addElement(true); // Is IP
								listOfValues.addElement(false); // No subdomains
								listOfValues.addElement(0); // No subdomains (num_subdomains = 0)
								
								int k;
								for ( k = 0; k < 7; k++){
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
								int m = urlValues.length-2;
								if (m >= 1) {
									listOfValues.addElement(true); // Has subdomains
									listOfValues.addElement(m); // Number of subdomains
								} else {
									listOfValues.addElement(false); // No subdomains
									listOfValues.addElement(0); // No subdomains (num_subdomains = 0)
								}
								while (m < 5) {
									listOfValues.addElement(noSubdomain);
									m++;
								}
								
								int n;
								for ( n = 0; n < urlValues.length; n++){
									listOfValues.addElement(urlValues[n]);
								}
							}
						} else {
							listOfValues.addElement(false); // Is not IP
							listOfValues.addElement(false); // No subdomains
							listOfValues.addElement(0); // No subdomains (num_subdomains = 0)
							
							int l;
							for ( l = 0; l < 7; l++){
								listOfValues.addElement("?");
							}
						}
						
						/****************************
						 * PATH & FILE EXTENSION
						 ****************************/
						if (matcherUrl.group(4) != null) {
							
							listOfValues.addElement(true); // Has path
							String[] pathValues = matcherUrl.group(4).split("\\/");
							if (pathValues.length >= 2) {
								if (pathValues[0].matches("[a-zA-Z]*")) {
									listOfValues.addElement(pathValues[0]);
								} else {
									listOfValues.addElement("?");
								}
								if (pathValues[1].matches("[a-zA-Z]*")) {
									listOfValues.addElement(pathValues[1]);
								} else {
									listOfValues.addElement("?");
								}
							} else if (pathValues.length >= 1) {
								if (pathValues[0].matches("[a-zA-Z]*")) {
									listOfValues.addElement(pathValues[0]);
								} else {
									listOfValues.addElement("?");
								}
								listOfValues.addElement("?");
							}
							
							Pattern patternForFileExtension = Pattern.compile(fileExtension);							
							
							int index;
							boolean nofileext = false;
							for (index = 0; index < pathValues.length; index++) {
								Matcher matcherFileExtension = patternForUrl.matcher(pathValues[index]);
								
								if (matcherFileExtension.find() && matcherFileExtension.group(2).length() <= 4) {
									
									listOfValues.addElement(true); // Has file extension
									listOfValues.addElement(matcherUrl.group(7));
									nofileext = false;
									break;
									
								} else {
									
									nofileext = true;
								}
							}
							
							if (nofileext) {
								listOfValues.addElement(false); // No file extension
								listOfValues.addElement("?"); // No file extension
							}
							
						} else {
							
							listOfValues.addElement(false); // No path
							listOfValues.addElement("?"); // No folder 1
							listOfValues.addElement("?"); // No folder 2
						}
						
						/****************************
						 * FILE EXTENSION
						 ****************************/
						/*if (matcherUrl.group(7) != null && matcherUrl.group(7).length() <= 4) {

							listOfValues.addElement(true); // Has file extension
							listOfValues.addElement(matcherUrl.group(7));														
							
						} else {
							
							listOfValues.addElement(false); // No file extension
							listOfValues.addElement("?"); // No file extension
						}*/
						
						/****************************
						 * REQUEST PROTOCOL
						 ****************************/
						if (matcherUrl.group(1) != null) {	
							listOfValues.addElement(matcherUrl.group(1));
						} else {
							listOfValues.addElement(" "); // No protocol
						}
						
												
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
		
		if (listOfValues.size() == 27) {			
			
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
			Boolean url_is_IP = (Boolean)listOfValues.elementAt(10);
			Boolean url_has_subdomains = (Boolean)listOfValues.elementAt(11);
			int num_subdomains = (int)listOfValues.elementAt(12);
			String subdomain5 = (String)listOfValues.elementAt(13);
			String subdomain4 = (String)listOfValues.elementAt(14);
			String subdomain3 = (String)listOfValues.elementAt(15);
			String subdomain2 = (String)listOfValues.elementAt(16);
			String subdomain1 = (String)listOfValues.elementAt(17);
			String url_core = (String)listOfValues.elementAt(18);
			String url_TLD = (String)listOfValues.elementAt(19);
			Boolean url_has_path = (Boolean)listOfValues.elementAt(20);
			String folder1 = (String)listOfValues.elementAt(21);
			String folder2 = (String)listOfValues.elementAt(22);
			Boolean url_has_file_extension = (Boolean)listOfValues.elementAt(23);
			String file_extension = (String)listOfValues.elementAt(24);
			String url_protocol = (String)listOfValues.elementAt(25);
			String client_address = (String)listOfValues.elementAt(26);
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
					url_has_file_extension,
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

}
