import java.util.ArrayList;
import java.util.List;

import mtproject.parsers.RuleParser;
import mtproject.parsers.DataParser;
import mtproject.objects.Condition;
import mtproject.objects.Rule;
import mtproject.objects.LogEntry;
import junit.framework.TestCase;
import org.junit.Test;
import java.io.IOException;
import java.util.Date;

/**
 * 
 */

/**
 * @author osica
 *
 */
public class Labeller {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		// Partimos de una lista de reglas y otra de entradas
		
		List<Rule> DRL_Rules = new ArrayList<Rule>();
		List<LogEntry> unlabelled_Entries = new ArrayList<LogEntry>();
		
		try {
			DRL_Rules = RuleParser.parsing_DRL();
			unlabelled_Entries = DataParser.parsing_Log();
			
			List<Condition> Conditions = new ArrayList<Condition>();
			boolean Permission;
			
			int k;
			for ( k = 0; k < DRL_Rules.size(); k++) {
				
				Conditions.clear();
				Conditions = DRL_Rules.get(k).getConditions();
				Permission = DRL_Rules.get(k).getPermission();
			}
			
			
		}
		catch (IOException e) {
			System.out.println(e.getMessage());
		}
				
		
	}
	
	public boolean is_allowed(List<Condition> Conditions, List<LogEntry> unlabelled_Entries, boolean Permission){
		
		/*****************************************************
		 * Variables que van a hacer falta
		 *****************************************************/
		
		
		String DataType;
		String Relationship;
		String Value;
		int http_reply_code;
		String http_method;
		int duration_milliseconds;
		String content_type_MCT;
		String content_type;
		String server_or_cache_address;
		Date time;
		String squid_hierarchy;
		int bytes;
		String url_core;
		String url_TLD;
		String client_address;
		
		/*****************************************************
		 *****************************************************/
		
		// Hay que comprobar todas las entradas para cada regla...
		
		int i; // Recorrer entradas del log
		for ( i = 0; i < unlabelled_Entries.size(); i++) {
			
			http_reply_code = unlabelled_Entries.get(i).getHTTP_reply_code();
			http_method = unlabelled_Entries.get(i).getHTTP_method();
			duration_milliseconds = unlabelled_Entries.get(i).getNum_miliseconds();
			content_type_MCT = unlabelled_Entries.get(i).getMCT();
			content_type = unlabelled_Entries.get(i).getContent_type();
			server_or_cache_address = unlabelled_Entries.get(i).getIP_server();
			time = unlabelled_Entries.get(i).getTime();
			squid_hierarchy = unlabelled_Entries.get(i).getSquid_hierarchy();
			bytes = unlabelled_Entries.get(i).getBytes();
			url_core = unlabelled_Entries.get(i).getURL_core();
			url_TLD = unlabelled_Entries.get(i).getTLD();
			client_address = unlabelled_Entries.get(i).getIP_client();
			
			int j;
			for ( j = 0; j < Conditions.size(); j++) {
				
				DataType = Conditions.get(j).getDataType();
				Relationship = Conditions.get(j).getRelationship();
				Value = Conditions.get(j).getValue();
				
			}
		}
		
	}

}
