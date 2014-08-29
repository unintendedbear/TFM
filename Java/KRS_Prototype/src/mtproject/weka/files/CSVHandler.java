/**
 * 
 */
package mtproject.weka.files;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import mtproject.file.handlers.*;
import mtproject.objects.LogEntry;
import mtproject.objects.Rule;
import mtproject.parsers.DataParser;
import mtproject.parsers.RuleParser;

import weka.core.Instances;

/**
 * @author osica
 *
 */
public class CSVHandler {

	/**
	 * 
	 */
	public CSVHandler() {
		
	}

	/**
	 * @param args
	 */
	public static String[] obtain_csv(List<LogEntry> labelled_Entries, String file_name, boolean no_labels) throws IOException {
		
		File CSV_File = new File("/home/osica/workspace/KRS_Prototype/Logs/"+file_name+".csv");
		
		try {
			
			FileWriter CSV_Data = new FileWriter(CSV_File);
			
			// Primero metemos los atributos
			CSV_Data.append("http_reply_code");
			CSV_Data.append(',');
			CSV_Data.append("http_method");
			CSV_Data.append(',');
			CSV_Data.append("duration_milliseconds");
			CSV_Data.append(',');
			CSV_Data.append("content_type_MCT");
			CSV_Data.append(',');
			CSV_Data.append("content_type");
			CSV_Data.append(',');
			CSV_Data.append("server_or_cache_address");
			CSV_Data.append(',');
			CSV_Data.append("time");
			CSV_Data.append(',');
			CSV_Data.append("squid_hierarchy");
			CSV_Data.append(',');
			CSV_Data.append("bytes");
			CSV_Data.append(',');
			CSV_Data.append("url_core");
			CSV_Data.append(',');
			CSV_Data.append("url_TLD");
			CSV_Data.append(',');
			CSV_Data.append("client_address");
			CSV_Data.append(',');
			CSV_Data.append("label");
			CSV_Data.append('\n');
			
			int i;
			for (i = 0; i < labelled_Entries.size(); i++) {
				
				if (labelled_Entries.get(i).getLabel() == null && !no_labels) {
					continue;
				}
				
				CSV_Data.append(""+labelled_Entries.get(i).getHTTP_reply_code());
				CSV_Data.append(',');
				CSV_Data.append(labelled_Entries.get(i).getHTTP_method());
				CSV_Data.append(',');
				CSV_Data.append(""+labelled_Entries.get(i).getNum_miliseconds());
				CSV_Data.append(',');
				CSV_Data.append(labelled_Entries.get(i).getMCT());
				CSV_Data.append(',');
				CSV_Data.append(labelled_Entries.get(i).getContent_type());
				CSV_Data.append(',');
				CSV_Data.append(labelled_Entries.get(i).getIP_server());
				CSV_Data.append(',');
				CSV_Data.append(labelled_Entries.get(i).getTime());
				CSV_Data.append(',');
				CSV_Data.append(labelled_Entries.get(i).getSquid_hierarchy());
				CSV_Data.append(',');
				CSV_Data.append(""+labelled_Entries.get(i).getBytes());
				CSV_Data.append(',');
				CSV_Data.append(labelled_Entries.get(i).getURL_core());
				CSV_Data.append(',');
				CSV_Data.append(labelled_Entries.get(i).getTLD());
				CSV_Data.append(',');
				CSV_Data.append(labelled_Entries.get(i).getIP_client());
				CSV_Data.append(',');
				
				if (labelled_Entries.get(i).getLabel() != null) {
					CSV_Data.append(labelled_Entries.get(i).getLabel());
					CSV_Data.append('\n');
				} else {
					CSV_Data.append("no_label");
					CSV_Data.append('\n');
				}
			}
			
			 CSV_Data.flush();
			 CSV_Data.close();
			 
			 String[] csv_path_data = new String[2];
			 csv_path_data[0] = CSV_File.getName();
			 csv_path_data[1] = CSV_File.getAbsolutePath();
			 return csv_path_data;
			
			
		} catch (IOException e) {
			System.out.println(e.getMessage());
			return null;
		}		

	}

}