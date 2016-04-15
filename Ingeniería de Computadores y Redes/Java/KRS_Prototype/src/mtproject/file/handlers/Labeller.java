package mtproject.file.handlers;

import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

import mtproject.objects.Condition;
import mtproject.objects.Rule;
import mtproject.objects.LogEntry;

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
	public static List<LogEntry> obtain_labels(List<LogEntry> unlabelled_Entries, List<Rule> DRL_Rules){
		// TODO Auto-generated method stub
		
		// Partimos de una lista de reglas y otra de entradas
		
		List<LogEntry> labelled_Entries = new ArrayList<LogEntry>();
		
		int trues = 0;
		int falses = 0;
		int nolabels = 0;
		
					
		List<Condition> Conditions = new ArrayList<Condition>();
		String Permission;
		boolean apply_permission;
		
		// Hay que comprobar todas las entradas para cada regla...
		
		int i; // Recorrer entradas del log
		for ( i = 0; i < unlabelled_Entries.size(); i++) {
			
			//System.out.println("Entrada nº "+i);
			
			int j; // Recorrer las reglas
			for ( j = 0; j < DRL_Rules.size(); j++) {
				
				Conditions = DRL_Rules.get(j).getConditions();
				Permission = DRL_Rules.get(j).getPermission();
				
				apply_permission = meets_conditions(Conditions, unlabelled_Entries.get(i));
				
				// En caso de que Permission sea false, es un DENY, y prevalece ante cualquier ALLOW
				if (apply_permission) { 
					if (Permission.compareTo("deny") == 0) {
						unlabelled_Entries.get(i).setLabel(Permission);
						break;
					} else {
						unlabelled_Entries.get(i).setLabel(Permission);							
					}
				}

			} // for j
			
			// Para debugging
			// LogEntry mirar = unlabelled_Entries.get(i);
			// String mirartambien = mirar.getLabel();
			labelled_Entries.add(unlabelled_Entries.get(i));				
		
		} // for i
			
			
		
		
		/*int m;
		for ( m = 0; m < labelled_Entries.size(); m++) {
			if (labelled_Entries.get(m).getLabel() != null) {
				if (labelled_Entries.get(m).getLabel().compareTo("allow") == 0) {
					trues++;
				} else  {
					falses++;
				}
			}else {
				nolabels++;
			}
		}
		int total = trues+falses+nolabels;
		System.out.println("+"+trues+"-"+falses+"--"+nolabels+" = "+total);*/
		
		return labelled_Entries;
		
	}
	
	public static boolean meets_conditions(List<Condition> check_conditions, LogEntry check_Entry){
		
		/*****************************************************
		 * Variables que van a hacer falta
		 *****************************************************/
		
		String DataType;
		String Relationship;
		String Value;
		String http_reply_code;
		String http_method;
		int duration_milliseconds;
		String content_type_MCT;
		String content_type;
		String server_or_cache_address;
		String time;
		String squid_hierarchy;
		int bytes;
		String url;
		String url_core;
		String url_TLD;
		String client_address;
		
		int count = 0;
		Map logMap = new HashMap();
		
		/*****************************************************
		 *****************************************************/
		
		http_reply_code = check_Entry.getHTTP_reply_code();
		http_method = check_Entry.getHTTP_method();
		duration_milliseconds = check_Entry.getNum_miliseconds();
		content_type_MCT = check_Entry.getMCT();
		content_type = check_Entry.getContent_type();
		server_or_cache_address = check_Entry.getIP_server();
		time = check_Entry.getTime();
		squid_hierarchy = check_Entry.getSquid_hierarchy();
		bytes = check_Entry.getBytes();
		url = check_Entry.getURL();
		url_core = check_Entry.getURL_core();
		url_TLD = check_Entry.getTLD();
		client_address = check_Entry.getIP_client();
		
		logMap.clear();
		logMap.put("http_reply_code", http_reply_code);
		logMap.put("http_method", http_method);
		logMap.put("duration_milliseconds", duration_milliseconds);
		logMap.put("content_type_MCT", content_type_MCT);
		logMap.put("content_type", content_type);
		logMap.put("server_or_cache_address", server_or_cache_address);
		logMap.put("time", time);
		logMap.put("squid_hierarchy", squid_hierarchy);
		logMap.put("bytes", bytes);
		logMap.put("url", url);
		logMap.put("url_core", url_core);
		logMap.put("url_TLD", url_TLD);
		logMap.put("client_address", client_address);
							
		int k;
		for ( k = 0; k < check_conditions.size(); k++) {
			
			DataType = check_conditions.get(k).getDataType();
			Relationship = check_conditions.get(k).getRelationship();
			Value = check_conditions.get(k).getValue();
			int temporalInteger;
			String temporalString;
			
			if (logMap.containsKey(DataType)) {
				switch (Relationship) {
				case "==":
					if (logMap.get(DataType) instanceof Integer) {
						temporalInteger = Integer.parseInt(Value);
						if ((int)logMap.get(DataType) == temporalInteger) {
							//System.out.println("-"+(int)logMap.get(DataType)+" == ? "+temporalInteger);
							count++;
						}
					} else {
						temporalString = (String)logMap.get(DataType);
						if (temporalString.toLowerCase().contains(Value.toLowerCase())) {
							//System.out.println("-"+temporalString+" compareTo "+Value);
							count++;
						}
					}
					break;
				case "matches":
					temporalString = (String)logMap.get(DataType);
					if (temporalString.toLowerCase().contains(Value.toLowerCase())) {
						//System.out.println("-"+temporalString+" compareTo "+Value);
						count++;
					}
					break;
				case "<":
					temporalInteger = Integer.parseInt(Value);
					if ((int)logMap.get(DataType) < temporalInteger) {
						//System.out.println("-"+(int)logMap.get(DataType)+" < ? "+temporalInteger);
						count++;
					}
					break;
				case ">":
					temporalInteger = Integer.parseInt(Value);
					if ((int)logMap.get(DataType) > temporalInteger) {
						//System.out.println("-"+(int)logMap.get(DataType)+" > ? "+temporalInteger);
						count++;
					}
					break;
				} // switch
			} else {
				System.out.println("No "+DataType+" en el hash");
			}
			
		} // for k	
		
		if (count == check_conditions.size()) {
			return true;
		} else {
			return false;
		}
	}

}
