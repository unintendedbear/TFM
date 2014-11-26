/**
 * 
 */
package mtproject.parsers;

import java.io.IOException;
import java.util.regex.Pattern;
import java.util.regex.Matcher;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;

import mtproject.file.handlers.*;
import mtproject.objects.*;

import java.util.Date;
import java.util.Vector;
import java.text.*;

/**
 * @author paloma
 *
 */
public class SessionParser {

	/**
	 * 
	 */
	public SessionParser() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param args
	 */
	public static List<Session> main(List<LogEntry> labelled_Entries) {
		
		Vector listOfValues = new Vector();
		List<Session> listOfSessions = new ArrayList<Session>();
		
		String client_session_IP;
		int num_allows = 0;
		int num_denies = 0;
		int session_bytes = 0;
		int session_ms = 0;
		int session_latency = 0;
		int num_servers = 0;
		int num_core_domains = 0;
		boolean if_error = false;
		
		String time_session_starts, time_session_ends;
		int[] session_time; // 1 minute definition for a session time
		boolean in_session = false;
		
		String[] Clients = obtain_clients(labelled_Entries);
		
		int indexClients; // Recorrer las entradas etiquetadas tantas veces como clientes diferentes hay
		for ( indexClients = 0; indexClients < Clients.length; indexClients++) {
			
			List<LogEntry> cleaned_labelled_entries = clean_clients(labelled_Entries, Clients[indexClients]);
			
			int time[] = time_splitter(cleaned_labelled_entries.get(0).getTime());
			int seconds = time[1]*60 + time[2];
			
			int indexValues;
			for ( indexValues = 0; indexValues < cleaned_labelled_entries.size(); indexValues++) {
				
				client_session_IP = cleaned_labelled_entries.get(indexValues).getIP_client();
				
				if ( seconds <= 60 ) {
					
					
					
				} else {
					
					time = time_splitter(cleaned_labelled_entries.get(indexValues).getTime());
					seconds = time[1]*60 + time[2];
					
				}
			}
			
		}
		
		return listOfSessions;

	}
	
	public static int[] time_splitter (String time) {
		
		String[] time_components = time.split(":");
		int[] components = new int[3];
		
		components[0] = Integer.parseInt(time_components[0]);
		components[1] = Integer.parseInt(time_components[1]);
		components[2] = Integer.parseInt(time_components[2]);
		
		return components;
				
	}
	
	public static String[] obtain_clients (List<LogEntry> log) {
		
		String[] clients;
		Map clientMap = new HashMap();
		clientMap.clear();
		
		int i;
		for ( i = 0; i < log.size(); i++) {
			if ( !clientMap.containsKey(log.get(i).getIP_client()) ) {
				clientMap.put(log.get(i).getIP_client(), 0);
			}
		}
		
		clients = (String[]) clientMap.keySet().toArray();
		
		return clients;
	}
	
	public static List<LogEntry> clean_clients (List<LogEntry> logManyClients, String client) {
		
		List<LogEntry> logOneClients = logManyClients;
				
		int i;
		for ( i = 0; i < logManyClients.size(); i++) {
			if ( !logManyClients.get(i).getIP_client().contentEquals(client) ) {
				logManyClients.remove(i);
			}
		}
		
		return logOneClients;
	}

}
