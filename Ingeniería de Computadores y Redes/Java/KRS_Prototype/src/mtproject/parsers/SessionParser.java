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
import java.util.Set;

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
	public static List<Session> log_by_sessions(List<LogEntry> labelled_Entries) {
		
		System.out.println("Size is :"+labelled_Entries.size());
		/*for (int j=0; j<labelled_Entries.size(); j++) {
			System.out.println("Label ? - "+labelled_Entries.get(j).getLabel());
		}*/
		
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
		
		String[] Clients = obtain_clients(labelled_Entries);
		Map serversMap = new HashMap();
		Map domainsMap = new HashMap();
		serversMap.clear();
		domainsMap.clear();
		
		int indexClients; // Recorrer las entradas etiquetadas tantas veces como clientes diferentes hay
		for ( indexClients = 0; indexClients < Clients.length; indexClients++) {
			
			System.out.println("Analysing sessions for client "+indexClients+" of "+Clients.length);
			
			List<LogEntry> cleaned_labelled_entries = clean_clients(labelled_Entries, Clients[indexClients]);
			
			String time = cleaned_labelled_entries.get(0).getTime();
			int seconds = 0;
			
			client_session_IP = cleaned_labelled_entries.get(0).getIP_client(); // Same client in all sessions
			System.out.println("Client with IP: "+client_session_IP);
			
			int indexValues;
			for ( indexValues = 0; indexValues < cleaned_labelled_entries.size(); indexValues++) {
				
				seconds += seconds_calculator(time, cleaned_labelled_entries.get(indexValues).getTime());
				System.out.println("Time passed: "+seconds);
							
				while ( seconds <= 60 ) {
					
					if ( cleaned_labelled_entries.get(indexValues).getLabel().contains("allow") ) {
						num_allows++;
					} else {
						num_denies++;
					}
					session_bytes += cleaned_labelled_entries.get(indexValues).getBytes();
					session_ms += cleaned_labelled_entries.get(indexValues).getNum_miliseconds();
					serversMap.put(cleaned_labelled_entries.get(indexValues).getIP_server(), 0);
					domainsMap.put(cleaned_labelled_entries.get(indexValues).getURL_core(), 0);
					
					if ( cleaned_labelled_entries.get(indexValues).getHTTP_reply_code().matches("[4|5]\\d\\d") ) {
						if_error = true;
					}
					
					continue;
					
				} 
					
				if (session_ms > 0) { 
					session_latency = session_bytes/session_ms;
				} else {
					session_latency = 0;
				}
				Set<String> servers = serversMap.keySet();
				Set<String> domains = domainsMap.keySet();
				num_servers = servers.size();
				num_core_domains = domains.size();
				
				Session mySession = new Session(client_session_IP, num_allows, num_denies, session_bytes, session_ms, session_latency, num_servers, num_core_domains, if_error);
				listOfSessions.add(mySession);
				
				seconds = 0;
				num_allows = 0;
				num_denies = 0;
				session_bytes = 0;
				session_ms = 0;
				session_latency = 0;
				num_servers = 0;
				num_core_domains = 0;
				if_error = false;
				serversMap.clear();
				domainsMap.clear();
					
				
			}
			
		}
		
		return listOfSessions;

	}
	
	public static int seconds_calculator (String timeA, String timeB) {
		
		String[] timeA_components = timeA.split(":");
		String[] timeB_components = timeB.split(":");
		
		int hourDiff = Integer.parseInt(timeB_components[0])-Integer.parseInt(timeA_components[0]);
		int minuteDiff = Integer.parseInt(timeB_components[1])-Integer.parseInt(timeA_components[1]);
		int secondsDiff = Integer.parseInt(timeB_components[2])-Integer.parseInt(timeB_components[2]);
		
		int seconds = hourDiff*360+minuteDiff*60+secondsDiff;
		
		return seconds;
				
	}
	
	public static String[] obtain_clients (List<LogEntry> log) {
		
		Set<String> clients;
		Map clientMap = new HashMap();
		clientMap.clear();
		
		int i;
		for ( i = 0; i < log.size(); i++) {
			if ( !clientMap.containsKey(log.get(i).getIP_client()) ) {
				clientMap.put(log.get(i).getIP_client(), 0);
			}
		}
		
		clients = clientMap.keySet();
		String[] arrayClients = clients.toArray(new String[clients.size()]);
		
		return arrayClients;
	}
	
	public static List<LogEntry> clean_clients (List<LogEntry> logManyClients, String client) {
		
		List<LogEntry> logOneClient = new ArrayList<LogEntry>();;
				
		int i;
		for ( i = 0; i < logManyClients.size(); i++) {
			//System.out.println("Label (shouldN'T be null) - "+logManyClients.get(i).getLabel());
			if ( logManyClients.get(i).getIP_client().contentEquals(client) ) {
				logOneClient.add(logManyClients.get(i));
			}
		}
		
		return logOneClient;
	}

}
