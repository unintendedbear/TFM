/**
 * 
 */
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
		
		int i; // Recorrer las entradas etiquetadas
		for ( i = 0; i < labelled_Entries.size(); i++) {
			
			client_session_IP = labelled_Entries.get(i).getIP_client();
			if (!listOfValues.contains(client_session_IP)) {
				listOfValues.add(client_session_IP);
				System.out.println("Client: "+client_session_IP);
			}
		}
		
		return listOfSessions;

	}
	
	public static int[] hour_calculator(String timeA, String timeB) {
		
		String[] timeA_components = timeA.split(":");
		String[] timeB_components = timeB.split(":");
		int[] differences = new int[3];
		
		differences[0] = Integer.parseInt(timeB_components[0])-Integer.parseInt(timeA_components[0]);
		differences[1] = Integer.parseInt(timeB_components[1])-Integer.parseInt(timeA_components[1]);
		differences[2] = Integer.parseInt(timeB_components[2])-Integer.parseInt(timeA_components[2]);
		
		return differences;
				
	}

}
