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
		
		File CSV_File = new File("/home/paloma/workspace/KRS_Prototype/CSV/"+file_name+".csv");
		
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
	
	/**
	 * @param args
	 */
	public static void undersampling (String[] CSV_File_input) throws Exception {
			
		try {
			
			String File_name = CSV_File_input[0].substring(0, CSV_File_input[0].length()-4);
						
			ReadFile in_file = new ReadFile(CSV_File_input[1]);
			String CSV_File_output = "/home/paloma/workspace/KRS_Prototype/CSV/"+File_name+"_undersampled.csv";
			WriteFile out_file = new WriteFile(CSV_File_output, true);
			
			String[] arrayLines = in_file.OpenFile();
			
			int i;
			for ( i = 0; i < arrayLines.length; i++ ) {
				
				if (arrayLines[i].toLowerCase().contains("allow") && (int)Math.random() < 0.5){
					out_file.writeToFile(arrayLines[i]);
				} else {
					out_file.writeToFile(arrayLines[i]);
				}
			}
			
			
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}		

	}
	
	/**
	 * @param args
	 */
	public static void oversampling (String[] CSV_File_input) throws Exception {
					
		try {
			
			String File_name = CSV_File_input[0].substring(0, CSV_File_input[0].length()-4);
						
			ReadFile in_file = new ReadFile(CSV_File_input[1]);
			String CSV_File_output = "/home/paloma/workspace/KRS_Prototype/CSV/"+File_name+"_oversampled.csv";
			WriteFile out_file = new WriteFile(CSV_File_output, true);
			
			String[] arrayLines = in_file.OpenFile();
			
			int i;
			for ( i = 0; i < arrayLines.length; i++ ) {
				
				if (arrayLines[i].toLowerCase().contains("deny")){
					out_file.writeToFile(arrayLines[i]);
					out_file.writeToFile(arrayLines[i]);
				} else {
					out_file.writeToFile(arrayLines[i]);
				}
			}
			
			
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}		

	}
	
	/**
	 * @param args
	 */
	public static void training_test (String[] CSV_File_input, String option) throws Exception {
		
		int[] training_percentage = new int[3];
		training_percentage[0] = 90;
		training_percentage[1] = 80;
		training_percentage[2] = 60;
		
		String File_name = CSV_File_input[0].substring(0, CSV_File_input[0].length()-4);
			
		try {
			
			ReadFile in_file = new ReadFile(CSV_File_input[1]);
			
			String[] arrayLines = in_file.OpenFile();
			
			int i;
			for ( i = 0; i < training_percentage.length; i++ ) {
				
				int test_percentage = 100-training_percentage[i];
				
				if (option.compareTo("random") == 0) {
					
					String CSV_File_training = "/home/paloma/workspace/KRS_Prototype/CSV/"+File_name+"_training_"+training_percentage[i]+"_random.csv";
					String CSV_File_test = "/home/paloma/workspace/KRS_Prototype/CSV/"+File_name+"_test_"+test_percentage+"_random.csv";
					WriteFile training_file = new WriteFile(CSV_File_training, true);
					WriteFile test_file = new WriteFile(CSV_File_test, true);
					
					int j;
					for ( j = 0; j < arrayLines.length; j++ ) {
						if (!arrayLines[j].contentEquals(arrayLines[j+1])) {
							if ((int)Math.random() < test_percentage/100){
								test_file.writeToFile(arrayLines[j]);
							} else {
								training_file.writeToFile(arrayLines[j]);
							}
						} else {
							if ((int)Math.random() < test_percentage/100){
								test_file.writeToFile(arrayLines[j]);
								test_file.writeToFile(arrayLines[j+1]);
								j++;
							} else {
								training_file.writeToFile(arrayLines[j]);
								training_file.writeToFile(arrayLines[j+1]);
								j++;
							}
						} // else
						
					} // for j
					
				} else if (option.compareTo("consecutive") == 0) {
					
					String CSV_File_training = "/home/paloma/workspace/KRS_Prototype/CSV/"+File_name+"_training_"+training_percentage[i]+"_consecutive.csv";
					String CSV_File_test = "/home/paloma/workspace/KRS_Prototype/CSV/"+File_name+"_test_"+test_percentage+"_consecutive.csv";
					WriteFile training_file = new WriteFile(CSV_File_training, true);
					WriteFile test_file = new WriteFile(CSV_File_test, true);
					
					int training_length = arrayLines.length*training_percentage[i]/100;
					
					int j;
					for ( j = 0; j < arrayLines.length; j++ ) {
						if (!arrayLines[j].contentEquals(arrayLines[j+1])) {
							if (training_length > 0){
								test_file.writeToFile(arrayLines[j]);
							} else {
								training_file.writeToFile(arrayLines[j]);
							}
						} else {
							if ((int)Math.random() < test_percentage/100){
								test_file.writeToFile(arrayLines[j]);
								test_file.writeToFile(arrayLines[j+1]);
								j++;
							} else {
								training_file.writeToFile(arrayLines[j]);
								training_file.writeToFile(arrayLines[j+1]);
								j++;
							}
						} // else
						
					} // for j
					
				} else {
					throw new IllegalArgumentException("Unknown option '" + option + "'!");
				}
				
			}
			
			
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}		

	}

}